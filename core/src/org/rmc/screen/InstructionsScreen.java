package org.rmc.screen;

import org.rmc.framework.actions.SceneActions;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.inputcontrol.InputGamepad;
import org.rmc.framework.inputcontrol.InputGamepadScreen;
import org.rmc.framework.scene.Scene;
import org.rmc.framework.scene.SceneSegment;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class InstructionsScreen extends InputGamepadScreen {

    private Scene scene;

    @Override
    public void initialize() {
        this.scene = new Scene();
        this.mainStage.addActor(this.scene);

        this.addSegments();

        this.scene.start();
    }

    private void addSegments() {
        for (int i = 1; i <= 8; i++) {
            BaseActor screen = new BaseActor(0, 0, this.mainStage);
            screen.loadTexture("images/instructions" + i + ".png");
            screen.setVisible(false);
            this.scene.addSegment(new SceneSegment(screen, Actions.show()));
            this.scene.addSegment(new SceneSegment(screen, SceneActions.pause()));
            this.scene.addSegment(new SceneSegment(screen, Actions.hide()));
        }
    }

    @Override
    public void update(float delta) {
        if (this.scene.isSceneFinished())
            BaseGame.setActiveScreen(new ScoreScreen());
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.C)
            this.scene.loadNextSegment();
        return false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == InputGamepad.getInstance().getButtonA())
            this.scene.loadNextSegment();
        return false;
    }

}
