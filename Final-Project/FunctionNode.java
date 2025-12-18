import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionNode implements ExpressionNode {

    private final String name;
    private final List<ExpressionNode> arguments;

    public FunctionNode(String name, List<ExpressionNode> args) {
        this.name = name.toUpperCase();
        this.arguments = new ArrayList<>(args);
    }

    public String getName() {
        return name;
    }

    public List<ExpressionNode> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public double evaluate(Spreadsheet sheet) {
        if (arguments.isEmpty()) {
            throw new IllegalStateException("Function " + name + " needs at least one argument");
        }

        // Expand ranges into plain values
        List<Double> values = new ArrayList<>();
        for (ExpressionNode arg : arguments) {
            if (arg instanceof RangeNode) {
                RangeNode range = (RangeNode) arg;
                for (String coord : range.getCellsInRange()) {
                    values.add(sheet.getCellValueAsNumber(coord));
                }
            } else {
                values.add(arg.evaluate(sheet));
            }
        }

        switch (name) {
            case "SUM":
                double sum = 0.0;
                for (double v : values) sum += v;
                return sum;

            case "MIN":
                double min = values.get(0);
                for (double v : values) if (v < min) min = v;
                return min;

            case "MAX":
                double max = values.get(0);
                for (double v : values) if (v > max) max = v;
                return max;

            case "AVERAGE":
                double s = 0.0;
                for (double v : values) s += v;
                return s / values.size();

            default:
                throw new IllegalStateException("Unknown function: " + name);
        }
    }
}
