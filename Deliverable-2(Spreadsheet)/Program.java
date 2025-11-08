import java.io.File;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        SpreadsheetController ctl = new SpreadsheetController(10, 10);

        while (true) {
            System.out.println("\nSpreadsheet menu");
            System.out.println("1) New sheet");
            System.out.println("2) Load (S2V)");
            System.out.println("3) Save (S2V)");
            System.out.println("4) Set cell content");
            System.out.println("5) View cell content");
            System.out.println("6) Exit");
            System.out.print("Choose: ");

            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1": {
                        System.out.print("Rows: ");
                        int rows = Integer.parseInt(in.nextLine().trim());
                        System.out.print("Cols: ");
                        int cols = Integer.parseInt(in.nextLine().trim());
                        ctl.newSheet(rows, cols);
                        System.out.println("New sheet " + rows + "x" + cols);
                        break;
                    }
                    case "2": {
                        System.out.print("Path to .s2v: ");
                        String path = in.nextLine().trim();
                        ctl.load(new File(path));
                        System.out.println("Loaded. Size: " + ctl.rows() + "x" + ctl.cols());
                        break;
                    }
                    case "3": {
                        System.out.print("Path to save .s2v: ");
                        String path = in.nextLine().trim();
                        ctl.save(new File(path));
                        System.out.println("Saved.");
                        break;
                    }
                    case "4": {
                        System.out.print("Cell (e.g., A1): ");
                        String a1 = in.nextLine().trim();
                        System.out.print("Content (text, number, or formula starting with '='). Use leading ' to force text: ");
                        String raw = in.nextLine();
                        ctl.setCell(a1, raw);
                        System.out.println("Set " + a1 + " = " + ctl.getCell(a1));
                        break;
                    }
                    case "5": {
                        System.out.print("Cell (e.g., A1): ");
                        String a1 = in.nextLine().trim();
                        System.out.println(a1 + " content: " + ctl.getCell(a1));
                        break;
                    }
                    case "6": return;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
