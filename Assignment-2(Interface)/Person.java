public class Person {
    private final String name;
    private final String surname;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void print() {
        System.out.println(name + " " + surname);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
