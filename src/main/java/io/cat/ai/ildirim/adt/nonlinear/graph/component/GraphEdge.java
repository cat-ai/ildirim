package io.cat.ai.ildirim.adt.nonlinear.graph.component;

import io.cat.ai.ildirim.adt.nonlinear.graph.Graph;

import lombok.*;

@AllArgsConstructor @Getter @Setter
class GraphEdge<E, N> {

    private E val;
    private final Graph<E, N> source;
    private final Graph<E, N> target;
}