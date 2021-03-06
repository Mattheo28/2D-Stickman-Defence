package Upgrades;

import Players.Player;
import Weapons.GoldSword;
import processing.core.PApplet;

import java.util.HashMap;

public class StickmanLevel4 extends Stickman {

    private static final int UNIT = 7;

    public StickmanLevel4(PApplet app, int colour, Player owner, float x, float y) {
        super(app, colour, owner, new GoldSword(app, UNIT), x, y, UNIT);
        if (owner == null) {
            getWeaponEquipped().setAttacking(true);
        }
        HashMap<String, Float> stats = new HashMap<>();
        stats.put("strength", (float) 1);
        stats.put("dexterity", (float) 0.10);
        stats.put("health", (float) 100);
        stats.put("armour", (float) 50);
        super.setStats(stats);
    }

    @Override
    public void draw() {
        super.draw();
    }
}
