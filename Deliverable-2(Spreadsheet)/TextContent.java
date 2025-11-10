public class TextContent implements Content {
    private final String value;

    public TextContent(String value) { this.value = value; }
    public String getValue() { return value; }
    @Override public String asText() { return value == null ? "" : value; }
}
