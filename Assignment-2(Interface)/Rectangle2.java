public class Rectangle2 implements ComparableItem {
    private double width;
    private double height;

    public Rectangle2(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double area() {
        return width * height;
    }

    @Override
    public int compareTo(ComparableItem other) {
        Rectangle2 o = (Rectangle2) other;
        double a1 = this.area();
        double a2 = o.area();
        return Double.compare(a1, a2);
    }
}
