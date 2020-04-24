package io.cat.ai.ildirim.adt.nonlinear.tree.control;

import java.util.function.Function;

public interface TreeFunctor<F, T> {

    <R extends Comparable<R>> Function<? extends TreeFunctor<F, T>, ? extends TreeFunctor<?, R>> fmap(Function<T, R> f);
}