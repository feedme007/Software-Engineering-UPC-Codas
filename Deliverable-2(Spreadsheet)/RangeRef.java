public class RangeRef extends Expression {
    private final Coord a;
    private final Coord b;

    public RangeRef(Coord a, Coord b) {
        this.a = a;
        this.b = b;
    }
    public Coord getA() { return a; }
    public Coord getB() { return b; }
}
