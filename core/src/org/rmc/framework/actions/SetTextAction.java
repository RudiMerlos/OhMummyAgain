package org.rmc.framework.actions;

import org.rmc.framework.dialog.DialogBox;
import com.badlogic.gdx.scenes.scene2d.Action;

public class SetTextAction extends Action {

    protected String textToDisplay;

    public SetTextAction(String text) {
        this.textToDisplay = text;
    }

    @Override
    public boolean act(float delta) {
        ((DialogBox) this.target).setText(this.textToDisplay);
        return true;
    }

}
