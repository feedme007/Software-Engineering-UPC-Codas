import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class S2VStorage {

    // replace ';' inside a formula with ',' when saving
    static String mapArgSepForSave(String content) {
        if (content != null && content.startsWith("=")) {
            return content.replace(';', ',');
        }
        return content;
    }

    // replace ',' back to ';' inside a formula when loading
    static String mapArgSepForLoad(String content) {
        if (content != null && content.startsWith("=")) {
            return content.replace(',', ';');
        }
        return content;
    }

    public static void save(File file, Spreadsheet sheet) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {

            for (int r = 1; r <= sheet.getRows(); r++) {
                List<String> tokens = new ArrayList<>();
                int lastNonEmpty = 0;
                for (int c = 1; c <= sheet.getCols(); c++) {
                    String raw = sheet.getContentForSave(r, c);
                    if (raw == null) raw = "";
                    if (!raw.isEmpty()) lastNonEmpty = c;
                    tokens.add(mapArgSepForSave(raw));
                }
                // omit trailing empties
                StringBuilder line = new StringBuilder();
                for (int c = 1; c <= lastNonEmpty; c++) {
                    if (c > 1) line.append(';');
                    String t = tokens.get(c - 1);
                    line.append(t == null ? "" : t);
                }
                w.write(line.toString());
                w.newLine();
            }
        }
    }

    public static Spreadsheet load(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) lines.add(line);
        }

        int rows = Math.max(1, lines.size());
        int cols = 1;
        List<String[]> parsed = new ArrayList<>();
        for (String line : lines) {
            // keep empty tokens between separators
            String[] parts = line.isEmpty() ? new String[0] : line.split(";", -1);
            parsed.add(parts);
            cols = Math.max(cols, parts.length);
        }

        Spreadsheet sheet = new Spreadsheet(rows, cols);

        for (int r = 0; r < lines.size(); r++) {
            String[] parts = parsed.get(r);
            for (int c = 0; c < parts.length; c++) {
                String token = parts[c];
                if (token == null) token = "";
                token = mapArgSepForLoad(token);
                if (!token.isEmpty()) {
                    String a1 = ColumnRef.fromIndex(c + 1) + (r + 1);
                    sheet.setCellContent(a1, token);
                }
            }
        }

        return sheet;
    }
}
