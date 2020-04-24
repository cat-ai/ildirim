package io.cat.ai.ildirim.adt.nonlinear.set.sorted;

import io.cat.ai.ildirim.adt.nonlinear.set.Set;
import io.cat.ai.ildirim.adt.nonlinear.set.SortedSet;
import io.cat.ai.ildirim.adt.nonlinear.tree.Tree;

import io.cat.ai.ildirim.util.IterableEx;
import io.cat.ai.ildirim.util.Pair;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.nonlinear.set.sorted.AbstractSortedSet.SortedSetImplCompanion.*;

@AllArgsConstructor
public abstract class AbstractSortedSet<T extends Comparable<T>> implements SortedSet<T> {

    private Tree<T> treeRepr;

    @Override
    public Set<T> add(T e) {
        return SortedSet(treeRepr.insert(e));
    }

    @Override
    public Set<T> remove(T e) {
        return SortedSet(treeRepr.delete(e));
    }

    @Override
    public <R> R fold(R z, BiFunction<R, T, R> f) {
        return treeRepr.fold(z, f);
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, T, R> f) {
        return treeRepr.foldl(z, f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<T, R, R> f) {
        return treeRepr.foldr(z, f);
    }

    @Override
    public AbstractSortedSet<T> slice(int from, int until) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T getAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<AbstractSortedSet<T>, AbstractSortedSet<T>> splitAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AbstractSortedSet<T> filter(Predicate<T> cond) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int sum() {
        return 0;
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public Set<T> difference(Set<T> that) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IterableEx<T> drop(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IterableEx<T> take(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<T> intersection(Set<T> that) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<T> concat(SortedSet<T> sortedSet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R extends Comparable<R>> SortedSet<R> map(Function<T, R> f) {
        return SortedSet(treeRepr.map(f));
    }

    public <R extends Comparable<R>> SortedSet<R> flatMap(Function<T, ? extends SortedSet<R>> f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T max() {
        return treeRepr.max();
    }

    @Override
    public T min() {
        return treeRepr.min();
    }

    private final static class SortedSetImpl<T extends Comparable<T>> extends AbstractSortedSet<T> {

        public SortedSetImpl(Tree<T> tree) {
            super(tree);
        }
    }


    public static final class SortedSetImplCompanion {

        public static <T extends Comparable<T>> SortedSet<T> SortedSet(Tree<T> tree) {
            return new SortedSetImpl<>(tree);
        }
    }
}