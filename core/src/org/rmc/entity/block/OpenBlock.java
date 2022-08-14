package org.rmc.entity.block;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class OpenBlock extends BaseActor {

    private int id;

    public OpenBlock(float x, float y, float width, float height, Stage stage, int id) {
        super(x, y, stage);
        this.setSize(width, height);
        this.setBoundaryRectangle();
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
