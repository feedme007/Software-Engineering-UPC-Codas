public class Cell {
    private final Coord coord;
    private Content content; // null means empty

    public Cell(Coord coord) { this.coord = coord; }

    public Coord getCoord() { return coord; }
    public Content getContent() { return content; }
    public void setContent(Content content) { this.content = content; }

    public boolean isEmpty() { return content == null || content.asText().isEmpty(); }
}
