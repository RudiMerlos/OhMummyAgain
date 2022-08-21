package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class NewScoreDialog extends BaseActor {

    public NewScoreDialog(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadTexture("images/enter_name.png");
        this.setPosition(MainGame.WIDTH / 2 - this.getWidth() / 2,
                MainGame.HEIGHT / 2 - this.getHeight() / 1.23f);
    }

}
