package Upgrades;

import Players.Player;
import Weapons.WoodenDagger;
import processing.core.PApplet;

import java.util.HashMap;

public class StickmanLevel1 extends Stickman {

    private static final int UNIT = 5;

    public StickmanLevel1(PApplet app, int colour, Player owner, float x, float y) {
        super(app, colour, owner, new WoodenDagger(app, UNIT), x, y, UNIT);
        if (owner == null) {
            getWeaponEquipped().setAttacking(true);
        }
        HashMap<String, Float> stats = new HashMap<>();
        stats.put("strength", (float) 1);
        stats.put("dexterity", (float) 0.28);
        stats.put("health", (float) 100);
        stats.put("armour", (float) 0);
        super.setStats(stats);
    }

    @Override
    public void draw() {
        super.draw();
    }
}
