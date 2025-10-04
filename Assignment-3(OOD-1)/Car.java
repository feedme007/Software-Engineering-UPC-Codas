public class Car extends Vehicle {
    private String fuelType; // "petrol", "diesel", "hybrid"
    private double co2;      // grams of CO2 per km

    public Car(String plate, String maker, String model, String fuelType, double co2) {
        super(plate, maker, model);
        this.fuelType = fuelType.toLowerCase();
        this.co2 = co2;
    }

    public String getFuelType() { return fuelType; }
    public double getCo2() { return co2; }

    public void setFuelType(String fuelType) { this.fuelType = fuelType.toLowerCase(); }
    public void setCo2(double co2) { this.co2 = co2; }

    @Override
    public double annualTax() {
        if ("petrol".equals(fuelType)) return 1.4 * co2;
        if ("diesel".equals(fuelType)) return 1.8 * co2;
        if ("hybrid".equals(fuelType)) return 1.2 * co2;
        return 0.0;
    }
}
