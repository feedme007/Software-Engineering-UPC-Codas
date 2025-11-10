public class NumberLit extends Expression {
    private final double value;
    public NumberLit(double value) { this.value = value; }
    public double getValue() { return value; }
}
