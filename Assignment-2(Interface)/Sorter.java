public class Sorter extends Person {
    public void sort(Person[] people) {
        int n = people.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Person a = people[j];
                Person b = people[j + 1];
                int bySurname = a.getSurname().compareToIgnoreCase(b.getSurname());
                if (bySurname > 0) {
                    people[j] = b;
                    people[j + 1] = a;
                } else if (bySurname == 0 && a.getName().compareToIgnoreCase(b.getName()) > 0) {
                    people[j] = b;
                    people[j + 1] = a;
                }

            }
        }
    }
}
