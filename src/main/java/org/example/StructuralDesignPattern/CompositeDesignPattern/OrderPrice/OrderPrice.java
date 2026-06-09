import java.util.*;

interface Box {
    double calculatePrice();     // corrected spelling
}

// ---------------------- Product (Abstract Component) ----------------------
abstract class Product implements Box {
    protected final String title;
    protected final double price;

    public Product(String title, double price) {
        this.title = title;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

// ---------------------- Leaf Classes ----------------------
class Book extends Product {
    public Book(String title, double price) {
        super(title, price);
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }
}

class VideoGame extends Product {
    public VideoGame(String title, double price) {
        super(title, price);
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }
}

// ---------------------- Composite Class ----------------------
class CompositeBox implements Box {
    private final List<Box> children = new ArrayList<>();

    public CompositeBox(Box... boxes) {
        children.addAll(Arrays.asList(boxes));
    }

    @Override
    public double calculatePrice() {
        return children.stream()
                       .mapToDouble(Box::calculatePrice)
                       .sum();
    }
}

// ---------------------- Client ----------------------
class DeliveryService {
    private Box box;

    public void setupOrder(Box... boxes) {
        this.box = new CompositeBox(boxes);
    }

    public double calculateOrderPrice() {
        return box.calculatePrice();
    }
}

// ---------------------- Main ----------------------
public class OrderPrice {
    public static void main(String[] args) {
        DeliveryService deliveryService = new DeliveryService();

        deliveryService.setupOrder(
            new CompositeBox(new VideoGame("1", 100)),
            new CompositeBox(
                new CompositeBox(
                    new Book("2", 200),
                    new Book("3", 300)
                )
            ),
            new VideoGame("4", 400),
            new VideoGame("5", 500)
        );

        System.out.println("Total Price: " + deliveryService.calculateOrderPrice());
    }
}
