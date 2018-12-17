package GameObjects;

public class GameState {
    private float health, attack, dexterity, range, enemyHealth, enemyAttack, enemyDexterity, enemyRange, coins, evaluation;

    public GameState(float health, float attack, float dexterity, float range, float enemyHealth, float enemyAttack,
                     float enemyDexterity, float enemyRange, float coins) {
        this.health = health;
        this.attack = attack;
        this.dexterity = dexterity;
        this.range = range;
        this.enemyHealth = enemyHealth;
        this.enemyAttack = enemyAttack;
        this.enemyDexterity = enemyDexterity;
        this.enemyRange = enemyRange;
        this.coins = coins;
        evaluation = evaluate();
    }

    private float evaluate() {
        if (health <= 0) return Integer.MIN_VALUE;
        if (enemyHealth <= 0) return Integer.MAX_VALUE;
        return health + attack + dexterity + coins + range/10- (enemyHealth + 1 * enemyAttack + enemyDexterity + enemyRange/10);
    }

    public float getEvaluation() {
        return evaluation;
    }

    public float getHealth() {
        return health;
    }

    public float getAttack() {
        return attack;
    }

    public float getDexterity() {
        return dexterity;
    }

    public float getRange() { return range; }

    public float getEnemyHealth() {
        return enemyHealth;
    }

    public float getEnemyAttack() {
        return enemyAttack;
    }

    public float getEnemyDexterity() {
        return enemyDexterity;
    }

    public float getEnemyRange() {
        return enemyRange;
    }

    public float getCoins() {
        return coins;
    }
}
