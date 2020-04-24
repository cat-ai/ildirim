package io.cat.ai.ildirim.data;

public interface Eq<T> {

    boolean eqv(T a, T b);

    default boolean neqv(T a, T b) {
        return !eqv(a, b);
    }
}