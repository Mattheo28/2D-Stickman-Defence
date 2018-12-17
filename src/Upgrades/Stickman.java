package Upgrades;

import GameObjects.Healthbar;
import GameObjects.PlayerObject;
import Players.Player;
import Weapons.Fireball;
import Weapons.Weapon;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashMap;

import static jdk.nashorn.internal.objects.NativeMath.random;
import static processing.core.PApplet.radians;
import static processing.core.PApplet.sin;

public abstract class Stickman extends Upgrade {

    private static final float MOVE_INCREMENT = (float) 1;

    private PApplet app;
    private int start = (int) random(360);
    private float x, y, t, tk, tf0, tf1, tw, unit;
    private int movingConst, colour;
    private Knee[] knees = new Knee[2];
    private Foot[] feet = new Foot[2];
    private Elbow[] elbows = new Elbow[2];
    private Hand[] hands = new Hand[2];
    private Weapon weaponEquipped;
    private boolean movingLeft, movingRight, movingUp, movingDown, isDead, isFighting, isChasing;
    private HashMap<String, Float> stats;
    private Healthbar healthbar;
    private Player owner, target;
    private PlayerObject targetToFight;

    public Stickman(PApplet app, int colour, Player owner, Weapon weaponEquipped, float x, float y, int unit) {
        this.app = app;
        this.colour = colour;
        this.owner = owner;
        this.weaponEquipped = weaponEquipped;
        this.x = x;
        this.y = y;
        this.unit = unit;
        target = null;
        isDead = isFighting = isChasing = false;
        targetToFight = null;

        for (int i = 0; i < 2; i++) {
            feet[i] = new Foot(i);
            hands[i] = new Hand(i);
        }
        for (int i = 0; i < 2; i++) {
            knees[i] = new Knee(i);
            elbows[i] = new Elbow(i);
        }
        movingConst = 0;
    }

    @Override
    public void draw() {
        if (isDead) return;
        app.pushMatrix();
        // Paint source
        app.strokeWeight(5);
        t = (app.frameCount + start) % 360;
        tk = sin (radians(t * 8));
        tf0 = sin (radians((t + 12) * 8));
        tf1 = sin (radians((t - 12) * 8));
        tw = sin (radians((t + 82) * 16));

        //Position of player
        if (movingRight)
            x += MOVE_INCREMENT + getDexterity();
        else if (movingLeft)
            x -= MOVE_INCREMENT + getDexterity();

        if (movingUp)
            y -= MOVE_INCREMENT/2.5 + getDexterity();
        else if (movingDown)
            y += MOVE_INCREMENT/2.5 + getDexterity();

        //Position of weapon
        if (weaponEquipped != null) {
            if (movingLeft)
                weaponEquipped.draw(x - unit*4, y + unit*14/3 + tw, false);
            else
                weaponEquipped.draw(x, y + unit*14/3 + tw, true);
        }
        app.translate(x, y + unit*14/3);
        app.pushMatrix();
        app.translate(0, tw * 2);
        app.pushMatrix();
        app.translate (0, (float) (-unit * 4.5));

        if (owner != null)
            healthbar.draw((float) (-unit * 2.5), -unit * 3);

        //Exclamation if fighting
        if (isFighting && owner != null) {
            app.stroke(0,255,0);
            app.strokeWeight(3);
            app.fill(0,255,0);
            app.line(0, -30, 0, -20);
            app.ellipse (0, -12, unit/4, unit/4);
            fight();
        } else if (owner != null) {
            chase();
        }

        resetColour();
        app.ellipse (0, 0, unit, unit);

        //Ninja Mask
        app.stroke(0);
        app.fill(0);
        app.line(-unit/2, 0, unit/2, 0);

        //Eyes
        app.fill(255);
        app.strokeWeight(0);
        app.ellipse (-unit/3, 0, unit/4, unit/6);
        app.ellipse (unit/3, 0, unit/4, unit/6);
        app.fill(255,0,0);
        app.strokeWeight(5);

        resetColour();

        //Movement
        app.translate (0, (float) (unit * 0.5));
        for (Elbow e:elbows) e.move();
        app.line (0, 0, 0, (float) (unit * 1.7));
        app.translate (0, (float) (unit * 1.7));
        for (Knee k:knees) k.move();
        app.popMatrix ();
        app.popMatrix ();
        app.strokeWeight(1);
        app.popMatrix();
    }

