import java.util.*;

abstract class Shape {
    public Color c;

    public Shape(Color c) { this.c = c; }

    public abstract void applyColor();
}

abstract interface Color {
   public void applyColor();
}

class Triangle extends Shape {
    public Triangle(Color c) {
        super(c);
    }
    
    @Override
    public void applyColor() { c.applyColor(); }
}

class Square extends Shape {
    public Square(Color c) {
        super(c);
    }

    @Override
    public void applyColor() { c.applyColor(); }
}

class RedColor implements Color {
    public void applyColor() { System.out.println("applied Red Color"); }
}

class GreenColor implements Color {
    public void applyColor() { System.out.println("applied Green Color"); }
}



public class ShapeAndColorBridgeDesignPattern {
    public static void main(String[] args) {
        Shape tri = new Triangle(new RedColor());
        tri.applyColor();

        Shape sqr = new Square(new GreenColor());
        sqr.applyColor();
    }
}
