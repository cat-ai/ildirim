package io.cat.ai.ildirim.adt.nonlinear.heap;

import io.cat.ai.ildirim.util.IterableEx;
import io.cat.ai.ildirim.data.Foldable;

import java.util.function.BiFunction;

public interface Heap<T extends Comparable<T>> extends Foldable<T>, IterableEx<T> {

    T min();

    int height();

    Heap<T> left();

    Heap<T> right();

    boolean isEmpty();

    default boolean nonEmpty() {
        return !isEmpty();
    }

    Heap<T> insert(T elem);

    static <T extends Comparable<T>, R> R foldlLoop(Heap<T> tree,
                                                    R z,
                                                    BiFunction<R, T, R> f) {
        return tree.isEmpty() ? z
                :
                foldlLoop(tree.right(),
                        foldlLoop(tree.left(),
                                f.apply(z, tree.min()), f),
                        f);
    }

    static <T extends Comparable<T>, R> R foldrLoop(Heap<T> tree,
                                                    R z,
                                                    BiFunction<T, R, R> f) {
        return tree.isEmpty() ? z
                :
                f.apply(tree.min(),
                        foldrLoop(tree.left(),
                                foldrLoop(tree.right(),
                                        f.apply(tree.min(), z), f),
                                f));
    }
}