package org.rmc.framework.inputcontrol;

import org.rmc.framework.base.BaseScreen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;

public abstract class InputGamepadScreen extends BaseScreen implements ControllerListener {

    protected InputGamepadScreen() {
        super();
        Controllers.clearListeners();
        Controllers.addListener(this);
    }

    @Override
    public void connected(Controller controller) {
        // check if controller is connected
    }

    @Override
    public void disconnected(Controller controller) {
        // check if controller is disconnected
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

}
