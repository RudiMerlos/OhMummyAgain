package org.rmc.entity.block;

import org.rmc.MainGame;
import org.rmc.entity.Mummy;
import org.rmc.entity.Player;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlockMummy extends Block {

    private Stage stage;
    private Player player;

    private boolean mummyDiscovered;

    public BlockMummy(float x, float y, Stage stage, int idBlock, Player player) {
        super(x, y, stage, "images/block_mummy.png", idBlock, 0.2f);
        this.stage = stage;
        this.player = player;
        this.mummyDiscovered = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (this.isAnimationFinished() && !this.mummyDiscovered) {
            this.mummyDiscovered = true;
            Mummy mummy = new Mummy(0, 0, this.stage, this.player, MainGame.getMummyRange(),
                    MainGame.SOUTH);
            float x = this.getX() + this.getWidth();
            float y = this.getY() - mummy.getHeight();
            mummy.centerAtPosition(x, y);
        }
    }

}
