package io.cat.ai.ildirim.data;

import java.util.function.BiFunction;

public interface Foldable<T> {

    <R> R fold(R z, BiFunction<R, T, R> f);

    <R> R foldl(R z, BiFunction<R, T, R> f);

    <R> R foldr(R z, BiFunction<T, R, R> f);
}