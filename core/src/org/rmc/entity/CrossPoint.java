package org.rmc.entity;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CrossPoint extends BaseActor {

    private final boolean[] route; // NORTH, EAST, SOUTH, WEST

    public CrossPoint(float x, float y, float width, float height, Stage stage, boolean[] route) {
        super(x, y, stage);
        this.setSize(width, height);
        this.setBoundaryRectangle();
        this.route = route;
    }

    public boolean[] getRoute() {
        return route;
    }

}
