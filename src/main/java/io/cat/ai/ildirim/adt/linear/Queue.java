package io.cat.ai.ildirim.adt.linear;

import io.cat.ai.ildirim.adt.linear.util.LinearIterator;
import io.cat.ai.ildirim.control.Monad;
import io.cat.ai.ildirim.util.Pair;

import io.cat.ai.ildirim.util.Lazy;

import lombok.NoArgsConstructor;
import lombok.val;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.cat.ai.ildirim.adt.linear.List.ListCompanion.*;
import static io.cat.ai.ildirim.adt.linear.Queue.QueueCompanion.*;

import static io.cat.ai.ildirim.util.Lazy.lazy;

@SuppressWarnings("unchecked")
@NoArgsConstructor
public abstract class Queue<T> implements Linear<T> {

    private List<T> in = Nil();
    private List<T> out = Nil();

    Queue(List<T> in, List<T> out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public T getAt(int n) {
        val outLen = out.length();
        if (n < outLen)
            return out.getAt(n);
        else {
            val m = n - outLen;
            val inLen = in.length();
            if (m < inLen)
                return in.getAt(inLen - m - 1);
            else
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Pair<Queue<T>, Queue<T>> splitAt(int n) {
        return isEmpty() ? Pair.of(Queue(), Queue())
                : Pair.of(take(n), drop(n));
    }

    public Queue<T> concat(Queue<T> that) {
        return isEmpty() ? that
                : rear().concat(that).enqueue(front());
    }

    @Override
    public T head() {
        return front();
    }

    @Override
    public Queue<T> tail() {
        return out.nonEmpty() ? Queue(in, out.tail())
                    : in.nonEmpty() ? Queue(Nil(), in.reverse().tail())
                        : fail(new NoSuchElementException("Queue(<empty>)")) ;
    }

    @Override
    public Queue<T> append(T e) {
        return enqueue(e);
    }

    @Override
    public Queue<T> prepend(T e) {
        return Queue(in, out.append(e));
    }

    @Override
    public Queue<T> remove(T e) {
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinearIterator<>(this);
    }

    @Override
    public Queue<T> take(int n) {
        return isEmpty() || n == 0 ? Queue()
                : n == 1 ? Queue(List(front()), Nil())
                    : Queue(in.concat(out).take(n), Nil());
    }

    @Override
    public Queue<T> drop(int n) {
        return nonEmpty() && n >= 1
                ? rear().drop(n - 1)
                    : n == 1 ?
                        Queue(List(front()), Nil())
                        : this;
    }

    @Override
    public Queue<T> slice(int from, int until) {
        return isEmpty() || until == 0 ? Queue()
                : from == 0 ? tail().slice(from, until - 1).prepend(head())
                : tail().slice(from - 1, until - 1);
    }

    @Override
    public int size() {
        return isEmpty() ? 0 : 1 + tail().size();
    }

    @Override
    public Queue<T> reverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(T e) {
        return false;
    }

    public abstract T front();

    public abstract Queue<T> rear();

    public abstract Pair<T, Queue<T>> peek();

    public abstract Queue<T> enqueue(T e);

    public abstract Pair<T, Queue<T>> dequeue();

    @Override
    public <R> R fold(R z, BiFunction<R, T, R> f) {
        return foldl(z, f);
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, T, R> f) {
        return out.concat(in).reverse().foldl(z, f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<T, R, R> f) {
        return out.concat(in).reverse().foldr(z, f);
    }

    public <R> R get() {
        return (R) this;
    }

    @Override
    public <R> Queue<R> unit(R elem) {
        return Queue(elem);
    }

    @Override
    public <R> Queue<R> yield(Function<T, R> f) {
        return foldl(Queue(), (acc, e) -> acc.append(f.apply(e)));
    }

    @Override
    public <R> Queue<R> join() {
        return foldl(Queue(), (acc, nested) -> acc.concat((Queue<R>)nested));
    }

    @Override
    public <R> Queue<R> fail(RuntimeException e) {
        throw e;
    }

    @Override
    public <R> Queue<R> flatMap(Function<T, ? extends Monad<?, R>> f) {
        return isEmpty() ? Queue()
                : ((Queue<R>) f.apply(head())).concat(tail().flatMap(f));
    }

    public <R> Queue<R> map(Function<T, R> f) {
        return Queue(out.map(f), in.map(f));
    }

    public Queue<T> filter(Predicate<T> cond) {
        return Queue(in.filter(cond), out.filter(cond));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        out.concat(in).reverse().forEach(action);
    }

    @Override
    public String toString() {
        return linearToString("Queue(", out.concat(in.reverse()), ")");
    }

    private static final class QueueImpl<T> extends Queue<T> {

        private List<T> in = Nil();
        private List<T> out = Nil();

        QueueImpl(List<T> in, List<T> out) {
            super(in, out);
            this.in = in;
            this.out = out;
        }

        QueueImpl() {}

        private Lazy<List<T>> inReversedL = lazy(() -> in.reverse());

        @Override
        public T front() {
            return dequeue()._1();
        }

        @Override
        public Queue<T> rear() {
            return dequeue()._2();
        }

        @Override
        public Queue<T> enqueue(T e) {
            return Queue(in.prepend(e), out);
        }

        @Override
        public Pair<T, Queue<T>> dequeue() {
            return !out.isEmpty() ? Pair.of(out.head(), Queue(in, out.tail()))
                    : withReversed(inReversedL.eval().reverse());
        }

        @Override
        public Pair<T, Queue<T>> peek() {
            return out.isEmpty() && !in.isEmpty()
                    ? Pair.of(inReversedL.eval().head(), Queue(Nil(), inReversedL.eval()))
                        : !out.isEmpty() ? Pair.of(out.head(), this)
                            : failEntry(new NoSuchElementException("Queue(<empty>)"));
        }

        @Override
        public boolean isEmpty() {
            return in.isEmpty() && out.isEmpty();
        }

        private Pair<T, Queue<T>> withReversed(final List<T> l) {
            return l.nonEmpty() ? Pair.of(l.head(), Queue(Nil(), l.tail()))
                    : failEntry(new NoSuchElementException("Queue(<empty>)"));
        }

        private Pair<T, Queue<T>> failEntry(RuntimeException e) {
            throw e;
        }
    }

    public static class QueueCompanion {

        public static <T> Queue<T> Queue() {
            return new QueueImpl<>();
        }

        public static <T> Queue<T> Queue(List<T> in, List<T> out) {
            return new QueueImpl<>(in, out);
        }

        @SafeVarargs
        public static <T> Queue<T> Queue(T... ts) {
            return List(ts).foldl(Queue(), Queue::enqueue);
        }
    }
}