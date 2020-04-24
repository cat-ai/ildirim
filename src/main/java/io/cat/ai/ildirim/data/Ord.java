package io.cat.ai.ildirim.data;

public abstract class Ord<T extends Comparable<T>> {

    private T cmpVal;

    Ord(T cmpVal) {
        this.cmpVal = cmpVal;
    }

    public abstract boolean lessThan(T that);

    public abstract boolean lessThanOrEquiv(T that);

    public abstract boolean greaterThan(T that);

    public abstract boolean greaterThanOrEquiv(T that);

    public abstract boolean equiv(T that);

    private static class OrdImpl<T extends Comparable<T>> extends Ord<T> {

        private T cmpVal;

        OrdImpl(T cmpVal) {
            super(cmpVal);
            this.cmpVal = cmpVal;
        }

        @Override
        public boolean lessThan(T that) {
            return cmpVal.compareTo(that) < 0;
        }

        @Override
        public boolean lessThanOrEquiv(T that) {
            return cmpVal.compareTo(that) <= 0;
        }

        @Override
        public boolean greaterThan(T that) {
            return cmpVal.compareTo(that) > 0;
        }

        @Override
        public boolean greaterThanOrEquiv(T that) {
            return cmpVal.compareTo(that) >= 0;
        }

        @Override
        public boolean equiv(T that) {
            return cmpVal.compareTo(that) == 0;
        }
    }

    public static <R extends Comparable<R>> Ord<R> ord(R r) {
        return new OrdImpl<>(r);
    }
}