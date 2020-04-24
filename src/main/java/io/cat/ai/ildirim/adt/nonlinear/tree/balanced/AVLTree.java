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

import static io.cat.ai.ildirim.adt.nonlinear.tree.balanced.AVLTree.AVLTreeCompanion.*;
import static io.cat.ai.ildirim.data.Ord.ord;

public abstract class AVLTree<T extends Comparable<T>> implements AdelsonVelskyLandisTree<T> {

    private T value;
    private AdelsonVelskyLandisTree<T> left;
    private AdelsonVelskyLandisTree<T> right;

    private AVLTree() {}

    AVLTree(T value, AdelsonVelskyLandisTree<T> left, AdelsonVelskyLandisTree<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public AdelsonVelskyLandisTree<T> concat(AdelsonVelskyLandisTree<T> that) {
        return isEmpty() ? that
                : that.isEmpty() ? AVLLeaf()
                    : right().concat(that).concat(left()).insert(value());
    }

    @Override
    public T getAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<AdelsonVelskyLandisTree<T>, AdelsonVelskyLandisTree<T>> splitAt(int n) {
        throw new UnsupportedOperationException();
    }

    private int depth(Tree<T> tree) {
        return tree.isEmpty() ? 0
                : Math.max(depth(tree.left()), depth(tree.right())) + 1;
    }

    @Override
    public int balance(Tree<T> tree) {
        return tree.isEmpty() ? 0
                : depth(tree.left()) - depth(tree.right());
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

        return Tree.min(this);
    }

    public <R extends Comparable<R>> AdelsonVelskyLandisTree<R> map(Function<T, R> f) {
        return isEmpty() ? AVLLeaf()
                : left().map(f).concat(right().map(f)).insert(f.apply(value()));
    }

    public <R extends Comparable<R>> AdelsonVelskyLandisTree<R> flatMap(Function<T, AdelsonVelskyLandisTree<R>> f) {
        return isEmpty() ? AVLLeaf()
                : left().flatMap(f).concat(right().flatMap(f)).concat(f.apply(value()));
    }

    @Override
    public boolean isAVLTree(Tree<T> tree) {
        if (tree.isEmpty())
            return true;
        else if (tree.left().isEmpty() && tree.right().isEmpty())
            return true;
        else {
            val treeBalance = balance(tree);
            val isBalanced = treeBalance <= 1 && -1 <= treeBalance;

            val left = tree.left();
            val right = tree.right();
            val value = tree.value();

            val isLeftAVL = left.isEmpty() || isAVLTree(left) && ord(left.max()).lessThan(value);
            val isrightAVL = right.isEmpty() || isAVLTree(right) && ord(right.max()).greaterThan(value);

            return isBalanced && isLeftAVL && isrightAVL;
        }
    }

    @Override
    public AdelsonVelskyLandisTree<T> insert(T e) {
        if (isAVLTree(this))
            throw new IllegalArgumentException("Tree is not AVL");

        if (isEmpty())
            return repair(AVLNode(e));
        else {
            val left = left();
            val right = right();
            val value = value();

            val ordValue = ord(value);

            val tree =  ordValue.equiv(e) ? this
                            : ordValue.greaterThan(e) ? AVLNode(value, left.insert(e), right)
                                : AVLNode(value, left, right.insert(e));

            return repair(tree);
        }
    }

    @Override
    public AdelsonVelskyLandisTree<T> repair(AdelsonVelskyLandisTree<T> tree) {
        val left = tree.left();
        val right = tree.right();
        val value = tree.value();

        val treeBalance = balance(tree);

        switch (treeBalance) {
            case -2:
                val balancedTreeRight = ord(balance(right)).lessThan(0) ? AVLNode(value, left, rotateRight(tree, tree.value(), left, right)) : tree;

                return rotateLeft(balancedTreeRight, balancedTreeRight.value(), balancedTreeRight.left(), balancedTreeRight.right());
            case 2:
                val balancedTreeLeft = ord(balance(left)).lessThan(0) ? AVLNode(value, rotateLeft(tree, tree.value(), left, right), right) : tree;

                return rotateRight(balancedTreeLeft, balancedTreeLeft.value(), balancedTreeLeft.left(), balancedTreeLeft.right());
            default:
                return tree;
        }
    }

    @Override
    public AVLTree<T> delete(T e) {
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
    public AVLTree<T> take(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AVLTree<T> drop(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AVLTree<T> slice(int from, int until) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AVLTree<T> filter(Predicate<T> cond) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator<>(this);
    }

    @Override
    public AdelsonVelskyLandisTree<T> rotateLeft(AdelsonVelskyLandisTree<T> tree,
                                                 T val,
                                                 AdelsonVelskyLandisTree<T> left,
                                                 AdelsonVelskyLandisTree<T> right) {
        if (tree.nonEmpty() && right.nonEmpty())
            return AVLNode(right.value(), AVLNode(val, left, right.left()), right);

        throw new IllegalArgumentException("Not suitable for left balancing");
    }

    @Override
    public AdelsonVelskyLandisTree<T> rotateRight(AdelsonVelskyLandisTree<T> tree,
                                                  T val,
                                                  AdelsonVelskyLandisTree<T> left,
                                                  AdelsonVelskyLandisTree<T> right) {
        if (tree.nonEmpty() && left.nonEmpty())
            return AVLNode(left.value(), left, AVLNode(val, left.right(), right));

        throw new IllegalArgumentException("Not suitable for right balancing");
    }

    public static final class AVLTreeCompanion {

        public static <T extends Comparable<T>> AVLTree<T> AVLLeaf() {
            return new AVLLeaf<>();
        }

        public static <T extends Comparable<T>> AVLTree<T> AVLNode(T val,
                                                                   AdelsonVelskyLandisTree<T> left,
                                                                   AdelsonVelskyLandisTree<T> right) {
            return new AVLNode<>(val, left, right);
        }

        public static <T extends Comparable<T>> AVLTree<T> AVLNode(T val) {
            return new AVLNode<>(val, AVLLeaf(), AVLLeaf());
        }

    }

    public static final class AVLNode<T extends Comparable<T>> extends AVLTree<T> {

        private T value;
        private AdelsonVelskyLandisTree<T> left;
        private AdelsonVelskyLandisTree<T> right;

        AVLNode(T value, AdelsonVelskyLandisTree<T> left, AdelsonVelskyLandisTree<T> right) {
            super(value, left, right);
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public AdelsonVelskyLandisTree<T> left() {
            return left;
        }

        @Override
        public AdelsonVelskyLandisTree<T> right() {
            return right;
        }
    }

    public static final class AVLLeaf<T extends Comparable<T>> extends AVLTree<T> {

        @Override
        public T value() {
            throw new NoSuchElementException("AVLLeaf");
        }

        @Override
        public AdelsonVelskyLandisTree<T> left() {
            return AVLLeaf();
        }

        @Override
        public AdelsonVelskyLandisTree<T> right() {
            return AVLLeaf();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}