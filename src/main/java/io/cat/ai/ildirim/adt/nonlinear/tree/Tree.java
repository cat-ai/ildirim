package io.cat.ai.ildirim.adt.nonlinear.tree;

import io.cat.ai.ildirim.adt.linear.Linear;
import io.cat.ai.ildirim.adt.linear.List;
import io.cat.ai.ildirim.adt.linear.Queue;
import io.cat.ai.ildirim.data.Foldable;
import io.cat.ai.ildirim.util.IterableEx;

import java.util.function.BiFunction;
import java.util.function.Function;

import static io.cat.ai.ildirim.adt.linear.List.ListCompanion.Nil;

public interface Tree<T extends Comparable<T>> extends IterableEx<T>, Foldable<T>/*, Monad<Tree<T>, T>*/ {

    T value();

    Tree<T> left();

    Tree<T> right();

    Tree<T> insert(T e);

    default Tree<T> delete(T e) {
        throw new UnsupportedOperationException();
    }

    T min();

    T max();

    <R extends Comparable<R>> Tree<R> map(Function<T, R> f);

    boolean isEmpty();

    default boolean nonEmpty() {
        return !isEmpty();
    }

    static <T extends Comparable<T>, R> R foldLoop(Tree<T> tree,
                                                   R z,
                                                   BiFunction<R, T, R> f) {
        return tree.isEmpty() ? z
                : foldlLoop(tree.right(), f.apply(foldLoop(tree.left(), z, f), tree.value()), f);
    }

    static <T extends Comparable<T>, R> R foldlLoop(Tree<T> tree,
                                                    R z,
                                                    BiFunction<R, T, R> f) {
        return tree.isEmpty() ? z
                : foldlLoop(tree.right(), foldlLoop(tree.left(), f.apply(z, tree.value()), f), f);
    }

    static <T extends Comparable<T>, R> R foldrLoop(Tree<T> tree,
                                                    R z,
                                                    BiFunction<T, R, R> f) {
        return tree.isEmpty() ? z
                : foldrLoop(tree.left(), foldrLoop(tree.right(), f.apply(tree.value(), z), f), f);
    }

    static <T extends Comparable<T>> T min(Tree<T> tree) {
        return tree.isEmpty() ? tree.value() : min(tree.left());
    }

    static <T extends Comparable<T>> T max(Tree<T> tree) {
        return tree.isEmpty() ? tree.value() : max(tree.right());
    }
}