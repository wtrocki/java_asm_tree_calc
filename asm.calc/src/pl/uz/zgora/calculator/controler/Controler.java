package pl.uz.zgora.calculator.controler;

import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.gramar.elements.Token;
import pl.uz.zgora.calculator.core.tree.Tree;

/**
 * Container for class
 */
// TODO Invalid solution. Symbols must be redistributed directly.
public class Controler {
	public String source;
	private Token[] tokens;
	private Symbol[] symbols;
	private Tree tree;

	public Controler(String text) {
		source = text;
		tokens = null;
		symbols = null;
		tree = null;
	}

	// Given a node in the parse tree, the corresponding range of source text
	// can be found. A Range is specified by two character positions.
	public static class Range {
		int start;
		int end;
	}

	// TODO use global range
	Range findRange(Tree tree) {
		return new Range();
	}

	public Tree getTree() {
		return tree;
	}

	public Symbol[] getSymbols() {
		return symbols;
	}

	public void setTokens(Token[] tokens) {
		this.tokens = tokens;
	}

	public Token[] getTokens() {
		return tokens;
	}

	public void setSymbols(Symbol[] array) {
		this.symbols = array;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}
};
