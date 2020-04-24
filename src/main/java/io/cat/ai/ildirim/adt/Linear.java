package io.cat.ai.ildirim.adt;

import io.cat.ai.ildirim.adt.linear.List;
import io.cat.ai.ildirim.adt.linear.Queue;
import io.cat.ai.ildirim.adt.linear.Stack;
import io.cat.ai.ildirim.adt.linear.lazy.Stream;

import lombok.var;

import static io.cat.ai.ildirim.adt.linear.List.*;
import static io.cat.ai.ildirim.adt.linear.Stack.*;
import static io.cat.ai.ildirim.adt.linear.Queue.*;
import static io.cat.ai.ildirim.adt.linear.lazy.Stream.*;

public class Linear {

    public static final class ListDsl {

        public static <T> List<T> List() {
            return ListCompanion.Nil();
        }

        public static <T> List<T> Nil() {
            return ListDsl.List();
        }

        public static <T> List<T> Cons(T head, List<T> tail) {
            return ListCompanion.Cons(head, tail);
        }

        @SafeVarargs
        public static <T> List<T> List(T... ts) {
            return ListCompanion.List(ts);
        }
    }

    public static final class LazyDsl {

        public static final class StreamDsl {

            public static <T> Stream<T> Stream() {
                return StreamCompanion.Stream();
            }

            public static <T> Stream<T> Stream(T t) {
                return StreamCompanion.Stream(t);
            }

            @SafeVarargs
            public static <T> Stream<T> Stream(T... ts) {
                var stream = StreamDsl.<T>Stream();

                switch (ts.length) {
                    case 0:
                        return stream;
                    case 1:
                        return stream.append(ts[0]);
                    default:
                        for (T t: ts)
                            stream = stream.append(t);

                        return stream;
                }
            }
        }
    }

    public static final class QueueDsl {

        public static <T> Queue<T> Queue() {
            return QueueCompanion.Queue();
        }

        @SafeVarargs
        public static <T> Queue<T> Queue(T... ts) {
            return QueueCompanion.Queue(ts);
        }

        public static <T> Queue<T> Queue(List<T> in, List<T> out) {
            return QueueCompanion.Queue(in, out);
        }
    }

    public static final class StackDsl {

        public static <T> Stack<T> Stack() {
            return StackCompanion.Stack();
        }

        public static <T> Stack<T> Stack(List<T> list) {
            return StackCompanion.Stack(list);
        }

        @SafeVarargs
        public static <T> Stack<T> Stack(T ...ts) {
            return Stack(ListDsl.List(ts));
        }

    }

}
