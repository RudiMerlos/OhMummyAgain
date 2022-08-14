package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Footprint extends BaseActor {

    private Animation<TextureRegion> northLeft;
    private Animation<TextureRegion> northRight;
    private Animation<TextureRegion> southLeft;
    private Animation<TextureRegion> southRight;
    private Animation<TextureRegion> eastLeft;
    private Animation<TextureRegion> eastRight;
    private Animation<TextureRegion> westLeft;
    private Animation<TextureRegion> westRight;

    private int direction;
    private boolean stepLeft;

    public Footprint(float x, float y, Stage stage, boolean stepLeft) {
        super(x, y, stage);
        Texture texture = new Texture(Gdx.files.internal("images/footprints.png"), true);
        int frameWidth = texture.getWidth() / 8;
        int frameHeight = texture.getHeight();
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();

        textureArray.add(temp[0][0]);
        this.northLeft = new Animation<>(1, textureArray);

        textureArray.clear();
        textureArray.add(temp[0][1]);
        this.northRight = new Animation<>(1, textureArray);

        textureArray.clear();
        textureArray.add(temp[0][2]);
        this.southLeft = new Animation<>(1, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        textureArray.add(temp[0][3]);
        this.southRight = new Animation<>(1, textureArray);

        textureArray.clear();
        textureArray.add(temp[0][4]);
        this.eastLeft = new Animation<>(1, textureArray);

        textureArray.clear();
        textureArray.add(temp[0][5]);
        this.eastRight = new Animation<>(1, textureArray);

        textureArray.clear();
        textureArray.add(temp[0][6]);
        this.westLeft = new Animation<>(1, textureArray);

        textureArray.clear();
        textureArray.add(temp[0][7]);
        this.westRight = new Animation<>(1, textureArray);

        this.direction = MainGame.SOUTH;
        this.stepLeft = stepLeft;
        this.setAnimationPaused(true);
        this.setVisible(false);
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        switch (this.direction) {
            case MainGame.NORTH:
                this.setAnimation(this.stepLeft ? this.northLeft : this.northRight);
                break;
            case MainGame.SOUTH:
                this.setAnimation(this.stepLeft ? this.southLeft : this.southRight);
                break;
            case MainGame.EAST:
                this.setAnimation(this.stepLeft ? this.eastLeft : this.eastRight);
                break;
            case MainGame.WEST:
                this.setAnimation(this.stepLeft ? this.westLeft : this.westRight);
                break;
            default:
                break;
        }
    }

}
