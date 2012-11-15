package pl.uz.zgora.calculator.core.gramar;

/**
 Class Parser - parses the Calc language using a simple interpretive
 recursive descent parser.
 @author troken
 */

import java.util.Stack;

import pl.uz.zgora.calculator.controler.Controler;
import pl.uz.zgora.calculator.core.gramar.elements.Rule;
import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.gramar.elements.Token;
import pl.uz.zgora.calculator.core.tree.Id;
import pl.uz.zgora.calculator.core.tree.Tree;

public class Parser

{
	private Rule[] rules;
	private Token[] tokens;
	private Symbol[] symbols;
	private int next, nextKind;
	private Stack nodes;

	// Initialise the grammar rules.

	public Parser() {
		Grammar grammar = new Grammar();
		rules = grammar.rules;
	}

	// Parse a Calc program
	public Controler parse(Controler program) {
		tokens = program.getTokens();
		symbols = program.getSymbols();
		next = 0;
		nextKind = symbols[tokens[next].ref].kind;
		nodes = new Stack();
		parse(rules[0]);
		if (nodes.size() != 1) {
			// Parser generator bug.
			System.out
					.println("Internal error: parse produces wrong no of nodes");
			System.exit(1);
		}
		program.setTree((Tree) nodes.pop());
		return program;
	}

	// This is the interpretive recursive descent parser

	private void parse(Rule rule) {
		switch (rule.getKind()) {
		case Rule.THEN:
			parse((Rule.Then) rule);
			break;
		case Rule.OR:
			parse((Rule.Or) rule);
			break;
		case Rule.EMPTY:
			break;
		case Rule.SKIP:
			parse((Rule.Skip) rule);
			break;
		case Rule.ACCEPT:
			parse((Rule.Accept) rule);
			break;
		case Rule.BUILD:
			parse((Rule.Build) rule);
			break;
		}
	}

	private void parse(Rule.Then r) {
		parse(rules[r.left]);
		parse(rules[r.right]);
	}

	private void parse(Rule.Or r) {
		if (startsWith(rules[r.left], nextKind)) {
			parse(rules[r.left]);
		} else {
			parse(rules[r.right]);
		}
	}

	private void parse(Rule.Skip r) {
		if (nextKind == r.symbolKind) {
			next++;
			if (nextKind != Symbol.END) {
				nextKind = symbols[tokens[next].ref].kind;
			}
		} else {
			report(tokens[next].start, nextKind, r.symbolKind);
		}
	}

	private void parse(Rule.Accept r) {
		if (nextKind == r.symbolKind) {
			nodes.add(new Id(tokens[next].ref, tokens[next].start));
			next++;
			nextKind = symbols[tokens[next].ref].kind;
		} else {
			report(tokens[next].start, nextKind, r.symbolKind);
		}
	}

	private void parse(Rule.Build r) {
		Tree node1, node2;
		if (r.size == 1) {
			node1 = (Tree) nodes.pop();
			nodes.add(Tree.build1(r.kind, node1));
		} else if (r.size == 2) {
			node2 = (Tree) nodes.pop();
			node1 = (Tree) nodes.pop();
			nodes.add(Tree.build2(r.kind, node1, node2));
		} else {
			System.out.println("Internal error: unimplemented node size");
			System.exit(1);
		}
	}

	private boolean startsWith(Rule r, int symbolKind) {
		int ruleKind = r.getKind();
		boolean result;
		switch (ruleKind) {
		case Rule.THEN: {
			Rule.Then rt = (Rule.Then) r;
			result = startsWith(rules[rt.left], symbolKind);
		}
			break;
		case Rule.SKIP: {
			Rule.Skip rk = (Rule.Skip) r;
			result = rk.symbolKind == symbolKind;
		}
			break;
		case Rule.ACCEPT: {
			Rule.Accept rs = (Rule.Accept) r;
			result = rs.symbolKind == symbolKind;
		}
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	private void report(int pos, int found, int expecting) {
		// TODO Show error in tree. Send information
		// Tak samo jak w parserze na zajeciach.
		System.out.println("Parse error at position " + pos);
		String message = null;
		if (found == Symbol.BAD_CHAR) {
			message = "Illegal character";
		} else if (found == Symbol.BAD_NUMBER) {
			message = "Incomplete number";
		} else if (expecting == Symbol.END) {
			message = "Expecting end of input";
		} else if (expecting == Symbol.NUMBER) {
			message = "Expecting a number";
		} else {
			int n = -1;
			for (int i = 0; i < Symbol.keys.length; i++) {
				if (expecting == Symbol.keys[i].kind) {
					n = i;
				}
			}
			message = "Expecting (e.g.) " + Symbol.keys[n].spelling;
		}

		if (message != null) {
			throw new IllegalArgumentException(message);
		}
	}
}