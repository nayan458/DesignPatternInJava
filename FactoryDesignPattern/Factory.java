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

// Creator
abstract class Logistics {
    abstract Transport createTransport();  // Factory Method

    public void planDelivery() {
        Transport t = createTransport();
        t.deliver();
    }
}

// Concrete Creators
class RoadLogistics extends Logistics {
    Transport createTransport() {
        return new Truck();
    }
}

class SeaLogistics extends Logistics {
    Transport createTransport() {
        return new Ship();
    }
}

// Client
public class Factory {
    public static void main(String[] args) {
        Logistics logistics = new RoadLogistics();
        logistics.planDelivery();
    }
}

// interface Transport {
//     void deliver();
// }

// class Truck implements Transport {
//     public void deliver() {
//         System.out.println("Delivering by road using a Truck.");
//     }
// }

// class Ship implements Transport {
//     public void deliver() {
//         System.out.println("Delivering by sea using a Ship.");
//     }
// }

// // Creator Class
// abstract class Logistics {
//     // Factory Method
//     abstract Transport createTransport();

//     void planDelivery() {
//         Transport transport = createTransport();
//         transport.deliver();
//     }
// }

// // Concrete Creators
// class RoadLogistics extends Logistics {
//     Transport createTransport() {
//         return new Truck();
//     }
// }

// class SeaLogistics extends Logistics {
//     Transport createTransport() {
//         return new Ship();
//     }
// }

// public class Factory {
//     public static void main(String[] args) {

//         // Based on config, env var, or user selection
//         Logistics logistics;

//         String type = args[0]; // not deciding which object to `new`

//         // No object creation logic here
//         if (type.equals("road")) {
//             logistics = new RoadLogistics();
//         } else if (type.equals("sea")) {
//             logistics = new SeaLogistics();
//         } else {
//             throw new RuntimeException("Unknown logistics type");
//         }

//         // business logic stays untouched
//         logistics.planDelivery();
//     }
// }
