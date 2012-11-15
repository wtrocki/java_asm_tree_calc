package pl.uz.zgora.calculator.core.tree;

public class Pow extends Tree {
	public Tree left;
	public Tree right;

	Pow(Tree l, Tree r) {
		left = l;
		right = r;
	}

	@Override
	public TreeElemens getKind() {
		return TreeElemens.POW;
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