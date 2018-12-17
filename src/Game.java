import GameObjects.Button;
import GameObjects.Path;
import GameObjects.UpgradeCard;
import Players.Enemy;
import Players.Move;
import Players.Player;
import Upgrades.*;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

public class Game extends PApplet {

    private final int BUTTON_BASE_COLOR = color(255), PLAYER_ONE_COLOUR = color(255,0,0),
            ENEMY_ONE_COLOUR = color(0, 0, 255), ENEMY_TWO_COLOUR = color(50, 169, 75),
            ENEMY_THREE_COLOUR = color(255, 255, 0);

    // These are determined by the size of the map above
    private int numOfEnemies, enemyMaxSteps;
    private Player player;
    private ArrayList<Enemy> enemies;
    private boolean showingMenu, showingDiffulty;
    private int lastSelected;
    private Button btn_one, btn_two, btn_three, btn_easy, btn_medium, btn_hard, btn_menu;
    private List<Path> paths;
    private List<UpgradeCard> upgradeCards;
    private PImage playerImg, titleImg;

    public void settings() {
        fullScreen();
        numOfEnemies = 0;
        enemyMaxSteps = 0;
        btn_one = new Button(this, displayWidth/2 - 170, displayHeight/3 + 100, 340, 80,
                "ONE", BUTTON_BASE_COLOR);
        btn_two = new Button(this, displayWidth/2 - 170, displayHeight/3 + 200, 340, 80,
                "TWO", BUTTON_BASE_COLOR);
        btn_three = new Button(this, displayWidth/2 - 170, displayHeight/3 + 300, 340, 80,
                "THREE", BUTTON_BASE_COLOR);
        btn_menu = new Button(this, displayWidth/2 - 170, displayHeight/3 + 150, 340, 80,
                "MENU", BUTTON_BASE_COLOR);
        showingMenu = true;
        enemies = new ArrayList<>();
        paths = new ArrayList<>();
        upgradeCards = new ArrayList<>();
        playerImg = loadImage("tower1.png");
        titleImg = loadImage("title.v4.png");
    }

    // Display the menu where the user selects what difficulty the AI should play at
    private void setupDifficultyScreen() {
        btn_easy = new Button(this, displayWidth/2 - 170, displayHeight/3 + 100, 340, 80,
                "EASY", BUTTON_BASE_COLOR);
        btn_medium = new Button(this, displayWidth/2 - 170, displayHeight/3 + 200, 340, 80,
                "MEDIUM", BUTTON_BASE_COLOR);
        btn_hard = new Button(this, displayWidth/2 - 170, displayHeight/3 + 300, 340, 80,
                "HARD", BUTTON_BASE_COLOR);
        showingDiffulty = true;
    }

    private void setupGame() {
        switch (numOfEnemies) {
            case 1:
                setupOneEnemyGame();
                break;
            case 2:
                setupTwoEnemyGame();
                break;
            case 3:
                setupThreeEnemyGame();
                break;
        }
        upgradeCards.add(new UpgradeCard(this, new StickmanLevel1(this, PLAYER_ONE_COLOUR, null, 20, 120),
                20, 0, 100, player.getX() - 50, 130, BUTTON_BASE_COLOR));
        upgradeCards.add(new UpgradeCard(this, new StickmanLevel2(this, PLAYER_ONE_COLOUR, null, 20, 270),
                30, 0, 250, player.getX() - 50, 130, BUTTON_BASE_COLOR));
        upgradeCards.add(new UpgradeCard(this, new StickmanLevel3(this, PLAYER_ONE_COLOUR, null, 20, 420),
                40, 0, 400, player.getX() - 50, 130, BUTTON_BASE_COLOR));
        upgradeCards.add(new UpgradeCard(this, new StickmanLevel4(this, PLAYER_ONE_COLOUR, null, 20, 570),
                50, 0, 550, player.getX() - 50, 130, BUTTON_BASE_COLOR));
        upgradeCards.add(new UpgradeCard(this, new StickmanLevel5(this, PLAYER_ONE_COLOUR, null, 20, 720),
                70, 0, 700, player.getX() - 50, 130, BUTTON_BASE_COLOR));
        upgradeCards.add(new UpgradeCard(this, new FortifiedTower(this, null, 20, 870),
                100, 0, 850, player.getX() - 50, 130, BUTTON_BASE_COLOR));
        lastSelected = 0;
    }

    private void setupOneEnemyGame() {
        PImage img2 = loadImage("tower2.png");
        player = new Player(this, 350, displayHeight/2 - playerImg.height, PLAYER_ONE_COLOUR, playerImg, 1);
        Enemy enemy = new Enemy(this, displayWidth - img2.width, displayHeight/2 - playerImg.height,
                ENEMY_ONE_COLOUR, img2, 2, upgradeCards, enemyMaxSteps);
        enemy.setEnemies(new ArrayList<>(Arrays.asList(player)));
        enemies.add(enemy);
        paths.add(new Path(this, 350 + playerImg.width/2, displayHeight/2,
                displayWidth - img2.width - 250, 100, 0, 8, 200, 8, 200));
        paths.get(lastSelected).setSelected(true);
        player.setEnemies(new ArrayList<>(enemies));
    }

