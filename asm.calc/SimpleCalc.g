// PROSTY GENERATOR KALKULATORA BAZUJACY NA SKRYPCIE Z KSIAZKI B. STROUSUPA.
// AUTOR W. TROCKI.

grammar SimpleCalc;

options {
    output = AST;
    ASTLabelType = CommonTree;
}

tokens {
	PLUS 	= '+' ;
	MINUS	= '-' ;
	MULT	= '*' ;
	DIV	= '/' ;
  LPAREN  = '(' ;
  RPAREN  = ')' ;
}

@header {
  package pl.zgora.uz.calc;
  import java.util.HashMap;
  import java.io.BufferedReader;
  import java.io.InputStreamReader;
}

@lexer::header {
   package pl.zgora.uz.calc;
}

@members {
    // Zapis zmiennych
    HashMap memory = new HashMap();
    public static void main(String[] args) throws Exception {
		      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    while (true) {
			      String readLine = br.readLine();
			      if (readLine.equals("q")) {
			        break;
			      }
			      readLine = readLine + "\n";
			      
			      SimpleCalcLexer lex = new SimpleCalcLexer(new ANTLRStringStream(
			          readLine));
			      CommonTokenStream tokens = new CommonTokenStream(lex);
			      SimpleCalcParser parser = new SimpleCalcParser(tokens);
			      
			      try {
			        System.out.print(parser.prog().getTree());
			      } catch (RecognitionException e) {
			        e.printStackTrace();
			      }
        }
    }
}

//parser

prog:   stat+ ;
                
stat:   expr NEWLINE {System.out.println($expr.value);}
    |   ID '=' expr NEWLINE
        {memory.put($ID.text, new Integer($expr.value));}
    |   NEWLINE
    ;

expr returns [int value]
    :   e=multExpr {$value = $e.value;}
        (   '+' e=multExpr {$value += $e.value;}
        |   '-' e=multExpr {$value -= $e.value;}
        )*
    ;

multExpr returns [int value]
    :   e=atom {$value = $e.value;} ('*' e=atom {$value *= $e.value;})*
    ; 

atom returns [int value]
    :   INT {$value = Integer.parseInt($INT.text);}
    |   ID
        {
        Integer v = (Integer)memory.get($ID.text);
        if ( v!=null ) $value = v.intValue();
        else System.err.println("undefined variable "+$ID.text);
        }
    |   '(' e=expr ')' {$value = $e.value;}
    ;
    
// lexer 
ID  :   ('a'..'z'|'A'..'Z')+ ;
INT :   '0'..'9'+ ;
NEWLINE:'\r'? '\n' ;
WS  :   (' '|'\t')+ {skip();} ;