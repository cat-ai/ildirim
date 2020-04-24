package io.cat.ai.ildirim.adt.linear.util;

import io.cat.ai.ildirim.adt.linear.Linear;

import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Iterator;
import java.util.NoSuchElementException;

@AllArgsConstructor
public final class LinearIterator<T> implements Iterator<T> {

    private Linear<T> self;

    @Override
    public boolean hasNext() {
        return !self.isEmpty();
    }

    @Override
    public T next() {
        if (self.isEmpty())
            throw new NoSuchElementException("Empty");
        else {
            val nextVal = self.head();
            self = self.tail();
            return nextVal;
        }
    }
}
