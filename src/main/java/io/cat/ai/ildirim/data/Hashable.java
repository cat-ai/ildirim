package io.cat.ai.ildirim.data;

@FunctionalInterface
public interface Hashable<T> {
    int hash(T t);
}