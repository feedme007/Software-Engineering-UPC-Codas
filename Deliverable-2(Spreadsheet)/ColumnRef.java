public class ColumnRef {
    private final String letters;

    public ColumnRef(String letters) {
        if (letters == null || !letters.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Invalid column: " + letters);
        }
        this.letters = letters.toUpperCase();
    }

    public String getLetters() {
        return letters;
    }

    // 1-based
    public static String fromIndex(int index) {
        StringBuilder sb = new StringBuilder();
        while (index > 0) {
            int rem = (index - 1) % 26;
            sb.append((char) ('A' + rem));
            index = (index - 1) / 26;
        }
        return sb.reverse().toString();
    }

    // 1-based
    public static int toIndex(String letters) {
        int n = 0;
        for (int i = 0; i < letters.length(); i++) {
            char c = Character.toUpperCase(letters.charAt(i));
            n = n * 26 + (c - 'A' + 1);
        }
        return n;
    }
}
