package io.cat.ai.ildirim.adt.nonlinear.set;

import java.util.function.Function;

public interface SortedSet<T extends Comparable<T>> extends Set<T> {

    T max();

    T min();

    int sum();

    SortedSet<T> concat(SortedSet<T> sortedSet);

    <R extends Comparable<R>> SortedSet<R> map(Function<T, R> f);
}