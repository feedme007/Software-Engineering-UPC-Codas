public abstract class Vehicle {
    private String plate;
    private String maker;
    private String model;
    private Person owner; // can be null

    public Vehicle(String plate, String maker, String model) {
        this.plate = plate;
        this.maker = maker;
        this.model = model;
    }

    public String getPlate() { return plate; }
    public String getMaker() { return maker; }
    public String getModel() { return model; }
    public Person getOwner() { return owner; }

    public void setMaker(String maker) { this.maker = maker; }
    public void setModel(String model) { this.model = model; }
    public void setOwner(Person owner) { this.owner = owner; }

    public abstract double annualTax();

    @Override
    public String toString() {
        String o = (owner == null) ? "no-owner" : owner.getLicenseNumber();
        return plate + " - " + maker + " " + model + " [" + o + "]";
    }
}
