package GameObjects;

import Players.Player;
import Upgrades.*;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpgradeCard extends Button {

    private PApplet app;
    private Upgrade upgrade;
    private int price;
    private float x, y, height, width;

    public UpgradeCard(PApplet app, Upgrade upgrade, int price, float x, float y, float width, float height, int colour) {
        super(app, (int) x, (int) y, (int) width, (int) height, "", colour);
        this.app = app;
        this.upgrade = upgrade;
        this.price = price;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public void draw() {
        super.draw();
        app.pushMatrix();
        app.pushStyle();
        upgrade.draw();
        app.noStroke();
        app.textSize(28);
        app.fill(0);
        app.text(price, width - 60, y + 30);
        HashMap<String, Float> stats = new HashMap<>(upgrade.getStats());
        Iterator it = stats.entrySet().iterator();
        int count = 1;
        app.textSize(14);
        app.textAlign(PConstants.LEFT);
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            app.text(pair.getKey() + ": ", width/3, y + 20 * count);
            if (pair.getKey().equals("dexterity"))
                app.text(Math.round((float) pair.getValue() * 100) + "", width/3 + 70, y + 20 * count++);
            else
                app.text(Math.round((float) pair.getValue()) + "", width/3 + 70, y + 20 * count++);
            it.remove();
        }
        if (upgrade instanceof Stickman) {
            app.text("range: ", width/3, y + 20 * count);
            app.text((int) ((Stickman) upgrade).getWeaponEquipped().getRange(), width/3 + 70, y + 20 * count++);
            app.text("rest: ", width/3, y+ 20 * count);
            app.text(((Stickman) upgrade).getWeaponEquipped().getRest(), width/3 + 70, y + 20 * count);
        }
        app.fill(255, 255, 0);
        app.ellipse(width - 20, y + 20, 20, 20);
        app.popStyle();
        app.popMatrix();
    }

    public Upgrade getUpgrade(int colour, Player owner, float x, float y) {
        if (upgrade instanceof StickmanLevel1)
            return new StickmanLevel1(app, colour, owner, x, y);
        else if (upgrade instanceof StickmanLevel2)
            return new StickmanLevel2(app, colour, owner, x, y);
        else if (upgrade instanceof StickmanLevel3)
            return new StickmanLevel3(app, colour, owner, x, y);
        else if (upgrade instanceof StickmanLevel4)
            return new StickmanLevel4(app, colour, owner, x, y);
        else if (upgrade instanceof StickmanLevel5)
            return new StickmanLevel5(app, colour, owner, x, y);
        else if (upgrade instanceof FortifiedTower)
            return new FortifiedTower(app, owner, x, y);
        return null;
    }

    public int getPrice() {
        return price;
    }
}
