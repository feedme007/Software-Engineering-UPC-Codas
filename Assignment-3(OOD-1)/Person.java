public class Person {
    private String licenseNumber;
    private String name;
    private String surname;
    private String address;

    public Person(String licenseNumber, String name, String surname, String address) {
        this.licenseNumber = licenseNumber;
        this.name = name;
        this.surname = surname;
        this.address = address;
    }

    public String getLicenseNumber() { return licenseNumber; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return licenseNumber + " - " + name + " " + surname + " (" + address + ")";
    }
}
