package org.rmc;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Oh Mummy Again");
        config.setWindowedMode(MainGame.WIDTH, MainGame.HEIGHT);
        config.setResizable(false);
        config.setWindowIcon("images/player_icon.png");
        new Lwjgl3Application(new MainGame(), config);
    }
}
