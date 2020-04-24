package io.cat.ai.ildirim.adt.nonlinear.tree.balanced;

import io.cat.ai.ildirim.adt.nonlinear.tree.Tree;

import java.util.function.Function;

/***
 *
 * Self-balancing (or height-balanced) binary search tree is any node-based binary search tree that automatically keeps
 * its height (maximal number of levels below the root) small in the face of arbitrary item insertions and deletions
 */
public interface SelfBalancingTree<T extends Comparable<T>, SBTree extends Tree<T>> extends Tree<T> {

    SBTree left();

    SBTree right();

    SBTree rotateLeft(SBTree tree,
                      T val,
                      SBTree left,
                      SBTree right);

    SBTree rotateRight(SBTree tree,
                       T val,
                       SBTree left,
                       SBTree right);

    <R extends Comparable<R>> Tree<R> map(Function<T, R> f);
}