import java.util.HashSet;
import java.util.Set;

public class Cell {
    private CellType type;
    private double value;
    private String text;
    private String formulaText;
    private ExpressionNode expression;
    private boolean hasError;
    private String errorMessage;
    private Set<String> references = new HashSet<>();

    public Cell(CellType type) {
        this.type = type;
    }

    public CellType getType() { return type; }

    public void setValue(double v) { value = v; }
    public double getValue() { return value; }

    public void setText(String t) { text = t; }
    public String getText() { return text; }

    public void setFormula(String f, ExpressionNode e, Set<String> refs) {
        formulaText = "=" + f;
        expression = e;
        references = refs;
    }

    public String getFormulaText() { return formulaText; }
    public ExpressionNode getExpression() { return expression; }

    public Set<String> getReferences() { return references; }

    public boolean hasError() { return hasError; }
    public String getErrorMessage() { return errorMessage; }

    public void setError(String msg) {
        hasError = true;
        errorMessage = msg;
    }

    public void clearError() {
        hasError = false;
        errorMessage = null;
    }
}
