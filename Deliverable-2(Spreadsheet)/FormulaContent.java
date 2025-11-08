public class FormulaContent implements Content {
    private final String exprText; // like "=A1+B2" (with ';' inside functions)
    private Expression expr;       // not used in this deliverable

    public FormulaContent(String exprText) {
        this.exprText = exprText;
    }

    public String getExprText() { return exprText; }
    public Expression getExpr() { return expr; }
    public void setExpr(Expression expr) { this.expr = expr; }

    @Override public String asText() { return exprText; }
}
