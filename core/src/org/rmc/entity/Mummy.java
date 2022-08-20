package org.rmc.entity;

import java.util.ArrayList;
import java.util.List;
import static org.rmc.MainGame.*;
import org.rmc.entity.block.Block;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Mummy extends BaseActor {

    private List<Animation<TextureRegion>> animations;
    private Stage stage;
    private float facingAngle;
    private Player player;
    private float range;

    private int crossChange;
    private static final int MAX_CROSS_CHANGE = 10;

    public Mummy(float x, float y, Stage stage, Player player, float range, int direction) {
        super(x, y, stage);
        this.stage = stage;
        this.player = player;
        this.range = range;

        int rows = 4;
        int cols = 2;
        Texture texture = new Texture(Gdx.files.internal("images/mummy.png"), true);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        float frameDuration = 0.12f;

        this.animations = new ArrayList<>(4);
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();

        for (int c = 0; c < cols; c++)
            textureArray.add(temp[2][c]);
        this.animations.add(NORTH,
                new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP));

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[0][c]);
        this.animations.add(EAST,
                new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP));

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[3][c]);
        this.animations.add(SOUTH,
                new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP));

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[1][c]);
        this.animations.add(WEST,
                new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP));

        switch (direction) {
            case EAST:
                this.facingAngle = 0;
                break;
            case WEST:
                this.facingAngle = 180;
                break;
            case NORTH:
                this.facingAngle = 90;
                break;
            default:
                break;
        }
        this.setAnimation(this.animations.get(direction));

        this.setBoundaryRectangle();
        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(140);
        this.setDeceleration(BaseActor.MAX_DECELERATION);

        this.crossChange = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (BaseActor solid : BaseActor.getList(this.stage, Solid.class))
            this.preventOverlap(solid);

        for (BaseActor block : BaseActor.getList(this.stage, Block.class))
            this.preventOverlap(block);

        for (BaseActor actor : BaseActor.getList(this.stage, CrossPoint.class)) {
            CrossPoint cross = (CrossPoint) actor;
            if (this.overlaps(cross, 0.05f))
                this.moveMummy(cross);
        }

        this.accelerateAtAngle(this.facingAngle);
        this.applyPhysics(delta);
    }

    private void moveMummy(CrossPoint cross) {
        float distance;

        if (this.player != null)
            distance = (float) Math.sqrt(Math.pow(this.player.getX() - this.getX(), 2)
                    + Math.pow(this.player.getY() - this.getY(), 2));
        else
            distance = this.range;

        if (distance < this.range) {
            this.moveToPlayer(cross);
        } else {
            if (this.crossChange > 0)
                this.crossChange--;
            else {
                this.moveRandom(cross);
                this.crossChange = MathUtils.random(MAX_CROSS_CHANGE);
            }
        }
    }

    private void moveToPlayer(CrossPoint cross) {
        float xRange = Math.abs(this.player.getX() - this.getX());
        float yRange = Math.abs(this.player.getY() - this.getY());

        if (xRange > yRange) {
            if (this.player.getX() > this.getX() && cross.getRoute()[EAST]) {
                this.facingAngle = 0;
                this.setAnimation(this.animations.get(EAST));
            } else if (this.player.getX() < this.getX() && cross.getRoute()[WEST]) {
                this.facingAngle = 180;
                this.setAnimation(this.animations.get(WEST));
            }
        } else {
            if (this.player.getY() > this.getY() && cross.getRoute()[NORTH]) {
                this.facingAngle = 90;
                this.setAnimation(this.animations.get(NORTH));
            } else if (this.player.getY() < this.getY() && cross.getRoute()[SOUTH]) {
                this.facingAngle = 270;
                this.setAnimation(this.animations.get(SOUTH));
            }
        }
    }

    private void moveRandom(CrossPoint cross) {
        int randomDirection = MathUtils.random(NORTH, WEST);
        while (!cross.getRoute()[randomDirection])
            randomDirection = MathUtils.random(NORTH, WEST);

        switch (randomDirection) {
            case NORTH:
                this.facingAngle = 90;
                this.setAnimation(this.animations.get(NORTH));
                break;
            case EAST:
                this.facingAngle = 0;
                this.setAnimation(this.animations.get(EAST));
                break;
            case SOUTH:
                this.facingAngle = 270;
                this.setAnimation(this.animations.get(SOUTH));
                break;
            default: // WEST
                this.facingAngle = 180;
                this.setAnimation(this.animations.get(WEST));
        }
    }

}
