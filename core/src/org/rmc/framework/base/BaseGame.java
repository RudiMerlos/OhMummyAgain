package org.rmc.framework.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created when program is launched. Manages the screens that appear during the game.
 */
public abstract class BaseGame extends Game {

    // Stores reference to game; used when calling setActiveScreen method.
    private static BaseGame game;

    // BitmapFont + Color
    public static final LabelStyle labelStyle = new LabelStyle();

    // NPD + BitmapFont + Color
    public static final TextButtonStyle textButtonStyle = new TextButtonStyle();

    private static FreeTypeFontGenerator fontGenerator = null;

    public static final FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();

    /**
     * Called when game is initialized. Stores global reference to game object.
     */
    protected BaseGame() {
        game = this;
    }

    /**
     * Called when game is initialized, after Gdx.input and other objects have been initialized.
     */
    public void create() {
        // prepare for multiple classes/stages to receive discrete input
        Gdx.input.setInputProcessor(new InputMultiplexer());

        // parameters for generating a custom bitmap font
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/amstrad_cpc464.ttf"));
        fontParameters.size = 24;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 0.5f;
        fontParameters.borderColor = Color.WHITE;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = TextureFilter.Linear;
        fontParameters.magFilter = TextureFilter.Linear;

        BitmapFont customFont = setLabelStyleFont();

        NinePatch buttonPatch = new NinePatch(new Texture("images/button.png"), 24, 24, 24, 24);
        textButtonStyle.up = new NinePatchDrawable(buttonPatch);
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = Color.GRAY;
    }

    /**
     * Used to switch screens while game is running. Method is static to simplify usage.
     *
     * @param screen BaseScreen to switch
     */
    public static void setActiveScreen(BaseScreen screen) {
        game.setScreen(screen);
    }

    public static BitmapFont setLabelStyleFont() {
        BitmapFont customFont = fontGenerator.generateFont(fontParameters);
        labelStyle.font = customFont;
        return customFont;
    }

}
