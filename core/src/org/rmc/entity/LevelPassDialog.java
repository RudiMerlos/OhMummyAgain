package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LevelPassDialog extends BaseActor {

    public LevelPassDialog(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadTexture("images/dialog.png");
        this.setPosition(MainGame.WIDTH / 2 - this.getWidth() / 2,
                MainGame.HEIGHT / 2 - this.getHeight() / 1.23f);
    }

}
