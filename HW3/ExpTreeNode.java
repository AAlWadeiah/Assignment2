/**
 * A class that represents a node in a binary expression tree.
 *
 */

// Abdullah Al-Wadeiah, 10172388
// CPSC 331, Assignment 3, Part 1
// Professor: Alim, TA: Zhou

public class ExpTreeNode {
	String el;

	ExpTreeNode left, right;

	ExpTreeNode(String e) {
		this(e, null, null);
	}

	ExpTreeNode(String e, ExpTreeNode l, ExpTreeNode r) {
		el = new String(e);
		left = l;
		right = r;
	}
}