    private void addTwoEnemies() {
        PImage img2 = loadImage("tower2.png");
        PImage img3 = loadImage("tower3.png");
        player = new Player(this, 350, 0, PLAYER_ONE_COLOUR, playerImg, 1);
        Enemy enemy = new Enemy(this, displayWidth - img2.width, 0, ENEMY_ONE_COLOUR, img2,
                2, upgradeCards, enemyMaxSteps);
        enemies.add(enemy);
        Enemy enemy2 = new Enemy(this, 350, displayHeight - img2.height, ENEMY_TWO_COLOUR, img3,
                3, upgradeCards, enemyMaxSteps);
        enemies.add(enemy2);
        paths.add(new Path(this, 350 + playerImg.width/2, playerImg.height,
                enemy.getX() - player.getX(), 100, 0, 8, 200, 5, 300));
        paths.add(new Path(this, 390 + playerImg.width/2, (float) (playerImg.height * 1.75) - 160,
                displayWidth - img2.width - 950, 90, PI/2, 5, 80, 4, 20));
        paths.add(new Path(this, 205 + img2.width, (float) (displayHeight - img2.height*1.25),
                (float) Math.hypot(enemies.get(0).getX() - enemies.get(1).getX() + img2.width*3/5,
                        enemies.get(0).getY() - enemies.get(1).getY()) - img2.width, 100, (float) (-PI/7.5),
                0, 0, 0, 0));
        paths.get(lastSelected).setSelected(true);
    }

    private void setupTwoEnemyGame() {
        addTwoEnemies();
        enemies.get(0).setEnemies(new ArrayList<>(Arrays.asList(player, enemies.get(1))));
        enemies.get(1).setEnemies(new ArrayList<>(Arrays.asList(player, enemies.get(0))));
        player.setEnemies(new ArrayList<>(enemies));
    }

    private void setupThreeEnemyGame() {
        addTwoEnemies();
        PImage img = loadImage("tower4.png");
        Enemy enemy2 = enemies.get(1);
        Enemy enemy3 = new Enemy(this, displayWidth - img.width, displayHeight - img.height,
                ENEMY_THREE_COLOUR, img, 4, upgradeCards, enemyMaxSteps);
        paths.add(new Path(this, 315 + img.width, img.height + 20,
                (float) Math.hypot(enemy3.getX() - player.getX(), enemy3.getY() - player.getY()) - img.width,
                100, (float) (PI/7.5), 0, 0, 0, 0));
        Collections.swap(paths, 2, 3);
        enemies.add(enemy3);
        paths.add(new Path(this, 350 + img.width/2, (float) (displayHeight - img.height * 1.12),
                enemy3.getX() - enemy2.getX(), 100, 0, 5, 420, 8, 100));
        paths.add(new Path(this, displayWidth - img.width/3, (float) (img.height * 1.75) - 160,
                displayWidth - img.width - 950, 90, PI/2, 3, 200, 5, 0));
        enemies.get(0).setEnemies(new ArrayList<>(Arrays.asList(player, enemies.get(1), enemies.get(2))));
        enemy2.setEnemies(new ArrayList<>(Arrays.asList(player, enemies.get(0), enemies.get(2))));
        enemy3.setEnemies(new ArrayList<>(Arrays.asList(player, enemies.get(0), enemies.get(1))));
        player.setEnemies(new ArrayList<>(enemies));
    }

