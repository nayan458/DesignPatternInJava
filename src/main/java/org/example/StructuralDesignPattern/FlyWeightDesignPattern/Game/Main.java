package org.example.StructuralDesignPattern.FlyWeightDesignPattern.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Poor Code
/* Code that eats memory and finaly exausht it */

/* ===================== PARTICLE ===================== */
class Particle {

    // Extrinsic (should not be here)
    public int x;
    public int y;
    public int vx;
    public int vy;
    public int speed;

    // Intrinsic (should be shared, but isn't)
    public String color;
    public byte[] sprite; // heavy (~20KB)

    public void move() {
        x += vx;
        y += vy;
    }

    public void draw(Canvas canvas) {
        canvas.draw(sprite, x, y, color);
    }
}

/* ===================== CANVAS ===================== */
class Canvas {
    public void draw(byte[] sprite, int x, int y, String color) {
        // dummy renderer
    }
}

/* ===================== UNIT ===================== */
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

        // ❌ very expensive
        p.sprite = SpriteLoader.load("bullet.jpeg");

        game.addParticle(p);
    }
}

/* ===================== GAME ===================== */
class Game {

    private List<Particle> particles = new ArrayList<>();

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public int particleCount() {
        return particles.size();
    }

    public void draw(Canvas canvas) {
        for (Particle p : particles) {
            p.move();
            p.draw(canvas);
        }
    }
}

/* ===================== SPRITE LOADER ===================== */
class SpriteLoader {

    public static byte[] load(String path) {
        byte[] sprite = new byte[20 * 1024]; // 20 KB
        new Random().nextBytes(sprite);
        return sprite;
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

/* ===================== MAIN ===================== */
public class Main {

    public static void main(String[] args) {

        Game game = new Game();
        Unit shooter = new Unit(0, 0, 10);
        Unit target = new Unit(100, 100, 10);

        Canvas canvas = new Canvas();

        MemoryUtil.print("START");

        for (int i = 1; i <= 1_000_000; i++) {

            shooter.fireAt(target, game);

            if (i % 100_000 == 0) {
                MemoryUtil.print("Particles: " + i);
            }
        }

        // simulate render loop once
        game.draw(canvas);

        MemoryUtil.print("END");
    }
}
