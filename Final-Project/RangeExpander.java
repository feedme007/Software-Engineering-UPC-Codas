import java.util.ArrayList;
import java.util.List;

public class RangeExpander {

    public static List<String> expand(String startRef, String endRef) {
        int[] start = parseCoord(startRef);
        int[] end   = parseCoord(endRef);

        int colStart = Math.min(start[0], end[0]);
        int colEnd   = Math.max(start[0], end[0]);
        int rowStart = Math.min(start[1], end[1]);
        int rowEnd   = Math.max(start[1], end[1]);

        List<String> result = new ArrayList<>();
        for (int c = colStart; c <= colEnd; c++) {
            for (int r = rowStart; r <= rowEnd; r++) {
                result.add(colIndexToLabel(c) + r);
            }
        }
        return result;
    }

    // --- helpers ---

    private static int[] parseCoord(String coord) {
        if (coord == null || coord.isEmpty()) {
            throw new IllegalArgumentException("Empty cell reference");
        }
        int i = 0;
        while (i < coord.length() && Character.isLetter(coord.charAt(i))) {
            i++;
        }
        if (i == 0 || i == coord.length()) {
            throw new IllegalArgumentException("Invalid cell reference: " + coord);
        }
        String colLetters = coord.substring(0, i);
        String rowDigits  = coord.substring(i);
        for (char c : rowDigits.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Invalid cell reference: " + coord);
            }
        }
        int row = Integer.parseInt(rowDigits);
        if (row < 1) {
            throw new IllegalArgumentException("Invalid row in reference: " + coord);
        }
        int col = (int) colLabelToIndex(colLetters);
        return new int[]{col, row};
    }

    private static long colLabelToIndex(String colLabel) {
        long result = 0;
        for (char c : colLabel.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Invalid column label: " + colLabel);
            }
            result = result * 26 + (Character.toUpperCase(c) - 'A' + 1);
        }
        return result;
    }

    private static String colIndexToLabel(int colIndex) {
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
}
