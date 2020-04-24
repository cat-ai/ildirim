package io.cat.ai.ildirim.adt.nonlinear.tree.balanced;

import io.cat.ai.ildirim.adt.nonlinear.tree.Tree;

import java.util.function.Function;

public interface AdelsonVelskyLandisTree<T extends Comparable<T>> extends SelfBalancingTree<T, AdelsonVelskyLandisTree<T>> {

    boolean isAVLTree(Tree<T> tree);

    int balance(Tree<T> tree);

    AdelsonVelskyLandisTree<T> insert(T e);

    AdelsonVelskyLandisTree<T> delete(T e);

    AdelsonVelskyLandisTree<T> repair(AdelsonVelskyLandisTree<T> t);

    <R extends Comparable<R>> AdelsonVelskyLandisTree<R> map(Function<T, R> f);

    <R extends Comparable<R>> AdelsonVelskyLandisTree<R> flatMap(Function<T, AdelsonVelskyLandisTree<R>> f);

    AdelsonVelskyLandisTree<T> concat(AdelsonVelskyLandisTree<T> that);
}