package pl.uz.zgora.calculator.asm.op;

import pl.uz.zgora.calculator.asm.StringTemplates;

public class Value extends Element {

	private double value;

	public Value(double value) {
		this.value = value;
	}

	@Override
	public String evaluate() {
		return StringTemplates.push(value);
	}

	@Override
	public String toString() {
		return value + " ";
	}
}
