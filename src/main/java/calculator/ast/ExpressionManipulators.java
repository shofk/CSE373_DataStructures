package calculator.ast;

import calculator.interpreter.Environment;

import java.util.Iterator;

import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NoSuchKeyException;

/**
 * All of the public static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Checks to make sure that the given node is an operation AstNode with the expected
     * name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        assertNodeMatches(node, "toDouble", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }

    /**
     * Helper method for handleToDouble method
     * @param variables variables saved
     * @param node node the user wants to evaluate
     * @return evaluated double of the given node
     */
    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases. 
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            try {
                return toDoubleHelper(variables, variables.get(node.getName()));
            } catch (NoSuchKeyException e) {
                throw new EvaluationError("Variable not found.");
            }
        } else {
            // save operator
            String operator = node.getName();
            IList<AstNode> children = node.getChildren();
            
            if (operator.equals("negate")) {
                assertNodeMatches(node, "negate", 1);
                return -toDoubleHelper(variables, children.get(0));
            } else if (operator.equals("sin")) {
                assertNodeMatches(node, "sin", 1);
                return Math.sin(toDoubleHelper(variables, children.get(0)));
            } else if (operator.equals("cos")) {
                assertNodeMatches(node, "cos", 1);
                return Math.cos(toDoubleHelper(variables, children.get(0)));
            } else if (operator.equals("+")) {
                assertNodeMatches(node, "+", 2);
                return toDoubleHelper(variables, children.get(0)) + toDoubleHelper(variables, children.get(1));
            } else if (operator.equals("-")) {
                assertNodeMatches(node, "-", 2);
                return toDoubleHelper(variables, children.get(0)) - toDoubleHelper(variables, children.get(1));
            } else if (operator.equals("*")) {
                assertNodeMatches(node, "*", 2);
                return toDoubleHelper(variables, children.get(0)) * toDoubleHelper(variables, children.get(1));
            } else if (operator.equals("/")) {
                assertNodeMatches(node, "/", 2);
                return toDoubleHelper(variables, children.get(0)) / toDoubleHelper(variables, children.get(1));
            } else if (operator.equals("^")) {
                assertNodeMatches(node, "^", 2);
                return Math.pow(toDoubleHelper(variables, children.get(0)), toDoubleHelper(variables, children.get(1)));
            }
            // when operator does not match to any of the expected ones
            throw new EvaluationError("Operator not found.");
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?

        assertNodeMatches(node, "simplify", 1);
        return simplifyHelper(env, node.getChildren().get(0));

        
    }
    
    /**
     * Helper method for handleSimplify method
     * @param env environment settings
     * @param node node user wants to simplify
     * @return simplified node
     */
    private static AstNode simplifyHelper(Environment env, AstNode node) {
        // set reusable variables
        IList<AstNode> originalChildren = node.getChildren();
        // creates children list separate from the original one
        IList<AstNode> children = new DoubleLinkedList<AstNode>();
        Iterator<AstNode> iter = originalChildren.iterator();
        while (iter.hasNext()) {
            children.add(iter.next());
        }
        
        // variables
        IDictionary<String, AstNode> variables = env.getVariables(); 
        AstNode simplified;
        
        // checks one-by-one
        if (!node.isOperation()) {
            if (node.isVariable() && variables.containsKey(node.getName())) {
                String name = node.getName();
                // simplify the variable
                simplified =  simplifyHelper(env, variables.get(name));
                // base case 1: return the variable in numeric form
                if (simplified.isNumber()) {
                    return simplified;
                } else {
                    return variables.get(name);
                }
            } else {
                // base case 2 & 3: return the node as it is
                return node;
            }
        } else {
            String operator = node.getName();
            // if the children are not numeric, check if they can be numeric
            if (!children.get(0).isNumber()) {
                children.set(0, simplifyHelper(env, children.get(0)));
            }
            if (children.size() == 2 && !children.get(1).isNumber()) {
                children.set(1, simplifyHelper(env, children.get(1)));
            }
            simplified = new AstNode(operator, children);
            if (children.size() == 1 && children.get(0).isNumber()) {
                AstNode child0 = children.get(0);
                if (operator.equals("negate")) {
                    simplified = new AstNode(-child0.getNumericValue());
                }
            } else if (children.size() == 2 && children.get(0).isNumber() && children.get(1).isNumber()) {
                AstNode child0 = children.get(0);
                AstNode child1 = children.get(1);
                if (operator.equals("+")) {
                    simplified = new AstNode(child0.getNumericValue() + child1.getNumericValue());
                } else if (operator.equals("-")) {
                    simplified = new AstNode(child0.getNumericValue() - child1.getNumericValue());
                } else if (operator.equals("*")) {
                    simplified = new AstNode(child0.getNumericValue() * child1.getNumericValue());
                }
            }
            return simplified;
        }
    }
    
    

    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax, step)'
     * AstNode and generates the corresponding plot on the ImageDrawer attached to the
     * environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        // checks if the correct input is given
        assertNodeMatches(node, "plot", 5);

        // get children for simplicity
        IList<AstNode> children = node.getChildren();
        // variable name
        String xVar = children.get(1).getName();
        if (env.getVariables().containsKey(xVar)) {
            throw new EvaluationError("Variable already defined.");
        }
        
        // increment saved
        double[] increments = {toDoubleHelper(env.getVariables(), children.get(2)),
                toDoubleHelper(env.getVariables(), children.get(3)),
                toDoubleHelper(env.getVariables(), children.get(4))};
        
        if (increments[0] > increments[1]) {
            throw new EvaluationError("Minimum larger than Maximum.");
        }
        if (increments[2] <= 0) {
            throw new EvaluationError("Incrementation less than or equal to zero.");
        }
        
        // creates the lists of x and y variables
        IList<Double> xVals = new DoubleLinkedList<>();
        IList<Double> yVals = new DoubleLinkedList<>();
        IDictionary<String, AstNode> variables = env.getVariables();
        for (double i = increments[0]; i <= increments[1]; i += increments[2]) {
            xVals.add(i);
            variables.put(xVar, new AstNode(i));
            yVals.add(toDoubleHelper(variables, children.get(0))); // throws EvaluationError
        }
        // clears the variable
        variables.remove(xVar);
        
        // plot
        env.getImageDrawer().drawScatterPlot("Plot", xVar, "output", xVals, yVals);
        
        // Returns the simplified node
        return simplifyHelper(env, children.get(0));
    }
}
