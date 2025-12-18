import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Spreadsheet: stores text, numbers, and formulas.
 * Uses ExpressionParser (Shunting Yard) to build expression trees.
 */
public class Spreadsheet {

    // Map of cell coordinates (e.g., "A1") to Cell objects
    private final Map<String, Cell> cells = new HashMap<>();
    // Dependency graph: for each cell, set of cells that depend on it
    private final Map<String, Set<String>> dependents = new HashMap<>();

    // ------------------- public API -------------------

    // Sets the content of a cell to the given content (number, text, or formula).
    public void setCellContent(String coord, String content) {
        coord = coord.toUpperCase();  // normalize coordinate (columns to uppercase)
        parseCellCoord(coord);        // validate format

        // If content is null or empty, treat as clearing the cell
        if (content == null || content.trim().isEmpty()) {
            Cell oldCell = cells.get(coord);
            if (oldCell != null) {
                if (oldCell.getType() == CellType.FORMULA) {
                    for (String ref : oldCell.getReferences()) {
                        removeDependent(ref, coord);
                    }
                }
                cells.remove(coord);
                updateDependents(coord);
            }
            return;
        }

        content = content.trim();

        // Formula
        if (content.startsWith("=")) {
            String formulaText = content.substring(1).trim();

            ExpressionNode expr;
            try {
                expr = parseFormula(formulaText);
            } catch (Exception e) {
                throw new IllegalArgumentException("Formula syntax error: " + e.getMessage());
            }

            Set<String> references = new HashSet<>();
            collectReferences(expr, references);

            if (createsCircularDependency(coord, references)) {
                throw new IllegalStateException("Circular dependency detected involving " + coord);
            }

            Cell oldCell = cells.get(coord);
            if (oldCell != null && oldCell.getType() == CellType.FORMULA) {
                for (String ref : oldCell.getReferences()) {
                    removeDependent(ref, coord);
                }
            }

            Cell cell = new Cell(CellType.FORMULA);
            cell.setFormula(formulaText, expr, references);

            double result = expr.evaluate(this);
            cell.setValue(result);
            cell.clearError();

            cells.put(coord, cell);

            for (String ref : references) {
                addDependent(ref, coord);
            }

            updateDependents(coord);
            return;
        }

        // Not a formula: numeric or text
        double number;
        boolean isNumber;
        try {
            number = Double.parseDouble(content);
            isNumber = true;
        } catch (NumberFormatException e) {
            isNumber = false;
            number = 0;
        }

        if (isNumber) {
            Cell oldCell = cells.get(coord);
            if (oldCell != null && oldCell.getType() == CellType.FORMULA) {
                for (String ref : oldCell.getReferences()) {
                    removeDependent(ref, coord);
                }
            }
            Cell cell = new Cell(CellType.NUMERIC);
            cell.setValue(number);
            cells.put(coord, cell);
            updateDependents(coord);
        } else {
            Cell oldCell = cells.get(coord);
            if (oldCell != null && oldCell.getType() == CellType.FORMULA) {
                for (String ref : oldCell.getReferences()) {
                    removeDependent(ref, coord);
                }
            }
            Cell cell = new Cell(CellType.TEXT);
            cell.setText(content);
            cells.put(coord, cell);
            updateDependents(coord);
        }
    }

    // Returns the cell's content as string (text, number, or "=formula").
    public String getCellContentAsString(String coord) {
        coord = coord.toUpperCase();
        parseCellCoord(coord);
        Cell cell = cells.get(coord);
        if (cell == null) return "";

        switch (cell.getType()) {
            case TEXT:
                return cell.getText();
            case NUMERIC:
                double val = cell.getValue();
                if (val == Math.floor(val) && !Double.isInfinite(val)) {
                    return String.valueOf((long) val);
                } else {
                    return String.valueOf(val);
                }
            case FORMULA:
                return cell.getFormulaText();  // already includes leading "="
            default:
                return "";
        }
    }

