package io.cat.ai.ildirim.util;

import java.util.function.Predicate;

public interface IterableEx<T> extends Iterable<T> {

    IterableEx<T> take(int n);

    IterableEx<T> drop(int n);

    Pair<? extends IterableEx<T>, ? extends IterableEx<T>> splitAt(int n);

    T getAt(int n);

    IterableEx<T> slice(int from, int until);

    IterableEx<T> filter(Predicate<T> cond);

    boolean contains(T e);
}