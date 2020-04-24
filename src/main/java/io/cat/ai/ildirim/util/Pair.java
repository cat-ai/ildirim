package io.cat.ai.ildirim.util;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Pair<F, S> implements Comparable<Pair<F, S>> {
    private F _1;
    private S _2;

    public F _1() {
        return _1;
    }

    public S _2() {
        return _2;
    }

    @Override
    public String toString() {
        return "Pair(" + _1 + ", " + _2 + ")";
    }

    @Override
    public int compareTo(Pair<F, S> o) {
        return Integer.compare(_1.hashCode(), _2.hashCode());
    }
}
