package io.cat.ai.ildirim.control;

import lombok.val;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public interface Monad<M, T> extends Applicative<M, T> {

    default <R> Monad<?, R> yield(Function<T, R> f) {
        val appF = (Applicative<Function<T, R>, R>) unit(f);
        return (Monad<?, R>) apply(appF).apply(this);
    }

    default <R> Monad<?, R> join() {
        return get();
    }

    default <R> Monad<?, R> fail(RuntimeException e) {
        throw e;
    }

    default <R> Function<Monad<M, T>, Monad<?, R>> fmap(Function<T, R> f) {
        return MT -> {
            try {
                return MT.yield(f);
            } catch(RuntimeException e) {
                return MT.fail(e);
            }
        };
    }

    default <R> Monad<?, R> map(Function<T, R> f) {
        return fmap(f).apply(this);
    }

    default <R> Monad<?, R> flatMap(Function<T, ? extends Monad<?, R>> f) {
        return map(f).join();
    }
}