package io.cat.ai.ildirim.adt.nonlinear.tree.balanced;

import io.cat.ai.ildirim.adt.nonlinear.tree.Tree;
import io.cat.ai.ildirim.util.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.nonlinear.tree.balanced.AATree.AATreeCompanion.*;
import static io.cat.ai.ildirim.data.Ord.*;

public abstract class AATree<T extends Comparable<T>> implements ArneAnderssonTree<T> {

    @Override
    public abstract ArneAnderssonTree<T> left();

    @Override
    public abstract ArneAnderssonTree<T> right();

    public ArneAnderssonTree<T> concat(ArneAnderssonTree<T> that) {
        return isEmpty() ? that
                : that.isEmpty() ? AAEmptyNode()
                    : right().concat(that).concat(left()).insert(value());
    }

    @Override
    public ArneAnderssonTree<T> insert(T e) {
        return isEmpty() ? this
                : ord(e).lessThan(value()) ? rotateLeft(this, e, left(), right())
                    : ord(e).lessThan(value()) ? rotateRight(this, e, left(), right())
                        : this;
    }

    @Override
    public T getAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T max() {
        if (isEmpty())
            throw new IllegalArgumentException("Unable to get max of AVLLeaf");

        return Tree.max(this);
    }

    @Override
    public T min() {
        if (isEmpty())
            throw new IllegalArgumentException("Unable to get min of AVLLeaf");

        return Tree.max(this);
    }

    @Override
    public Pair<AATree<T>, AATree<T>> splitAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArneAnderssonTree<T> delete(T e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArneAnderssonTree<T> skew() {
        return isEmpty() || right().isEmpty() ? this
                : ord(level()).equiv(left().level()) ? AANode(left().level(), left().value(), left().left(), AANode(level(), value(), left().right(), right()))
                    : this;
    }

    @Override
    public ArneAnderssonTree<T> split() {
        return isEmpty() || right().isEmpty() || right().right().isEmpty() ? this
                : ord(level()).equiv(right().right().level()) ? AANode(right().level() + 1, right().value(), AANode(level(), value(), left(), right().left()), right().right())
                    : this;
    }

    @Override
    public ArneAnderssonTree<T> rotateLeft(ArneAnderssonTree<T> tree,
                                           T val,
                                           ArneAnderssonTree<T> left,
                                           ArneAnderssonTree<T> right) {
        return AANode(tree.level(), tree.value(), left.insert(val), right).skew().split();
    }

    @Override
    public ArneAnderssonTree<T> rotateRight(ArneAnderssonTree<T> tree,
                                            T val,
                                            ArneAnderssonTree<T> left,
                                            ArneAnderssonTree<T> right) {
        return AANode(tree.level(), tree.value(), left, right.insert(val)).skew().split();
    }

    @Override
    public <R extends Comparable<R>> ArneAnderssonTree<R> map(Function<T, R> f) {
        return isEmpty() ? AAEmptyNode()
                : left().map(f).concat(right().map(f)).insert(f.apply(value()));
    }

    @Override
    public <R extends Comparable<R>> ArneAnderssonTree<R> flatMap(Function<T, ArneAnderssonTree<R>> f) {
        return isEmpty() ? AAEmptyNode()
                : left().flatMap(f).concat(right().flatMap(f)).concat(f.apply(value()));
    }

    @Override
    public ArneAnderssonTree<T> filter(Predicate<T> cond) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R> R fold(R z, BiFunction<R, T, R> f) {
        return Tree.foldLoop(this, z, f);
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, T, R> f) {
        return Tree.foldlLoop(this, z, f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<T, R, R> f) {
        return Tree.foldrLoop(this, z, f);
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AATree<T> take(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AATree<T> drop(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AATree<T> slice(int from, int until) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    public static final class AANode<T extends Comparable<T>> extends AATree<T> {

        private int level;
        private T val;
        private ArneAnderssonTree<T> left;
        private ArneAnderssonTree<T> right;

        AANode(int level, T val, ArneAnderssonTree<T> left, ArneAnderssonTree<T> right) {
            this.level = level;
            this.val = val;
            this.left = left;
            this.right = right;
        }

        @Override
        public T value() {
            return this.val;
        }

        @Override
        public int level() {
            return this.level;
        }

        @Override
        public ArneAnderssonTree<T> left() {
            return this.left;
        }

        @Override
        public ArneAnderssonTree<T> right() {
            return this.right;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    public static final class AAEmptyNode<T extends Comparable<T>> extends AATree<T> {

        AAEmptyNode() {}

        @Override
        public T value() {
            throw new NoSuchElementException("AAEmptyNode");
        }

        @Override
        public ArneAnderssonTree<T> left() {
            return AAEmptyNode();
        }

        @Override
        public ArneAnderssonTree<T> right() {
            return AAEmptyNode();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int level() {
            return 0;
        }
    }

    public static final class AATreeCompanion {

        public static <T extends Comparable<T>> ArneAnderssonTree<T> AAEmptyNode() {
            return new AAEmptyNode<>();
        }

        public static <T extends Comparable<T>> ArneAnderssonTree<T> AANode(int level,
                                                                            T val,
                                                                            ArneAnderssonTree<T> left,
                                                                            ArneAnderssonTree<T> right) {
            return new AANode<>(level, val, left, right);
        }

    }
}
