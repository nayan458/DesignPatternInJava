import java.util.*;

class TreeTypeFlyWeight {
    private final byte[] sprite;
    private final String type;

    public TreeTypeFlyWeight(String type) {
        this.type = type;
        this.sprite = new byte[20 * 1024];
    }

    public void draw(int x, int y) {
        // rendering logic (noop)
    }

    public String getType() {
        return type;
    }
}

class TreeTypeFactory {
    private static final Map<String, TreeTypeFlyWeight> cache = new HashMap<>();

    public static TreeTypeFlyWeight getTreeType(String type) {
        return cache.computeIfAbsent(type,k -> new TreeTypeFlyWeight(type));
    }

    public static int totalTypes() {
        return cache.size();
    }
}

class Tree {
    private final int x;
    private final int y;
    private final TreeTypeFlyWeight treeType;

    public Tree(int x, int y, TreeTypeFlyWeight treeType) {
        this.x = x;
        this.y = y;
        this.treeType = treeType;
    }

    public void draw() {
        treeType.draw(x, y);
    }
}

class Forest {
    
    private final List<Tree> forest = new ArrayList<>();
    
    public void add(int x, int y, String t) {
        TreeTypeFlyWeight type = TreeTypeFactory.getTreeType(t);
        Tree tree = new Tree(x, y, type);
        forest.add(tree);
        forest.add(new Tree(1, 2,type));
    }

    public void draw() {
        System.out.println("This is a forest");
        for (Tree tree : forest) {
            tree.draw();
        }
    }
}

public class FactoryFlyWeightDesignPattern {
    public static void main(String[] args) {
        Forest forest = new Forest();
        MemoryUtil.print("START");
        
        for (int i = 0; i < 1_000_000; i++) {
            forest.add(1, 2,"big");
            if (i % 10_000 == 0)
                MemoryUtil.print("Trees: " + i);
        }
        
        forest.draw();
        System.out.println("Total TreeTypes: " + TreeTypeFactory.totalTypes());
    }
}

class MemoryUtil {
    public static void print(String tag) {
        Runtime r = Runtime.getRuntime();
        long used = r.totalMemory() - r.freeMemory();
        System.out.printf("%s | Used Memory: %d MB%n", tag, used / 1024 / 1024);
    }
}
