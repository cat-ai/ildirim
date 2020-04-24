package io.cat.ai.ildirim.adt.nonlinear.tree.balanced;

import io.cat.ai.ildirim.adt.nonlinear.tree.Tree;
import io.cat.ai.ildirim.adt.nonlinear.tree.util.TreeIterator;

import io.cat.ai.ildirim.util.Pair;
import lombok.val;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.nonlinear.tree.balanced.RedBlackTree.Color.*;

import static io.cat.ai.ildirim.adt.nonlinear.tree.balanced.RBTree.RedBlackTreeCompanion.*;
import static io.cat.ai.ildirim.data.Ord.ord;

public abstract class RBTree<T extends Comparable<T>> implements RedBlackTree<T> {

    public abstract Color color();

    public abstract T value();

    public abstract RedBlackTree<T> left();

    public abstract RedBlackTree<T> right();

    public abstract boolean isEmpty();

    public RedBlackTree<T> concat(RedBlackTree<T> that) {
        return isEmpty() ? that
                : that.isEmpty() ? Leaf()
                    : right().concat(that).concat(left()).insert(value());
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
    public T getAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<RBTree<T>, RBTree<T>> splitAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RedBlackTree<T> filter(Predicate<T> cond) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RedBlackTree<T> insert(T e) {
        return insert(e, this);
    }

    private RBTree<T> insert(T value, RBTree<T> tree) {
        val rbTree = insertAndBalance(value, tree);
        return Node(BLACK, rbTree.value(), rbTree.left(), rbTree.right());
    }

    private RedBlackTree<T> insertAndBalance(T value, RedBlackTree<T> tree) {
        return tree.isEmpty() ? RedNode(value)

                : ord(value).lessThan(tree.value()) ?
                rotateLeft(
                        tree,
                        tree.value(),
                        insertAndBalance(value, tree.left()), tree.right())

                : ord(value).greaterThan(tree.value()) ?
                rotateRight(
                        tree,
                        tree.value(),
                        tree.left(), insertAndBalance(value, tree.right()))

                : tree;
    }

    public RBTree<T> rotateLeft(RedBlackTree<T> tree,
                                T val,
                                RedBlackTree<T> left,
                                RedBlackTree<T> right) {
        return balanceLeft(tree.color(), val, left, right);
    }

    public RBTree<T> rotateRight(RedBlackTree<T> tree,
                                 T val,
                                 RedBlackTree<T> left,
                                 RedBlackTree<T> right) {
        return balanceRight(tree.color(), val, left, right);
    }

    private RBTree<T> balanceLeft(Color color,
                                  T val,
                                  RedBlackTree<T> left,
                                  RedBlackTree<T> right) {
        switch (color) {
            case BLACK:
                if (left.color() == RED && left.left().color() == RED)
                    return Node(
                            RED,
                            left.value(),
                            Node(BLACK, left.left().value(), left.left().left(), left.left().right()),
                            Node(BLACK, val, left.right(), right)
                    );
                else if (left.color() == RED && left.right().color() == RED) {
                    return Node(
                            RED,
                            left.right().value(),
                            Node(BLACK, left.value(), left.left(), left.right().left()),
                            Node(BLACK, val, left.right().right(), right)
                    );
                }
            case RED:
            default:
                return Node(color, val, left, right);
        }
    }

    private RBTree<T> balanceRight(Color color,
                                   T val,
                                   RedBlackTree<T> left,
                                   RedBlackTree<T> right) {
        switch (color) {
            case BLACK:
                if (right.color() == RED && right.right().color() == RED)
                    return Node(
                            RED,
                            right.value(),
                            Node(BLACK, val, left, right.left()),
                            Node(BLACK, right.right().value(), right.right().left(), right.right().right())
                    );
                else if (left.color() == RED && right.left().color() == RED) {
                    return Node(
                            RED,
                            left.right().value(),
                            Node(BLACK, val, left, left.left().left()),
                            Node(BLACK, left.value(), left.left().right(), right)
                    );
                }
            case RED:
            default:
                return Node(color, val, left, right);
        }
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

    public <R extends Comparable<R>> RedBlackTree<R> map(Function<T, R> f) {
        return isEmpty() ? Leaf()
                : left().map(f).concat(right().map(f)).insert(f.apply(value()));
    }

    @Override
    public <R extends Comparable<R>> RedBlackTree<R> flatMap(Function<T, RedBlackTree<R>> f) {
        return isEmpty() ? Leaf()
                : left().flatMap(f).concat(right().flatMap(f)).concat(f.apply(value()));
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator<>(this);
    }

    @Override
    public RBTree<T> take(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RBTree<T> drop(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RBTree<T> slice(int from, int until) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    public static final class RedBlackTreeCompanion {

        static <T extends Comparable<T>> RBTree<T> Node(Color color,
                                                        T val,
                                                        RedBlackTree<T> left,
                                                        RedBlackTree<T> right) {
            return new Node<>(color, val, left, right);
        }

        public static <R extends Comparable<R>> RBTree<R> Leaf() {
            return new Leaf<>();
        }

        static <R extends Comparable<R>> RBTree<R> RedNode(R val) {
            return new Node<>(Color.RED, val, Leaf(), Leaf());
        }

        public static <R extends Comparable<R>> RBTree<R> RBTree(R val) {
            return RedNode(val);
        }
    }

    public static final class Node<T extends Comparable<T>> extends RBTree<T> {

        private Color color;
        private T val;
        private RedBlackTree<T> left;
        private RedBlackTree<T> right;

        Node(Color color, T val, RedBlackTree<T> left, RedBlackTree<T> right) {
            this.color = color;
            this.val = val;
            this.left = left;
            this.right = right;
        }

        @Override
        public Color color() {
            return color;
        }

        @Override
        public T value() {
            return val;
        }

        @Override
        public RedBlackTree<T> left() {
            return left;
        }

        @Override
        public RedBlackTree<T> right() {
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

    public static final class Leaf<T extends Comparable<T>> extends RBTree<T> {

        @Override
        public Color color() {
            return Color.BLACK;
        }

        @Override
        public T value() {
            throw new NoSuchElementException("Leaf");
        }

        @Override
        public RBTree<T> left() {
            return RedBlackTreeCompanion.Leaf();
        }

        @Override
        public RBTree<T> right() {
            return RedBlackTreeCompanion.Leaf();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public T next() {
                    throw new NoSuchElementException("RBTree.Leaf");
                }
            };
        }

        @Override
        public String toString() {
            return "Leaf";
        }
    }
}