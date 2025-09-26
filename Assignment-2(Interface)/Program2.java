public class Program2 extends Rectangle2 {
    public static void main(String[] args) {
        // Persons
        ComparableItem[] people = new ComparableItem[] {
                new Person2("Faraz", "Siddiqui"),
                new Person2("Rayyan", "Ahmad"),
                new Person2("Fahad", "Rehman"),
                new Person2("Tabish", "Hashmi"),
                new Person2("Azhan", "Hasan")
        };

        Sorter2 sorter = new Sorter2();
        sorter.sort(people);

        System.out.println("\nPersons (sorted by surnames):");
        for (ComparableItem ci : people) ((Person2) ci).print();

        // Rectangles
        ComparableItem[] rects = new ComparableItem[] {
                new Rectangle2(3, 5),
                new Rectangle2(2, 9),
                new Rectangle2(4, 4),
                new Rectangle2(2, 11),
                new Rectangle2(3, 3)
        };

        sorter.sort(rects);

        System.out.println("\nRectangles (sorted by areas):");
        for (ComparableItem ci : rects)
            System.out.println(((Rectangle2) ci).area());
    }
}
