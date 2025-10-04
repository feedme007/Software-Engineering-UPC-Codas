import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxSystem {
    private Map<String, Person> persons = new HashMap<>();
    private Map<String, Vehicle> vehicles = new HashMap<>();

    // Persons
    public boolean addPerson(Person p) {
        if (p == null || persons.containsKey(p.getLicenseNumber())) return false;
        persons.put(p.getLicenseNumber(), p);
        return true;
    }

    public boolean editPerson(String license, String name, String surname, String address) {
        Person p = persons.get(license);
        if (p == null) return false;
        if (name != null) p.setName(name);
        if (surname != null) p.setSurname(surname);
        if (address != null) p.setAddress(address);
        return true;
    }

    public boolean deletePerson(String license) {
        Person p = persons.remove(license);
        if (p == null) return false;
        for (Vehicle v : vehicles.values()) {
            if (v.getOwner() != null && v.getOwner().getLicenseNumber().equals(license)) {
                v.setOwner(null);
            }
        }
        return true;
    }

    public Person getPerson(String license) { return persons.get(license); }

    public List<Person> searchPersons(String keyword) {
        List<Person> out = new ArrayList<>();
        if (keyword == null) return out;
        String k = keyword.toLowerCase();
        for (Person p : persons.values()) {
            if (p.getLicenseNumber().toLowerCase().contains(k)
                    || p.getName().toLowerCase().contains(k)
                    || p.getSurname().toLowerCase().contains(k)
                    || p.getAddress().toLowerCase().contains(k)) {
                out.add(p);
            }
        }
        return out;
    }

    // Vehicles
    public boolean addVehicle(Vehicle v) {
        if (v == null || vehicles.containsKey(v.getPlate())) return false;
        vehicles.put(v.getPlate(), v);
        return true;
    }

    public boolean editVehicleBasic(String plate, String maker, String model) {
        Vehicle v = vehicles.get(plate);
        if (v == null) return false;
        if (maker != null) v.setMaker(maker);
        if (model != null) v.setModel(model);
        return true;
    }

    public boolean editMotorcycle(String plate, Double displacement) {
        Vehicle v = vehicles.get(plate);
        if (!(v instanceof Motorcycle)) return false;
        if (displacement != null) ((Motorcycle) v).setEngineDisplacement(displacement);
        return true;
    }

    public boolean editCar(String plate, String fuelType, Double co2) {
        Vehicle v = vehicles.get(plate);
        if (!(v instanceof Car)) return false;
        if (fuelType != null) ((Car) v).setFuelType(fuelType);
        if (co2 != null) ((Car) v).setCo2(co2);
        return true;
    }

    public boolean deleteVehicle(String plate) {
        return vehicles.remove(plate) != null;
    }

    public Vehicle getVehicle(String plate) { return vehicles.get(plate); }

    public List<Vehicle> searchVehicles(String keyword) {
        List<Vehicle> out = new ArrayList<>();
        if (keyword == null) return out;
        String k = keyword.toLowerCase();
        for (Vehicle v : vehicles.values()) {
            if (v.getPlate().toLowerCase().contains(k)
                    || v.getMaker().toLowerCase().contains(k)
                    || v.getModel().toLowerCase().contains(k)) {
                out.add(v);
            }
        }
        return out;
    }

    // Ownership
    public boolean assignVehicleToOwner(String plate, String license) {
        Vehicle v = vehicles.get(plate);
        Person p = persons.get(license);
        if (v == null || p == null) return false;
        v.setOwner(p);
        return true;
    }

    public boolean removeOwnerFromVehicle(String plate) {
        Vehicle v = vehicles.get(plate);
        if (v == null) return false;
        v.setOwner(null);
        return true;
    }

    // Taxes
    public double calculateVehicleTax(String plate) {
        Vehicle v = vehicles.get(plate);
        if (v == null) return 0.0;
        return v.annualTax();
    }

    public void printAnnualOwnerTaxesReport() {
        for (Person p : persons.values()) {
            List<Vehicle> owned = new ArrayList<>();
            for (Vehicle v : vehicles.values()) {
                if (v.getOwner() != null && v.getOwner().getLicenseNumber().equals(p.getLicenseNumber())) {
                    owned.add(v);
                }
            }
            if (owned.isEmpty()) continue;
            System.out.println(p.getLicenseNumber() + " - " + p.getName() + " " + p.getSurname());
            double total = 0.0;
            for (Vehicle v : owned) {
                double tax = v.annualTax();
                total += tax;
                System.out.println("  " + v.getPlate() + " " + v.getMaker() + " " + v.getModel() + " -> tax: " + tax);
            }
            System.out.println("  Total: " + total + "\n");
        }

        List<Vehicle> unowned = new ArrayList<>();
        for (Vehicle v : vehicles.values()) {
            if (v.getOwner() == null) unowned.add(v);
        }
        if (!unowned.isEmpty()) {
            System.out.println("Vehicles without owner:");
            for (Vehicle v : unowned) {
                System.out.println("  " + v.getPlate() + " " + v.getMaker() + " " + v.getModel() + " -> tax: " + v.annualTax());
            }
            System.out.println();
        }
    }
}
