public class BinaryOpNode implements ExpressionNode {

    public enum Op {
        ADD('+'),
        SUB('-'),
        MUL('*'),
        DIV('/');

        private final char symbol;

        Op(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }

        public static Op fromChar(char c) {
            switch (c) {
                case '+': return ADD;
                case '-': return SUB;
                case '*': return MUL;
                case '/': return DIV;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + c);
            }
        }
    }

    private final Op op;
    private final ExpressionNode left;
    private final ExpressionNode right;

    public BinaryOpNode(char opChar, ExpressionNode left, ExpressionNode right) {
        this(Op.fromChar(opChar), left, right);
    }

    public BinaryOpNode(Op op, ExpressionNode left, ExpressionNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    @Override
    public double evaluate(Spreadsheet sheet) {
        double a = left.evaluate(sheet);
        double b = right.evaluate(sheet);
        switch (op) {
            case ADD:
                return a + b;
            case SUB:
                return a - b;
            case MUL:
                return a * b;
            case DIV:
                if (b == 0.0) {
                    throw new IllegalStateException("Division by zero");
                }
                return a / b;
            default:
                throw new IllegalStateException("Unhandled operator");
        }
    }
}
