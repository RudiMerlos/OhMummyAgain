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

    public static final String TITLE_COLOR = "f36110";
    public static final String SCORE_COLOR = "5171ff";

    public static final int INITIAL_LEVEL = 1;
    public static final int INITIAL_LIVES = 5;
    public static final int INITIAL_SCORE = 0;
    public static final int INITIAL_NUMBER_MUMMIES = 1;
    public static final int INITIAL_MUMMY_RANGE = 100;

    private static int level = INITIAL_LEVEL;
    private static int lives = INITIAL_LIVES;
    private static int score = INITIAL_SCORE;
    private static int numberMummies = INITIAL_NUMBER_MUMMIES;
    private static int mummyRange = INITIAL_MUMMY_RANGE;

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

    public static void incrementMummyRange() {
        mummyRange += 100;
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
