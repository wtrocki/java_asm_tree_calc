package pl.uz.zgora.calculator.core.tree;

public class Id extends Tree {
	public int ref;
	int start;

	public Id(int r, int s) {
		ref = r;
		start = s;
	}

	@Override
	public TreeElemens getKind() {
		return TreeElemens.ID;
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
		return new Tree[] {};
	}
}
