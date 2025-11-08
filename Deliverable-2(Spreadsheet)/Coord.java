import java.util.Objects;

public class Coord {
    private final ColumnRef col;
    private final int row;

    public Coord(ColumnRef col, int row) {
        if (row < 1) throw new IllegalArgumentException("row must be >= 1");
        this.col = col;
        this.row = row;
    }

    public ColumnRef getCol() { return col; }
    public int getRow() { return row; }

    public static Coord fromA1(String a1) {
        if (a1 == null || a1.isEmpty()) throw new IllegalArgumentException("Empty coord");
        int i = 0;
        while (i < a1.length() && Character.isLetter(a1.charAt(i))) i++;
        if (i == 0 || i == a1.length()) throw new IllegalArgumentException("Invalid A1: " + a1);
        String col = a1.substring(0, i);
        String rowPart = a1.substring(i);
        for (int k = 0; k < rowPart.length(); k++) {
            if (!Character.isDigit(rowPart.charAt(k))) throw new IllegalArgumentException("Invalid A1: " + a1);
        }
        int row = Integer.parseInt(rowPart);
        return new Coord(new ColumnRef(col), row);
    }


    public String toA1() {
        return col.getLetters() + row;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coord)) return false;
        Coord coord = (Coord) o;
        return row == coord.row && col.getLetters().equals(coord.col.getLetters());
    }

    @Override public int hashCode() {
        return Objects.hash(col.getLetters(), row);
    }

    @Override public String toString() { return toA1(); }
}
