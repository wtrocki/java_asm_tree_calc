package pl.uz.zgora.calculator.core.tree;

public class Div extends Tree {
	public Tree left;
	public Tree right;

	Div(Tree l, Tree r) {
		left = l;
		right = r;
	}

	@Override
	public TreeElemens getKind() {
		return TreeElemens.DIV;
	}

	@Override
	public int getPrefix() {
		return 0;
	}

	@Override
	public int getPostfix() {
		return 0;
	}

	@Override
	public Tree[] getChild() {
		return new Tree[] { left, right };
	}
}
