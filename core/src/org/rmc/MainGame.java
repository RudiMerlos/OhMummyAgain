package org.rmc;

import org.rmc.framework.base.BaseGame;
import org.rmc.screen.LevelScreen;

public class MainGame extends BaseGame {

    public static final int WIDTH = 984;
    public static final int HEIGHT = 744;

    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    private static int numberMummies = 1;
    private static int mummyRange = 100;

    private static int lives = 5;
    private static int score = 0;
    private static int level = 1;

    @Override
    public void create() {
        super.create();
        BaseGame.setActiveScreen(new LevelScreen());
    }

    public static int getNumberMummies() {
        return numberMummies;
    }

    public static void setNumberMummies(int numberMummies) {
        MainGame.numberMummies = numberMummies;
    }

    public static void incrementNumberMummies() {
        if (numberMummies < 9)
            numberMummies++;
    }

    public static void decrementNumberMummies() {
        if (numberMummies > 0)
            numberMummies--;
    }

    public static int getMummyRange() {
        return mummyRange;
    }

    public static void setMummyRange(int mummyRange) {
        MainGame.mummyRange = mummyRange;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        MainGame.lives = lives;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        MainGame.score = score;
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        MainGame.level = level;
    }

    public static void incrementLevel() {
        level++;
    }

}
