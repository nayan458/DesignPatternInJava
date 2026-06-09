package org.example.StructuralDesignPattern.FlyWeightDesignPattern.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class SpriteLoader {
    public static byte[] load(String path){
        byte[] sprite = new byte[20 * 1024];
        new Random().nextBytes(sprite);
        return sprite;
    }
}

class Particle {
    int x;
    int y;
    int vx;
    int vy;
    int speed;

    String color;
    byte[] sprite;

}



class Unit {
    int x;
    int y;
    int weaponPower;


    public Unit(int x, int y, int weaponPower) {
        this.x = x;
        this.y = y;
        this.weaponPower = weaponPower;
    }

    public void fireAt(Unit target, Game game) {
        Particle p = new Particle();
        
        p.x = this.x;
        p.y = this.y;
        p.vx = target.x - this.x;
        p.vy = target.y - this.y;
        p.speed = weaponPower;
        p.color = "RED";

        p.sprite = SpriteLoader.load("bullet.jpeg"); 

        game.addParticle(p);
    }
}

class Game {
    private List<Particle> particles = new ArrayList();

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public int particleCount() {
        return particles.size();
    }
}

/* ===================== MEMORY UTIL ===================== */
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

public class Games {
    public static void main(String[] args) {
        Game game = new Game();
        Unit shooter = new Unit(0,0,10);
        Unit target = new Unit(100,100,10);

        MemoryUtil.print("START");

        for(int i = 1; i <= 1_000_000; i++) {
            shooter.fireAt(target, game);

            if(i % 100_00 == 0)
                MemoryUtil.print(new StringBuilder("Particles: " + i).toString());
        }

        MemoryUtil.print("END");
    }
}