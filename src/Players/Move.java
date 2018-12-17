package Players;

import GameObjects.UpgradeCard;

public class Move {

    private UpgradeCard upgradeCard;
    private Player target;

    public Move(UpgradeCard upgradeCard, Player target) {
        this.upgradeCard = upgradeCard;
        this.target = target;
    }

    public UpgradeCard getUpgradeCard() {
        return upgradeCard;
    }

    public Player getTarget() {
        return target;
    }
}


