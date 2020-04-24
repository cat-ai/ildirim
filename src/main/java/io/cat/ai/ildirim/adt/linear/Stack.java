package io.cat.ai.ildirim.adt.linear;

import io.cat.ai.ildirim.adt.linear.util.LinearIterator;
import io.cat.ai.ildirim.control.Monad;
import io.cat.ai.ildirim.util.Pair;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.linear.List.ListCompanion.*;
import static io.cat.ai.ildirim.adt.linear.Stack.StackCompanion.*;

@SuppressWarnings("unchecked")
public abstract class Stack<T> implements Linear<T> {

    private List<T> elems;

    Stack(List<T> list) {
        this.elems = list;
    }

    @Override
    public T head() {
        return top();
    }

    @Override
    public Stack<T> tail() {
        return rest();
    }

    @Override
    public Stack<T> append(T e) {
        return Stack(elems.append(e));
    }

    @Override
    public Stack<T> prepend(T e) {
        return push(e);
    }

    @Override
    public Stack<T> remove(T e) {
        return this;
    }

    public Stack<T> concat(Stack<T> that) {
        return isEmpty() ? that
                : rest().concat(that).prepend(top());
    }

    @Override
    public Stack<T> reverse() {
        return reverseLoop(this, Stack());
    }

    @Override
    public T getAt(int n) {
        if (isEmpty() || n < 0)
            throw new IndexOutOfBoundsException();
        else
            return elems.getAt(n);
    }

    @Override
    public Pair<Stack<T>, Stack<T>> splitAt(int n) {
        return isEmpty() ? Pair.of(Stack(), Stack())
                : Pair.of(take(n), drop(n));
    }

    @Override
    public Iterator<T> iterator() {
        return new LinearIterator<>(this);
    }

    @Override
    public Stack<T> take(int n) {
        return isEmpty() || n == 0 ? Stack()
                : n == 1 ? Stack(top())
                    : Stack(elems.take(n));
    }

    @Override
    public Stack<T> drop(int n) {
        return !isEmpty() && n >= 1
                ? rest().drop(n - 1)
                    : n == 1 ?
                        Stack(top())
                            : this;
    }

    @Override
    public Stack<T> slice(int from, int until) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    public abstract T top();

    public abstract Stack<T> rest();

    public abstract Pair<T, Stack<T>> pop();

    public abstract Stack<T> push(T e);

    public <R> R get() {
        return (R) this;
    }

    @Override
    public <R> Stack<R> unit(R elem) {
        return Stack(elem);
    }

    @Override
    public <R> Stack<R> yield(Function<T, R> f) {
        return foldl(Stack(), (acc, e) -> acc.append(f.apply(e)));
    }

    @Override
    public <R> Stack<R> join() {
        return foldl(Stack(), (acc, nested) -> acc.concat((Stack<R>)nested));
    }

    @Override
    public <R> Stack<R> fail(RuntimeException e) {
        throw e;
    }

    @Override
    public <R> Stack<R> flatMap(Function<T, ? extends Monad<?, R>> f) {
        return isEmpty() ? Stack()
                : ((Stack<R>)f.apply(head())).concat(tail().flatMap(f));
    }

    public <R> Stack<R> map(Function<T, R> f) {
        return Stack(elems.map(f));
    }

    public Stack<T> filter(Predicate<T> cond) {
        return Stack(elems.filter(cond));
    }

    @Override
    public <R> R fold(R z, BiFunction<R, T, R> f) {
        return elems.fold(z, f);
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, T, R> f) {
        return elems.foldl(z, f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<T, R, R> f) {
        return elems.foldr(z, f);
    }

    @Override
    public int size() {
        return isEmpty() ? 0 : 1 + tail().size();
    }

    private static final class StackImpl<T> extends Stack<T> {

        private List<T> list;

        private StackImpl(List<T> list) {
            super(list);
            this.list = list;
        }

        @Override
        public T top() {
            return list.head();
        }

        @Override
        public Stack<T> rest() {
            return Stack(list.tail());
        }

        @Override
        public Pair<T, Stack<T>> pop() {
            return Pair.of(top(), rest());
        }

        @Override
        public Stack<T> push(T e) {
            return Stack(list.prepend(e));
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return new LinearIterator<>(this);
        }
    }

    @Override
    public String toString() {
        return linearToString("Stack(",  this,")");
    }

    public static final class StackCompanion {

        @SafeVarargs
        public static <T> Stack<T> Stack(T ...ts) {
            return Stack(List(ts));
        }

        public static <T> Stack<T> Stack(T t) {
            return Stack(List(t));
        }

        public static <T> Stack<T> Stack(List<T> list) {
            return new StackImpl<>(list);
        }

        public static <T> Stack<T> Stack() {
            return new StackImpl<>(Nil());
        }

        static <T> Stack<T> reverseLoop(Stack<T> s, Stack<T> d) {
            return s.isEmpty() ? d : reverseLoop(s.tail(), d.prepend(s.head()));
        }
    }
}