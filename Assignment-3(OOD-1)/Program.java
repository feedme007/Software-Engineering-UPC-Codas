public class Program {
    public static void main(String[] args) {
        TaxSystem sys = new TaxSystem();

        sys.addPerson(new Person("LIC001", "Faraz", "Idris", "Carrer Pareto, 1"));
        sys.addPerson(new Person("LIC002", "Juan", "Lopez", "Eixample, 2"));
        sys.addPerson(new Person("LIC003", "Cristiano", "Ronaldo", "Camp Nou, 3"));

        sys.addVehicle(new Motorcycle("AA-111", "Yamaha", "MT-07", 689));
        sys.addVehicle(new Car("BB-222", "Toyota", "Corolla", "petrol", 110));
        sys.addVehicle(new Car("CC-333", "Volkswagen", "Golf", "diesel", 130));
        sys.addVehicle(new Car("DD-444", "Hyundai", "Ioniq", "hybrid", 98));
        sys.addVehicle(new Motorcycle("EE-555", "Honda", "CB500F", 471));

        sys.assignVehicleToOwner("AA-111", "LIC001");
        sys.assignVehicleToOwner("BB-222", "LIC002");
        sys.assignVehicleToOwner("CC-333", "LIC002");

        sys.editPerson("LIC003", "Cristiano", "Ronaldo", "Camp Nou, 007");
        sys.editCar("BB-222", null, 115.0); // update CO2

        // transfer vehicle
        sys.assignVehicleToOwner("CC-333", "LIC001");

        sys.printAnnualOwnerTaxesReport();

        System.out.println("Search persons 'an':");
        for (Person p : sys.searchPersons("an")) System.out.println("  " + p);

        System.out.println("\nSearch vehicles 'toy':");
        for (Vehicle v : sys.searchVehicles("toy")) System.out.println("  " + v);

        // individual tax
        System.out.println("\nTax for AA-111: " + sys.calculateVehicleTax("AA-111"));
    }
}
