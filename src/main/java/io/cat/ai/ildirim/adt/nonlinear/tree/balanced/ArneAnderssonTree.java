package io.cat.ai.ildirim.adt.nonlinear.tree.balanced;

import java.util.function.Function;

public interface ArneAnderssonTree<T extends Comparable<T>> extends SelfBalancingTree<T, ArneAnderssonTree<T>> {

    int level();

    ArneAnderssonTree<T> insert(T e);

    ArneAnderssonTree<T> delete(T e);

    /**
     * Removes a left horizontal link
     * Might create consecutive right horizontal links
     *
     *        Before skew()                After skew()
     *
     *        P ------- X                 P ---------- X
     *       /\          \               /            /\
     *      /  \          \             /            /  \
     *     /    \          \           /            /    \
     *    A      B          C         A            B      C
     *
     * */
    ArneAnderssonTree<T> skew();

    /**
     * Remove consecutive horizontal links
     *
     *
     *           Before split()                 After split()
     *
     *        X ------- P ------- G                 P
     *       /         /                           /\
     *      /         /                           /  \
     *     /         /                           /    \
     *    A         B                           X      G
     *                                         /\
     *                                        /  \
     *                                       /    \
     *                                      A      B
     * */
    ArneAnderssonTree<T> split();

    ArneAnderssonTree<T> concat(ArneAnderssonTree<T> that);

    <R extends Comparable<R>> ArneAnderssonTree<R> map(Function<T, R> f);

    <R extends Comparable<R>> ArneAnderssonTree<R> flatMap(Function<T, ArneAnderssonTree<R>> f);
}