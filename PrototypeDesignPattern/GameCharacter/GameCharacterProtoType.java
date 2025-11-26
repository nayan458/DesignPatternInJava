import java.util.*;

class GameCharacterRegistry {
    private Map<String, GameCharacter<?>> hm = new HashMap<>();

    public <T extends GameCharacter<T>> void addCharacter(String key, T prototype) {
        hm.put(key, prototype);
    }

    public <T extends GameCharacter<T>> T get(String key, Class<T> type) {
        GameCharacter<?> prototype = hm.get(key);
        if(prototype == null)
            throw  new IllegalArgumentException("No such character: " + key);
        return type.cast(prototype.copy());
    }
}

interface Prototype<T>{
    T copy();
}

abstract class GameCharacter <T extends GameCharacter<T>> implements Prototype<T>{
    protected String name;
    protected double health;
    protected double stamina;
    protected double speed;

    public GameCharacter(String name, double health, double stamina, double speed) {
        this.name = name;
        this.health = health;
        this.stamina = stamina;
        this.speed = speed;
    }

    @Override
    public abstract T copy();

    @Override
    public String toString() {
        return "name " + name +"health " + health +"stamina " + stamina +"speed " + speed + " ";
    }
}

class Warrior extends GameCharacter<Warrior> {
    protected String WeaponType;
    protected double  armorLevel;

    public Warrior(String name, double health, double stamina, double speed, String WeaponType, double armorLevel) {
        super(name, health, stamina, speed);
        this.WeaponType = WeaponType;
        this.armorLevel = armorLevel;
    }

    public Warrior(Warrior worrier) {
        super(worrier.name, worrier.health, worrier.stamina, worrier.speed);
        this.WeaponType = worrier.WeaponType;
        this.armorLevel = worrier.armorLevel;
    }

    @Override
    public Warrior copy(){
        return new Warrior(this);
    }

    @Override
    public String toString() {
        return super.toString() + "WeaponType " + WeaponType +"armorLevel " + armorLevel + " ";
    }

}

class Sniper extends GameCharacter<Sniper> {
    public String rifleModel;
    public double range;

    public Sniper(String name, double health, double stamina, double speed, String rifleModel, double range) {
        super(name, health, stamina, speed);
        this.rifleModel = rifleModel;
        this.range = range;
    }

    public Sniper(Sniper sniper) {
        super(sniper.name, sniper.health, sniper.stamina, sniper.speed);
        this.rifleModel = sniper.rifleModel;
        this.range = sniper.range;
    }

    @Override
    public Sniper copy(){
        return new Sniper(this);
    }

    @Override
    public String toString() {
        return super.toString() + "rifleModel " + rifleModel +"range " + range;
    }

}

public class GameCharacterProtoType {
    public static void main(String[] args) {
        GameCharacterRegistry registry = new GameCharacterRegistry();

        // character registering
        registry.addCharacter("warrior-default", new Warrior("Alucard", 2000, 200, 3, "Sword", 300));
        registry.addCharacter("warrior-heavy", new Warrior("Thamus", 5000, 200, 3, "Sword", 300));
        registry.addCharacter("sniper-pro", new Sniper("lesly", 1000, 200, 3, "Sniper0.22", 3000));
        registry.addCharacter("sniper-default", new Sniper("Beatrix", 1000, 200, 3, "Sniper0.22", 3000));

        Sniper player1 = registry.get("sniper-pro", Sniper.class);
        player1.name = "ShadowHunter";
        player1.range = 900;

        Warrior player2 = registry.get("warrior-heavy", Warrior.class);
        player2.name = "IronFist";
        player2.armorLevel = 150;

        System.out.println(player1);
        System.out.println(player2);
    }
}