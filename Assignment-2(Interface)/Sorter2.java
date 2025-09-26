public class Sorter2 extends Person2 {
    public void sort(ComparableItem[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                ComparableItem a = arr[j];
                ComparableItem b = arr[j + 1];
                if (a.compareTo(b) > 0) {
                    arr[j] = b;
                    arr[j + 1] = a;
                }
            }
        }
    }
}
