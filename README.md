# Ildirim

Ildirim translated as "lightning"

Experimental library provides a data structures which are strongly immutable

# Examples

```java

import static io.cat.ai.ildirim.adt.Linear.ListDsl.*;
import static io.cat.ai.ildirim.adt.Linear.LazyDsl.StreamDsl.Stream;

import static io.cat.ai.ildirim.data.Ord.ord;

import lombok.var;

public class Tests {

    public static <T extends Comparable<T>> List<T> quicksort(List<T> list) {
       if (list.isEmpty())
           return list;
       else {
           val pivot = list.getAt(random.nextInt(list.length()));
           return quicksort(list.filter(__ -> ord(__).lessThan(pivot)))
                   .concat(list.filter(__ -> ord(__).equiv(pivot)))
                       .concat(quicksort(list.filter(__ -> ord(__).greaterThan(pivot))));
       }
   }

    public static <T extends Comparable<T>> List<T> mergesort(List<T> list) {
        val n = list.length() / 2;

        switch (n) {
            case 0:
                return list;
            case 1:
            default:
                val splittedPair = list.splitAt(n);
                val left = splittedPair._1();
                val right = splittedPair._2();
                return merge(mergesort(left), mergesort(right));
        }
    }

    private static <T extends Comparable<T>> List<T> merge(List<T> left, List<T> right) {
        return left.nonEmpty() && right.isEmpty() ? left

                : left.isEmpty() && right.nonEmpty() ? right

                    : left.nonEmpty() && right.nonEmpty() && ord(left.head()).lessThan(right.head()) ? merge(left.tail(), right).prepend(left.head())

                        : merge(left, right.tail()).prepend(right.head());
    }

    public static <T extends Comparable<T>> List<T> insertionsort(List<T> list) {
        return list.isEmpty() ? Nil()
                : insert(list.head(), insertionsort(list.tail()));
    }
    
    public static <T extends Comparable<T>> List<T> insert(T x, List<T> xs) {
        return xs.isEmpty() || ord(x).lessThan(xs.head()) ? xs.prepend(x)
                : insert(x, xs.tail()).prepend(xs.head());
    }
    
    public static <T> anotherTests() {
        Function<Integer, List<Integer>> f = x -> List(x -1, x + 2);
        Function<Integer, List<Integer>> g = x -> List(x, - x);

        System.out.println(List(2).flatMap(Linear.ListDsl::List));

        System.out.println(listOf1And2.flatMap(f).flatMap(g));
        System.out.println(listOf1And2.flatMap(x -> f.apply(x).flatMap(g)));

        System.out.println(Stream(1, 2, 3, 4).foldr(20, (c, n) -> c - n));

    }
}
```