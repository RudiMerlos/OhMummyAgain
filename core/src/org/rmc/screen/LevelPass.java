package org.rmc.screen;

import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import com.badlogic.gdx.Input.Keys;

public class LevelPass extends BaseScreen {

    @Override
    public void initialize() {
        BaseActor screen = new BaseActor(0, 0, this.mainStage);
        screen.loadTexture("images/level_pass.png");
    }

    @Override
    public void update(float delta) {
        // not used
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.C)
            BaseGame.setActiveScreen(new LevelScreen());
        return false;
    }

}
