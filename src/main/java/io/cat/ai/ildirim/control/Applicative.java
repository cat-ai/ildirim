package io.cat.ai.ildirim.control;

import java.util.function.Function;

public interface Applicative<F, T> extends Functor<F, T> {

    <R> R get();

    <R> Applicative<?, R> unit(R value);

    default <R> Function<Applicative<F, T>, Applicative<?, R>> apply(Applicative<Function<T, R>, R> appF) {
        return F_T -> {
            Function<T, R> f = appF.get();
            return unit(f.apply(F_T.get()));
        };
    }
}