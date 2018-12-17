package Players;

import GameObjects.GameState;
import GameObjects.UpgradeCard;
import Upgrades.FortifiedTower;
import Upgrades.Stickman;
import Upgrades.Upgrade;
import processing.core.PApplet;
import processing.core.PImage;
import sun.text.resources.cldr.ia.FormatData_ia;

import java.util.*;

public class Enemy extends Player {

    private static final int INTERVAL = 50;

    private List<UpgradeCard> upgradeCards;
    private int maxSteps, moveCount;

    public Enemy(PApplet app, int x, int y, int colour, PImage img, int playerNum,
                 List<UpgradeCard> upgradeCards, int maxSteps) {
        super(app, x, y, colour, img, playerNum);
        this.upgradeCards = upgradeCards;
        this.maxSteps = maxSteps;
        moveCount = 0;
    }

    public Move getMove(GameState gameState, int step) {
        if (getEnemies() == null || moveCount++ < getPlayerNum() * 10) return null;
        moveCount = 0;
        float coins;
        if (gameState == null)
            coins = getCoins();
        else
            coins = gameState.getCoins();
        float maxEval = Integer.MIN_VALUE;
        Move bestMove = null;
        List<Move> moves = new ArrayList<>(getPossibleMoves(coins));
        for (Move move : moves) {
            if (gameState == null) {
                if (move == null)
                    gameState = getInitialGameState(null);
                else
                    gameState = getInitialGameState(move.getTarget());
            }
            GameState nextGameState = getNextGameState(move, gameState);
            float eval;
            if (step < maxSteps) {
                Move nextMove = getMove(nextGameState, step + 1);
                eval = getNextGameState(nextMove, gameState).getEvaluation();
            } else {
                eval = nextGameState.getEvaluation();
            }
            if (maxEval < eval || (maxEval == eval && new Random().nextInt(2) == 1)) {
                maxEval = eval;
                bestMove = move;
            }
        }
        return bestMove;
    }

    public GameState getInitialGameState(Player target) {
        float constant = 1;
        float health = getHealth();
        float attack = 0;
        float dexterity = 0;
        float range = 0;
        for (Stickman s : getPlayerStickmen(this)) {
            health += s.getHealth();
            attack += s.getDamage();
            dexterity += s.getDexterity();
            range += s.getWeaponEquipped().getRange();
        }
        float enemyHealth = 0;
        List<Stickman> enemyStickmen = new ArrayList<>();
        if (target == null) {
            constant = getEnemies().size();
            for (Player e : getEnemies()) {
                enemyHealth += e.getHealth() / constant;
                enemyStickmen.addAll(new ArrayList<>(getPlayerStickmen(e)));
            }
        } else {
            enemyHealth = target.getHealth();
            enemyStickmen = new ArrayList<>(getPlayerStickmen(target));
        }
        float enemyAttack = 0;
        float enemyDexterity = 0;
        float enemyRange = 0;
        for (Stickman s : enemyStickmen) {
            health += s.getHealth() / constant;
            attack += s.getDamage() / constant;
            dexterity += s.getDexterity() / constant;
            dexterity += s.getWeaponEquipped().getRange() / constant;
        }
        float coins = getCoins();
        return new GameState(health, attack, dexterity, range, enemyHealth, enemyAttack, enemyDexterity, enemyRange, coins);
    }

    private GameState getNextGameState(Move move, GameState currentGameState) {
        float health = currentGameState.getHealth();
        float attack = currentGameState.getAttack();
        float dexterity = currentGameState.getDexterity();
        float range = currentGameState.getRange();
        float enemyHealth = currentGameState.getEnemyHealth();
        float enemyAttack = currentGameState.getEnemyAttack();
        float enemyDexterity = currentGameState.getEnemyDexterity();
        float enemyRange = currentGameState.getEnemyRange();
        float coins = currentGameState.getCoins();
        if (move == null) {
            return new GameState(getHealthAfterMove(this, health, null), attack, dexterity, range,
                    enemyHealth, enemyAttack, enemyDexterity, enemyRange, coins);
        }
        return new GameState(getHealthAfterMove(this, health, move.getTarget()), getAttackAfterMove(move, attack),
                getDexterityAfterMove(move, dexterity), getRangeAfterMove(move, range), getHealthAfterMove(move.getTarget(), enemyHealth, this),
                enemyAttack, enemyDexterity, enemyRange, getCoinsAfterMove(coins, move));
    }

