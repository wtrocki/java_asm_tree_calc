package pl.uz.zgora.calculator.asm;

import java.util.Stack;

import pl.uz.zgora.calculator.asm.op.Element;
import pl.uz.zgora.calculator.asm.op.Operation;
import pl.uz.zgora.calculator.asm.op.Value;
import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.tree.Id;
import pl.uz.zgora.calculator.core.tree.Tree;
import pl.uz.zgora.calculator.core.tree.TreeElemens;

public class ComputeHeapFromTree {
	private Stack<Element> stack;
	private Tree tree;
	private Symbol[] symbols;

	public ComputeHeapFromTree(Tree tree, Symbol[] symbols) {
		super();
		this.tree = tree;
		this.symbols = symbols;
		stack = new Stack<Element>();
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public void setSymbols(Symbol[] symbols) {
		this.symbols = symbols;
	}

	public void computeHeap() {
		TreeElemens kind = tree.getKind();
		if (!kind.equals(TreeElemens.ID)) {
			stack.push(new Operation(kind));
			appendToQuenue(tree);
		} else {
			int ref = ((Id) tree).ref;
			stack.push(new Value(symbols[ref].value));
		}
	}

	private void appendToQuenue(Tree tree) {
		if ((tree == null) || (symbols == null)) {
			return;
		}

		Tree[] children = tree.getChild();
		// TODO Inefficient way. Use back propagation
		for (int i = 0; i < children.length; i++) {
			if (children[i].getChild().length == 0) {
				TreeElemens kind = children[i].getKind();
				if (kind.equals(TreeElemens.ID)) {
					int ref = ((Id) children[i]).ref;
					stack.push(new Value(symbols[ref].value));
				}
				children[i] = null;
			}
		}

		for (Tree element : children) {
			if (element == null) {
				return;
			}
			TreeElemens kind = element.getKind();
			if (kind.equals(TreeElemens.ID)) {
				int ref = ((Id) element).ref;
				stack.push(new Value(symbols[ref].value));
			} else if (kind.equals(TreeElemens.BRACKET)) {
				element = element.getChild()[0];
				if (!element.getKind().equals(TreeElemens.ID)) {
					stack.push(new Operation(element.getKind()));
				}
			} else {
				stack.push(new Operation(element.getKind()));
			}
			appendToQuenue(element);
		}
	}

	public Stack<Element> getStack() {
		System.out.println(stack);
		return stack;
	}
}
