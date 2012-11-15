package pl.uz.zgora.calculator.core.tree;

// An error node represents a scan or parse error. It contains an error
// message and the source range which the error covers as start and end
// positions in the tokens array. (Not used at present.)

public class Error extends Tree {
	String message;
	int start, end;

	Error(int s, int e) {
		start = s;
		end = e;
	}

	@Override
	public TreeElemens getKind() {
		return TreeElemens.ERROR;
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