    // Returns the numeric value of a cell (evaluated).
    public double getCellValueAsNumber(String coord) {
        coord = coord.toUpperCase();
        parseCellCoord(coord);
        Cell cell = cells.get(coord);
        if (cell == null) {
            return 0.0;
        }
        if (cell.getType() == CellType.NUMERIC) {
            return cell.getValue();
        }
        if (cell.getType() == CellType.FORMULA) {
            if (cell.hasError()) {
                throw new IllegalStateException("Cell " + coord + " error: " + cell.getErrorMessage());
            }
            return cell.getValue();
        }
        String text = cell.getText();
        if (text.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Cell " + coord + " cannot be converted to a number");
        }
    }

    // ------------------- S2V load / save -------------------

    public void loadFromFile(String filepath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            int row = 1;
            cells.clear();
            dependents.clear();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    row++;
                    continue;
                }
                String[] fields = line.split(";", -1);
                for (int colIndex = 0; colIndex < fields.length; colIndex++) {
                    String field = fields[colIndex];
                    if (field.isEmpty()) continue;

                    String colLetters = colIndexToLabel(colIndex + 1);
                    String coord = colLetters + row;

                    if (field.startsWith("=")) {
                        String formulaText = field.replace(",", ";");
                        setCellContent(coord, formulaText);
                    } else {
                        setCellContent(coord, field);
                    }
                }
                row++;
            }
        }
    }

    public void saveToFile(String filepath) throws IOException {
        try (FileWriter fw = new FileWriter(filepath)) {
            int maxRow = 0;
            Map<Integer, Integer> lastColForRow = new HashMap<>();
            for (String coord : cells.keySet()) {
                int[] rc = parseCellCoord(coord);
                int col = rc[0];
                int row = rc[1];
                maxRow = Math.max(maxRow, row);
                Integer lastCol = lastColForRow.get(row);
                if (lastCol == null || col > lastCol) {
                    lastColForRow.put(row, col);
                }
            }

            for (int r = 1; r <= maxRow; r++) {
                Integer maxCol = lastColForRow.get(r);
                if (maxCol == null) {
                    fw.write("\n");
                    continue;
                }
                for (int c = 1; c <= maxCol; c++) {
                    String coord = colIndexToLabel(c) + r;
                    Cell cell = cells.get(coord);
                    if (c > 1) fw.write(";");
                    if (cell == null) continue;

                    if (cell.getType() == CellType.FORMULA) {
                        String formulaText = cell.getFormulaText();
                        if (!formulaText.startsWith("=")) {
                            formulaText = "=" + formulaText;
                        }
                        formulaText = formulaText.replace(";", ",");
                        fw.write(formulaText);
                    } else if (cell.getType() == CellType.NUMERIC) {
                        double val = cell.getValue();
                        if (val == Math.floor(val) && !Double.isInfinite(val)) {
                            fw.write(String.valueOf((long) val));
                        } else {
                            fw.write(String.valueOf(val));
                        }
                    } else if (cell.getType() == CellType.TEXT) {
                        fw.write(cell.getText());
                    }
                }
                fw.write("\n");
            }
        }
    }

    // ------------------- dependency handling -------------------

    private void updateDependents(String coord) {
        if (!dependents.containsKey(coord)) {
            return;
        }
        Set<String> directDeps = new HashSet<>(dependents.get(coord));
        for (String dep : directDeps) {
            Cell cell = cells.get(dep);
            if (cell == null || cell.getType() != CellType.FORMULA) continue;

            try {
                double newVal = cell.getExpression().evaluate(this);
                cell.setValue(newVal);
                cell.clearError();
            } catch (RuntimeException e) {
                cell.setError(e.getMessage());
            }
            updateDependents(dep);
        }
    }

    private boolean createsCircularDependency(String target, Set<String> references) {
        return checkCycle(target, references, new HashSet<>());
    }

    private boolean checkCycle(String target, Set<String> refs, Set<String> visited) {
        for (String ref : refs) {
            if (ref.equals(target)) return true;
            if (visited.contains(ref)) continue;
            visited.add(ref);
            Cell refCell = cells.get(ref);
            if (refCell != null && refCell.getType() == CellType.FORMULA) {
                if (checkCycle(target, refCell.getReferences(), visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addDependent(String referencedCell, String dependentCell) {
        Set<String> deps = dependents.computeIfAbsent(referencedCell, k -> new HashSet<>());
        deps.add(dependentCell);
    }

    private void removeDependent(String referencedCell, String dependentCell) {
        Set<String> deps = dependents.get(referencedCell);
        if (deps != null) {
            deps.remove(dependentCell);
            if (deps.isEmpty()) dependents.remove(referencedCell);
        }
    }

    // ------------------- expression handling -------------------

    /**
     * Now the Spreadsheet uses the dedicated ExpressionParser
     * that implements the Shunting Yard algorithm.
     */
    private ExpressionNode parseFormula(String formula) {
        return ExpressionParser.parse(formula);
    }

    // Collects all referenced cell coordinates from an expression tree into the set.
    private void collectReferences(ExpressionNode node, Set<String> refs) {
        if (node instanceof ReferenceNode) {
            refs.add(((ReferenceNode) node).getCellRef());
        } else if (node instanceof RangeNode) {
            for (String coord : ((RangeNode) node).getCellsInRange()) {
                refs.add(coord);
            }
        } else if (node instanceof BinaryOpNode) {
            collectReferences(((BinaryOpNode) node).getLeft(), refs);
            collectReferences(((BinaryOpNode) node).getRight(), refs);
        } else if (node instanceof FunctionNode) {
            for (ExpressionNode arg : ((FunctionNode) node).getArguments()) {
                collectReferences(arg, refs);
            }
        }
        // ConstantNode has no references
    }

    // ------------------- coordinate helpers -------------------

    // Validate and parse a cell coordinate string into [colIndex, rowIndex] (1-based).
    private int[] parseCellCoord(String coord) {
        if (coord == null || coord.isEmpty()) {
            throw new IllegalArgumentException("Cell reference is empty");
        }
        int idx = 0;
        while (idx < coord.length() && Character.isLetter(coord.charAt(idx))) {
            idx++;
        }
        if (idx == 0 || idx == coord.length()) {
            throw new IllegalArgumentException("Invalid cell reference: " + coord);
        }
        String colLetters = coord.substring(0, idx);
        String rowDigits = coord.substring(idx);
        for (char c : rowDigits.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Invalid cell reference: " + coord);
            }
        }
        int row = Integer.parseInt(rowDigits);
        if (row < 1) throw new IllegalArgumentException("Invalid row in reference: " + coord);

        long colIndex = colLabelToIndex(colLetters);
        if (colIndex < 1 || colIndex > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid column in reference: " + coord);
        }
        return new int[]{(int) colIndex, row};
    }

    private String colIndexToLabel(int colIndex) {
        StringBuilder sb = new StringBuilder();
        int n = colIndex;
        while (n > 0) {
            int rem = (n - 1) % 26;
            char letter = (char) ('A' + rem);
            sb.insert(0, letter);
            n = (n - 1) / 26;
        }
        return sb.toString();
    }

    private long colLabelToIndex(String colLabel) {
        long result = 0;
        for (char c : colLabel.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Invalid column label: " + colLabel);
            }
            result = result * 26 + (Character.toUpperCase(c) - 'A' + 1);
        }
        return result;
    }

    // ------------------- helpers for S2VStorage -------------------

    // Largest row index with any content
    public int getRows() {
        int maxRow = 0;
        for (String coord : cells.keySet()) {
            int[] rc = parseCellCoord(coord);
            maxRow = Math.max(maxRow, rc[1]);
        }
        return maxRow;
    }

    // Largest column index with any content
    public int getCols() {
        int maxCol = 0;
        for (String coord : cells.keySet()) {
            int[] rc = parseCellCoord(coord);
            maxCol = Math.max(maxCol, rc[0]);
        }
        return maxCol;
    }

    // Returns raw content for S2VStorage (no comma/semicolon conversion here).
    public String getCellContentForSave(int row, int col) {
        String coord = colIndexToLabel(col) + row;
        Cell cell = cells.get(coord);
        if (cell == null) return "";

        if (cell.getType() == CellType.FORMULA) {
            String f = cell.getFormulaText();
            if (!f.startsWith("=")) f = "=" + f;
            return f;
        }
        if (cell.getType() == CellType.NUMERIC) {
            double val = cell.getValue();
            if (val == Math.floor(val) && !Double.isInfinite(val)) {
                return String.valueOf((long) val);
            } else {
                return String.valueOf(val);
            }
        }
        return cell.getText();
    }
}
