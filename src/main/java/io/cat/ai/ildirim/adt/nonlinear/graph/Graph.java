package io.cat.ai.ildirim.adt.nonlinear.graph;

import io.cat.ai.ildirim.adt.linear.List;
import io.cat.ai.ildirim.data.Foldable;
import io.cat.ai.ildirim.util.IterableEx;

public interface Graph<E, N> extends IterableEx<N>, Foldable<E> {

    List<Graph<E, N>> inEdges();
    List<Graph<E, N>> outEdges();

    List<Graph<E, N>> successors();
    List<Graph<E, N>> predecessors();

    List<Graph<E, N>> depthFirstSearch();
    List<Graph<E, N>> breadthFirstSearch();
}