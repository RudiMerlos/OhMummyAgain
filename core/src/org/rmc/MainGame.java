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

    private static int numberMummies = 8;
    private static int mummySpeed = 140;
    private static int mummyRange = 200;

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

    public static int getMummySpeed() {
        return mummySpeed;
    }

    public static void setMummySpeed(int mummySpeed) {
        MainGame.mummySpeed = mummySpeed;
    }

    public static int getMummyRange() {
        return mummyRange;
    }

    public static void setMummyRange(int mummyRange) {
        MainGame.mummyRange = mummyRange;
    }

}
