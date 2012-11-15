package pl.uz.zgora.calculator.core.gramar.elements;

/* A symbol has a spelling, an integer tag to say what kind of symbol it is,
 and a value (for numbers).  A list of the key symbols is provided. */

public class Symbol {
	public String spelling;
	public int kind;
	public double value;

	public static final int PLUS = 0, MINUS = 1, TIMES = 2, SLASH = 3, HAT = 4,
			OPEN = 5, CLOSE = 6, END = 7;

	public static final int NUMBER = 8;
	public static final int BAD_CHAR = 9;
	public static final int BAD_NUMBER = 10;
	public static final int KEY_OR_BAD_CHAR = 11;
	public static Symbol[] keys = { new Symbol("+", PLUS, 0.0),
			new Symbol("-", MINUS, 0.0), new Symbol("*", TIMES, 0.0),
			new Symbol("/", SLASH, 0.0), new Symbol("^", HAT, 0.0),
			new Symbol("(", OPEN, 0.0), new Symbol(")", CLOSE, 0.0),
			new Symbol("", END, 0.0) };

	public Symbol(String s, int k, double v) {
		spelling = s;
		kind = k;
		value = v;
	}
	
	@Override
	public String toString() {
		return spelling;
	}
}
