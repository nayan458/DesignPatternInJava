// PRODUCT FAMILY 1
interface Transport {
    void deliver();
}

class Truck implements Transport {
    public void deliver() {
        System.out.println("Delivering goods by Truck.");
    }
}

class Ship implements Transport {
    public void deliver() {
        System.out.println("Delivering goods by Ship.");
    }
}

// PRODUCT FAMILY 2
interface Packaging {
    void pack();
}

class Box implements Packaging {
    public void pack() {
        System.out.println("Packing goods in a Box.");
    }
}

class Container implements Packaging {
    public void pack() {
        System.out.println("Packing goods in a Container.");
    }
}

// ABSTRACT FACTORY
interface LogisticsFactory {
    Transport createTransport();
    Packaging createPackaging();
}

// CONCRETE FACTORIES
class RoadLogisticsFactory implements LogisticsFactory {
    public Transport createTransport() {
        return new Truck();
    }
    public Packaging createPackaging() {
        return new Box();
    }
}

class SeaLogisticsFactory implements LogisticsFactory {
    public Transport createTransport() {
        return new Ship();
    }
    public Packaging createPackaging() {
        return new Container();
    }
}

// CLIENT
public class AbstractFactory {
    public static void main(String[] args) {
        // choose family
        LogisticsFactory factory = new SeaLogisticsFactory();

        // get entire compatible family
        Transport t = factory.createTransport();
        Packaging p = factory.createPackaging();

        p.pack();
        t.deliver();
    }
}
