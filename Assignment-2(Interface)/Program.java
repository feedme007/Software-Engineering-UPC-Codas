public class Program extends Sorter {
    public static void main(String[] args) {
        Person[] arr = new Person[] {
                new Person("Faraz", "Siddiqui"),
                new Person("Rayyan", "Ahmad"),
                new Person("Fahad", "Rehman"),
                new Person("Tabish", "Hashmi"),
                new Person("Azhan", "Hasan")
        };

        Sorter sorter = new Sorter();
        sorter.sort(arr);

        for (Person p : arr) {
            p.print();
        }
    }
}
