package GameObjects;

import processing.core.PApplet;

public class Healthbar {

    private PApplet app;
    private float health, max_health, width, height;

    public Healthbar(PApplet app, float max_health, float width, float height) {
        this.app = app;
        this.width = width;
        this.height = height;
        this.max_health = max_health;
        health = max_health;
    }

    public void draw(float x, float y) {
        app.pushMatrix();
        app.pushStyle();
        // Change color
        if (health < max_health/4)
            app.fill(255, 0, 0);
        else if (health < max_health/2)
            app.fill(255, 200, 0);
        else
            app.fill(0, 255, 0);

        // Draw bar
        app.noStroke();
        // Get fraction 0->1 and multiply it by width of bar
        float drawWidth = (health / max_health) * width;
        app.rect(x, y, drawWidth, height);

        // Outline
        app.stroke(0);
        app.strokeWeight(1);
        app.noFill();
        app.rect(x, y, width, height);
        app.popStyle();
        app.popMatrix();
    }

    public void reduceHealth(float amount) {
        health -= amount;
    }
}
