public class BinaryOp extends Expression {
    public enum Op { ADD, SUB, MUL, DIV }
    private final Op op;
    private final Expression left;
    private final Expression right;

    public BinaryOp(Op op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public Op getOp() { return op; }
    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }
}
