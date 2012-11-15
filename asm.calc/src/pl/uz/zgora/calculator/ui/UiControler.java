package pl.uz.zgora.calculator.ui;

import java.io.IOException;

import pl.uz.zgora.calculator.controler.Controler;
import pl.uz.zgora.calculator.core.gramar.Interpreter;
import pl.uz.zgora.calculator.core.gramar.Parser;
import pl.uz.zgora.calculator.core.gramar.Scanner;
import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.tree.Tree;

public class UiControler {

	private Controler program;
	private Scanner scanner;
	private Parser parser;
	private Interpreter interpreter;

	public static void main(String[] args) throws IOException {
		UiControler prog = new UiControler();
		MainWorkbench main = new MainWorkbench(prog);
		prog.init();
		main.open();
	}

	public void init() {
		scanner = new Scanner();
		parser = new Parser();
		interpreter = new Interpreter();
	}

	public double calc(String text) {
		program = new Controler(text);
		program = scanner.scan(program);
		program = parser.parse(program);
		return interpreter.interpret(program);
	}

	public Tree getTree() {
		return program.getTree();
	}

	public Symbol[] getSymbols() {
		return program.getSymbols();
	}
}
