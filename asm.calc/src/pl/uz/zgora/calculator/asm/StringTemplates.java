package pl.uz.zgora.calculator.asm;

public class StringTemplates {
	public static String ASM_COMMENT = ";; Autogenerated X86 asembler\n";
	public static String PUSH_VARIABLE = "push dword %1$s";
	public static String PUSH_REGISTER = "push %1$s";
	public static String POP_VARIABLE = "pop %1$s";
	public static String ADD_VARIABLE = "add %1$s %2$s";
	public static String SUB_VARIABLE = "sub %1$s %2$s";
	public static String MUL_VARIABLE = "mul %1$s %2$s";
	public static String DIV_VARIABLE = "div %1$s %2$s";
	public static String POWER_VARIABLE = "shl %1$s %2$s";
	public static String RESULT = "mov dword %1$s, ebx";
	public static String DELIMITER = "\n";

	public static String push(Double value) {
		// TODO operacje tylko na integerach
		return String.format(StringTemplates.PUSH_VARIABLE, value.intValue())
				+ DELIMITER;
	}

	public static String push(Registers value) {
		return String.format(StringTemplates.PUSH_REGISTER, value) + DELIMITER;
	}

	public static String pop(Registers register) {
		return String.format(StringTemplates.POP_VARIABLE, register.name())
				+ DELIMITER;
	}

	public static String add(Registers first, Registers second) {
		return String.format(StringTemplates.ADD_VARIABLE, first.name(),
				second.name())
				+ DELIMITER;
	}

	public static String sub(Registers first, Registers second) {
		return String.format(StringTemplates.SUB_VARIABLE, first.name(),
				second.name())
				+ DELIMITER;
	}

	public static String mul(Registers first, Registers second) {
		return String.format(StringTemplates.MUL_VARIABLE, first.name(),
				second.name())
				+ DELIMITER;
	}

	public static String div(Registers first, Registers second) {
		return String.format(StringTemplates.DIV_VARIABLE, first.name(),
				second.name())
				+ DELIMITER;
	}

	public static String pow(Registers first, Registers second) {
		return String.format(StringTemplates.POWER_VARIABLE, first.name(),
				second.name()) + DELIMITER;
	}

	public static String result(Registers reg) {
		return String.format(StringTemplates.RESULT, reg.name()) + DELIMITER;
	}
}
