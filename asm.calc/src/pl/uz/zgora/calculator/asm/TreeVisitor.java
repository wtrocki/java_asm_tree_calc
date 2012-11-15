package pl.uz.zgora.calculator.asm;

import pl.uz.zgora.calculator.controler.Controler;
import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.tree.Add;
import pl.uz.zgora.calculator.core.tree.Bracket;
import pl.uz.zgora.calculator.core.tree.Div;
import pl.uz.zgora.calculator.core.tree.Id;
import pl.uz.zgora.calculator.core.tree.Mul;
import pl.uz.zgora.calculator.core.tree.Pow;
import pl.uz.zgora.calculator.core.tree.Sub;
import pl.uz.zgora.calculator.core.tree.Tree;

/* Class Interpreter - evaluate the expression held in the parse tree */

public class TreeVisitor {
	Symbol[] symbols;

	public TreeVisitor() {
	}

	public double interpret(Controler program) {
		symbols = program.getSymbols();
		return eval(program.getTree());
	}

	private double eval(Tree tree) {
		switch (tree.getKind()) {
		case ID:
			return eval((Id) tree);
		case BRACKET:
			return eval((Bracket) tree);
		case ADD:
			return eval((Add) tree);
		case SUB:
			return eval((Sub) tree);
		case MUL:
			return eval((Mul) tree);
		case DIV:
			return eval((Div) tree);
		case POW:
			return eval((Pow) tree);
		default:
			return 0;
		}
	}

	private double eval(Id t) {
		return symbols[t.ref].value;
	}

	private double eval(Bracket t) {
		return eval(t.expr);
	}

	private double eval(Add t) {
		return eval(t.left) + eval(t.right);
	}

	private double eval(Sub t) {
		return eval(t.left) - eval(t.right);
	}

	private double eval(Mul t) {
		return eval(t.left) * eval(t.right);
	}

	private double eval(Div t) {
		return eval(t.left) / eval(t.right);
	}

	private double eval(Pow t) {
		return Math.pow(eval(t.left), eval(t.right));
	}
}
