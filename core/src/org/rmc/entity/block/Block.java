package org.rmc.entity.block;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Block extends BaseActor {

    public enum BlockType { EMPTY, TREASURE, KEY, ROYAL, SCROLL }

    private boolean discovered;

    private int idBlock;

    protected Block(float x, float y, Stage stage, String fileName, int idBlock) {
        super(x, y, stage);
        this.loadAnimationFromSheet(fileName, 1, 13, 0.05f, false);
        this.setBoundaryRectangle();
        this.setAnimationPaused(true);
        this.discovered = false;
        this.idBlock = idBlock;
    }

    public boolean isDiscovered() {
        return this.discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public int getIdBlock() {
        return this.idBlock;
    }

}
