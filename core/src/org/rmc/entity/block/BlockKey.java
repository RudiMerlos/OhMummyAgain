package org.rmc.entity.block;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlockKey extends Block {

    public BlockKey(float x, float y, Stage stage, int idBlock) {
        super(x, y, stage, "images/block_key.png", idBlock, 0.1f);
    }

}
