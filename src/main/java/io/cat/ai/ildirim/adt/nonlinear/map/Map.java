package io.cat.ai.ildirim.adt.nonlinear.map;

import io.cat.ai.ildirim.data.Foldable;
import io.cat.ai.ildirim.util.IterableEx;
import io.cat.ai.ildirim.util.Pair;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Map<K, V> extends IterableEx<Pair<K, V>>, Foldable<Pair<K, V>> {

    default Map<K, V> insert(K k, V v) {
        throw new UnsupportedOperationException();
    }

    default Map<K, V> remove(K k) {
        throw new UnsupportedOperationException();
    }

    default <K2, V2> Map<K2, V2> map(Function<Pair<K, V>, Pair<K2, V2>> f) {
        throw new UnsupportedOperationException();
    }

    default <K2, V2> Map<K2, V2> flatMap(Function<Pair<K, V>, Map<K2, V2>> f) {
        throw new UnsupportedOperationException();
    }

    default Map<K, V> filter(Predicate<Pair<K, V>> f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public default void forEach(Consumer<? super Pair<K, V>> action) {

    }
}
