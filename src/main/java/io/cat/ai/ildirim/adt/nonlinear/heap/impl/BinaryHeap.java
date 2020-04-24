package io.cat.ai.ildirim.adt.nonlinear.heap.impl;

import io.cat.ai.ildirim.adt.nonlinear.heap.Heap;
import io.cat.ai.ildirim.util.Pair;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.nonlinear.heap.impl.BinaryHeap.BinaryHeapCompanion.*;
import static io.cat.ai.ildirim.data.Ord.*;

@NoArgsConstructor
public abstract class BinaryHeap<T extends Comparable<T>> implements Heap<T> {

    private T min;
    private BinaryHeap<T> left;
    private BinaryHeap<T> right;
    private int size;
    private int height;

    BinaryHeap(T min, BinaryHeap<T> left, BinaryHeap<T> right, int size, int height) {
        this.min = min;
        this.left = left;
        this.right = right;
        this.size = size;
        this.height = height;
    }

    public abstract T min();

    @Override
    public T getAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<? extends BinaryHeap<T>, ? extends BinaryHeap<T>> splitAt(int n) {
        throw new UnsupportedOperationException();
    }

    public abstract BinaryHeap<T> left();

    public abstract BinaryHeap<T> right();

    public abstract int size();

    public abstract int height();

    public abstract boolean isEmpty();

    public BinaryHeap<T> upHeap(T e, BinaryHeap<T> left, BinaryHeap<T> right) {
        return left.nonEmpty() && ord(left.min()).greaterThan(e)

                ? HeapNodeConstructor(left.min(), HeapNodeConstructor(e, left.left(), left.right()), right)

                    : right.nonEmpty() && ord(right.min()).greaterThan(e)

                        ? HeapNodeConstructor(right.min(), left, HeapNodeConstructor(e, right.left(), right.right()))

                            : HeapNodeConstructor(e, left, right);
    }

    public BinaryHeap<T> downHeap(T e, BinaryHeap<T> left, BinaryHeap<T> right) {
        return left.nonEmpty() && right.nonEmpty() && (ord(left.min()).lessThan(right.min()) && ord(e).greaterThan(right.min()))

                ? HeapNodeConstructor(right.min(), left, downHeap(e, right.left(), right.right()))

                    : right.nonEmpty() && ord(e).greaterThan(right.min())

                        ? HeapNodeConstructor(right.min(), HeapNodeConstructor(e, left.left(), left.right()), right)

                            : HeapNodeConstructor(e, left, right);
    }

    @Override
    public BinaryHeap<T> insert(T e) {
        return isEmpty() ? HeapNode(e, HeapLeaf(), HeapLeaf(),0, 0)

                : ord(left().size()).lessThan((int) Math.pow(2, left().height()) - 1) ? upHeap(min(), left().insert(e), right())

                    : ord(right().size()).lessThan((int) Math.pow(2, right().height()) - 1) ? upHeap(min(), left(), right().insert(e))

                        : ord(right().height()).lessThan(left().height()) ? upHeap(e, left(), right().insert(e))

                            : upHeap(e, left().insert(e), right());
    }

    public <R extends Comparable<R>> BinaryHeap<R> map(Function<T, R> f) {
        return isEmpty() ? HeapLeaf() : HeapNode(f.apply(min()), left().map(f), right().map(f), size(), height());
    }

    @Override
    public <R> R fold(R z, BiFunction<R, T, R> f) {
        return foldl(z, f);
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, T, R> f) {
        return Heap.foldlLoop(this, z, f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<T, R, R> f) {
        return Heap.foldrLoop(this, z, f);
    }

    @Override
    public BinaryHeap<T> take(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BinaryHeap<T> drop(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BinaryHeap<T> slice(int from, int until) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BinaryHeap<T> filter(Predicate<T> cond) {
        throw new UnsupportedOperationException();
    }

    public static final class BinaryHeapCompanion {


        public static <R extends Comparable<R>> BinaryHeap<R> HeapNode(R min,
                                                                       BinaryHeap<R> left,
                                                                       BinaryHeap<R> right,
                                                                       int size,
                                                                       int height) {
            return new Node<>(min, left, right, size, height);
        }

        static <T extends Comparable<T>> BinaryHeap<T> HeapLeaf() {
            return new Leaf<>();
        }

        static <T extends Comparable<T>> BinaryHeap<T> HeapNodeConstructor(T min,
                                                                           BinaryHeap<T> left,
                                                                           BinaryHeap<T> right) {
            return HeapNode(min, left, right, left.size() + right.size(), Math.max(left.height(), right.height()) + 1);
        }

    }

    public static final class Node<T extends Comparable<T>> extends BinaryHeap<T> {

        private T min;
        private BinaryHeap<T> left;
        private BinaryHeap<T> right;
        private int size;
        private int height;

        Node(T min, BinaryHeap<T> left, BinaryHeap<T> right, int size, int height) {
            super(min,left, right, size, height);
            this.min = min;
            this.left = left;
            this.right = right;
            this.size = size;
            this.height = height;
        }

        @Override
        public T min() {
            return min;
        }

        @Override
        public BinaryHeap<T> left() {
            return left;
        }

        @Override
        public BinaryHeap<T> right() {
            return right;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public int height() {
            return height;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    public static final class Leaf<T extends Comparable<T>> extends BinaryHeap<T> {

        @Override
        public T min() {
            throw new NoSuchElementException("BinaryHeap.Leaf");
        }

        @Override
        public BinaryHeap<T> left() {
            return HeapLeaf();
        }

        @Override
        public BinaryHeap<T> right() {
            return HeapLeaf();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}