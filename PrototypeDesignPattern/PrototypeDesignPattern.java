interface Prototype<T> {
    T copy();
}

abstract class Vehicle<T extends Vehicle<T>> implements Prototype<T> {
    protected String brand;
    protected String model;
    protected int price;

    public Vehicle(String brand, String model, int price) {
        this.brand = brand;
        this.model = model;
        this.price = price;
    }

    // Base clone (implemented using copy constructor of subclasses)
    @Override
    public abstract T copy();

    @Override
    public String toString() {
        return "Brand: " + brand + ", Model: " + model + ", Price: " + price;
    }
}


class Car extends Vehicle<Car> {

    private int seatCount;

    public Car(String brand, String model, int price, int seatCount) {
        super(brand, model, price);
        this.seatCount = seatCount;
    }

    // Copy constructor for cloning
    private Car(Car car) {
        super(car.brand, car.model, car.price);
        this.seatCount = car.seatCount;
    }

    @Override
    public Car copy() {
        return new Car(this);  // deep copy
    }

    @Override
    public String toString() {
        return super.toString() + ", Seats: " + seatCount;
    }
}


class Bike extends Vehicle<Bike> {

    private final boolean hasGear;

    public Bike(String brand, String model, int price, boolean hasGear) {
        super(brand, model, price);
        this.hasGear = hasGear;
    }

    // Copy constructor for cloning
    private Bike(Bike bike) {
        super(bike.brand, bike.model, bike.price);
        this.hasGear = bike.hasGear;
    }

    @Override
    public Bike copy() {
        return new Bike(this);
    }

    @Override
    public String toString() {
        return super.toString() + ", Has Gear: " + hasGear;
    }
}


public class PrototypeDesignPattern {
    public static void main(String[] args) {

        Car car1 = new Car("Tesla", "Model X", 90000, 7);
        Car car2 = car1.copy();

        Bike bike1 = new Bike("Yamaha", "R15", 150000, true);
        Bike bike2 = bike1.copy();

        System.out.println("Original Car: " + car1);
        System.out.println("Cloned Car:   " + car2);

        System.out.println("\nOriginal Bike: " + bike1);
        System.out.println("Cloned Bike:   " + bike2);
    }
}
