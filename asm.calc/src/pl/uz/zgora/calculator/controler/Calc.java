package pl.uz.zgora.calculator.controler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.uz.zgora.calculator.core.gramar.Interpreter;
import pl.uz.zgora.calculator.core.gramar.Parser;
import pl.uz.zgora.calculator.core.gramar.Scanner;


/**
 * Class Calc - a simple calculator, written using general compilator techniques
 * 
 */

public class Calc {
	private static boolean perform = true;

	public static void main(String[] args) throws IOException {
		Calc prog = new Calc();
		while (perform) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String readLine = br.readLine();
			if (readLine.isEmpty())
				return;
			prog.calc(readLine);
		}
	}

	void calc(String text) {
		Controler program = new Controler(text);
		Scanner scanner = new Scanner();
		Parser parser = new Parser();
		Interpreter interpreter = new Interpreter();
		program = scanner.scan(program);
		program = parser.parse(program);
		double result = interpreter.interpret(program);
		printResult(result);
		showTree(program);
	}

	private void printResult(double result) {
		System.out.println(result);
	}
	
	//TODO show tree
	void showTree(Controler program){
		program.getTree();
	}
}
