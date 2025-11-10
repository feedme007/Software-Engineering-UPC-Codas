import java.util.*;

public class Spreadsheet {
    private int rows;
    private int cols;
    private final Map<Coord, Cell> cells = new HashMap<>();
    private final DependencyGraph depGraph = new DependencyGraph();

    public Spreadsheet(int rows, int cols) {
        if (rows < 1 || cols < 1) throw new IllegalArgumentException("size must be >=1");
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    private boolean inBounds(Coord c) {
        int colIdx = ColumnRef.toIndex(c.getCol().getLetters());
        return c.getRow() >= 1 && c.getRow() <= rows && colIdx >= 1 && colIdx <= cols;
    }

    public Cell getCell(Coord c) {
        return cells.computeIfAbsent(c, Cell::new);
    }

    public void setCellContent(Coord c, String raw) {
        if (!inBounds(c)) throw new IllegalArgumentException("Out of bounds: " + c.toA1());
        Cell cell = getCell(c);
        if (raw == null) { cell.setContent(null); return; }
        String s = raw.trim();
        if (s.isEmpty()) { cell.setContent(null); return; }

        // allow "'=hello" to be stored as text "=hello"
        if (s.startsWith("'")) {
            cell.setContent(new TextContent(s.substring(1)));
            return;
        }

        if (s.startsWith("=")) {
            cell.setContent(new FormulaContent(s));
        } else {
            try {
                double d = Double.parseDouble(s);
                cell.setContent(new NumericContent(d));
            } catch (NumberFormatException e) {
                cell.setContent(new TextContent(s));
            }
        }
    }

    public void setCellContent(String a1, String raw) {
        setCellContent(Coord.fromA1(a1.trim()), raw);
    }

    public String getCellContent(Coord c) {
        Cell cell = cells.get(c);
        return (cell == null || cell.getContent() == null) ? "" : cell.getContent().asText();
    }

    public String getCellContent(String a1) {
        return getCellContent(Coord.fromA1(a1.trim()));
    }

    public String getContentForSave(int row, int col) {
        String colLetters = ColumnRef.fromIndex(col);
        Coord c = new Coord(new ColumnRef(colLetters), row);
        return getCellContent(c);
    }

    public void ensureSize(int newRows, int newCols) {
        rows = Math.max(rows, newRows);
        cols = Math.max(cols, newCols);
    }
}
