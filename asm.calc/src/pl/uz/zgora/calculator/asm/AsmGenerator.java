package pl.uz.zgora.calculator.asm;

import java.util.Stack;

import pl.uz.zgora.calculator.asm.op.Element;
import pl.uz.zgora.calculator.core.gramar.elements.Symbol;
import pl.uz.zgora.calculator.core.tree.Tree;

public class AsmGenerator {

	private Stack<Element> stack;

	public AsmGenerator(Tree tree, Symbol[] symbols) {
		ComputeHeapFromTree asmComputing = new ComputeHeapFromTree(tree,
				symbols);
		asmComputing.computeHeap();
		stack = asmComputing.getStack();
	}

	public String generate() throws Exception {
		if ((stack != null) && !stack.empty()) {
			StringBuffer sb = new StringBuffer(StringTemplates.ASM_COMMENT);
			while (!stack.empty()) {
				Element currentElement = stack.pop();
				sb.append(currentElement.evaluate());
			}
			sb.append(StringTemplates.result(Registers.eax));
			return sb.toString();
		} else {
			// TODO Change to generation Exception
			throw new Exception();
		}
	}
}
