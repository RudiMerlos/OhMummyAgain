package org.rmc.framework.base;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 * Extends functionality of the LibGDX Actor class by adding support for textures/animation,
 * collision polygons, movement, world boundaries and camera scrolling. Most game objects should
 * extend this class; lists of extensions can be retrieved by stage and class name.
 */
public class BaseActor extends Group {

    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;

    protected Vector2 velocityVec;
    protected Vector2 accelerationVec;

    private float acceleration;
    private float maxSpeed;
    private float deceleration;

    private Polygon boundaryPolygon;

    // stores size of game world for all actors
    private static Rectangle worldBounds;

    public static final float MAX_ACCELERATION = 2099999999;
    public static final float MAX_DECELERATION = Float.MAX_VALUE;

    public BaseActor(float x, float y, Stage stage) {
        // call constructor from Actor class
        super();

        // perform additional initialization tasks
        this.setPosition(x, y);
        stage.addActor(this);

        // initialize animation data
        this.animation = null;
        this.elapsedTime = 0;
        this.animationPaused = false;

        // initialize physics data
        this.velocityVec = new Vector2(0, 0);
        this.accelerationVec = new Vector2(0, 0);
        this.acceleration = 0;
        this.maxSpeed = 1000;
        this.deceleration = 0;

        this.boundaryPolygon = null;
    }

    /**
     * VelocityVec getter.
     *
     * @return The velocityVec Vector2
     */
    public Vector2 getVelocityVec() {
        return this.velocityVec;
    }

    /**
     * VelocityVec setter with another Vector2.
     *
     * @param velocityVec The new velocityVec
     */
    public void setVelocityVec(Vector2 velocityVec) {
        this.velocityVec = velocityVec;
    }

    /**
     * VelocityVec setter with x and y coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setVelocityVec(float x, float y) {
        this.velocityVec.x = x;
        this.velocityVec.y = y;
    }

    /**
     * Set x-coordinate to velocityVec.
     *
     * @param x x-coordinate
     */
    public void setVelocityVecX(float x) {
        this.velocityVec.x = x;
    }

    /**
     * Set y-coordinate to velocityVec.
     *
     * @param y y-coordinate
     */
    public void setVelocityVecY(float y) {
        this.velocityVec.y = y;
    }

    /**
     * AccelerationVec getter.
     *
     * @return The accelerationVec Vector2
     */
    public Vector2 getAccelerationVec() {
        return this.accelerationVec;
    }

    /**
     * AccelerationVec setter with another Vector2.
     *
     * @param accelerationVec The new accelerationVec
     */
    public void setAccelerationVec(Vector2 accelerationVec) {
        this.accelerationVec = accelerationVec;
    }

    /**
     * AccelerationVec setter with x and y coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setAccelerationVec(float x, float y) {
        this.accelerationVec.x = x;
        this.accelerationVec.y = y;
    }

    /**
     * Set x-coordinate to accelerationVec.
     *
     * @param x x-coordinate
     */
    public void setAccelerationVecX(float x) {
        this.accelerationVec.x = x;
    }

    /**
     * Set y-coordinate to accelerationVec.
     *
     * @param y y-coordinate
     */
    public void setAccelerationVecY(float y) {
        this.accelerationVec.y = y;
    }

    /**
     * Align center of actor at given position coordinates.
     *
     * @param x x-coordinate to center at
     * @param y y-coordinate to center at
     */
    public void centerAtPosition(float x, float y) {
        this.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
    }

    /**
     * Repositions this BaseActor so its center is aligned with center of other BaseActor. Useful
     * when one BaseActor spawns another.
     *
     * @param other BaseActor to align this BaseActor with
     */
    public void centerAtActor(BaseActor other) {
        this.centerAtPosition(other.getX() + other.getWidth() / 2,
                other.getY() + other.getHeight() / 2);
    }


    // =====================================================
    // Animation methods
    // =====================================================

    /**
     * Sets the animation used when rendering this actor; also sets actor size.
     *
     * @param animation Animation that will be drawn when actor is rendered
     */
    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        TextureRegion textureRegion = this.animation.getKeyFrame(0);
        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();
        this.setSize(width, height);
        this.setOrigin(width / 2, height / 2);

