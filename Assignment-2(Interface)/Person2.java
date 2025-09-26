public class Person2 extends Program2 implements ComparableItem {
    private String name;
    private String surname;

    public Person2(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void print() {
        System.out.println(name + " " + surname);
    }

    @Override
    public int compareTo(ComparableItem other) {
        Person2 o = (Person2) other;
        int bySurname = this.surname.compareToIgnoreCase(o.surname);
        if (bySurname == 0) {
            return this.name.compareToIgnoreCase(o.name);
        } else {
            return bySurname;
        }
    }
}
