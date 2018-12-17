package Weapons;

import GameObjects.PlayerObject;
import Upgrades.Stickman;
import processing.core.PApplet;

public abstract class Weapon {

    private PApplet app;
    private float unit;
    private String name;
    private float damage, range, angle, initialAngle, newX, newY;
    private int restPeriod, currentRest;
    private boolean movingUp, isAttacking;

    public Weapon(PApplet app, float unit, String name, float damage, float range,
                  int restPeriod, float angle) {
        this.app = app;
        this.unit = unit;
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.restPeriod = restPeriod;
        this.angle = initialAngle = angle;
        movingUp = true;
        currentRest = restPeriod;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public abstract void draw(float x, float y, boolean faceRight);

    public void attack(Stickman owner, PlayerObject playerObject) {
        if (angle > initialAngle + 0.2) movingUp = false;
        else if (angle < initialAngle - 0.2) movingUp = true;
        if (movingUp) angle += 0.02;
        else angle -= 0.02;
        if (updateRest() && playerObject != null)
            playerObject.getHit(getDamage());
    }

    public float getNewX(float initialX) {
        if (newX < initialX - 9) movingUp = true;
        else if (newX >= initialX - 2) movingUp = false;
        double constant = 0.375;
        if (movingUp) {
            newX += constant;
            newY -= constant;
        }
        else {
            newX -= constant;
            newY += constant;
        }
        return newX;
    }

    public float getNewY() {
        return newY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }

    public int getRest() {
        return restPeriod;
    }

    public boolean updateRest() {
        if (currentRest == 0) {
            currentRest = restPeriod;
            return true;
        } else {
            currentRest--;
            return false;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Weapon)) {
            return false;
        }
        Weapon weapon = (Weapon) obj;
        return weapon.getName().equals(name);
    }
}