    public void draw() {
        // Walls are depicted as black
        background(239, 228, 176);
        noStroke();

        if(showingMenu) {
            textAlign(CENTER);
            image(titleImg, displayWidth/2 - titleImg.width/2, 20);
            btn_one.draw();
            btn_two.draw();
            btn_three.draw();
            fill(0);
            textSize(45);
            text("Number of enemies:", displayWidth/2, displayHeight/3);
            text("Build an army of stickmen, upgrade your defences and ", displayWidth/2, displayHeight/3 + 500);
            text("destroy the enemy towers! Last tower standing wins.", displayWidth/2, displayHeight/3 + 550);
            return;
        }
        if (showingDiffulty) {
            image(titleImg, displayWidth/2 - titleImg.width/2, 20);
            fill(0);
            text("Difficulty:", displayWidth/2, displayHeight/3);
            btn_easy.draw();
            btn_medium.draw();
            btn_hard.draw();
            text("Use the arrow keys (up/down) to select", displayWidth/2, displayHeight/3 + 500);
            text("the path you spawn stickmen on", displayWidth/2, displayHeight/3 + 550);
            return;
        }
        int deaths = 0;
        for (int i = 0; i < paths.size(); i++) {
            if (i != lastSelected)
                paths.get(i).draw();
        }
        paths.get(lastSelected).draw();
        player.draw();
        for (Enemy e : enemies) {
            if (e.isDead()) {
                deaths++;
                continue;
            }
            Move move = e.getMove(null, 0);
            if (move != null) {
                e.deductCoins(move.getUpgradeCard().getPrice());
                if (e.getPlayerNum() == 2)
                    e.addUpgrade(move.getUpgradeCard().getUpgrade(e.getColour(), e, e.getX() + playerImg.width/2,
                            e.getY() + playerImg.height), move.getTarget());
                else {
                    if (move.getUpgradeCard().getUpgrade(PLAYER_ONE_COLOUR, null, 0, 0) instanceof StickmanLevel5)
                        e.addUpgrade(move.getUpgradeCard().getUpgrade(e.getColour(), e, e.getX() + playerImg.width/2,
                                e.getY() - playerImg.height/10), move.getTarget());
                    else
                        e.addUpgrade(move.getUpgradeCard().getUpgrade(e.getColour(), e, e.getX() + playerImg.width/2,
                                e.getY()), move.getTarget());
                }
            }
            e.draw();
            for (Upgrade u : e.getUpgrades())
                u.draw();
        }
        for (GameObjects.Path p : paths) {
            p.drawTrees();
        }
        fill(191, 191, 191);
        rect(0, 0, player.getX() - 50, displayHeight);
        textSize(28);
        fill(0);
        text(Math.round(player.getCoins()) + "", player.getX() - 110, 30);
        fill(255, 255, 0);
        ellipse(player.getX() - 70, 20, 20, 20);
        for (UpgradeCard uc : upgradeCards) {
            if (uc.getPrice() > player.getCoins())
                uc.setClickable(false);
            else {
                boolean setFalse = false;
                if (uc.getUpgrade(PLAYER_ONE_COLOUR, null, 0,0) instanceof FortifiedTower) {
                    for (Upgrade u : player.getUpgrades()) {
                        if (u instanceof FortifiedTower) {
                            uc.setClickable(false);
                            setFalse = true;
                        }
                    }
                }
                if (!setFalse)
                    uc.setClickable(true);
            }
            uc.draw();
        }
        if (!player.isDead()) {
            for (Upgrade u : player.getUpgrades()) {
                u.draw();
            }
        } else
            deaths++;
        if (deaths == numOfEnemies) {
            String text = "";
            if (!player.isDead()) {
                text = "You";
            } else {
                for (Enemy e : enemies) {
                    if (!e.isDead())
                        text = "Player " + e.getPlayerNum();
                }
            }
            textSize(75);
            fill(0);
            text(text + " Won!", displayWidth/2, displayHeight/3);
            btn_menu.draw();
        }
    }

    public static void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Game" };
        PApplet.main(appletArgs);
    }

    public void mousePressed() {
        if (showingMenu) {
            if (btn_one.overRect()) {
                numOfEnemies = 1;
                showingMenu = false;
                setupDifficultyScreen();
            }
            if (btn_two.overRect()) {
                numOfEnemies = 2;
                showingMenu = false;
                setupDifficultyScreen();
            }
            if (btn_three.overRect()) {
                numOfEnemies = 3;
                showingMenu = false;
                setupDifficultyScreen();
            }
            return;
        }
        if (showingDiffulty) {
            if (btn_easy.overRect()) {
                enemyMaxSteps = 2;
                showingDiffulty = false;
                setupGame();
            }
            if (btn_medium.overRect()) {
                enemyMaxSteps = 5;
                showingDiffulty = false;
                setupGame();
            }
            if (btn_hard.overRect()) {
                enemyMaxSteps = 10;
                showingDiffulty = false;
                setupGame();
            }
            return;
        }
        if (btn_menu.overRect()) {
            settings();
            return;
        }
        for (UpgradeCard u : upgradeCards) {
            if (u.overRect() && u.isClickable()) {
                Upgrade upgrade = u.getUpgrade(PLAYER_ONE_COLOUR, player, player.getX() + playerImg.width/2,
                        player.getY() + playerImg.height);
                player.deductCoins(u.getPrice());
                player.addUpgrade(upgrade, enemies.get(lastSelected));
                if (upgrade instanceof FortifiedTower)
                    u.setClickable(false);
            }
        }
    }

    // Read keyboard for input
    public void keyPressed() {
        if (showingDiffulty || showingMenu) return;
        if (key == CODED) {
            switch (keyCode) {
                case UP :
                    paths.get(lastSelected).setSelected(false);
                    if (lastSelected == numOfEnemies - 1)
                        lastSelected = 0;
                    else
                        lastSelected++;
                    paths.get(lastSelected).setSelected(true);
                    break;
                case DOWN :
                    paths.get(lastSelected).setSelected(false);
                    if (lastSelected == 0)
                        lastSelected = numOfEnemies - 1;
                    else
                        lastSelected--;
                    paths.get(lastSelected).setSelected(true);
                    break;
            }
        }
    }
}