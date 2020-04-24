package io.cat.ai.ildirim.adt.nonlinear.tree.util;

import io.cat.ai.ildirim.adt.nonlinear.tree.Tree;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;

public class BTreePrinter {

        public static <T extends Comparable<T>> void printNode(Tree<T> root) {
            val maxLevel = BTreePrinter.maxLevel(root);

            printNodeInternal(Collections.singletonList(root), 1, maxLevel);
        }

        private static <T extends Comparable<T>> void printNodeInternal(java.util.List<Tree<T>> nodes, int level, int maxLevel) {
            if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes))
                return;

            val floor = maxLevel - level;
            val edgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
            val firstSpaces = (int) Math.pow(2, (floor)) - 1;
            val betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

            BTreePrinter.printWhitespaces(firstSpaces);

            val newNodes = new ArrayList<Tree<T>>();

            for (val node : nodes) {
                if (node != null && node.nonEmpty()) {
                    System.out.print(node.value());
                    newNodes.add(node.left());
                    newNodes.add(node.right());
                } else {
                    newNodes.add(null);
                    newNodes.add(null);
                    System.out.print(" ");
                }

                BTreePrinter.printWhitespaces(betweenSpaces);
            }
            System.out.println();

            for (int i = 1; i <= edgeLines; i++) {

                for (int j = 0; j < nodes.size(); j++) {

                    BTreePrinter.printWhitespaces(firstSpaces - i);

                    if (nodes.get(j) == null || nodes.get(j).isEmpty()) {
                        BTreePrinter.printWhitespaces(edgeLines + edgeLines + i + 1);
                        continue;
                    }

                    if (nodes.get(j).left() != null && nodes.get(j).left().nonEmpty())
                        System.out.print("/");
                    else
                        BTreePrinter.printWhitespaces(1);

                    BTreePrinter.printWhitespaces(i + i - 1);

                    if (nodes.get(j).right() != null && nodes.get(j).right().nonEmpty())
                        System.out.print("\\");
                    else
                        BTreePrinter.printWhitespaces(1);

                    BTreePrinter.printWhitespaces(edgeLines + edgeLines - i);
                }

                System.out.println("");
            }

            printNodeInternal(newNodes, level + 1, maxLevel);
        }

        private static void printWhitespaces(int count) {
            for (int i = 0; i < count; i++)
                System.out.print(" ");
        }

        private static <T extends Comparable<T>> int maxLevel(Tree<T> node) {
            if (node != null && node.isEmpty())
                return 0;

            return Math.max(BTreePrinter.maxLevel(node.left()), BTreePrinter.maxLevel(node.right())) + 1;
        }

        private static <T extends Comparable<T>> boolean isAllElementsNull(java.util.List<Tree<T>> list) {
            for (val n : list) {
                if (n != null && n.nonEmpty())
                    return false;
            }
            return true;
        }

    }