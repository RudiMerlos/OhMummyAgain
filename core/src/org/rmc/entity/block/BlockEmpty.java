package org.rmc.entity.block;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlockEmpty extends Block {

    public BlockEmpty(float x, float y, Stage stage, int idBlock) {
        super(x, y, stage, "images/block_empty.png", idBlock, 0.05f);
    }

}
