package Weapons;

import GameObjects.PlayerObject;
import Players.Player;
import Upgrades.Stickman;
import processing.core.PApplet;
import processing.core.PImage;

public class Fireball extends Weapon {

    private static final int SPEED = 3;

    private PApplet app;
    private float unit, xStart, yStart, xInc, yInc, margin;
    private PImage img;

    public Fireball(PApplet app, float unit) {
        super(app, unit, "Fireball", 30, 600, 2, 0);
        this.app = app;
        this.unit = unit;
        xInc = 0;
        yInc = 0;
        margin = 20;
        img = app.loadImage("fireball.png");
    }

    @Override
    public void draw(float x, float y, boolean faceRight) {
        xStart = x + unit/2;
        yStart = y + unit*2;
        float xSource = unit/2;
        float ySource = -unit*2;
        app.pushMatrix();
        app.translate(x, y);
        app.fill(0);
        app.stroke(0);
        app.strokeWeight(4);
        if (!faceRight) {
            app.scale(-1, 1);
            app.translate((float) (-unit*3.5), 0);
        }
        app.fill(130, 82, 1);
        app.line(xSource * 3, 0, xSource * 3, ySource*2);
        if (!isAttacking()) {
            app.popMatrix();
            return;
        }
        // draw the fireball image
        app.translate(xInc, yInc);
        app.image(img, -img.width/7, -img.height);
        app.fill(255);
        app.noStroke();
        app.popMatrix();
    }

    @Override
    public void attack(Stickman owner, PlayerObject playerObject) {
        float yConst = 0;
        if (playerObject instanceof Player && ((Player) playerObject).getPlayerNum() > 2)
            yConst = 200;
        if (xStart + xInc < playerObject.getTargetX() - margin) {
            xInc += SPEED;
            if (yStart + yInc < playerObject.getTargetY() + yConst - margin) {
                yInc += SPEED/2.5;
            } else if (yStart + yInc > playerObject.getTargetY() + yConst + margin) {
                yInc -= SPEED/2.5;
            } else
                yInc += 0;
        }
        else if (xStart + xInc > playerObject.getTargetX() + margin) {
            xInc -= SPEED;
            if (yStart + yInc < playerObject.getTargetY() + yConst - margin) {
                yInc += SPEED/2.5;
            } else if (yStart + yInc > playerObject.getTargetY() + yConst + margin) {
                yInc -= SPEED/2.5;
            } else
                yInc += 0;
        }
        else {
            if (yStart + yInc < playerObject.getTargetY() + yConst - margin) {
                yInc += SPEED/2.5;
            } else if (yStart + yInc > playerObject.getTargetY() + yConst + margin) {
                yInc -= SPEED/2.5;
            } else {
                if (updateRest())
                    playerObject.getHit(getDamage());
                reset();
            }
        }
    }

    public void reset() {
        xInc = 0;
        yInc = 0;
    }
}