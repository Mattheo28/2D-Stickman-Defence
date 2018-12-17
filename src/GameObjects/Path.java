package GameObjects;

import processing.core.PApplet;
import processing.core.PImage;

public class Path {
    private PApplet app;
    private float startX, startY, width, height, angle;
    private int trees, constant, trees2, constant2;
    private PImage tree;
    private boolean selected;

    public Path(PApplet app, float startX, float startY, float width, float height, float angle, int trees,
                int constant, int trees2, int constant2) {
        this.app = app;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.trees = trees;
        this.constant = constant;
        this.trees2 = trees2;
        this.constant2 = constant2;
        tree = app.loadImage("rsz_tree.png");
        selected = false;
    }

    public void draw() {
        app.pushMatrix();
        app.pushStyle();
        app.translate(startX, startY);
        if (angle == app.PI/2)
            app.translate(tree.height * 6/64, 0);
        app.rotate(angle);
        if (selected)
            app.fill(196, 255, 255);
        else
            app.fill(234, 183, 0);
        app.rect(0, 0, width, height);
        app.popStyle();
        app.popMatrix();
    }

    public void drawTrees() {
        app.pushMatrix();
        app.pushStyle();
        app.translate(startX, startY);
        if (angle == app.PI/2) {
            app.translate(-tree.height*6/8, 0);
            app.scale(-1, 1);
        }
        app.rotate(angle);
        for (int i = 0; i < trees; i++) {
            app.image(tree, i * 120 + constant, -tree.height);
        }
        for (int i = 0; i < trees2; i++) {
            app.pushMatrix();
            app.translate(200, 0);
            app.scale(-1, 1);
            app.image(tree, -(i * 120 + constant2), (float) (-tree.height + height*1.75));
            app.popMatrix();
        }
        app.popStyle();
        app.popMatrix();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
