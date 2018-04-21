// Abdullah Al-Wadeiah, 10172388
// CPSC 331, Assignment 3, Part 1
// Professor: Alim, TA: Zhou

import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.StringTokenizer;

public class ExpressionTree {

	private ExpTreeNode root = null;

	// Builds empty tree
	public ExpressionTree() {
	}
	
	// Parse an expression from a string
	public void parse(String line) throws ParseException {
		Scanner sc = new Scanner(line);

		root = expression(sc);

	}

	/**
	 * Parses addition and subtraction expressions.
	 * @param sc
	 * @return node
	 * @throws ParseException
	 */
	private ExpTreeNode expression(Scanner sc) throws ParseException {
		ExpTreeNode node, p;
		String oper;
		node = term(sc);

		while ((sc.hasNext("\\+")) || (sc.hasNext("\\-"))) {
			oper = sc.next();
			p = term(sc);
			node = new ExpTreeNode(oper, node, p);
		}

		return node;
	}
	
	/**
	 * Parses multiplication and division expressions
	 * @param sc
	 * @return node
	 * @throws ParseException
	 */
	private ExpTreeNode term(Scanner sc) throws ParseException {
		ExpTreeNode node, p;
		String oper;
		node = factor(sc);

		while ((sc.hasNext("\\*")) || (sc.hasNext("\\/"))) {
			oper = sc.next();
			p = factor(sc);
			node = new ExpTreeNode(oper, node, p);
		}
		return node;
	}

	/**
	 * Parses exponential expressions.
	 * @param sc
	 * @return node
	 * @throws ParseException
	 */
	private ExpTreeNode factor(Scanner sc) throws ParseException {
		ExpTreeNode node, p;
		node = base(sc);

		while (sc.hasNext("\\^")) {
			sc.next();
			p = factor(sc);
			node = new ExpTreeNode("^", node, p);
		}
		return node;
	}

	/**
	 * Parses integer, bracket expressions, and unary negation
	 * @param sc
	 * @return node
	 * @throws ParseException
	 */
	private ExpTreeNode base(Scanner sc) throws ParseException {
		ExpTreeNode node;

		if (sc.hasNext("\\(")) {
			sc.next();
			node = expression(sc);
			if (sc.hasNext("\\)"))
				sc.next();
			else
				throw new ParseException("Invalid Expression. Missing \")\". ", 0);
		}

		else if (sc.hasNext("\\-")) {
			sc.next();
			node = term(sc);
			node = new ExpTreeNode("~", node, null);
		}

		else {
			try {
				String num = sc.next();
				try {
					Integer.parseInt(num);
					return new ExpTreeNode(num);
				} catch (NumberFormatException e) {
					throw new ParseException("Non-integer token " + num, 0);
				}
			} catch (NoSuchElementException e) {
				throw new ParseException("Extra operator ", 0);
			}
		}

		return node;
	}

	/**
	 * Evaluate an expression
	 * @return
	 */
	public int evaluate() {
		return eval(root);
	}
	
	/**
	 * Recursively evaluates an expression tree
	 * @param node
	 * @return
	 */
	private int eval(ExpTreeNode node) {
		if (node == null)
			return 0;

		if (node.left == null && node.right == null)
			return Integer.parseInt(node.el);

		int value1 = eval(node.left);
		int value2 = eval(node.right);

		if (node.el.equals("+"))
			return value1 + value2;
		if (node.el.equals("-"))
			return value1 - value2;
		if (node.el.equals("*"))
			return value1 * value2;
		if (node.el.equals("/"))
			return value1 / value2;
		if (node.el.equals("~"))
			return -value1;
		if (node.el.equals("^"))
			return (int) Math.pow(value1, value2);

		return 0;
	}

	/**
	 * Returns a postfix representation of the expression. Tokens are separated by
	 * whitespace. An empty tree returns a zero length string.
	 */
	public String postfix() {
		if (root == null)
			return "";

		final StringJoiner postfixSj = new StringJoiner(" ");
		postorder(root, postfixSj);
		return postfixSj.toString();
	}

	/**
	 * Traverses the expression tree in a postorder traversal
	 */
	private void postorder(ExpTreeNode node, StringJoiner sj) {
		if (node != null) {
			postorder(node.left, sj);
			postorder(node.right, sj);
			sj.add(node.el);
		}
	}

	/**
	 * Returns a prefix representation of the expression. Tokens are separated by
	 * whitespace. An empty tree returns a zero length string.
	 */
	public String prefix() {

		if (root == null)
			return "";

		final StringJoiner prefixSj = new StringJoiner(" ");
		preorder(root, prefixSj);
		return prefixSj.toString();
	}

	/**
	 * Traverses the expression tree in a preorder traversal
	 */
	private void preorder(ExpTreeNode node, StringJoiner sj) {
		if (node != null) {
			sj.add(node.el);
			preorder(node.left, sj);
			preorder(node.right, sj);
		}
	}
}
