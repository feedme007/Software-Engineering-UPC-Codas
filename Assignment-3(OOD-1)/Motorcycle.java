public class Motorcycle extends Vehicle {
    private double engineDisplacement;

    public Motorcycle(String plate, String maker, String model, double engineDisplacement) {
        super(plate, maker, model);
        this.engineDisplacement = engineDisplacement;
    }

    public double getEngineDisplacement() { return engineDisplacement; }
    public void setEngineDisplacement(double d) { this.engineDisplacement = d; }

    @Override
    public double annualTax() {
        return 0.10 * engineDisplacement;
    }
}
