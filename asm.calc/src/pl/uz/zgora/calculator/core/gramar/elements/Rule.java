package pl.uz.zgora.calculator.core.gramar.elements;

import pl.uz.zgora.calculator.core.tree.TreeElemens;

/* Provide a number of record classes for storing BNF grammar rules.  Each has
 an integer kind for use in switches (implemented as a method because Java does
 not override constants). */

public abstract class Rule {
	public static final int EMPTY = 0, SKIP = 1;
	public static final int ACCEPT = 2;
	public static final int BUILD = 3, THEN = 4, OR = 5;

	public abstract int getKind();

	// The empty rule (consume no input, build no tree node)
	public static class Empty extends Rule {
		public Empty() {
		}

		@Override
		public int getKind() {
			return EMPTY;
		}
	}

	// The skip rule (move past a key token without building anything)
	public static class Skip extends Rule {
		public int symbolKind;

		public Skip(int k) {
			symbolKind = k;
		}

		@Override
		public int getKind() {
			return SKIP;
		}
	}

	// The accept rule (accept a token and build a leaf node from it)
	public static class Accept extends Rule {
		public int symbolKind;

		public Accept(int k) {
			symbolKind = k;
		}

		@Override
		public int getKind() {
			return ACCEPT;
		}
	}

	// The build rule (ignore input, build a compound tree node with n subnodes)
	public static class Build extends Rule {
		public TreeElemens kind;
		public int size;

		public Build(TreeElemens add, int n) {
			kind = add;
			size = n;
		}

		@Override
		public int getKind() {
			return BUILD;
		}
	}

	// The then rule (deal with the two subrules one after the other)
	public static class Then extends Rule {
		public int left, right;

		public Then(int l, int r) {
			left = l;
			right = r;
		}

		@Override
		public int getKind() {
			return THEN;
		}
	}

	// The or rule (choose one of the two alternative subrules)
	public static class Or extends Rule {
		public int left, right;

		public Or(int l, int r) {
			left = l;
			right = r;
		}

		@Override
		public int getKind() {
			return OR;
		}
	}
}
