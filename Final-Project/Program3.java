import java.io.IOException;
import java.util.Scanner;

public class Program3 {
    public static void main(String[] args) {
        Spreadsheet sheet = new Spreadsheet();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        System.out.println("Spreadsheet Program3 - Functions, Ranges, Circular Reference Detection");
        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. Set cell content");
            System.out.println("2. Get cell content (text)");
            System.out.println("3. Get cell value (number)");
            System.out.println("4. Load spreadsheet from file");
            System.out.println("5. Save spreadsheet to file");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Enter cell (e.g., A1): ");
                    String cell = scanner.nextLine().trim();
                    System.out.print("Enter content: ");
                    String content = scanner.nextLine();
                    try {
                        sheet.setCellContent(cell, content);
                        System.out.println("Cell " + cell + " set.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "2":
                    System.out.print("Enter cell: ");
                    cell = scanner.nextLine().trim();
                    try {
                        String text = sheet.getCellContentAsString(cell);
                        System.out.println("Content of " + cell + ": " + text);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "3":
                    System.out.print("Enter cell: ");
                    cell = scanner.nextLine().trim();
                    try {
                        double value = sheet.getCellValueAsNumber(cell);
                        System.out.println("Value of " + cell + ": " + value);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "4":
                    System.out.print("Enter file path to load: ");
                    String loadPath = scanner.nextLine().trim();
                    try {
                        sheet.loadFromFile(loadPath);
                        System.out.println("Spreadsheet loaded from " + loadPath);
                    } catch (IOException e) {
                        System.out.println("File error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "5":
                    System.out.print("Enter file path to save: ");
                    String savePath = scanner.nextLine().trim();
                    try {
                        sheet.saveToFile(savePath);
                        System.out.println("Spreadsheet saved to " + savePath);
                    } catch (IOException e) {
                        System.out.println("File error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "6":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}
