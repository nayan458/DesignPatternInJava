import java.util.*;

class Tree {
    byte[] sprite = new byte[20 * 1024];
    int x;
    int y;
    String type;

    public Tree(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}

class Forest {
    List<Tree> forest = new ArrayList<>();

    public void add(Tree tree){
        forest.add(tree);
    }

    public void draw() {
        System.out.println("This is a forest");
    }
}

public class PlantTrees {
    public static void main(String[] args) {
        Forest forest = new Forest();
        MemoryUtil.print("START");

        for(int i = 0;  i < 1_000_000; i++){
            forest.add(new Tree(1,2,"big"));
            if(i % 100_00 == 0)
                MemoryUtil.print(new StringBuilder("Particles: " + i).toString());
        }
        
        forest.draw();
    }
}

class MemoryUtil {

    public static void print(String tag) {
        Runtime r = Runtime.getRuntime();
        long used = r.totalMemory() - r.freeMemory();

        System.out.printf(
            "%s | Used Memory: %d MB%n",
            tag,
            used / 1024 / 1024
        );
    }
}
