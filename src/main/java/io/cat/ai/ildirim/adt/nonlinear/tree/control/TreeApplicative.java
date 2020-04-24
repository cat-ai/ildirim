package io.cat.ai.ildirim.adt.nonlinear.tree.control;

import java.util.function.Function;

public interface TreeApplicative<F, T> extends TreeFunctor<F, T> {

    <R extends Comparable<R>> R get();

    <R extends Comparable<R>> TreeApplicative<?, R> unit(R value);

    @Override
    <R extends Comparable<R>> Function<? extends TreeApplicative<F, T>, ? extends TreeApplicative<?, R>> fmap(Function<T, R> f);

    default <R extends Comparable<R>> Function<TreeApplicative<F, T>, TreeApplicative<?, R>> apply(TreeApplicative<Function<T, R>, R> appF) {
        return F_T -> {
            Function<T, R> f = appF.get();
            return unit(f.apply(F_T.get()));
        };
    }
}