package org.rmc.entity;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Goal extends BaseActor {

    public Goal(float x, float y, float width, float height, Stage stage) {
        super(x, y, stage);
        this.setSize(width, height);
        this.setBoundaryRectangle();
    }

}
