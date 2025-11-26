interface Chair {
    void hasLegs();
    void sitOn();
}

interface Sofa {
    void material();
    void hasLegs();
}

interface CoffeTable {
    void material();
    void hasLegs();
}

// Victorian products

class VictorianChair implements Chair {
    @Override
    public void hasLegs() {
        System.out.println("4 curved legs");
    }

    @Override
    public void sitOn() {
        System.out.println("cussion");
    }
}

class VictorianSofa implements Sofa {
    @Override
    public void hasLegs() {
        System.out.println("6, 2 in between");
    }

    @Override
    public void material() {
        System.out.println("victorian cussion");
    }
}

class VictorianCoffeTable implements CoffeTable {
    @Override
    public void material() {
        System.out.println("victorian coffe table material");
    }

    @Override
    public  void hasLegs() {
        System.out.println("victorian legs");
    }
}

// Modern products

class ModernChair implements Chair {
    @Override
    public void hasLegs() {
        System.out.println("4 eligent legs");
    }

    @Override
    public void sitOn() {
        System.out.println("wood or plastic");
    }
}

class ModernSofa implements Sofa {
    @Override
    public void hasLegs() {
        System.out.println("4, regular");
    }

    @Override
    public void material() {
        System.out.println("Modern cussion");
    }
}

class ModernCoffeTable implements CoffeTable {
    @Override
    public void material() {
        System.out.println("Modern coffe table material");
    }

    @Override
    public  void hasLegs() {
        System.out.println("Modern legs");
    }
}


// ArtDecor products

class ArtDecorChair implements Chair {
    @Override
    public void hasLegs() {
        System.out.println("4 artistic legs");
    }

    @Override
    public void sitOn() {
        System.out.println("cur tyers");
    }
}

class ArtDecorSofa implements Sofa {
    @Override
    public void hasLegs() {
        System.out.println("4, Artistic");
    }

    @Override
    public void material() {
        System.out.println("ArtDecor cussion");
    }
}

class ArtDecorCoffeTable implements CoffeTable {
    @Override
    public void material() {
        System.out.println("ArtDecor coffe table material");
    }

    @Override
    public  void hasLegs() {
        System.out.println("ArtDecor legs");
    }
}

interface AbstractFurnitureFactory {
    Chair createChair();
    Sofa createSofa();
    CoffeTable createCoffeTable();
}

class VictorianFurnitureFactory implements AbstractFurnitureFactory{
    @Override
    public Chair createChair() {
        return new VictorianChair();
    }

    @Override
    public Sofa createSofa() {
        return new VictorianSofa();
    }

    @Override
    public CoffeTable createCoffeTable() {
        return new VictorianCoffeTable();
    }
}

class ModernFurnitureFactory implements AbstractFurnitureFactory{
    @Override
    public Chair createChair() {
        return new ModernChair();
    }

    @Override
    public Sofa createSofa() {
        return new ModernSofa();
    }

    @Override
    public CoffeTable createCoffeTable() {
        return new ModernCoffeTable();
    }
}

class ArtDecorFurnitureFactory implements AbstractFurnitureFactory{
    @Override
    public Chair createChair() {
        return new ArtDecorChair();
    }

    @Override
    public Sofa createSofa() {
        return new ArtDecorSofa();
    }

    @Override
    public CoffeTable createCoffeTable() {
        return new ArtDecorCoffeTable();
    }
}


public class FurnitureFactory {
    public static void main(String[] args) {
        AbstractFurnitureFactory VictorianFactory = new VictorianFurnitureFactory();
        Chair vChair = VictorianFactory.createChair();
        Sofa vSofa = VictorianFactory.createSofa();
        CoffeTable vCoffeTable = VictorianFactory.createCoffeTable();

        vChair.hasLegs();
        vSofa.material();
        vCoffeTable.material();
    }
}