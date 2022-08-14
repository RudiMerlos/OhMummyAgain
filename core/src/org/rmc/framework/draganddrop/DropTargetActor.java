package org.rmc.framework.draganddrop;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DropTargetActor extends BaseActor {

    private boolean targetable;

    public DropTargetActor(float x, float y, Stage stage) {
        super(x, y, stage);

        this.targetable = true;
    }

    /**
     * Check if this actor can be targeted by a DragAndDrop actor.
     *
     * @return
     */
    public boolean isTargetable() {
        return targetable;
    }

    /**
     * Set whether this actor can be targeted by a DragAndDrop actor.
     *
     * @param targetable
     */
    public void setTargetable(boolean targetable) {
        this.targetable = targetable;
    }

}
