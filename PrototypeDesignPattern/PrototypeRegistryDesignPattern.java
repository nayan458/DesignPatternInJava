import java.util.HashMap;
import java.util.Map;

class VehicleRegistry {

    private Map<String, Vehicle<?>> registry = new HashMap<>();

    public <T extends Vehicle<T>> void addPrototype(String key, T prototype) {
        registry.put(key, prototype);
    }

    @SuppressWarnings("unchecked")
    public <T extends Vehicle<T>> T getPrototype(String key) {
        Vehicle<?> prototype = registry.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype registered with key: " + key);
        }
        return (T) prototype.copy();
    }
}


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


public class PrototypeRegistryDesignPattern {
    public static void main(String[] args) {

        // 1. Create registry
        VehicleRegistry registry = new VehicleRegistry();

        // 2. Register standard templates
        registry.addPrototype("basic-car", 
                new Car("Tesla", "Model 3", 45000, 5));

        registry.addPrototype("premium-car", 
                new Car("BMW", "X5", 90000, 7));

        registry.addPrototype("sport-bike", 
                new Bike("Yamaha", "R15", 150000, true));

        registry.addPrototype("city-bike", 
                new Bike("Honda", "Shine", 70000, false));


        // 3. Client wants a new instance → clone it
        Car customerCar = registry.getPrototype("premium-car");
        customerCar.brand = "BMW";       // modify after cloning
        customerCar.model = "X5 - Custom Edition";

        Bike deliveryBike = registry.getPrototype("city-bike");
        deliveryBike.brand = "Honda";
        deliveryBike.price = 68000;


        // 4. Print results
        System.out.println("=== Cloned Vehicles ===");
        System.out.println(customerCar);
        System.out.println(deliveryBike);

        // 5. Registry originals remain unchanged
        System.out.println("\n=== Registry Originals ===");
        System.out.println(registry.getPrototype("premium-car"));
        System.out.println(registry.getPrototype("city-bike"));
    }
}
