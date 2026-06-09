package org.example.StructuralDesignPattern.DecoratorDesignPatther.DiscountSystem;

// Component interface
interface PriceComponent {
    double getPrice();
}

// Base price component (core object)
class BasePrice implements PriceComponent {
    private final double price;

    public BasePrice(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return this.price;
    }
}

// Base decorator
abstract class BaseDiscountDecorator implements PriceComponent {
    protected final PriceComponent wrapped;

    public BaseDiscountDecorator(PriceComponent wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public double getPrice() {
        return wrapped.getPrice();   // Pass-through
    }
}

// Seasonal discount: 10% off
class SeasonalDiscount extends BaseDiscountDecorator {
    public SeasonalDiscount(PriceComponent wrapped) {
        super(wrapped);
    }

    @Override
    public double getPrice() {
        double original = super.getPrice();
        return original * 0.90; // 10% OFF
    }
}

// Coupon discount: flat 200 off
class CouponDiscount extends BaseDiscountDecorator {
    public CouponDiscount(PriceComponent wrapped) {
        super(wrapped);
    }

    @Override
    public double getPrice() {
        double original = super.getPrice();
        return original - 200;
    }
}

// GST tax: 18%
class GSTTax extends BaseDiscountDecorator {
    public GSTTax(PriceComponent wrapped) {
        super(wrapped);
    }

    @Override
    public double getPrice() {
        double original = super.getPrice();
        return original * 1.18;
    }
}

// Main Runner
public class DiscountSystem {
    public static void main(String[] args) {
        PriceComponent price = new GSTTax(
                                    new CouponDiscount(
                                        new SeasonalDiscount(
                                            new BasePrice(2000)
                                        )
                                    )
                                );

        System.out.println("Final Price = ₹" + price.getPrice());
    }
}
