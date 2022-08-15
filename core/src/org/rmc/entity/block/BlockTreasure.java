package org.rmc.entity.block;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlockTreasure extends Block {

    public BlockTreasure(float x, float y, Stage stage, int idBlock) {
        super(x, y, stage, "images/block_treasure.png", idBlock, 0.05f);
    }
}
