package io.cat.ai.ildirim.control;

import java.util.function.Function;

public interface Functor<F, T> {

    <R> Function<? extends Functor<F, T>, ? extends Functor<?, R>> fmap(Function<T, R> f);
}