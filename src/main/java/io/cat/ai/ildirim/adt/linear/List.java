package io.cat.ai.ildirim.adt.linear;

import io.cat.ai.ildirim.adt.linear.util.LinearIterator;
import io.cat.ai.ildirim.control.Monad;

import io.cat.ai.ildirim.util.Pair;

import lombok.val;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.linear.List.ListCompanion.*;

@SuppressWarnings("unchecked")
public abstract class List<T> implements Linear<T> {

    public abstract T head();

    public abstract List<T> tail();

    public abstract boolean isEmpty();

    public abstract List<T> concat(List<T> that);

    @Override
    public int size() {
        return isEmpty() ? 0 : 1 + tail().size();
    }

    public int length() {
        return isEmpty() ? 0 : 1 + tail().length();
    }

    @Override
    public Pair<List<T>, List<T>> splitAt(int n) {
        return isEmpty() ? Pair.of(Nil(), Nil())
                : Pair.of(take(n), drop(n));
    }

    @Override
    public T getAt(int n) {
        if (n < 0 || length() == 0)
            throw new IndexOutOfBoundsException();

        val dropped = drop(n);
        return dropped.nonEmpty() ? dropped.head() : getAt(0);
    }

    @Override
    public List<T> append(T e) {
        return isEmpty() ? Cons(e, this)
                : Cons(head(), tail().append(e));
    }

    @Override
    public List<T> prepend(T e) {
        return Cons(e, this);
    }

    @Override
    public List<T> remove(T e) {
        return isEmpty() ? this
                : !e.equals(head())
                    ? Cons(head(), tail().remove(e))
                        : tail();
    }

    @Override
    public List<T> take(int n) {
        return isEmpty() || n == 0 ? Nil()
                : Cons(head(), tail().take(n - 1));
    }

    @Override
    public List<T> drop(int n) {
        return !isEmpty() && n >= 1
                ? tail().drop(n - 1)
                    : n == 1 ?
                        Cons(head(), Nil())
                            : this;
    }

    @Override
    public boolean contains(T e) {
        return !isEmpty() && (e.equals(head()) || tail().contains(e));
    }

    @Override
    public <R> R get() {
        return (R) this;
    }

    @Override
    public <R> List<R> unit(R elem) {
        return Cons(elem, Nil());
    }

    @Override
    public <R> List<R> yield(Function<T, R> f) {
        return foldl(Nil(), (acc, e) -> acc.append(f.apply(e)));
    }

    @Override
    public <R> List<R> join() {
        return foldl(Nil(), (acc, nested) -> acc.concat((List<R>)nested));
    }

    public <R> List<R> flatMap(Function<T, ? extends Monad<?, R>> f) {
        return isEmpty() ? Nil()
                : ((List<R>)f.apply(head())).concat(tail().flatMap(f));
    }

    public <R> List<R> map(Function<T, R> f) {
        return isEmpty() ? Nil()
                : tail().map(f).prepend(f.apply(head()));
    }

    @Override
    public <R> R fold(R z, BiFunction<R, T, R> f) {
        return isEmpty() ? z : foldLoop(this, z, f);
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, T, R> f) {
        return isEmpty() ? z : tail().foldl(f.apply(z, head()), f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<T, R, R> f) {
        return isEmpty() ? z : f.apply(head(), tail().foldr(z, f));
    }

    @Override
    public List<T> filter(Predicate<T> cond) {
        return isEmpty() ? this
                : cond.test(head()) ? Cons(head(), tail().filter(cond))
                    : tail().filter(cond);
    }

    @Override
    public List<T> slice(int from, int to) {
        return isEmpty() || to == 0 ? Nil()
                : from == 0 ? tail().slice(from, to - 1).prepend(head())
                    : tail().slice(from - 1, to - 1);
    }

    public List<T> reverse() {
        return reverseLoop(this, Nil());
    }


    public T min() {
        throw new UnsupportedOperationException();
    }

    public T max() {
        throw new UnsupportedOperationException();
    }

    public static class ListCompanion {

        static <T, R> R foldLoop(List<T> list, R z, BiFunction<R, T, R> f) {
            return list.isEmpty() ? z : foldLoop(list.tail(), f.apply(z, list.head()), f);
        }

        static <T> List<T> reverseLoop(List<T> s, List<T> d) {
            return s.isEmpty() ? d : reverseLoop(s.tail(), d.prepend(s.head()));
        }

        public static <T> List<T> Nil() {
            return new Nil<>();
        }

        public static <T> List<T> Cons(T h, List<T> t) {
            return new Cons<>(h, t);
        }

        public static <T> List<T> List(T t) {
            return Cons(t, Nil());
        }

        @SafeVarargs
        public static <T> List<T> List(T... ts) {
            List<T> list = Nil();

            switch (ts.length) {
                case 0:
                    return list;
                case 1:
                    return list.append(ts[0]);
                default:
                    for (val t: ts)
                        list = list.append(t);

                    return list;
            }
        }

        public static <T> List<T> List(Collection<T> jCollection) {
            List<T> list = Nil();

            switch (jCollection.size()) {
                case 0:
                    return list;
                case 1:
                    return list.append((T) jCollection.toArray()[0]);
                default:
                    return List( (T[]) jCollection.toArray());
            }
        }
    }

    public static final class Cons<T> extends List<T> {

        private T head;
        private List<T> tail;

        Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public List<T> tail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public List<T> concat(List<T> that) {
            return isEmpty() ? that : tail().concat(that).prepend(head());
        }

        @Override
        public Iterator<T> iterator() {
            return new LinearIterator<>(this);
        }

        @Override
        public String toString() {
            return linearToString("List(", this, ")");
        }
    }

    public static final class Nil<T> extends List<T> {

        @Override
        public T head() {
            throw new NoSuchElementException("Nil");
        }

        @Override
        public List<T> tail() {
            throw new NoSuchElementException("Nil");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public List<T> concat(List<T> that) {
            return that;
        }

        @Override
        public Iterator<T> iterator() {

            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public T next() {
                    throw new NoSuchElementException("Nil");
                }
            };
        }

        @Override
        public String toString() {
            return "Nil";
        }
    }
}