    private float getHealthAfterMove(Player player, float currHealth, Player target) {
        int totalDamage = 0, constant = 1;
        Map<Stickman, Float> stickmenDistance = new HashMap<>();
        if (target == null) {
            if (player instanceof Enemy) {
                List<Player> playerEnemies = new ArrayList<>(((Enemy) player).getEnemies());
                constant = playerEnemies.size();
                for (Player e : playerEnemies) {
                    stickmenDistance.putAll(new HashMap<>(getStickmenDistance(getPlayerStickmen(e), player)));
                }
            }
        }
        else
            stickmenDistance = new HashMap<>(getStickmenDistance(getPlayerStickmen(target), player));
        Iterator it = stickmenDistance.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if ((float) pair.getValue() == 0) {
                totalDamage += (((Stickman) pair.getKey()).getDamage() * INTERVAL)/constant;
                break;
            }
            it.remove();
        }
        return currHealth - totalDamage;
    }

    private float getAttackAfterMove(Move move, float currentAttack) {
        if (move.getUpgradeCard().getUpgrade(0, null, 0, 0) instanceof Stickman)
            currentAttack += ((Stickman) move.getUpgradeCard().getUpgrade(0, null, 0, 0)).getDamage();
        return currentAttack;
    }

    private float getDexterityAfterMove(Move move, float currentDexterity) {
        if (move.getUpgradeCard().getUpgrade(0, null, 0, 0) instanceof Stickman)
            currentDexterity += ((Stickman) move.getUpgradeCard().getUpgrade(0, null, 0, 0)).getDexterity();
        return currentDexterity;
    }

    private float getRangeAfterMove(Move move, float currentRange) {
        if (move.getUpgradeCard().getUpgrade(0, null, 0, 0) instanceof Stickman)
            currentRange += ((Stickman) move.getUpgradeCard().getUpgrade(0, null, 0, 0))
                    .getWeaponEquipped().getRange();
        return currentRange;
    }

    private List<Move> getPossibleMoves(float coins) {
        List<Move> possibleMoves = new ArrayList<>();
        for (UpgradeCard u : upgradeCards) {
            if (u.getPrice() <= coins) {
                boolean owned = false;
                if (u.getUpgrade(getColour(), null, 0, 0) instanceof FortifiedTower) {
                    for (Upgrade upgrade: getUpgrades()) {
                        if (upgrade instanceof FortifiedTower)
                            owned = true;
                    }
                }
                if (!owned) {
                    for (Player target : getEnemies()) {
                        possibleMoves.add(new Move(u, target));
                    }
                }
            }
        }
        //for not doing anything (waiting)
        possibleMoves.add(null);
        return possibleMoves;
    }

    private float getCoinsAfterMove(float currCoins, Move move) {
        return (float) (currCoins + (0.02 * INTERVAL) - move.getUpgradeCard().getPrice());
    }

    private Map<Stickman, Float> getStickmenDistance(List<Stickman> stickmen, Player player) {
        Map<Stickman, Float> stickmenDistance = new HashMap<>();
        for (Stickman s : stickmen) {
            float distance = getDistanceStickmanToTargetAfterMove(s, player);
            stickmenDistance.put(s, distance);
//            List<Float> damages;
//            if (distancesAndDamage.containsKey(distance))
//                damages = new ArrayList<>(distancesAndDamage.get(distance));
//            else
//                damages = new ArrayList<>();
//            damages.add(s.getDamage());
//            distancesAndDamage.put(distance, damages);
        }
        return stickmenDistance;
//        return distancesAndDamage;
    }

    private float getDistanceStickmanToTargetAfterMove(Stickman s, Player target) {
        float distance = 0;
        if ((s).isMoving()) {
            if (s.isMovingRight())
                distance = target.getTargetX() - s.getX() - (s.getXMoveIncrement() * INTERVAL);
            else if (s.isMovingLeft())
                distance = s.getX() - target.getTargetX() - (s.getXMoveIncrement() * INTERVAL);

            if (s.isMovingUp()) {
                if (distance == 0)
                    distance += s.getY() - target.getTargetY() - (s.getYMoveIncrement() * INTERVAL);
                else
                    distance = (float) Math.sqrt(Math.pow(distance, 2) +
                            (s.getY() - target.getTargetY() - (s.getYMoveIncrement() * INTERVAL)));
            }
            else if (s.isMovingDown()) {
                if (distance == 0)
                    distance += target.getTargetY() - s.getY() - (s.getYMoveIncrement() * INTERVAL);
                else
                    distance = (float) Math.sqrt(Math.pow(distance, 2) +
                            (target.getTargetY() - s.getY() - (s.getYMoveIncrement() * INTERVAL)));
            }
        }
        else distance = 0;
        return distance;
    }

    private List<Stickman> getPlayerStickmen(Player player) {
        List<Stickman> stickmen = new ArrayList<>();
        for (Upgrade u: player.getUpgrades()) {
            if (u instanceof Stickman) {
                stickmen.add((Stickman) u);
            }
        }
        return stickmen;
    }
}
