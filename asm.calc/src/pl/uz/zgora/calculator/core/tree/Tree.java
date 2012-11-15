package pl.uz.zgora.calculator.core.tree;

/* The Tree class provides a number of kinds of tree node, Tree.Id,
 Tree.Add etc for representing parse trees.  Each kind of node has a kind tag, a
 prefix number, and a postfix number associated with it.  The kind tag,
 obtained by getKind(), is an integer allowing code to decide efficiently what
 kind of Tree node it is looking at, e.g. using a switch.  The prefix and
 postfix numbers help to find the range of source text corresponding to a tree
 without explicit range information in every node.

 The prefix number is the number of tokens in the source to the left of the
 construct, and similarly for the postfix number.  For the "( _ )"
 bracketed-expression construct, both prefix and postfix are 1 because there is
 1 key "(" to the left and 1 key ")" to the right.

 The build methods allow a tree node of a given kind to be built.

 */

public abstract class Tree {

	// Build a tree with one subtree, given its kind tag

	public static Tree build1(TreeElemens kind, Tree node) {
		if (!kind.equals(TreeElemens.BRACKET)) {
			// TODO
		}
		return new Bracket(node);
	}

	// Build a tree with two subtrees, given its kind tag

	public static Tree build2(TreeElemens kind, Tree node1, Tree node2) {
		switch (kind) {
		case ADD:
			return new Add(node1, node2);
		case SUB:
			return new Sub(node1, node2);
		case MUL:
			return new Mul(node1, node2);
		case DIV:
			return new Div(node1, node2);
		case POW:
			return new Pow(node1, node2);
		default:
			// TODO
			System.out.println("Internal error: wrong kind of tree");
			// System.exit(1);
		}
		return null;
	}

	public abstract TreeElemens getKind();

	public abstract int getPrefix();

	public abstract int getPostfix();

	public abstract Tree[] getChild();

	@Override
	public String toString() {
		return getKind().name();
	}
}
