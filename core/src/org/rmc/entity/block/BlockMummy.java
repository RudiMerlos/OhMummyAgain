package org.rmc.entity.block;

import org.rmc.MainGame;
import org.rmc.entity.Explosion;
import org.rmc.entity.Mummy;
import org.rmc.entity.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlockMummy extends Block {

    private Stage stage;
    private Player player;

    private boolean mummyDiscovered;

    private float waitExplosion;

    public BlockMummy(float x, float y, Stage stage, int idBlock, Player player) {
        super(x, y, stage, "images/block_mummy.png", idBlock, 0.2f);
        this.stage = stage;
        this.player = player;
        this.mummyDiscovered = false;
        this.waitExplosion = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (this.isDiscovered()) {
            this.waitExplosion += delta;
            if (this.waitExplosion >= 2.1 && this.waitExplosion < 2.2) {
                new Explosion(this.getX() + this.getWidth() - 48, this.getY(), this.stage);
                Sound explosionSound =
                        Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.ogg"));
                explosionSound.play(0.5f);
            }
            if (this.isAnimationFinished() && !this.mummyDiscovered) {
                this.mummyDiscovered = true;
                Mummy mummy = new Mummy(0, 0, this.stage, this.player, MainGame.getMummyRange(),
                        MainGame.EAST);
                float x = this.getX() + this.getWidth() - mummy.getWidth() / 2;
                float y = this.getY() - mummy.getHeight() / 2;
                mummy.centerAtPosition(x, y);
                MainGame.setNumberMummies(MainGame.getNumberMummies() + 1);
            }
        }
    }

}
