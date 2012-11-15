package pl.uz.zgora.calculator.asm.op;

import pl.uz.zgora.calculator.asm.Registers;
import pl.uz.zgora.calculator.asm.StringTemplates;
import pl.uz.zgora.calculator.core.tree.TreeElemens;

public class Operation extends Element {

	private double op1, op2;
	private final TreeElemens kind;

	public Operation(TreeElemens kind) {
		this.kind = kind;
	}

	@Override
	public String evaluate() {
		StringBuffer sb = new StringBuffer();
		sb.append(";; ").append(kind).append("\n");
		sb.append(StringTemplates.pop(Registers.eax));
		sb.append(StringTemplates.pop(Registers.ebx));
		switch (this.kind) {
		case ADD:
			sb.append(StringTemplates.add(Registers.ebx, Registers.eax));
			break;
		case SUB:
			sb.append(StringTemplates.sub(Registers.ebx, Registers.eax));
			break;
		case MUL:
			sb.append(StringTemplates.mul(Registers.ebx, Registers.eax));
			break;
		case DIV:
			sb.append(StringTemplates.div(Registers.ebx, Registers.eax));
			break;
		case POW:
			sb.append(StringTemplates.push(Registers.eax));
			sb.append(StringTemplates.push(Registers.ebx));
			sb.append("call near power\n");
			// TODO POWER
		}
		sb.append(StringTemplates.push(Registers.eax));
		return sb.toString();
	}

	public void setOp1(double op1) {
		this.op1 = op1;
	}

	public double getOp1() {
		return op1;
	}

	public void setOp2(double op2) {
		this.op2 = op2;
	}

	public double getOp2() {
		return op2;
	}

	public TreeElemens getKind() {
		return kind;
	}

	@Override
	public String toString() {
		return " " + kind + " ";
	}
}
