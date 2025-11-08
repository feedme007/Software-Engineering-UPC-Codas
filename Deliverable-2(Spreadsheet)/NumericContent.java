public class NumericContent implements Content {
    private final double value;
    public NumericContent(double value) { this.value = value; }
    public double getValue() { return value; }
    @Override public String asText() {
        return (value == Math.rint(value)) ? Long.toString((long) value) : Double.toString(value);
    }
}
