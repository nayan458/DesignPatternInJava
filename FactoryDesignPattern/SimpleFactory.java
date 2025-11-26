interface Transport {
    void deliver();
}

class Truck implements Transport {
    public void deliver() {
        System.out.println("Delivering by Truck.");
    }
}

class Ship implements Transport {
    public void deliver() {
        System.out.println("Delivering by Ship.");
    }
}

// Simple Factory
class TransportFactory {
    public static Transport create(String type) {
        if (type.equals("truck")) return new Truck();
        if (type.equals("ship")) return new Ship();
        throw new RuntimeException("Unknown type");
    }
}

// Client
public class SimpleFactory {
    public static void main(String[] args) {
        Transport t = TransportFactory.create("ship");
        t.deliver();
    }
}
