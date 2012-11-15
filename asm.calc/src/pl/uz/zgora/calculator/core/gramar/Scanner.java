package pl.uz.zgora.calculator.core.gramar;

/**
 Provides a scan method which converts a source text into tokens.  The
 lexical rules for the Calc language are

 program = (' ' | '\n' | token)*
 token   = number | '+' | '-' | '*' | '/' | '^' | '(' | ')'
 number = digit+ ('.' digit+)?

 @author troken
 */
import java.util.HashMap;
import java.util.Vector;

import pl.uz.zgora.calculator.controler.Controler;
import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.gramar.elements.Token;

public class Scanner {
	// Scan a source text, producing an array of tokens and symbol table
	public Controler scan(Controler program) {
		program.source = prepare(program.source);
		PreToken[] toks = split(program.source);
		symbolize(program, toks);
		return program;
	}

	// Prepare the source text for processing
	private String prepare(String s1) {
		String s2 = lines(s1);
		String s3 = tabs(s2);
		return s3;
	}

	// Convert line endings to Unix "\n" format, ensure last line has an end
	private String lines(String text) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\r') {
				if ((i < text.length() - 1) && (text.charAt(i + 1) == '\n')) {
					i++;
				}
				buffer.append('\n');
			} else {
				buffer.append(c);
			}
		}
		if ((buffer.length() > 0)
				&& (buffer.charAt(buffer.length() - 1) != '\n')) {
			buffer.append('\n');
		}
		return buffer.toString();
	}

	// Expand tabs, assuming tab stops every 8 columns
	private String tabs(String text) {
		StringBuffer buffer = new StringBuffer();
		int col = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\t') {
				int skip = 8 - col % 8;
				for (int j = 0; j < skip; j++) {
					buffer.append(' ');
				}
				col = col + skip;
			} else {
				buffer.append(c);
				if (c == '\n') {
					col = 0;
				} else {
					col++;
				}
			}
		}
		return buffer.toString();
	}

	// A pre-token is a temporary data structure for holding tokens after
	// splitting, but before symbolizing. A pre-token has start and end
	// positions in the source text, and a tentative kind tag, to be refined
	// later by symbolize().
	private static class PreToken {
		int start, end, kind;

		PreToken(int s, int e, int k) {
			start = s;
			end = e;
			kind = k;
		}
	}

	// Split the prepared source text into pre-tokens. Key recognition is left
	// to the symbolize stage. (In a larger language, simple keys would be
	// recognized here, but keywords would be left to later.)
	private PreToken[] split(String source) {
		Vector toks = new Vector();
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			switch (c) {
			case ' ':
			case '\n':
				break;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9': {
				PreToken t = number(source, i);
				toks.add(t);
				i = t.end - 1;
			}
				break;
			default:
				toks.add(new PreToken(i, i + 1, Symbol.KEY_OR_BAD_CHAR));
				break;
			}
		}
		toks.add(new PreToken(source.length(), source.length(), Symbol.END));
		return (PreToken[]) toks.toArray(new PreToken[0]);
	}

	// Get a number from the source text, starting at a given position
	private PreToken number(String source, int i) {
		PreToken num;
		int j = i, n = source.length();

		while ((j < n) && Character.isDigit(source.charAt(j))) {
			j++;
		}
		if ((j < n) && (source.charAt(j) == '.')) {
			j++;
			if ((j < n) && Character.isDigit(source.charAt(j))) {
				while ((j < n) && Character.isDigit(source.charAt(j))) {
					j++;
				}
				num = new PreToken(i, j, Symbol.NUMBER);
			} else {
				num = new PreToken(i, j, Symbol.BAD_NUMBER);
			}
		} else {
			num = new PreToken(i, j, Symbol.NUMBER);
		}
		return num;
	}

	// Convert the pre-tokens into tokens which refer to a symbol table
	private void symbolize(Controler program, PreToken[] toks) {
		String source = program.source;
		HashMap table = new HashMap();
		Vector<Symbol> syms = new Vector();
		Token[] tokens = new Token[toks.length];
		// TODO Wszystkie symbole s¹ generowane niezaleznie od u¿ycia.
		// Nale¿y wyeliminowaæ nadmiarowoœæ poprzez poŸniejsze tworzenie
		// symboli zale¿nie od tokenów.
		for (int i = 0; i < Symbol.keys.length; i++) {
			syms.add(Symbol.keys[i]);
			table.put(Symbol.keys[i].spelling, new Integer(i));
		}

		for (int i = 0; i < toks.length; i++) {
			PreToken tok = toks[i];
			String spelling;
			Integer ref;

			if (tok.start == source.length()) {
				spelling = "";
			} else {
				spelling = source.substring(tok.start, tok.end);
			}

			ref = (Integer) table.get(spelling);
			if (ref == null) {
				int r = syms.size();
				if (tok.kind == Symbol.NUMBER) {
					double val = Double.parseDouble(spelling);
					syms.add(new Symbol(spelling, tok.kind, val));
				} else if (tok.kind == Symbol.KEY_OR_BAD_CHAR) {
					syms.add(new Symbol(spelling, Symbol.BAD_CHAR, 0.0));
				} else {
					syms.add(new Symbol(spelling, tok.kind, 0.0));
				}
				table.put(spelling, new Integer(r));
				tokens[i] = new Token(tok.start, r);
			} else {
				tokens[i] = new Token(tok.start, ref.intValue());
			}
		}
		program.setSymbols(syms.toArray(new Symbol[0]));
		program.setTokens(tokens);
	}
}
