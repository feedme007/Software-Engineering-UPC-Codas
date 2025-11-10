import java.io.File;

public class SpreadsheetController {
    private Spreadsheet sheet;

    public SpreadsheetController(int rows, int cols) {
        this.sheet = new Spreadsheet(rows, cols);
    }

    public void newSheet(int rows, int cols) {
        this.sheet = new Spreadsheet(rows, cols);
    }

    public void setCell(String a1, String raw) {
        sheet.setCellContent(a1, raw);
    }

    public String getCell(String a1) {
        return sheet.getCellContent(a1);
    }

    public void save(File f) throws Exception {
        S2VStorage.save(f, sheet);
    }

    public void load(File f) throws Exception {
        this.sheet = S2VStorage.load(f);
    }

    public int rows() { return sheet.getRows(); }
    public int cols() { return sheet.getCols(); }
}
