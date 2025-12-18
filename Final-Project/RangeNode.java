import java.util.List;

public class RangeNode implements ExpressionNode {

    private final String startRef;
    private final String endRef;
    private final List<String> cells;

    public RangeNode(String startRef, String endRef) {
        this.startRef = startRef.toUpperCase();
        this.endRef = endRef.toUpperCase();
        this.cells = RangeExpander.expand(this.startRef, this.endRef);
    }

    public String getRangeRef() {
        return startRef + ":" + endRef;
    }

    public List<String> getCellsInRange() {
        return cells;
    }

    @Override
    public double evaluate(Spreadsheet sheet) {
        // Ranges are meant to be used inside functions, not as standalone values.
        throw new IllegalStateException("Range " + getRangeRef() + " cannot be evaluated directly");
    }
}
