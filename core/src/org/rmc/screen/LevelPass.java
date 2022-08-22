package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.inputcontrol.InputGamepad;
import org.rmc.framework.inputcontrol.InputGamepadScreen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;

public class LevelPass extends InputGamepadScreen {

    @Override
    public void initialize() {
        BaseActor screen = new BaseActor(0, 0, this.mainStage);
        if ((MainGame.getLevel() % 10 - 1) == 0)
            screen.loadTexture("images/level_pass_live.png");
        else
            screen.loadTexture("images/level_pass_points.png");
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

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == InputGamepad.getInstance().getButtonA())
            BaseGame.setActiveScreen(new LevelScreen());
        return false;
    }

}
