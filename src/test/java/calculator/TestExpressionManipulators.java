package calculator;

import org.junit.Test;

import calculator.ast.AstNode;
import calculator.ast.ExpressionManipulators;
import calculator.interpreter.Calculator;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ArrayDictionary;

public class TestExpressionManipulators {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println(calc.evaluate("sin(42)"));
    }
}
