public class ReferenceNode implements ExpressionNode {

    private final String cellRef;

    public ReferenceNode(String cellRef) {
        this.cellRef = cellRef.toUpperCase();
    }

    public String getCellRef() {
        return cellRef;
    }

    @Override
    public double evaluate(Spreadsheet sheet) {
        return sheet.getCellValueAsNumber(cellRef);
    }
}
