package org.rmc.entity;

import java.util.ArrayList;
import java.util.List;
import static org.rmc.MainGame.*;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.inputcontrol.InputGamepad;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Player extends BaseActor {

    private List<Animation<TextureRegion>> animations;

    private Stage stage;

    private int direction;

    public Player(float x, float y, Stage stage) {
        super(x, y, stage);
        this.stage = stage;

        Texture texture = new Texture(Gdx.files.internal("images/player.png"), true);
        int rows = 4;
        int cols = 2;
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

        this.direction = SOUTH;
        this.setAnimation(this.animations.get(SOUTH));
        this.setBoundaryRectangle();
        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(200);
        this.setDeceleration(BaseActor.MAX_DECELERATION);
    }

    public int getDirection() {
        return this.direction;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.setAnimationPaused(this.getSpeed() == 0);

        // player movement controls
        for (BaseActor actor : BaseActor.getList(this.stage, Direction.class)) {
            Direction direction = (Direction) actor;
            if (this.overlaps(direction, 0.15f)) {
                this.movePlayer(direction);
            }
        }

        this.applyPhysics(delta);
    }

    private void movePlayer(Direction direction) {
        float xAxis = 0;
        float yAxis = 0;
        if (Controllers.getControllers().size > 0) {
            Controller gamepad = Controllers.getControllers().get(0);
            xAxis = gamepad.getAxis(InputGamepad.getInstance().getAxisLeftX());
            yAxis = gamepad.getAxis(InputGamepad.getInstance().getAxisLeftY());
        }
        if (this.isVisible()) {
            if ((Gdx.input.isKeyPressed(Keys.Q) || Gdx.input.isKeyPressed(Keys.UP) || yAxis <= -1)
                    && direction.getRoute()[NORTH]) {
                this.direction = NORTH;
                this.setAnimation(this.animations.get(NORTH));
                this.accelerateAtAngle(90);
            } else if ((Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.DOWN)
                    || yAxis >= 1) && direction.getRoute()[SOUTH]) {
                this.direction = SOUTH;
                this.setAnimation(this.animations.get(SOUTH));
                this.accelerateAtAngle(270);
            }
            if ((Gdx.input.isKeyPressed(Keys.O) || Gdx.input.isKeyPressed(Keys.LEFT) || xAxis <= -1)
                    && direction.getRoute()[WEST]) {
                this.direction = WEST;
                this.setAnimation(this.animations.get(WEST));
                this.accelerateAtAngle(180);
            } else if ((Gdx.input.isKeyPressed(Keys.P) || Gdx.input.isKeyPressed(Keys.RIGHT)
                    || xAxis >= 1) && direction.getRoute()[EAST]) {
                this.direction = EAST;
                this.setAnimation(this.animations.get(EAST));
                this.accelerateAtAngle(0);
            }
        }
    }

    public boolean overlapsWithOffset(BaseActor other, int offset) {
        Polygon poly1 = this.getBoundaryPolygon();
        poly1.setScale(0.001f, 0.001f);
        switch (this.direction) {
            case NORTH:
                poly1.setOrigin(poly1.getOriginX(), poly1.getOriginY() - offset);
                break;
            case SOUTH:
                poly1.setOrigin(poly1.getOriginX(), poly1.getOriginY() + offset);
                break;
            case EAST:
                poly1.setOrigin(poly1.getOriginX() - offset, poly1.getOriginY());
                break;
            case WEST:
                poly1.setOrigin(poly1.getOriginX() + offset, poly1.getOriginY());
                break;
            default:
                break;
        }
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

}
