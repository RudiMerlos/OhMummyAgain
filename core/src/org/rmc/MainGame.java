package org.rmc;

import org.rmc.framework.base.BaseGame;
import org.rmc.screen.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MainGame extends BaseGame {

    public static final int WIDTH = 984;
    public static final int HEIGHT = 744;

    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    public static final String TITLE_COLOR = "f36110";
    public static final String SCORE_COLOR = "5171ff";
    public static final String LEVEL_COLOR = "dcc93a";

    public static final int INITIAL_LEVEL = 1;
    public static final int INITIAL_LIVES = 5;
    public static final int INITIAL_SCORE = 0;
    public static final int INITIAL_NUMBER_MUMMIES = 1;
    public static final int INITIAL_MUMMY_RANGE = 80;

    private static int level = INITIAL_LEVEL;
    private static int lives = INITIAL_LIVES;
    private static int score = INITIAL_SCORE;
    private static int numberMummies = INITIAL_NUMBER_MUMMIES;
    private static int mummyRange = INITIAL_MUMMY_RANGE;

    private static int[] scores = {2500, 2000, 1500, 1000, 0};
    private static String[] scoreNames =
            {"Stupendous!", "Excelent!", "Very Good!", "Quite Good", "Not Bad"};

    public static boolean winGame = false;

    @Override
    public void create() {
        super.create();
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        BaseGame.setActiveScreen(new MenuScreen());
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
        mummyRange += INITIAL_MUMMY_RANGE;
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

    public static String getScoreRecord(int pos) {
        return getScoreString(scores[pos]) + " " + scoreNames[pos];
    }

    public static void setScoreRecord(int s, String n) {
        int i;
        for (i = 0; i < 5; i++) {
            if (s > scores[i])
                break;
        }
        for (int j = 4; j > i; j--) {
            scores[j] = scores[j - 1];
            scoreNames[j] = scoreNames[j - 1];
        }
        scores[i] = s;
        scoreNames[i] = n;
    }

    public static String getScoreString(int s) {
        String scoreStr = String.valueOf(s);
        StringBuilder score = new StringBuilder(scoreStr);
        for (int i = 0; i < 5 - scoreStr.length(); i++)
            score.insert(0, '0');
        return score.toString();
    }

    public static int getMinScore() {
        return scores[scores.length - 1];
    }

}
