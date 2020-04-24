package io.cat.ai.ildirim.adt.nonlinear.map.hash;

import io.cat.ai.ildirim.adt.nonlinear.map.MultiMap;
import io.cat.ai.ildirim.adt.nonlinear.set.Set;

public interface HashMultiMap<K, V> extends MultiMap<K, Set<V>> {
}
