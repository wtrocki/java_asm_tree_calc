package pl.uz.zgora.calculator.core.gramar;

/* Class Grammar - produces grammar rules for the interpretive parser. */

import java.util.StringTokenizer;

import pl.uz.zgora.calculator.core.gramar.elements.Rule;
import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.tree.TreeElemens;

public class Grammar {
	Rule[] rules;

	Grammar() {
		compile();
	}

	private static String[] grammar = {
			"program = expr end",
			"expr = term expr2",
			"expr2 = plus term add expr2 | minus term sub expr2 | nothing",
			"term = factor term2",
			"term2 = times factor mul term2 | slash factor div term2 | nothing",
			"factor = atom factor2", "factor2 = hat factor pow | nothing",
			"atom = number | open expr close bracket" };

	private static String[] primNames = { "end", "plus", "minus", "times",
			"slash", "hat", "open", "close", "number", "add", "sub", "mul",
			"div", "pow", "bracket", "nothing" };

	private static Rule[] primRules = { new Rule.Skip(Symbol.END),
			new Rule.Skip(Symbol.PLUS), new Rule.Skip(Symbol.MINUS),
			new Rule.Skip(Symbol.TIMES), new Rule.Skip(Symbol.SLASH),
			new Rule.Skip(Symbol.HAT), new Rule.Skip(Symbol.OPEN),
			new Rule.Skip(Symbol.CLOSE), new Rule.Accept(Symbol.NUMBER),
			new Rule.Build(TreeElemens.ADD, 2),
			new Rule.Build(TreeElemens.SUB, 2),
			new Rule.Build(TreeElemens.MUL, 2),
			new Rule.Build(TreeElemens.DIV, 2),
			new Rule.Build(TreeElemens.POW, 2),
			new Rule.Build(TreeElemens.BRACKET, 1), new Rule.Empty() };

	// Convert the text of the grammar into an array of rules. As the rules are
	// mutually recursive, an array of nodes is used, with indexes as pointers.
	// (An alternative approach would be to create the top node of each
	// definition first, with null pointers in it, and fill in the pointers in a
	// second pass.)

	private void compile() {
		int ndefs = grammar.length;
		String[][] definitions = new String[ndefs][];
		int nitems = ndefs + primNames.length;
		String[] names = new String[nitems];
		int[] sizes = new int[nitems];
		int[] indexes = new int[nitems];

		for (int i = 0; i < ndefs; i++) {
			definitions[i] = scanDef(grammar[i]);
			names[i] = definitions[i][0];
			sizes[i] = sizeDef(definitions[i]);
			if (i == 0) {
				indexes[i] = 0;
			} else {
				indexes[i] = indexes[i - 1] + sizes[i - 1];
			}
		}
		for (int i = 0; i < primNames.length; i++) {
			names[ndefs + i] = primNames[i];
			sizes[ndefs + i] = 1;
			if (i == 0) {
				indexes[ndefs + i] = indexes[ndefs - 1] + sizes[ndefs - 1];
			} else {
				indexes[ndefs + i] = indexes[ndefs + i - 1] + 1;
			}
		}

		rules = new Rule[indexes[nitems - 1] + sizes[nitems - 1]];
		for (int i = 0; i < ndefs; i++) {
			parseDef(definitions[i], indexes[i], names, indexes);
		}
		for (int i = 0; i < primNames.length; i++) {
			rules[indexes[ndefs + i]] = primRules[i];
		}
	}

	// Split a string into tokens, assuming they are separated by white space.
	private String[] scanDef(String text) {
		StringTokenizer scanner = new StringTokenizer(text);
		String[] tokens = new String[scanner.countTokens()];
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = scanner.nextToken();
		}
		return tokens;
	}

	// Predict how many rule nodes will be needed by a grammar definition

	private int sizeDef(String[] def) {
		int n = def.length - 3;
		for (int i = 2; i < def.length; i++) {
			if (def[i].equals("|")) {
				n = n - 1;
			}
		}
		return n;
	}

	// Convert a grammar definition def into rules, starting at index r in the
	// rules array, using the array of rule names and their indexes in the rules
	// array.
	private void parseDef(String[] def, int r, String[] names, int[] indexes) {
		int nalts;
		int[] altIndexes, altStarts, altEnds;

		if ((def.length < 4) || !def[1].equals("=")) {
			System.out.println("Internal error: bad grammar rule");
			System.exit(1);
		}

		nalts = 1;
		for (int i = 2; i < def.length; i++) {
			if (def[i].equals("|")) {
				nalts++;
			}
		}

		altStarts = new int[nalts];
		altEnds = new int[nalts];
		altStarts[0] = 2;
		altEnds[nalts - 1] = def.length;
		for (int i = 2, alt = 0; i < def.length; i++) {
			if (def[i].equals("|")) {
				altEnds[alt] = i;
				altStarts[alt + 1] = i + 1;
				alt = alt + 1;
			}
		}

		altIndexes = new int[nalts];
		altIndexes[0] = r + nalts - 1;
		for (int i = 0; i < nalts - 1; i++) {
			int n = altEnds[i] - altStarts[i];
			if (n < 1) {
				System.out.println("Internal error: empty alternative");
				System.exit(1);
			}
			altIndexes[i + 1] = altIndexes[i] + n - 1;
		}

		for (int i = 0; i < nalts; i++) {
			int[] seqIndexes = new int[altEnds[i] - altStarts[i]];
			for (int j = 0; j < seqIndexes.length; j++) {
				int item = find(def[altStarts[i] + j], names);
				seqIndexes[j] = indexes[item];
			}
			if (seqIndexes.length == 1) {
				altIndexes[i] = seqIndexes[0];
			} else {
				parseSeq(seqIndexes, altIndexes[i]);
			}
		}
		parseAlts(altIndexes, r);
	}

	// Build a sequence of symbols into Then rules
	private void parseSeq(int[] items, int r) {
		for (int i = 0; i < items.length - 1; i++) {
			if (i < items.length - 2) {
				rules[r + i] = new Rule.Then(items[i], r + i + 1);
			} else {
				rules[r + i] = new Rule.Then(items[i], items[i + 1]);
			}
		}
	}

	// Build a sequence of alternatives into Or rules
	private void parseAlts(int[] alts, int r) {
		for (int i = 0; i < alts.length - 1; i++) {
			if (i < alts.length - 2) {
				rules[r + i] = new Rule.Or(alts[i], r + i + 1);
			} else {
				rules[r + i] = new Rule.Or(alts[i], alts[i + 1]);
			}
		}
	}

	// Find the index of a string in an array of strings
	private int find(String s, String[] list) {
		int n = -1, i = 0;
		while ((n < 0) && (i < list.length)) {
			if (list[i].equals(s)) {
				n = i;
			}
			i++;
		}
		if (n < 0) {
			System.out.println("Internal error: can't find " + s);
			System.exit(1);
		}
		return n;
	}
}
