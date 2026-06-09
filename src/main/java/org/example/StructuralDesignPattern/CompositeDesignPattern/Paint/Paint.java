import java.util.ArrayList;
import java.util.List;

// ---------------------------
// Component Interface
// ---------------------------
interface Graphic {
    void move(int x, int y);
    void draw();
}

// ---------------------------
// Leaf: Dot
// ---------------------------
class Dot implements Graphic {
    protected int x, y;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Dot at (" + x + ", " + y + ")");
    }
}

// ---------------------------
// Leaf: Circle
// ---------------------------
class Circle extends Dot {
    private final int radius;

    public Circle(int x, int y, int radius) {
        super(x, y);
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println(
            "Drawing Circle at (" + x + ", " + y + ") with radius " + radius
        );
    }
}

// ---------------------------
// Composite: CompoundGraphic
// ---------------------------
class CompoundGraphic implements Graphic {
    private final List<Graphic> children = new ArrayList<>();

    public void add(Graphic child) {
        children.add(child);
    }

    public void remove(Graphic child) {
        children.remove(child);
    }

    @Override
    public void move(int x, int y) {
        for (Graphic child : children) {
            child.move(x, y);
        }
    }

    @Override
    public void draw() {
        System.out.println("Drawing CompoundGraphic:");
        for (Graphic child : children) {
            child.draw();
        }
        System.out.println("Finished drawing CompoundGraphic.");
    }
}

// ---------------------------
// Client: ImageEditor
// ---------------------------
class ImageEditor {
    private CompoundGraphic all;

    public void load() {
        all = new CompoundGraphic();
        all.add(new Dot(1, 2));
        all.add(new Circle(5, 3, 10));
    }

    public void groupSelected(List<Graphic> components) {
        CompoundGraphic group = new CompoundGraphic();
        for (Graphic component : components) {
            group.add(component);
            all.remove(component);
        }
        all.add(group);

        // Draw everything
        all.draw();
    }
}

// ---------------------------
// Main
// ---------------------------
public class Paint {
    public static void main(String[] args) {
        ImageEditor editor = new ImageEditor();
        editor.load();

        List<Graphic> selection = new ArrayList<>();
        selection.add(new Dot(7, 8));
        selection.add(new Circle(10, 10, 5));

        // Simulate adding selected items to editor and grouping them
        editor.groupSelected(selection);
    }
}
