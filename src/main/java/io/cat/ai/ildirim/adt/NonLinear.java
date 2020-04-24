package io.cat.ai.ildirim.adt;

import io.cat.ai.ildirim.adt.nonlinear.set.SortedSet;
import io.cat.ai.ildirim.adt.nonlinear.tree.balanced.*;
import io.cat.ai.ildirim.adt.nonlinear.tree.BSTree;
import lombok.val;
import lombok.var;

import static io.cat.ai.ildirim.adt.nonlinear.tree.BSTree.*;
import static io.cat.ai.ildirim.adt.nonlinear.tree.balanced.RBTree.*;

import static io.cat.ai.ildirim.adt.nonlinear.set.sorted.AbstractSortedSet.*;


public class NonLinear {

    public static final class MapDsl {

    }

    public static final class HeapDsl {

    }

    public static final class SetDsl {

        public static <T extends Comparable<T>> SortedSet<T> SortedSet(T... ts) {
            return SortedSetImplCompanion.SortedSet(TreeDsl.BSTree(ts));
        }

    }

    public static final class TreeDsl {

        public static <T extends Comparable<T>> RedBlackTree<T> RBTree() {
            return RedBlackTreeCompanion.Leaf();
        }

        public static <T extends Comparable<T>> RedBlackTree<T> RBTree(T t) {
            return RedBlackTreeCompanion.RBTree(t);
        }

        public static <T extends Comparable<T>> RedBlackTree<T> RBTree(T... ts) {
            switch (ts.length) {
                case 0:
                    return RBTree();
                case 1:
                    return RBTree(ts[0]);
                default:
                    var rbTree = RBTree(ts[0]);
                    for(int i = 1; i < ts.length; i++)
                        rbTree = rbTree.insert(ts[i]);

                    return rbTree;
            }
        }

        public static <T extends Comparable<T>> ArneAnderssonTree<T> AATree() {
            return AATree.AATreeCompanion.AAEmptyNode();
        }

        public static <T extends Comparable<T>> ArneAnderssonTree<T> AATree(T t) {
            return AATree.AATreeCompanion.AANode(0, t, AATree(), AATree());
        }

        public static <T extends Comparable<T>> ArneAnderssonTree<T> AATree(T... ts) {
            switch (ts.length) {
                case 0:
                    return AATree();
                case 1:
                    return AATree(ts[0]);
                default:
                    var aaTree = AATree(ts[0]);
                    for(int i = 1; i < ts.length; i++)
                        aaTree = aaTree.insert(ts[i]);

                    return aaTree;
            }
        }

        public static <T extends Comparable<T>> AVLTree<T> AVLTree(T t) {
            return AVLTree.AVLTreeCompanion.AVLNode(t);
        }


        public static <T extends Comparable<T>> BSTree<T> BSTree() {
            return BinarySearchTreeCompanion.Leaf();
        }

        public static <T extends Comparable<T>> BSTree<T> BSTree(T... ts) {
            return BinarySearchTreeCompanion.BSTree(ts);
        }

        public static <T extends Comparable<T>> BSTree<T> BSTree(T t) {
            return BinarySearchTreeCompanion.TreeNodeWithLeafSubtrees(t);
        }

        public static <T extends Comparable<T>> BSTree<T> BSTree(BSTree<T> t) {
            return BinarySearchTreeCompanion.TreeNode(t.value(), t.left(), t.right());
        }

        public static <T extends Comparable<T>> BSTree<T> BSTreeNode(BSTree<T> t) {
            return BSTree(t);
        }

        public static <T extends Comparable<T>> BSTree<T> BSTreeLeaf() {
            return BSTree();
        }
    }

    public static final class GraphDsl {

    }
}
