abstract class Vehicle {
    String brand;
    String model;
    String color;

    public Vehicle(String brand, String model, String color) {
        this.brand = brand;
        this.model = model;
        this.color = color;
    }
}

class Car extends Vehicle {
    private String glassType;   // bulletproof black

    public Car(String brand, String model, String color, String glassType) {
        super(brand, model, color);
        this.glassType = glassType;
    }

    public void getDetails(Car c) {
        System.out.println(c);
    }

    public void update(String glassType){
        this.glassType = glassType;
    }

    @Override
    public String toString() {
        return String.format("""
                                Car [
                                model: %s
                                color: %s
                                brand: %s
                                glassType: %s
                                ]
                                """, this.model, this.color, this.brand, this.glassType);
        }

}

// Shallo copy

public class Problem {
    public static void main(String[] args) {
        Car bugati = new Car("bugati", "ciron", "black", "bulletproof");
        System.out.println(bugati);

        Car bugati2 = bugati;
        System.out.println(bugati2);

        bugati2.update("normal");
        System.out.printf("After update\n Bugati1:\n %s Bugati2:\n", bugati, bugati2);

    }
}