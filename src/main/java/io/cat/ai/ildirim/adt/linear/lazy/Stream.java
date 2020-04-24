package io.cat.ai.ildirim.adt.linear.lazy;

import io.cat.ai.ildirim.adt.linear.Linear;
import io.cat.ai.ildirim.adt.linear.util.LinearIterator;
import io.cat.ai.ildirim.control.Monad;
import io.cat.ai.ildirim.util.Lazy;

import io.cat.ai.ildirim.util.Pair;
import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.util.Lazy.*;

import static io.cat.ai.ildirim.adt.linear.lazy.Stream.StreamCompanion.*;

@SuppressWarnings("unchecked")
public abstract class Stream<T> implements Linear<T> {

    Lazy<T> head;
    Lazy<Stream<T>> tail;

    @Override
    public Stream<T> append(T e) {
        return isEmpty() ? StreamCons(e, this)
                : StreamCons(head.eval(), tail.eval().append(e));
    }

    @Override
    public T getAt(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Linear<T> reverse() {
        return reverseLoop(this, Stream());
    }

    @Override
    public Pair<Stream<T>, Stream<T>> splitAt(int n) {
        return isEmpty() ? Pair.of(Stream(), Stream())
                : Pair.of(take(n), drop(n));
    }

    @Override
    public Stream<T> prepend(T e) {
        return StreamCons(e, this);
    }

    public Stream<T> concat(Stream<T> that) {
        return isEmpty() ? that
                : tail.eval().concat(that).prepend(this.head.eval());
    }

    @Override
    public T head() {
        return nonEmpty() ? head.eval()
                : throwNoSuchElemExc();
    }

    @Override
    public Stream<T> tail() {
        return !nonEmpty() ? tail.eval()
                : Stream();
    }

    @Override
    public Stream<T> take(int n) {
        return isEmpty() || n == 0 ? Stream()
                : StreamCons(head.eval(), tail.eval().take(n - 1));
    }

    @Override
    public Stream<T> drop(int n) {
        return nonEmpty() && n >= 1
                ? tail.eval().drop(n - 1)
                    : n == 1 ?
                        StreamCons(head.eval(), Stream())
                            : this;
    }

    @Override
    public Stream<T> slice(int from, int to) {
        return isEmpty() || to == 0 ? Stream()
                : from == 0 ? tail.eval().slice(from, to - 1).prepend(head.eval())
                    : tail.eval().slice(from -1, to - 1);
    }

    @Override
    public Stream<T> remove(T e) {
        return isEmpty() ? this
                : !e.equals(head()) ? StreamCons(head.eval(), tail.eval().remove(e))
                    : tail.eval();
    }

    @Override
    public Iterator<T> iterator() {
        return new LinearIterator<>(this);
    }

    @Override
    public boolean contains(T e) {
        return !isEmpty() && (e.equals(head.eval()) || tail.eval().contains(e));
    }

    public <R> R get() {
        return (R) this;
    }

    @Override
    public <R> Stream<R> unit(R elem) {
        return StreamCons(elem, Stream());
    }

    @Override
    public <R> Stream<R> yield(Function<T, R> f) {
        return foldl(Stream(), (acc, e) -> acc.append(f.apply(e)));
    }

    @Override
    public <R> Stream<R> join() {
        return foldl(Stream(), (acc, nested) -> acc.concat((Stream<R>)nested));
    }

    @Override
    public <R> Stream<R> flatMap(Function<T, ? extends Monad<?, R>> f) {
        return isEmpty() ? Stream()
                : ((Stream<R>) f.apply(head.eval())).concat(tail.eval().flatMap(f));
    }

    public <R> Stream<R> map(Function<T, R> f) {
        return isEmpty() ? Stream()
                : tail.eval().map(f).prepend(f.apply(head.eval()));
    }

    public Stream<T> filter(Predicate<T> cond) {
        return isEmpty() ? this
                : cond.test(head.eval()) ? StreamCons(head.eval(), tail.eval().filter(cond))
                    : tail().filter(cond);
    }

    @Override
    public <R> R fold(R z, BiFunction<R, T, R> f) {
        return foldl(z, f);
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, T, R> f) {
        return isEmpty() ? z
                : tail.eval().foldl(f.apply(z, head.eval()), f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<T, R, R> f) {
        return isEmpty() ? z
                : f.apply(head.eval(), tail.eval().foldr(z, f));
    }

    @AllArgsConstructor
    public static class Cons<T> extends Stream<T> {

        Cons(Lazy<T> head, Lazy<Stream<T>> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int size() {
            return isEmpty() ? 0 : 1 + tail().size();
        }

        private static <T> String loop(T head, Stream<T> tail, String res) {
            return !tail.isEmpty() ? loop(tail.head.eval(), tail.tail.eval(), res + head + ", ") : res + head;
        }

        @Override
        public String toString() {
            return "Stream(" + loop(head.eval(), tail.eval(), "") + ")";
        }
    }

    public static class Empty<T> extends Stream<T> {

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public String toString() {
            return "Stream.Empty";
        }
    }
    
    public static final class StreamCompanion {

        public static <T> Stream<T> Stream(T t) {
            return StreamCons(t, Stream());
        }

        public static <T> Stream<T> Stream() {
            return new Empty<>();
        }

        public static <T> Stream<T> StreamCons(T h, Stream<T> t) {
            return new Cons<>(lazy(() -> h), lazy(() -> t));
        }

        static <T> Stream<T> reverseLoop(Stream<T> s, Stream<T> d) {
            return s.isEmpty() ? d : reverseLoop(s.tail.eval(), d.prepend(s.head.eval()));
        }
    }

    private T throwNoSuchElemExc() {
        throw new NoSuchElementException("Stream.Empty");
    }
}