        if (this.boundaryPolygon == null)
            this.setBoundaryRectangle();
    }

    /**
     * Creates an animation from images stored in separate files.
     *
     * @param fileNames Array of names of files containing animation images
     * @param frameDuration How long each frame should be displayed
     * @param loop Should the animation loop
     * @return Animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration,
            boolean loop) {
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<>();

        for (int i = 0; i < fileCount; i++) {
            Texture texture = new Texture(fileNames[i]);
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        return this.loadAnimation(textureArray, frameDuration, loop);
    }

    /**
     * Creates an animation from a spritesheet: a rectangular grid of images stored in a single
     * file.
     *
     * @param fileName Name of file containing spritesheet
     * @param rows Number of rows of images in spritesheet
     * @param cols Number of columns of images in spritesheet
     * @param frameDuration How long each frame should be displayed
     * @param loop Should the animation loop
     * @return Animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols,
            float frameDuration, boolean loop) {
        Texture texture = new Texture(fileName);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                textureArray.add(temp[r][c]);

        return this.loadAnimation(textureArray, frameDuration, loop);
    }

    // Helper method to load animations
    private Animation<TextureRegion> loadAnimation(Array<TextureRegion> textureArray,
            float frameDuration, boolean loop) {
        Animation<TextureRegion> animation = new Animation<>(frameDuration, textureArray);

        if (loop)
            animation.setPlayMode(Animation.PlayMode.LOOP);
        else
            animation.setPlayMode(Animation.PlayMode.NORMAL);

        if (this.animation == null)
            this.setAnimation(animation);

        return animation;
    }

    /**
     * Convenience method for creating a 1-frame animation from a single texture.
     *
     * @param fileName Name of image file
     * @return Animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return this.loadAnimationFromFiles(fileNames, 1, true);
    }

    /**
     * Sets the pause of the animation.
     *
     * @param pause True to pause animation, false to resume animation
     */
    public void setAnimationPaused(boolean pause) {
        this.animationPaused = pause;
    }

    /**
     * Checks if animation is complete: if play mode is normal (not looping) and elapsed time is
     * greater than time corresponding to last frame.
     *
     * @return True if animation is finished, false if animation is not finished
     */
    public boolean isAnimationFinished() {
        return this.animation.isAnimationFinished(this.elapsedTime);
    }

    /**
     * Sets the opacity of this actor.
     *
     * @param opacity Value from 0 (transparent) to 1 (opaque)
     */
    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }


    // =====================================================
    // Physics/motion methods
    // =====================================================

    /**
     * Sets acceleration of this object.
     *
     * @param acceleration Acceleration in pixels per second
     */
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Sets deceleration of this object.
     *
     * @param deceleration Deceleration in pixels per second
     */
    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }

    /**
     * Sets maximum speed of this object.
     *
     * @param maxSpeed Maximum speed of this object in pixels per second
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Sets the speed of movement (in pixels/second) in current direction. If current speed is zero
     * (direction is undefined), direction will be set to 0 degrees.
     *
     * @param speed Speed of movement in pixels per second
     */
    public void setSpeed(float speed) {
        // if length is zero, then assume motion angle is zero degrees
        if (this.velocityVec.len() == 0)
            this.velocityVec.set(speed, 0);
        else
            this.velocityVec.setLength(speed);
    }

    /**
     * Calculates the speed of movement in pixels per second.
     *
     * @return Speed of movement in pixels per second
     */
    public float getSpeed() {
        return this.velocityVec.len();
    }

    /**
     * Determines if this object is moving (if speed is greater than zero).
     *
     * @return False when speed is zero, true otherwise
     */
    public boolean isMoving() {
        return this.getSpeed() > 0;
    }

    /**
     * Sets the angle of motion (in degrees). If current speed is zero, this will have no effect.
     *
     * @param angle Angle of motion (degrees)
     */
    public void setMotionAngle(float angle) {
        this.velocityVec.setAngleDeg(angle);
    }

    /**
     * Gets the angle of motion (in degrees), calculated from the velocity vector.<br>
     * To align actor image angle with motion angle, use <code>setRotation(getMotionAngle())</code>.
     *
     * @return Angle of motion (degrees)
     */
    public float getMotionAngle() {
        return this.velocityVec.angleDeg();
    }

    /**
     * Updates accelerate vector by angle and value stored in acceleration field. Acceleration is
     * applied by <code>applyPhysics</code> method.
     *
     * @param angle Angle (degrees) in which to accelerate
     * @see #acceleration
     * @see #applyPhysics
     */
    public void accelerateAtAngle(float angle) {
        this.accelerationVec.add(new Vector2(this.acceleration, 0).setAngleDeg(angle));
    }

    /**
     * Updates accelerate vector by current rotation angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @see #acceleration
     * @see #applyPhysics
     */
    public void accelerateForward() {
        this.accelerateAtAngle(this.getRotation());
    }

    /**
     * Adjusts velocity vector based on acceleration vector, then adjusts position based on velocity
     * vector.<br>
     * If not accelerating, deceleration velue is applied.<br>
     * Speed is limited by maxSpeed value.<br>
     * Acceleration vector reset to (0,0) at end of method.
     *
     * @param delta Time elapsed since previous frame (delta time); tipically obtained from
     *        <code>act</code>
     * @see #acceleration
     * @see #deceleration
     * @see #maxSpeed
     */
    public void applyPhysics(float delta) {
        // apply acceleration
        this.velocityVec.add(this.accelerationVec.x * delta, this.accelerationVec.y * delta);

        float speed = this.getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (this.accelerationVec.len() == 0)
            speed -= this.deceleration * delta;

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, this.maxSpeed);

        // update velocity
        this.setSpeed(speed);

        // apply velocity
        this.moveBy(this.velocityVec.x * delta, this.velocityVec.y * delta);

        // reset acceleration
        this.accelerationVec.set(0, 0);
    }


    // =====================================================
    // Collision polygon methods
    // =====================================================

    /*
     * Set rectangular-shaped collision polygon. This method is automatically called when animation
     * is set, provided that the current boundary polygon is null.
     */
    public void setBoundaryRectangle() {
        float w = this.getWidth();
        float h = this.getHeight();
        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        this.boundaryPolygon = new Polygon(vertices);
    }

    /**
     * Replace default (rectangle) collision polygon with an n-sided polygon.<br>
     * Vertices of polygon lie on the ellipse contained within bounding rectangle.<br>
     * Note: one vertex will be located at point (0,width); a 4-sided polygon will appear in the
     * orientation of a diamond.
     *
     * @param numSides Number of sides of the collision polygon
     */
    public void setBoundaryPolygon(int numSides) {
        float w = this.getWidth();
        float h = this.getHeight();
        float t = 2 * MathUtils.PI;

        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * t / numSides;
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2; // x-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2; // y-coordinate
        }
        this.boundaryPolygon = new Polygon(vertices);
    }

    /**
     * Returns bounding polygon for this BaseActor, adjusted by Actor's current position and
     * rotation.
     *
     * @return Bounding polygon for this BaseActor
     */
    public Polygon getBoundaryPolygon() {
        this.boundaryPolygon.setPosition(this.getX(), this.getY());
        this.boundaryPolygon.setOrigin(this.getOriginX(), this.getOriginY());
        this.boundaryPolygon.setRotation(this.getRotation());
        this.boundaryPolygon.setScale(this.getScaleX(), this.getScaleY());
        return this.boundaryPolygon;
    }

    /**
     * Determines if this BaseActor overlaps other BaseActor (according to collision polygons).
     *
     * @param other BaseActor to check for overlap
     * @return True if collision polygons of this and other BaseActor overlap
     */
    public boolean overlaps(BaseActor other) {
        return overlaps(other, 1);
    }

    /**
     * Determines if this BaseActor overlaps other BaseActor (according to collision polygons).
     *
     * @param other BaseActor to check for overlap
     * @param scale Polygon scale for the first BaseActor
     * @return True if collision polygons of this and other BaseActor overlap
     */
    public boolean overlaps(BaseActor other, float scale) {
        Polygon poly1 = this.getBoundaryPolygon();
        poly1.setScale(scale, scale);
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    /**
     * Implements a "solid"-like behavior: when there is overlap, move this BaseActor away from
     * other BaseActor along minimum translation vector until there is no overlap.
     *
     * @param other BaseActor to check for overlap
     * @return Direction vector by which actor was translated, null if no overlap
     */
    public Vector2 preventOverlap(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return null;

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if (!polygonOverlap)
            return null;

        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        return mtv.normal;
    }

    /**
     * Determine if this BaseActor is near other BaseActor (according to collision polygons).
     *
     * @param distance Amount (pixels) by which to enlarge collision polygon width and height
     * @param other BaseActor to check if nearby
     * @return True if collision polygons of this (enlarged) and other BaseActor overlap
     * @see #setBoundaryRectangle()
     * @see #setBoundaryPolygon(int)
     */
    public boolean isWithinDistance(float distance, BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        float scaleX = (this.getWidth() + 2 * distance) / this.getWidth();
        float scaleY = (this.getHeight() + 2 * distance) / this.getHeight();
        poly1.setScale(scaleX, scaleY);

        Polygon poly2 = other.getBoundaryPolygon();
        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    /**
     * Returns the angle to another point.
     *
     * @param other Vector2 with point coordinates
     * @return The angle to point
     */
    public float getAngleToPoint(Vector2 other) {
        return this.getAngleToPoint(other.x, other.y);
    }

    /**
     * Returns the angle to another point.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return The angle to point
     */
    public float getAngleToPoint(float x, float y) {
        float angle = (float) (Math.atan2(y - this.getY(), x - this.getX()) * 180 / Math.PI);
        if (angle < 0)
            angle += 360;
        return angle;
    }

    /**
     * Sets world dimensions for use by methods boudToWorld() an scrollTo().
     *
     * @param width Width of world
     * @param height Height of world
     */
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    /**
     * Sets world dimensions for use by methods boudToWorld() an scrollTo().
     *
     * @param other BaseActor whose size determines the world bounds (typically a background image)
     */
    public static void setWorldBounds(BaseActor other) {
        setWorldBounds(other.getWidth(), other.getHeight());
    }

    /**
     * Gets world dimensions.
     *
     * @return Rectangle whose width/height represent world bounds
     */
    public static Rectangle getWorldBounds() {
        return worldBounds;
    }

    /**
     * In an edge of an object moves past the world bounds, adjust its position to keep it
     * completely on screen.
     */
    public void boundToWorld() {
        // check left edge
        if (this.getX() < 0)
            this.setX(0);
        // check right edge
        if (this.getX() + this.getWidth() > worldBounds.width)
            this.setX(worldBounds.width - this.getWidth());
        // check bottom edge
        if (this.getY() < 0)
            this.setY(0);
        // check top edge
        if (this.getY() + this.getHeight() > worldBounds.height)
            this.setY(worldBounds.height - this.getHeight());
    }

    /**
     * If an edge of an object moves past bounds passed as parameters, adjusts its position to keep
     * it completely on screen.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    public void boundToScreen(int x, int y, int w, int h) {
        int width = x + w;
        int height = y + h;
        // check left edge
        if (this.getX() < x)
            this.setX(x);
        // check right edge
        if (this.getX() + this.getWidth() > width)
            this.setX(width - this.getWidth());
        // check bottom edge
        if (this.getY() < y)
            this.setY(y);
        // check top edge
        if (this.getY() + this.getHeight() > height)
            this.setY(height - this.getHeight());
    }

    /**
     * If this object moves completely past the world bounds, adjust its position to the opposite
     * side of the world.
     */
    public void wrapAroundWorld() {
        if (this.getX() + this.getWidth() < 0)
            this.setX(worldBounds.width);
        if (this.getX() > worldBounds.width)
            this.setX(-this.getWidth());
        if (this.getY() + this.getHeight() < 0)
            this.setY(worldBounds.height);
        if (this.getY() > worldBounds.height)
            this.setY(-this.getHeight());
    }

    /**
     * Center camera on this object, while keeping camera's range of view (determined by screen
     * size) completely within world bounds.
     */
    public void alignCamera() {
        Camera camera = this.getStage().getCamera();

        // center camera on actor
        camera.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);

        // bound camera to layout
        camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth / 2,
                worldBounds.width - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight / 2,
                worldBounds.height - camera.viewportHeight / 2);
        camera.update();
    }


    // =====================================================
    // Instance list methods
    // =====================================================

    /**
     * Retrieves a list of all instances of the object from the given stage with the given class or
     * whose class extends the given class.<br>
     * If no instances exists, returns an empty list.<br>
     * Useful when coding interactions between different types of game objects in update method.
     *
     * @param stage Stage containing BaseActor instances
     * @param className Class that extends the BaseActor class
     * @return List of instances of the object in stage which extend with the given class
     */
    public static List<BaseActor> getList(Stage stage, Class<?> className) {
        List<BaseActor> list = new ArrayList<>();

        for (Actor actor : stage.getActors())
            if (className.isInstance(actor))
                list.add((BaseActor) actor);

        return list;
    }

    /**
     * Returns the number of instances of a given class (that extends BaseActor).
     *
     * @param stage Stage containing BaseActor instances
     * @param className Class that extends the BaseActor class
     * @return Number of instances of the class
     */
    public static int count(Stage stage, Class<?> className) {
        return getList(stage, className).size();
    }


    // =====================================================
    // Actor methods: act and draw
    // =====================================================

    /**
     * Processes all actions and related code for this object. Automatically called by act method in
     * Stage class.
     *
     * @param delta Elapsed time (seconds) since last frame (supplied by Stage act method)
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        if (!this.animationPaused)
            this.elapsedTime += delta;
    }

    /**
     * Draws current frame of animation. Automatically called by draw method in Stage class.<br>
     * If color has been set, image will be tinted by that color.<br>
     * If no animation has been set or object is invisible, nothing will be drawn.
     *
     * @param batch (supplied by Stage draw method)
     * @param parentAlpha (supplied by Stage draw method)
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // apply color tint effect
        batch.setColor(this.getColor());

        if (this.animation != null && this.isVisible())
            batch.draw(this.animation.getKeyFrame(this.elapsedTime), this.getX(), this.getY(),
                    this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(),
                    this.getScaleX(), this.getScaleY(), this.getRotation());

        super.draw(batch, parentAlpha);
    }

}
