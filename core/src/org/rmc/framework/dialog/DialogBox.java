package org.rmc.framework.dialog;

import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class DialogBox extends BaseActor {

    private Label dialogLabel;
    private static final float PADDING = 16;

    public DialogBox(float x, float y, Stage stage) {
        super(x, y, stage);

        this.loadTexture("images/dialog-translucent.png");

        this.dialogLabel = new Label(" ", BaseGame.labelStyle);
        this.dialogLabel.setWrap(true);
        this.alignTopLeft();
        this.dialogLabel.setPosition(PADDING, PADDING);
        this.setDialogSize(this.getWidth(), this.getHeight());
        this.addActor(this.dialogLabel);
    }


    public void setDialogSize(float width, float height) {
        this.setSize(width, height);
        this.dialogLabel.setWidth(width - 2 * PADDING);
        this.dialogLabel.setHeight(height - 2 * PADDING);
    }

    public void setText(String text) {
        this.dialogLabel.setText(text);
    }

    public void setFontScale(float scale) {
        this.dialogLabel.setFontScale(scale);
    }

    public void setFontColor(Color color) {
        this.dialogLabel.setColor(color);
    }

    public void setBackgroundColor(Color color) {
        this.setColor(color);
    }

    public void alignTopLeft() {
        this.dialogLabel.setAlignment(Align.topLeft);
    }

    public void alignCenter() {
        this.dialogLabel.setAlignment(Align.center);
    }

}
