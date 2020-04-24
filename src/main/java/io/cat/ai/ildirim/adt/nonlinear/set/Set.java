package io.cat.ai.ildirim.adt.nonlinear.set;

import io.cat.ai.ildirim.data.Foldable;
import io.cat.ai.ildirim.util.IterableEx;

import java.util.function.Function;

public interface Set<T> extends IterableEx<T>, Foldable<T>/*, Monad<Set<T>, T>*/ {

    Set<T> add(T e);

    Set<T> remove(T e);

    int capacity();

    Set<T> intersection(Set<T> that);

    Set<T> difference(Set<T> that);

    <R extends Comparable<R>> Set<R> map(Function<T, R> f);
}