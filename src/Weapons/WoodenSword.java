package Weapons;

import processing.core.PApplet;

public class WoodenSword extends Weapon {

    private PApplet app;
    private float unit;

    public WoodenSword(PApplet app, float unit) {
        super(app, unit, "Wooden Sword", 5, 30, 1, (float) 44.5);
        this.app = app;
        this.unit = unit;
    }

    @Override
    public void draw(float x, float y, boolean faceRight) {
        float xSource = unit/2;
        float ySource = -unit*2;
        app.pushMatrix();
        app.translate(x, y);
        app.fill(130, 82, 1);
        app.stroke(130, 82, 1);
        app.strokeWeight(4);
        app.rotate(super.getAngle());
        if (!isAttacking())
            app.translate(super.getNewX(xSource), super.getNewY());
        app.line(xSource, ySource, xSource, ySource - 20);
        app.line(xSource - 5, ySource - 4, xSource + 5, ySource - 4);
        app.fill(255);
        app.noStroke();
        app.popMatrix();
    }
}
