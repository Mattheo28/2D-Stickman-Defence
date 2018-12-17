package Upgrades;

import Players.Player;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.HashMap;

public class FortifiedTower extends Upgrade {

    private PApplet app;
    private float x, y, armour;
    private PImage img;
    private Player owner;
    private boolean isDestroyed;

    public FortifiedTower(PApplet app, Player owner, float x, float y) {
        this.app = app;
        this.owner = owner;
        this.x = x;
        this.y = y;
        if (owner == null)
            img = app.loadImage("rsz_tower1black.png");
        else
            img = app.loadImage("tower" + owner.getPlayerNum() + "black.png");
        armour = 5000;
        HashMap<String, Float> stats = new HashMap<>();
        stats.put("armour", armour);
        super.setStats(stats);
    }

    @Override
    public void draw() {
        if (!isDestroyed) {
            if (owner == null)
                app.image(img, x, y);
            else
                app.image(img, owner.getX(), owner.getY());
        }
    }

    @Override
    public void getHit(float damage) {
        armour -= damage;
        if (armour <= 0)
            isDestroyed = true;
    }

    @Override
    public float getTargetX() {
        return owner.getTargetX();
    }

    @Override
    public float getTargetY() {
        return owner.getTargetY();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public float getArmour() {
        return armour;
    }
}
