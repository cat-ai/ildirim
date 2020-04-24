package io.cat.ai.ildirim.adt.nonlinear.tree.balanced;

import java.util.function.Function;

public interface RedBlackTree<T extends Comparable<T>> extends SelfBalancingTree<T, RedBlackTree<T>> /*implements Monad<Tree<T>, T>*/  {

    enum Color { RED, BLACK }

    Color color();

    <R extends Comparable<R>> RedBlackTree<R> map(Function<T, R> f);

    <R extends Comparable<R>> RedBlackTree<R> flatMap(Function<T, RedBlackTree<R>> f);

    RedBlackTree<T> concat(RedBlackTree<T> that);

    /**
     *
     * Example: insert value 15 to existing tree
     *
     *               BLACK(7)
     *                 /\
     *                /  \
     *               /    \
     *              /      \
     *        BLACK(3)    RED(18)
     *           /\         /\
     *                     /  \
     *                    /    \
     *                   /      \
     *             BLACK(10)    BLACK(22)
     *               /\           /\
     *              /  \            \
     *             /    \            \
     *            /      \            \
     *       RED(8)     RED(11)      RED(26)
     *         /\        /\            /\
     *
     *
     *  Move the violation up the tree by recoloring until it can be fixed with rotations and recoloring
     *
     * 2) Recolor, moving the violation up the tree.
     *
     *
     *              BLACK(7)
     *                 /\
     *                /  \
     *               /    \
     *              /      \
     *        BLACK(3)    RED(18)
     *           /\        /  \
     *                    /    \
     *                   /      \
     *                  /        \
     *             BLACK(10)    BLACK(22)
     *               /\           /\
     *              /  \            \
     *             /    \            \
     *            /      \            \
     *       RED(8)     RED(11)      RED(26)
     *         /\        /\            /\
     *                     \
     *                      \
     *                       \
     *                      RED(15)
     *
     * 3) Right rotate 10
     *
     *              BLACK(7)
     *                 /\
     *                /  \
     *               /    \
     *              /      \
     *        BLACK(3)    RED(18)
     *           /\         /\
     *                     /  \
     *                    /    \
     *                   /      \
     *              RED(10)    BLACK(22)
     *               /\           /\
     *              /  \            \
     *             /    \            \
     *            /      \            \
     *      BLACK(8)   BLACK(11)      RED(26)
     *         /\        /\            /\
     *                     \
     *                      \
     *                       \
     *                      RED(15)
     *
     * 4) Right rotate (10) and then left rotate (7) and recolor
     *
     *             BLACK(7)
     *                 /\
     *                /  \
     *               /    \
     *              /      \
     *        BLACK(3)    RED(10)
     *           /\         /\
     *                     /  \
     *                    /    \
     *                   /      \
     *             BLACK(8)    RED(18)
     *               /\           /\
     *                           /  \
     *                          /    \
     *                         /      \
     *                   BLACK(11)   BLACK(22)
     *                     /\          /\
     *                       \           \
     *                        \           \
     *                         \           \
     *                        RED(15)     RED(26)
     *                         /\          /\
     * then,
     *
     *              RED(10)
     *                /  \
     *               /    \
     *              /      \
     *             /        \
     *       BLACK(7)      RED(18)
     *        /\                /\
     *       /  \              /  \
     *      /    \            /    \
     *     /      \          /      \
     * BLACK(3)  BLACK(8) BLACK(11)  BLACK(22)
     *   /\        /\      /\             /\
     *                       \              \
     *                        \              \
     *                         \              \
     *                      RED(15)          RED(26)
     *                        /\               /\
     *
     * and then result after insertion value 15 (and after rotations and recoloring):
     *
     *              BLACK(10)
     *                /   \
     *               /     \
     *              /       \
     *             /         \
     *       BLACK(7)      BLACK(18)
     *        / \               /  \
     *       /   \             /    \
     *      /     \           /      \
     *     /       \         /        \
     * BLACK(3)  BLACK(8) BLACK(11)  BLACK(22)
     *   /\        /\      /\             /\
     *                       \              \
     *                        \              \
     *                         \              \
     *                       RED(15)         RED(26)
     *                         /\              /\
     * */
    @Override
    RedBlackTree<T> insert(T e);
}