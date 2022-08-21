package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameOver extends BaseActor {

    public GameOver(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadAnimationFromSheet("images/game_over.png", 1, 20, 0.2f, false);
        this.setPosition(MainGame.WIDTH / 2 - this.getWidth() / 2,
                MainGame.HEIGHT / 2 - this.getHeight() / 1.23f);
    }

}
