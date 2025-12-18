public class ConstantNode implements ExpressionNode {

    private final double value;

    public ConstantNode(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(Spreadsheet sheet) {
        return value;
    }
}
