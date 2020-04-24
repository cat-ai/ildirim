package io.cat.ai.ildirim.adt.nonlinear.tree.util;

import io.cat.ai.ildirim.adt.nonlinear.tree.Tree;

import lombok.AllArgsConstructor;

import java.util.Iterator;

@AllArgsConstructor
public class TreeIterator<T extends Comparable<T>> implements Iterator<T> {

    private Tree<T> tree;

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new UnsupportedOperationException();
    }
}