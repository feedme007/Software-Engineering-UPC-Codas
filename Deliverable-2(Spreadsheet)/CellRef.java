public class CellRef extends Expression {
    private final Coord ref;
    public CellRef(Coord ref) { this.ref = ref; }
    public Coord getRef() { return ref; }
}
