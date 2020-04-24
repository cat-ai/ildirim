package io.cat.ai.ildirim.adt.linear;

import io.cat.ai.ildirim.util.IterableEx;
import io.cat.ai.ildirim.control.Monad;
import io.cat.ai.ildirim.data.Foldable;

import java.util.function.Function;

public interface Linear<T> extends IterableEx<T>, Foldable<T>, Monad<Linear<T>, T> {

    T head();

    Linear<T> tail();

    boolean isEmpty();

    default boolean nonEmpty() {
        return !isEmpty();
    }

    Linear<T> reverse();

    Linear<T> append(T e);

    Linear<T> prepend(T e);

    Linear<T> remove(T e);

    @Override
    <R> Linear<R> unit(R elem);

    @Override
    <R> Linear<R> yield(Function<T, R> f);

    @Override
    <R> Linear<R> join();

    @Override
    default <R> Linear<R> fail(RuntimeException e) {
        throw e;
    }

    @Override
    <R> Linear<R> flatMap(Function<T, ? extends Monad<?, R>> f);

    <R> Linear<R> map(Function<T, R> f);

    int size();

    default String linearToString(String prefix, Linear<T> linear, String postfix) {
        return !linear.isEmpty() ? prefix + linearToStringHelper(linear.head(), linear.tail(), "") + postfix
                : prefix + postfix;
    }

    static <T> String linearToStringHelper(T head, Linear<T> tail, String res) {
        return !tail.isEmpty() ? linearToStringHelper(tail.head(), tail.tail(), res + head + ", ") : res + head;
    }
}