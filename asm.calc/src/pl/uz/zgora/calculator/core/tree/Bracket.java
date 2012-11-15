package pl.uz.zgora.calculator.core.tree;

// A bracket node represents a bracketed subexpression. In most compilers,
// bracket nodes would not be created, but in a compiler like this one which
// has no explicit source range info in tree nodes, they are needed in order
// to calculate source ranges.

public class Bracket extends Tree {
	public Tree expr;

	Bracket(Tree e) {
		expr = e;
	}

	@Override
	public TreeElemens getKind() {
		return TreeElemens.BRACKET;
	}

	@Override
	public int getPrefix() {
		return 1;
	}

	@Override
	public int getPostfix() {
		return 1;
	}

	@Override
	public Tree[] getChild() {
		return new Tree[] { expr };
	}
}

// An add node represents the sum of the two left and right subnodes.
// The same goes for the other arithmetic nodes.

