public class FunctionCall extends Expression {
    public enum Fn { SUMA, MIN, MAX, PROMEDIO }
    private final Fn name;
    private final Expression[] args;

    public FunctionCall(Fn name, Expression[] args) {
        this.name = name;
        this.args = args;
    }

    public Fn getName() { return name; }
    public Expression[] getArgs() { return args; }
}
