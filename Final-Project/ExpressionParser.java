import java.util.*;

/**
 * Parses a formula string into an ExpressionNode using the Shunting Yard algorithm.
 */
public class ExpressionParser {

    // Internal token used by the parser
    private static class OpToken {
        String type; // "op", "func", "paren"
        char op;
        String funcName;
        int argCount;

        OpToken(String t, char o) {
            type = t;
            op = o;
            funcName = null;
            argCount = 0;
        }

        OpToken(String t, String fn) {
            type = t;
            funcName = fn;
            argCount = 1; // first argument by default
        }
    }

    // Public entry point
    public static ExpressionNode parse(String formula) {
        Deque<OpToken> opStack = new ArrayDeque<>();
        Deque<ExpressionNode> valueStack = new ArrayDeque<>();

        Map<Character, Integer> prec = new HashMap<>();
        prec.put('+', 1);
        prec.put('-', 1);
        prec.put('*', 2);
        prec.put('/', 2);

        boolean prevTokenWasValue = false;

        for (int i = 0; i < formula.length(); ) {
            char ch = formula.charAt(i);

            if (Character.isWhitespace(ch)) {
                i++;
                continue;
            }

            if (ch == '(') {
                opStack.push(new OpToken("paren", '('));
                prevTokenWasValue = false;
                i++;
            } else if (ch == ')') {
                boolean foundLeftParen = false;
                while (!opStack.isEmpty()) {
                    OpToken top = opStack.pop();
                    if (top.type.equals("paren") && top.op == '(') {
                        foundLeftParen = true;
                        if (!opStack.isEmpty() && opStack.peek().type.equals("func")) {
                            OpToken funcToken = opStack.pop();
                            int argCount = funcToken.argCount;
                            if (argCount == 0) {
                                throw new IllegalArgumentException("Function " + funcToken.funcName + " has no arguments");
                            }
                            if (valueStack.size() < argCount) {
                                throw new IllegalArgumentException("Insufficient arguments for function " + funcToken.funcName);
                            }
                            ExpressionNode[] args = new ExpressionNode[argCount];
                            for (int k = argCount - 1; k >= 0; k--) {
                                args[k] = valueStack.pop();
                            }
                            FunctionNode funcNode = new FunctionNode(funcToken.funcName, Arrays.asList(args));
                            valueStack.push(funcNode);
                        }
                        break;
                    } else {
                        ExpressionNode rightNode = valueStack.pop();
                        ExpressionNode leftNode = valueStack.pop();
                        ExpressionNode opNode = new BinaryOpNode(top.op, leftNode, rightNode);
                        valueStack.push(opNode);
                    }
                }
                if (!foundLeftParen) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                prevTokenWasValue = true;
                i++;
            } else if (ch == ';') {
                boolean foundParen = false;
                while (!opStack.isEmpty()) {
                    OpToken top = opStack.peek();
                    if (top.type.equals("paren") && top.op == '(') {
                        foundParen = true;
                        break;
                    }
                    OpToken opToken = opStack.pop();
                    ExpressionNode rightNode = valueStack.pop();
                    ExpressionNode leftNode = valueStack.pop();
                    valueStack.push(new BinaryOpNode(opToken.op, leftNode, rightNode));
                }
                if (!foundParen) {
                    throw new IllegalArgumentException("Separator ';' not inside function call");
                }

                // find function token ABOVE the '('
                OpToken functionToken = null;
                Iterator<OpToken> it = opStack.descendingIterator();

                while (it.hasNext()) {
                    OpToken tok = it.next();
                    if (tok.type.equals("paren") && tok.op == '(') {
                        break;
                    }
                    if (tok.type.equals("func")) {
                        functionToken = tok;
                        break;
                    }
                }

                if (functionToken == null) {
                    throw new IllegalArgumentException("Argument separator used outside of function");
                }

                functionToken.argCount++;



                prevTokenWasValue = false;
                i++;
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                if (!prevTokenWasValue) {
                    if (ch == '+') {
                        i++;
                        continue;
                    } else if (ch == '-') {
                        valueStack.push(new ConstantNode(0.0));
                    }
                }
                while (!opStack.isEmpty() && opStack.peek().type.equals("op")) {
                    OpToken topOp = opStack.peek();
                    if (prec.containsKey(topOp.op) && prec.containsKey(ch) &&
                            prec.get(topOp.op) >= prec.get(ch)) {
                        topOp = opStack.pop();
                        ExpressionNode rightNode = valueStack.pop();
                        ExpressionNode leftNode = valueStack.pop();
                        valueStack.push(new BinaryOpNode(topOp.op, leftNode, rightNode));
                        continue;
                    }
                    break;
                }
                opStack.push(new OpToken("op", ch));
                prevTokenWasValue = false;
                i++;
            } else {
                // Operand: number, cell reference or function name
                if (Character.isDigit(ch) || ch == '.') {
                    int start = i;
                    boolean dotSeen = false;
                    while (i < formula.length() &&
                            (Character.isDigit(formula.charAt(i)) || formula.charAt(i) == '.')) {
                        if (formula.charAt(i) == '.') {
                            if (dotSeen) break;
                            dotSeen = true;
                        }
                        i++;
                    }
                    String numStr = formula.substring(start, i);
                    if (numStr.equals(".") || numStr.equals("-") || numStr.equals("+")) {
                        throw new IllegalArgumentException("Invalid numeric literal");
                    }
                    double value;
                    try {
                        value = Double.parseDouble(numStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid number: " + numStr);
                    }
                    valueStack.push(new ConstantNode(value));
                    prevTokenWasValue = true;
                } else if (Character.isLetter(ch)) {
                    int start = i;
                    while (i < formula.length() && Character.isLetter(formula.charAt(i))) {
                        i++;
                    }
                    String name = formula.substring(start, i).toUpperCase();

                    if (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                        int startDigits = i;
                        while (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                            i++;
                        }
                        String rowStr = formula.substring(startDigits, i);
                        String coord = name + rowStr;

                        if (i < formula.length() && formula.charAt(i) == ':') {
                            i++;
                            if (i >= formula.length() || !Character.isLetter(formula.charAt(i))) {
                                throw new IllegalArgumentException("Invalid range syntax");
                            }
                            int start2 = i;
                            while (i < formula.length() && Character.isLetter(formula.charAt(i))) {
                                i++;
                            }
                            String name2 = formula.substring(start2, i).toUpperCase();
                            if (i >= formula.length() || !Character.isDigit(formula.charAt(i))) {
                                throw new IllegalArgumentException("Invalid range syntax");
                            }
                            int startDigits2 = i;
                            while (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                                i++;
                            }
                            String rowStr2 = formula.substring(startDigits2, i);
                            String coord2 = name2 + rowStr2;

                            RangeNode rangeNode = new RangeNode(coord, coord2);
                            valueStack.push(rangeNode);
                        } else {
                            ReferenceNode refNode = new ReferenceNode(coord);
                            valueStack.push(refNode);
                        }
                        prevTokenWasValue = true;
                    } else if (i < formula.length() && formula.charAt(i) == '(') {
                        if (!isSupportedFunction(name)) {
                            throw new IllegalArgumentException("Unknown function: " + name);
                        }
                        OpToken funcToken = new OpToken("func", name);
                        funcToken.argCount = 1;
                        opStack.push(funcToken);
                        opStack.push(new OpToken("paren", '('));
                        prevTokenWasValue = false;
                        i++; // skip '(' (it will be consumed next loop)
                    } else {
                        throw new IllegalArgumentException("Invalid token: " + name);
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected character: " + ch);
                }
            }
        }

        while (!opStack.isEmpty()) {
            OpToken top = opStack.pop();
            if (top.type.equals("paren") && top.op == '(') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            if (top.type.equals("func")) {
                throw new IllegalArgumentException("Mismatched function call for " + top.funcName);
            }
            ExpressionNode rightNode = valueStack.pop();
            ExpressionNode leftNode = valueStack.pop();
            valueStack.push(new BinaryOpNode(top.op, leftNode, rightNode));
        }

        if (valueStack.size() != 1) {
            throw new IllegalArgumentException("Invalid formula expression");
        }

        ExpressionNode root = valueStack.pop();
        if (root instanceof RangeNode) {
            throw new IllegalArgumentException("Range " + ((RangeNode) root).getRangeRef() + " is not valid outside a function");
        }
        return root;
    }

    private static boolean isSupportedFunction(String name) {
        return name.equals("SUM") ||
                name.equals("MIN") ||
                name.equals("MAX") ||
                name.equals("AVERAGE");
    }
}
