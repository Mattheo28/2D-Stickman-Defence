package Players;

import GameObjects.Healthbar;
import GameObjects.PlayerObject;
import Upgrades.FortifiedTower;
import Upgrades.Stickman;
import Upgrades.Upgrade;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player extends PlayerObject {

    private static final int MAX_HEALTH = 10000;
    private static final float INCREMENT_AMOUNT = (float) 0.1;

    private PApplet app;
    private float x, y;
    private HashMap<String, Float> stats;
    private boolean isDead;
    private int colour, playerNum;
    private PImage img;
    private Healthbar healthbar;
    private List<Upgrade> upgrades;
    private List<Player> enemies;

    public Player(PApplet app, int x, int y, int colour, PImage img, int playerNum) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.colour = colour;
        this.img = img;
        this.playerNum = playerNum;
        stats = new HashMap<>();
        stats.put("health", (float) MAX_HEALTH);
        stats.put("armour", (float) 0);
        stats.put("coins", (float) 200);
        isDead = false;
        healthbar = new Healthbar(app, MAX_HEALTH, 100, 20);
        upgrades = new ArrayList<>();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public int getColour() {
        return colour;
    }

    public void addUpgrade(Upgrade upgrade, Player target) {
        upgrades.add(upgrade);
        if (upgrade instanceof Stickman)
            ((Stickman) upgrade).setTarget(target);
//        if (upgrade instanceof Stickman) {
//            ((Stickman) upgrade).setTarget(target);
//            float targetX = target.getX();
//            float targetY = target.getY();
//            if (targetX > ((Stickman) upgrade).getX())
//                ((Stickman) upgrade).moveRight();
//            else if (targetX < ((Stickman) upgrade).getX())
//                ((Stickman) upgrade).moveLeft();
//
//            if (targetY > ((Stickman) upgrade).getY())
//                ((Stickman) upgrade).moveDown();
//            else if (targetY < ((Stickman) upgrade).getY())
//                ((Stickman) upgrade).moveUp();
////            switch (lastSelected) {
////                case 0:
////                    ((Stickman) upgrade).moveRight();
////                    break;
////                case 1:
////                    ((Stickman) upgrade).moveDown();
////                    break;
////                case 2:
////                    ((Stickman) upgrade).moveRight();
////                    ((Stickman) upgrade).moveDown();
////                    break;
////            }
//        }
    }

    public void removeUpgrade(Upgrade upgrade) {
        upgrades.remove(upgrade);
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public float getCoins() { return stats.get("coins"); }

    public void deductCoins(float amount) {
        stats.put("coins", stats.get("coins") - amount);
    }

    public float getHealth() {
        float extraHealth = 0;
        for (Upgrade u : upgrades) {
            if (u instanceof FortifiedTower)
                extraHealth += ((FortifiedTower) u).getArmour();
        }
        return stats.get("health") + extraHealth;
    }

    public List<Player> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Player> enemies) {
        this.enemies = enemies;
    }

    public boolean isDead() {
        return isDead;
    }

    public void draw() {
        if (isDead) return;
        app.image(img, x, y);
        healthbar.draw(x + 10 + img.width/4, y + img.height/6);
        stats.put("coins", stats.get("coins") + INCREMENT_AMOUNT);
//        app.pushMatrix();
//        // Paint source
//        app.strokeWeight(5);
//        t = (app.frameCount + start) % 360;
//        tk = sin (radians(t * 8));
//        tf0 = sin (radians((t + 12) * 8));
//        tf1 = sin (radians((t - 12) * 8));
//        tw = sin (radians((t + 82) * 16));
//
//        //Position of player
////        if (weaponEquipped != null && !isMoving())
////            weaponEquipped.draw(x, y + blockHeight*2/3);
//        app.translate(x, y + blockHeight*2/3);
//
//
//        app.pushMatrix();
//        app.translate(0, tw * 2);
//        app.pushMatrix();
//        app.translate (0, (float) (-unit * 4.5));
//
//        resetColour();
//        app.ellipse (0, 0, unit, unit);
//
//        //Ninja Mask
//        app.stroke(0);
//        app.fill(0);
//        app.line(-unit/2, 0, unit/2, 0);
//
//        //Eyes
//        app.fill(255);
//        app.strokeWeight(0);
//        app.ellipse (-unit/3, 0, unit/4, unit/6);
//        app.ellipse (unit/3, 0, unit/4, unit/6);
//        app.fill(255,0,0);
//        app.strokeWeight(5);
//
//        resetColour();

        //Movement
//        app.translate (0, (float) (unit * 0.5));
//        for (Elbow e:elbows) e.move();
//        app.line (0, 0, 0, (float) (unit * 1.7));
//        app.translate (0, (float) (unit * 1.7));
//        for (Knee k:knees) k.move();
//        app.popMatrix ();
//        app.popMatrix ();
//        app.strokeWeight(1);
//        app.popMatrix();
    }

    public float getTargetX() {
        return x + img.width/2;
    }

    public float getTargetY() {
        if (playerNum == 1 || playerNum == 2)
            return y + img.height;
        else
            return y;
    }

    public void getHit(float damage) {
        for (Upgrade u : upgrades) {
            if (u instanceof FortifiedTower) {
                u.getHit(damage);
                if (((FortifiedTower) u).isDestroyed()) {
                    upgrades.remove(u);
                }
                return;
            }
        }
        stats.put("health", stats.get("health") - damage);
        if (stats.get("health") <= 0) {
            isDead = true;
            upgrades = new ArrayList<>();
            for (Player p : enemies) {
                List<Player> newEnemies = new ArrayList<>(p.getEnemies());
                newEnemies.remove(this);
                p.setEnemies(newEnemies);
            }
        }
        healthbar.reduceHealth(damage);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        Player player = (Player) obj;
        return player.getPlayerNum() == playerNum;
    }
}
