package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class OptionsScreen extends BaseScreen {

    private static final int MUSIC = 1;
    private static final int SOUNDS = 2;
    private static final int END = 3;

    private int optionNumber;
    private String answer;

    private Label titleLabel;
    private Label musicLabel;
    private Label soundsLabel;
    private Label footerLabel;

    @Override
    public void initialize() {
        new BaseActor(0, 0, this.mainStage);

        this.optionNumber = MUSIC;
        this.answer = null;

        Color color = Color.valueOf(MainGame.TITLE_COLOR);
        this.titleLabel = new Label("OH MUMMY AGAIN - OPTIONS", BaseGame.labelStyle);
        this.titleLabel.setColor(color);
        this.footerLabel = new Label("Press \"C\" or Fire Button to Continue", BaseGame.labelStyle);
        this.footerLabel.setColor(color);
        this.footerLabel.setVisible(false);

        color = Color.valueOf(MainGame.LEVEL_COLOR);
        this.musicLabel = new Label("BACKGROUND MUSIC (Y-N) ?", BaseGame.labelStyle);
        this.musicLabel.setColor(color);
        this.soundsLabel = new Label("SOUND EFFECTS (Y-N) ?", BaseGame.labelStyle);
        this.soundsLabel.setColor(color);
        this.soundsLabel.setVisible(false);

        this.uiTable.pad(10);
        this.uiTable.add(this.titleLabel).center().expandY();
        this.uiTable.row();
        this.uiTable.add(this.musicLabel).center().expandY();
        this.uiTable.row();
        this.uiTable.add(this.soundsLabel).center().expandY();
        this.uiTable.row();
        this.uiTable.add(this.footerLabel).center().expandY();
    }

    @Override
    public void update(float delta) {
        if (this.optionNumber == MUSIC && this.answer != null) {
            this.musicLabel.setText("BACKGROUND MUSIC (Y-N) ?  " + this.answer);
            if (this.answer.equals("Yes"))
                MainGame.setMusicOn(true);
            else
                MainGame.setMusicOn(false);
            this.answer = null;
            this.soundsLabel.setVisible(true);
            this.optionNumber++;
        } else if (this.optionNumber == SOUNDS && this.answer != null) {
            this.soundsLabel.setText("SOUND EFFECTS (Y-N)  ?  " + this.answer);
            if (this.answer.equals("Yes"))
                MainGame.setSoundsOn(true);
            else
                MainGame.setSoundsOn(false);
            this.answer = null;
            this.footerLabel.setVisible(true);
            this.optionNumber++;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.Y)
            this.answer = "Yes";
        else if (keycode == Keys.N)
            this.answer = "No";
        else if (keycode == Keys.C && this.optionNumber == END)
            BaseGame.setActiveScreen(new ScoreScreen());
        return false;
    }

}
