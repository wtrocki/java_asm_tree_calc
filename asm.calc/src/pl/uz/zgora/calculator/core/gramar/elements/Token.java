package pl.uz.zgora.calculator.core.gramar.elements;

/* A token consists of a position in the source and a reference to a symbol
 (which contains the spelling and kind).  The kinds are constants. */

public class Token {
	public int start;
	
	// TODO Change to symbol. Token should be extracted in tree. 
	// This will remove redundant data.
	// See Strausup C++ code
	public int ref;

	public Token(int s, int r) {
		start = s;
		ref = r;
	}
}
