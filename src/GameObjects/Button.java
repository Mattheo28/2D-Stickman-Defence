package GameObjects;

import processing.core.PApplet;

//Class representing a button for the user to make choices
public class Button {
    private PApplet app;
    // Position of button
    private int rectX, rectY;
    // Diameter of rect
    private int rectWidth, rectHeight, rectColor, rectHighlight;
    private boolean rectOver, shouldHighlight, clickable;
    private String text;

    public Button(PApplet app, int rectX, int rectY, int rectWidth, int rectHeight, String text, int rectColor) {
        //initialise button colours
        this.app = app;
        this.rectX = rectX;
        this.rectY = rectY;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.text = text;
        this.rectColor = rectColor;
        rectHighlight = app.color(81);
        rectOver = false;
        shouldHighlight = true;
        clickable = true;
    }

    public void draw() {
        update();

        app.pushMatrix();
        app.pushStyle();
        app.noStroke();
        //if the mouse is over the button, change its colour
        if (!clickable || (rectOver && shouldHighlight))
            app.fill(rectHighlight);
        else
            app.fill(rectColor);

        app.rect(rectX, rectY, rectWidth, rectHeight);
        app.textSize(20);
        app.fill(0);
        app.textAlign(app.CENTER, app.CENTER);
        app.text(text, rectX + rectWidth/2, rectY + rectHeight/2);
        app.popStyle();
        app.popMatrix();
    }

    private void update() {
        rectOver = overRect();
    }

    //check if the mouse is over the button
    public boolean overRect()  {
        return app.mouseX >= rectX && app.mouseX <= rectX + rectWidth &&
                app.mouseY >= rectY && app.mouseY <= rectY + rectHeight;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isClickable() {
        return clickable;
    }
}