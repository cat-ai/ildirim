package io.cat.ai.ildirim.adt.nonlinear.tree;

import io.cat.ai.ildirim.adt.nonlinear.tree.util.TreeIterator;
import io.cat.ai.ildirim.util.Pair;
import lombok.val;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.nonlinear.tree.BSTree.BinarySearchTreeCompanion.*;
import static io.cat.ai.ildirim.data.Ord.*;

public abstract class BSTree<T extends Comparable<T>> implements Tree<T> {

    private T value;
    private BSTree<T> left;
    private BSTree<T> right;

    BSTree(T val, BSTree<T> left, BSTree<T> right) {
        this.value = val;
        this.left = left;
        this.right = right;
    }

    BSTree() {}

    public abstract T value();

    public abstract BSTree<T> left();

    public abstract BSTree<T> right();

    public abstract boolean isEmpty();

    @Override
    public T getAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T min() {
        return Tree.min(this);
    }

    @Override
    public T max() {
        return Tree.max(this);
    }

    @Override
    public Pair<BSTree<T>, BSTree<T>> splitAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BSTree<T> filter(Predicate<T> cond) {
        throw new UnsupportedOperationException();
    }

    public BSTree<T> insert(T e) {

        return isEmpty() ? TreeNodeWithLeafSubtrees(e)

                : ord(e).lessThan(value()) ? TreeNode(value(), left().insert(e), right())

                : ord(e).greaterThan(value()) ? TreeNode(value(), left(), right().insert(e))

                : this;
    }

    public <R extends Comparable<R>> BSTree<R> map(Function<T, R> f) {
        return isEmpty() ? Leaf() : TreeNode(f.apply(value()), left().map(f), right().map(f));
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
        return new TreeIterator<>(this);
    }

    @Override
    public BSTree<T> take(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BSTree<T> drop(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BSTree<T> slice(int from, int until) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    public static final class BinarySearchTreeCompanion {

        public static <R extends Comparable<R>> BSTree<R> TreeNodeWithLeafSubtrees(R val) {
            return new Node<>(val, Leaf(), Leaf());
        }

        public static <R extends Comparable<R>> BSTree<R> Leaf() {
            return new Leaf<>();
        }

        public static <R extends Comparable<R>> BSTree<R> TreeNode(R val,
                                                                   BSTree<R> left,
                                                                   BSTree<R> right) {
            return new Node<>(val, left, right);
        }

        public static <R extends Comparable<R>> BSTree<R> BSTree(R... rs) {
            BSTree<R> bsTree = Leaf();

            for (val r: rs)
                bsTree = bsTree.insert(r);

            return bsTree;
        }
    }

    public static final class Node<T extends Comparable<T>> extends BSTree<T> {

        private T val;
        private BSTree<T> left;
        private BSTree<T> right;

        Node(T val, BSTree<T> left, BSTree<T> right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        @Override
        public T value() {
            return val;
        }

        @Override
        public BSTree<T> left() {
            return left;
        }

        @Override
        public BSTree<T> right() {
            return right;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return new TreeIterator<>(this);
        }
    }

    public static final class Leaf<T extends Comparable<T>> extends BSTree<T> {

        @Override
        public T value() {
            throw new NoSuchElementException("Leaf");
        }

        @Override
        public BSTree<T> left() {
            return Leaf();
        }

        @Override
        public BSTree<T> right() {
            return Leaf();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
