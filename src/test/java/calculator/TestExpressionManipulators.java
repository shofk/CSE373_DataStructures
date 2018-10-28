package calculator;

import calculator.interpreter.Calculator;

public class TestExpressionManipulators {
    public static void main(String[] args) {
        Calculator calc = new Calculator();

        System.out.println(calc.evaluate("z := 2 + x"));
        System.out.println(calc.evaluate("simplify(z)"));
        System.out.println(calc.evaluate("x := 10"));
        System.out.println(calc.evaluate("simplify(z)"));
        System.out.println(calc.evaluate("y := x * 3 + sin(40)"));
        System.out.println(calc.evaluate("x := 2"));
        System.out.println(calc.evaluate("z"));
        System.out.println(calc.evaluate("toDouble(y)"));
    }
}
