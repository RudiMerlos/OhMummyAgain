package org.rmc.entity;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Explosion extends BaseActor {

    public Explosion(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadAnimationFromSheet("images/explosion.png", 1, 10, 0.05f, false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.isAnimationFinished())
            this.remove();
    }

}
