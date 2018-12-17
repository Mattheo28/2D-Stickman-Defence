package Upgrades;

import GameObjects.PlayerObject;

import java.util.HashMap;

public abstract class Upgrade extends PlayerObject {
    private HashMap<String, Float> stats;

    abstract public void draw();

    public HashMap<String, Float> getStats() {
        return stats;
    }

    public void setStats(HashMap<String, Float> stats) {
        this.stats = stats;
    }
}
