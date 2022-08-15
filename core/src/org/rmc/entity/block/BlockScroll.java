package org.rmc.entity.block;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlockScroll extends Block {

    public BlockScroll(float x, float y, Stage stage, int idBlock) {
        super(x, y, stage, "images/block_scroll.png", idBlock, 0.1f);
    }

}
