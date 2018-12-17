package Upgrades;

import Players.Player;
import Weapons.Fireball;
import processing.core.PApplet;

import java.util.HashMap;

public class StickmanLevel5 extends Stickman {

    private static final int UNIT = 7;

    public StickmanLevel5(PApplet app, int colour, Player owner, float x, float y) {
        super(app, colour, owner, new Fireball(app, UNIT), x, y, UNIT);
        if (owner == null) {
            getWeaponEquipped().setAttacking(true);
        }
        HashMap<String, Float> stats = new HashMap<>();
        stats.put("strength", (float) 1);
        stats.put("dexterity", (float) 0);
        stats.put("health", (float) 50);
        stats.put("armour", (float) 0);
        super.setStats(stats);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void moveRight() {}

    @Override
    public void moveLeft() {}

    @Override
    public void moveUp() {}

    @Override
    public void moveDown() {}
}
