package org.rmc.framework.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

public abstract class BaseScreen implements Screen, InputProcessor {

    protected Stage mainStage;
    protected Stage uiStage;

    protected Table uiTable;

    protected boolean paused;

    protected BaseScreen() {
        this.mainStage = new Stage();
        this.uiStage = new Stage();

        this.uiTable = new Table();
        this.uiTable.setFillParent(true);
        this.uiStage.addActor(this.uiTable);

        this.initialize();
    }

    public abstract void initialize();

    public abstract void update(float delta);

    // Gameloop
    @Override
    public void render(float delta) {
        if (!paused) {
            // 1 - Process input
            this.mainStage.act(delta);
            this.uiStage.act(delta);

            // 2 - Update game logic
            this.update(delta);
        }

        // 3 - Render the graphics
        // clear the screen (if there is a background image it isn't necessary)
        ScreenUtils.clear(0, 0, 0, 1);

        // draw the graphics
        this.mainStage.draw();
        this.uiStage.draw();
    }

    /**
     * Useful for checking for touch-down events.
     *
     * @param e Event to check
     * @return True if event is a touch-down event, false if not
     */
    public boolean isTouchDownEvent(Event e) {
        return (e instanceof InputEvent) && ((InputEvent) e).getType().equals(Type.touchDown);
    }


    // =========================================================
    //  Screen interface methods
    // =========================================================

    /**
     * Called when this becomes the active screen in a Game.
     * Set up InputMultiplexer here, in case screen is reactivated at later time.
     */
    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(this.uiStage);
        inputMultiplexer.addProcessor(this.mainStage);
    }

    /**
     * Called when this is no longer the active screen in a Game.
     * Screen class and Stages no longer process input.
     * Other InputProcessors must be removed manually.
     */
    @Override
    public void hide() {
        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputMultiplexer.removeProcessor(this);
        inputMultiplexer.removeProcessor(this.uiStage);
        inputMultiplexer.removeProcessor(this.mainStage);
    }

    @Override
    public void dispose() {}

    @Override
    public void pause() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void resume() {}


    // =========================================================
    //  InputProcessor interface methods
    // =========================================================

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