    public void setStats(HashMap<String, Float> stats) {
        this.stats = stats;
        HashMap<String, Float> temp = new HashMap<>(stats);
        temp.put("damage", temp.get("strength") + weaponEquipped.getDamage());
        temp.remove("strength");
        super.setStats(temp);
        healthbar = new Healthbar(app,getHealth() + getArmour(), unit * 5, 10);
    }

    public float getDamage() {
        if (weaponEquipped != null)
            return stats.get("strength") + weaponEquipped.getDamage();
        else
            return stats.get("strength");
    }

    public float getDexterity() { return stats.get("dexterity"); }

    public float getHealth() {
        return stats.get("health");
    }

    public float getArmour() { return stats.get("armour"); }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isMoving() {
        return movingDown || movingRight || movingUp || movingLeft;
    }

    public Weapon getWeaponEquipped() {
        return weaponEquipped;
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

    public float getTargetX() {
        return x + unit/2;
    }

    public float getTargetY() {
        return y + unit*14/3 + unit*2;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setTarget(Player target) {
        this.target = target;
        chase();
    }

    private Player getOwner() {
        return owner;
    }

    private Player getTarget() {return target;}

    private void changeTarget() {
        isFighting = false;
        targetToFight = null;
        boolean found = false;
        for (Player e : owner.getEnemies()) {
            if (!e.isDead()) {
                setTarget(e);
                found = true;
            }
        }
        if (!found) {
            stopMoving();
            isFighting = false;
        } else
            isChasing = true;

    }

    public float getXMoveIncrement() {
        return MOVE_INCREMENT + getDexterity();
    }

    public float getYMoveIncrement() {
        return (float) (MOVE_INCREMENT/2.5 + getDexterity());
    }

    public void moveRight() {
        movingRight = true;
        movingLeft = false;
        adjustMovingConst();
    }

    public void moveLeft() {
        movingLeft = true;
        movingRight = false;
        adjustMovingConst();
    }

    public void moveUp() {
        movingUp = true;
        movingDown = false;
        adjustMovingConst();
    }

    public void moveDown() {
        movingDown = true;
        movingUp = false;
        adjustMovingConst();
    }

    public void stopMovingRight() {
        movingRight = false;
        adjustMovingConst();
    }

    public void stopMovingLeft() {
        movingLeft = false;
        adjustMovingConst();
    }

    public void stopMovingUp() {
        movingUp = false;
        adjustMovingConst();
    }

    public void stopMovingDown() {
        movingDown = false;
        adjustMovingConst();
    }

    private void adjustMovingConst() {
        if (movingRight)
            movingConst = 1;
        else if (movingLeft)
            movingConst = -1;
        else
            movingConst = 0;
    }

    public void stopMoving() {
        movingConst = 0;
        movingDown = movingUp = movingRight = movingLeft = false;
    }

    public void getHit(float damage) {
        if (stats.get("armour") - damage >= 0)
            stats.put("armour", stats.get("armour") - damage);
        else if (stats.get("armour") > 0) {
            stats.put("armour", (float) 0);
            damage -= stats.get("armour");
            stats.put("health", stats.get("health") - damage);
        }
        else
            stats.put("health", stats.get("health") - damage);
        if (stats.get("health") <= 0) isDead = true;
        healthbar.reduceHealth(damage);
        if (isDead) owner.removeUpgrade(this);
    }

    public void fight() {
        if (targetToFight instanceof Player && ((Player) targetToFight).isDead())
            changeTarget();
        else if (targetToFight instanceof Stickman && ((Stickman) targetToFight).isDead()) {
            isFighting = false;
            targetToFight = null;
            return;
        }
        isFighting = true;
        weaponEquipped.setAttacking(true);
        stopMoving();
        weaponEquipped.attack(this, targetToFight);
    }

    public void chase() {
        isChasing = true;
        targetToFight = null;
        if (weaponEquipped instanceof Fireball)
            ((Fireball) weaponEquipped).reset();
        float range = weaponEquipped.getRange();
        Stickman targetStickman = getClosestStickman(target);
        Stickman otherStickman = getOtherClosestStickman();
        float xDistTarget = target.getTargetX() - x, yDistTarget = target.getTargetY() - y;
        //First priority - check if stickmen of the player we are targetting are next to us
        if (getDistanceToStickman(targetStickman) < range) {
            isChasing = false;
            targetToFight = targetStickman;
            isFighting = true;
        }
        //Second priority - check if we are next to our target player
        else if (Math.sqrt(Math.pow(xDistTarget, 2) + Math.pow(yDistTarget, 2)) < range) {
            isChasing = false;
            targetToFight = target;
            isFighting = true;
        }
        //Third priority - check if we are next to other stickmen from other players
        else if (getDistanceToStickman(otherStickman) < range) {
            isChasing = false;
            targetToFight = otherStickman;
            isFighting = true;
        }
        //Fourth priority - chase the target player
        else {
            if (Math.abs(xDistTarget) > range) {
                if (xDistTarget > 0)
                    moveRight();
                else
                    moveLeft();
            }

            if (Math.abs(yDistTarget) > range) {
                if (yDistTarget > 0)
                    moveDown();
                else
                    moveUp();
            }
        }
    }

    private Stickman getClosestStickman(Player p) {
        float minDist = Integer.MAX_VALUE;
        Stickman closest = null;
        for (Upgrade u : p.getUpgrades()) {
            if (u instanceof Stickman && ((Stickman) u).getTarget().getPlayerNum() == owner.getPlayerNum()) {
                float dist = getDistanceToStickman((Stickman) u);
                if (minDist > dist) {
                    minDist = dist;
                    closest = (Stickman) u;
                }
            }
        }
        return closest;
    }

    private Stickman getOtherClosestStickman() {
        float minDist = Integer.MAX_VALUE;
        Stickman closest = null;
        for (Player e : owner.getEnemies()) {
            Stickman stickman = getClosestStickman(e);
            float dist = getDistanceToStickman(stickman);
            if (minDist > dist) {
                minDist = dist;
                closest = stickman;
            }
        }
        return closest;
    }

    private float getDistanceToStickman(Stickman stickman) {
        if (stickman == null) return Integer.MAX_VALUE;
        float xDist = stickman.getX() - x, yDist = stickman.getY() - y;
        return (float) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
    }

    private void resetColour() {
        app.fill(colour);
        app.stroke(colour);
    }

    class Knee {
        int id, dir;
        PVector p = new PVector();
        Knee(int i) {
            id = i;
            if (i==0) dir = 1;
            else dir = -1;
        }
        void move() {
            float angle = movingConst * dir * radians(30 * tk);
            app.pushMatrix();
            app.rotate(angle);
            app.line(0, 0, 0, (float) (unit * 1.15));
            app.translate(0, (float) (unit * 1.15));
            feet[id].move();
            app.popMatrix();
        }
    }

    class Foot {
        float angle;
        int dir;
        PVector p = new PVector();
        Foot(int i) {
            if (i==0) dir = 1;
            else dir = -1;
        }
        void move() {
            if (dir==-1) angle = movingConst * radians(30*tf0 + 25);
            else angle = movingConst * radians(30*tf1 + 25);
            app.pushMatrix();
            app.rotate(angle);
            app.line(0, 0, 0, (float) (unit*1.15));
            app.popMatrix();
        }
    }

    class Elbow {
        int id, dir;
        PVector p = new PVector();
        Elbow(int i) {
            id = i;
            if (i==0) dir = 1;
            else dir = -1;
        }
        void move() {
            float angle = movingConst * -dir * radians(30 * tk);
            if (isFighting) angle = -dir * radians(30 * tk);
            app.pushMatrix ();
            app.rotate(angle);
            app.line(0, 0, 0, (float) (unit * 1.1));
            app.translate (0, (float) (unit * 1.1));
            hands[id].move();
            app.popMatrix();
        }
    }

    class Hand {
        float angle;
        int dir;
        PVector p = new PVector();
        Hand(int i) {
            if (i==0) dir = 1;
            else dir = -1;
        }
        void move() {
            if (dir==-1) angle = movingConst * -radians(30*tf0 + 25);
            else angle = movingConst * -radians(30*tf1 + 25);
            app.pushMatrix();
            app.rotate(angle);
            app.line(0, 0, 0, (float) (unit*1.1));
            app.popMatrix();
        }
    }
}
