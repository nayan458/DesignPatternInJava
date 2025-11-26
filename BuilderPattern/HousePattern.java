class House {
    private String walls;
    private String floor;
    private String door;
    private String windows;
    private String roof;
    private String backyard;
    private String plumbing;
    private String electrical;
    private String heatingSystem;

    // Setters
    public void setWalls(String walls) { this.walls = walls; }
    public void setFloor(String floor) { this.floor = floor; }
    public void setDoor(String door) { this.door = door; }
    public void setWindows(String windows) { this.windows = windows; }
    public void setRoof(String roof) { this.roof = roof; }
    public void setBackyard(String backyard) { this.backyard = backyard; }
    public void setPlumbing(String plumbing) { this.plumbing = plumbing; }
    public void setElectrical(String electrical) { this.electrical = electrical; }
    public void setHeatingSystem(String heatingSystem) { this.heatingSystem = heatingSystem; }

    @Override
    public String toString() {
        return "House [" +
                "walls=" + walls +
                ", floor=" + floor +
                ", door=" + door +
                ", windows=" + windows +
                ", roof=" + roof +
                ", backyard=" + backyard +
                ", plumbing=" + plumbing +
                ", electrical=" + electrical +
                ", heatingSystem=" + heatingSystem +
                ']';
    }
}

interface HouseBuilder {
    void buildWalls();
    void buildFloor();
    void buildDoor();
    void buildWindows();
    void buildRoof();

    // Optional features
    void buildBackyard();
    void buildPlumbing();
    void buildElectrical();
    void buildHeatingSystem();

    House getHouse();
}


class SimpleHouseBuilder implements HouseBuilder {

    private House house;

    public SimpleHouseBuilder() {
        this.house = new House();
    }

    @Override
    public void buildWalls() { house.setWalls("4 simple brick walls"); }

    @Override
    public void buildFloor() { house.setFloor("Concrete floor"); }

    @Override
    public void buildDoor() { house.setDoor("Wooden door"); }

    @Override
    public void buildWindows() { house.setWindows("2 small windows"); }

    @Override
    public void buildRoof() { house.setRoof("Simple sloped roof"); }

    @Override
    public void buildBackyard() { house.setBackyard("No backyard"); }

    @Override
    public void buildPlumbing() { house.setPlumbing("Basic plumbing"); }

    @Override
    public void buildElectrical() { house.setElectrical("Basic electrical wiring"); }

    @Override
    public void buildHeatingSystem() { house.setHeatingSystem("No heating system"); }

    @Override
    public House getHouse() { return this.house; }
}

class LuxuryHouseBuilder implements HouseBuilder {

    private House house;

    public LuxuryHouseBuilder() {
        this.house = new House();
    }

    @Override
    public void buildWalls() { house.setWalls("8 insulated walls"); }

    @Override
    public void buildFloor() { house.setFloor("Marble flooring"); }

    @Override
    public void buildDoor() { house.setDoor("Automatic smart door"); }

    @Override
    public void buildWindows() { house.setWindows("Large glass windows"); }

    @Override
    public void buildRoof() { house.setRoof("Premium solar roof"); }

    @Override
    public void buildBackyard() { house.setBackyard("Large backyard with garden"); }

    @Override
    public void buildPlumbing() { house.setPlumbing("Modern plumbing & water system"); }

    @Override
    public void buildElectrical() { house.setElectrical("Smart electrical system"); }

    @Override
    public void buildHeatingSystem() { house.setHeatingSystem("Central heating system"); }

    @Override
    public House getHouse() { return this.house; }
}

class HouseDirector {
    private HouseBuilder builder;

    public HouseDirector(HouseBuilder builder) {
        this.builder = builder;
    }

    public House buildSimpleHouse() {
        builder.buildWalls();
        builder.buildFloor();
        builder.buildDoor();
        builder.buildWindows();
        builder.buildRoof();
        builder.buildPlumbing();
        builder.buildElectrical();
        builder.buildBackyard();      // optional
        builder.buildHeatingSystem(); // optional
        return builder.getHouse();
    }

    public House buildLuxuryHouse() {
        builder.buildWalls();
        builder.buildFloor();
        builder.buildDoor();
        builder.buildWindows();
        builder.buildRoof();
        builder.buildBackyard();
        builder.buildPlumbing();
        builder.buildElectrical();
        builder.buildHeatingSystem();
        return builder.getHouse();
    }
}


public class HousePattern {
    public static void main(String[] args) {

        // Build a simple house
        HouseBuilder simpleBuilder = new SimpleHouseBuilder();
        HouseDirector simpleHouseDirector = new HouseDirector(simpleBuilder);
        House simpleHouse = simpleHouseDirector.buildSimpleHouse();
        System.out.println(simpleHouse);

        // Build a luxury house
        HouseBuilder luxuryBuilder = new LuxuryHouseBuilder();
        HouseDirector luxuryHouseDirector = new HouseDirector(luxuryBuilder);
        House luxuryHouse = luxuryHouseDirector.buildLuxuryHouse();
        System.out.println(luxuryHouse);
    }
}