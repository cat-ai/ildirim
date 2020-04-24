package io.cat.ai.ildirim.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Lazy<T> {

    private Supplier<T> valueSup;

    private Lazy(Supplier<T> valueSup) {
        this.valueSup = valueSup;
    }

    public T eval() {
        return compute();
    }

    private synchronized T compute() {
        val T = valueSup.get();
        requireNonNull(T);

        return T;
    }

    public static <T> Lazy<T> lazy(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }
}