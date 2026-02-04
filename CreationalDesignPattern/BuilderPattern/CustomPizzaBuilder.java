import java.util.ArrayList;
import java.util.List;

class Pizza {
    // 1. All fields are final (Immutability)
    private final String dough;
    private final String sauce;
    private final String cheese;
    private final List<String> toppings;
    private final String crust;
    private final String size;

    // 2. Private Constructor: The only way to create a Pizza is via the Builder
    private Pizza(Builder builder) {
        this.dough = builder.dough;
        this.sauce = builder.sauce;
        this.cheese = builder.cheese;
        this.toppings = builder.toppings;
        this.crust = builder.crust;
        this.size = builder.size;
    }

    // Public Getters (No setters, preserving immutability)
    public String toString() {
        return "Pizza [\n" +
            "  Size: " + size + "\n" +
            "  Crust: " + crust + "\n" +
            "  Dough: " + dough + "\n" +
            "  Sauce: " + sauce + "\n" +
            "  Cheese: " + cheese + "\n" +
            "  Toppings: " + toppings + "\n" +
            "]";
    }

    // 3. Static Builder Class
    public static class Builder {
        // Required Fields (passed to Builder constructor or built first)
        private final String size; 
        private final String crust;
        
        // Optional Fields (with defaults)
        private String dough = "Wheat";
        private String sauce = "Classic Tomato";
        private String cheese = "Mozzarella";
        private List<String> toppings = new ArrayList<>();

        // Builder constructor enforces required fields
        public Builder(String size, String crust) {
            this.size = size;
            this.crust = crust;
        }

        // Fluent methods for optional fields (return 'this' for chaining)
        public Builder withDough(String dough) {
            this.dough = dough;
            return this;
        }
        
        public Builder withSauce(String sauce) {
            this.sauce = sauce;
            return this;
        }

        public Builder withCheese(String cheese) {
            this.cheese = cheese;
            return this;
        }

        public Builder addTopping(String topping) {
            this.toppings.add(topping);
            return this;
        }
        
        // Final build method creates the immutable Pizza object
        public Pizza build() {
            // ** Validation (like Protobuf) would go here **
            if (this.toppings.isEmpty() && this.sauce.equals("Pesto")) {
                 System.out.println("Warning: Pesto sauce usually needs toppings!");
            }
            return new Pizza(this);
        }
    }
}

public class CustomPizzaBuilder {
    public static void main(String[] args) {
        // 1. Margherita-like Pizza (Telescoping Constructor solution)
        Pizza margherita = new Pizza.Builder("Medium", "Thin")
            .withDough("Maida")
            .withSauce("Simple Tomato")
            .withCheese("Fresh Mozzarella")
            .build();

        System.out.println("--- Standard Margherita ---");
        System.out.println(margherita);
        
        // 2. Custom Pizza (Flexibility/Customization)
        Pizza customPestoVeggie = new Pizza.Builder("Large", "Thick")
            .withDough("Sourdough")          // Custom dough
            .withSauce("Arugula Pesto")       // Custom sauce
            .withCheese("Goat Cheese")        // Custom cheese
            .addTopping("Sun-Dried Tomato")   // Custom toppings
            .addTopping("Artichoke Hearts")
            .build();

        System.out.println("\n--- Custom Pesto Veggie Pizza ---");
        System.out.println(customPestoVeggie);
    }
}
// import java.util.ArrayList;
// import java.util.List;

// class Pizza {
//     private String dough;
//     private String sauce;
//     private String cheese;
//     private List<String> toppings;
//     private String crust;
//     private String size;

//     private Pizza() {} // prevent direct construction

//     @Override
//     public String toString() {
//         return "Pizza [" +
//             "dough=" + dough +
//             ", sauce=" + sauce +
//             ", cheese=" + cheese +
//             ", toppings=" + toppings +
//             ", crust=" + crust +
//             ", size=" + size +
//             "]";
//     }

//     public static class Builder {
//         private final Pizza pizza;

//         public Builder() {
//             pizza = new Pizza();
//             pizza.toppings = new ArrayList<>();
//         }

//         public Builder dough(String dough) {
//             pizza.dough = dough;
//             return this;
//         }

//         public Builder sauce(String sauce) {
//             pizza.sauce = sauce;
//             return this;
//         }

//         public Builder cheese(String cheese) {
//             pizza.cheese = cheese;
//             return this;
//         }

//         public Builder addTopping(String topping) {
//             pizza.toppings.add(topping);
//             return this;
//         }

//         public Builder crust(String crust) {
//             pizza.crust = crust;
//             return this;
//         }

//         public Builder size(String size) {
//             pizza.size = size;
//             return this;
//         }

//         public Pizza build() {
//             return pizza;
//         }
//     }
// }

// public class CustomPizzaBuilder {
//     public static void main(String[] args) {
        
//         // Custom pizza 1
//         Pizza pizza1 = new Pizza.Builder()
//             .dough("maida")
//             .sauce("tomato")
//             .cheese("mozzarella")
//             .addTopping("Olives")
//             .addTopping("Jalapeno")
//             .crust("thin")
//             .size("large")
//             .build();

//         System.out.println(pizza1);

//         // Custom pizza 2
//         Pizza pizza2 = new Pizza.Builder()
//             .dough("atta")
//             .sauce("pesto")
//             .cheese("cheddar")
//             .addTopping("Mushrooms")
//             .addTopping("Pepperoni")
//             .crust("thick")
//             .size("medium")
//             .build();

//         System.out.println(pizza2);
//     }
// }
