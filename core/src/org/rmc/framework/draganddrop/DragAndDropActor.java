package org.rmc.framework.draganddrop;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Enables drag-and-drop functionality for actors.
 */
public abstract class DragAndDropActor extends BaseActor {

    private DragAndDropActor self;

    private float grabOffsetX;
    private float grabOffsetY;

    private DropTargetActor dropTarget;

    private boolean draggable;

    private float startPositionX;
    private float startPositionY;

    protected DragAndDropActor(float x, float y, Stage stage) {
        super(x, y, stage);

        this.self = this;

        this.draggable = true;

        this.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!self.isDraggable())
                    return false;

                self.grabOffsetX = x;
                self.grabOffsetY = y;

                self.startPositionX = self.getX();
                self.startPositionY = self.getY();

                self.toFront();
                self.addAction(Actions.scaleTo(1.1f, 1.1f, 0.25f));

                self.onDragStart();

                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                self.moveBy(x - self.grabOffsetX, y - self.grabOffsetY);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                self.setDropTarget(null);

                // keep track of distance to closest object
                float closestDistance = Float.MAX_VALUE;

                for (BaseActor actor : BaseActor.getList(self.getStage(), DropTargetActor.class)) {
                    DropTargetActor target = (DropTargetActor) actor;

                    if (target.isTargetable() && self.overlaps(target)) {
                        float currentDistance =
                                Vector2.dst(self.getX(), self.getY(), target.getX(), target.getY());

                        // check if this target is even closer
                        if (currentDistance < closestDistance) {
                            self.setDropTarget(target);
                            closestDistance = currentDistance;
                        }
                    }
                }

                self.addAction(Actions.scaleTo(1, 1, 0.25f));

                self.onDrop();
            }

        });
    }

    /**
     * If this actor is dropped on a "targetable" actor, that actor can be obtained from this
     * method.
     *
     * @return
     */
    public DropTargetActor getDropTarget() {
        return this.dropTarget;
    }

    /**
     * Automatically set when actor is dropped on a target.
     *
     * @param dropTarget
     */
    public void setDropTarget(DropTargetActor dropTarget) {
        this.dropTarget = dropTarget;
    }

    /**
     * Check if a drop target currently exists.
     *
     * @return
     */
    public boolean hasDropTarget() {
        return this.dropTarget != null;
    }

    /**
     * Check if this actor can be dragged.
     *
     * @return
     */
    public boolean isDraggable() {
        return this.draggable;
    }

    /**
     * Set whether this actor can be dragged.
     *
     * @param draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * Slide this actor to the center of another actor.
     */
    public void moveToActor(BaseActor other) {
        this.moveToActor(other, 0, 0);
    }

    /**
     * Slide this actor to the center of another actor with offset.
     */
    public void moveToActor(BaseActor other, int offsetX, int offsetY) {
        float x = other.getX() + (other.getWidth() - this.getWidth()) / 2;
        float y = other.getY() + (other.getHeight() - this.getHeight()) / 2;
        this.addAction(Actions.moveTo(x - offsetX, y - offsetY, 0.5f, Interpolation.pow3));
    }

    /**
     * Slide this actor back to its original position before it was dragged.
     */
    public void moveToStart() {
        this.addAction(Actions.moveTo(this.startPositionX, this.startPositionY, 0.50f,
                Interpolation.pow3));
    }

    /**
     * Called when drag begins.
     */
    public abstract void onDragStart();

    /**
     * Called when drop occurs.
     */
    public abstract void onDrop();

}
