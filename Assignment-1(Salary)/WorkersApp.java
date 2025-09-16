import java.util.ArrayList;
import java.util.List;

public class WorkersApp {
    public static void main(String[] args) {
        List<Worker> workers = new ArrayList<>();


        for (int i = 1; i <= 100; i++) {
            double base = 1000 + i * 10;
            double comp = 100 + i * 5;
            workers.add(new Worker("Worker" + i, base, comp));
        }

        double globalTotal = 0.0;

        System.out.println("Name\tBase\tComplement\tTotal");
        for (Worker w : workers) {
            double total = w.totalSalary();
            globalTotal += total;
            System.out.println(w.getName() + "\t" + w.getBaseSalary() + "\t" + w.getComplement() + "\t\t" + total);
        }

        System.out.println("\nGlobal total salary: " + globalTotal);
    }
}
