package org.rmc.framework.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class ParticleActor extends Group {

    private ParticleEffect effect;
    private ParticleRenderer renderer;

    private class ParticleRenderer extends Actor {

        private ParticleEffect effect;

        public ParticleRenderer(ParticleEffect effect) {
            this.effect = effect;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            this.effect.draw(batch);
        }

    }

    public ParticleActor(String pfxFile, String imageDirectory) {
        super();
        this.effect = new ParticleEffect();
        this.effect.load(Gdx.files.internal(pfxFile), Gdx.files.internal(imageDirectory));
        this.renderer = new ParticleRenderer(this.effect);
        this.addActor(this.renderer);
    }

    public void start() {
        this.effect.start();
    }

    // pauses continous emitters
    public void stop() {
        this.effect.allowCompletion();
    }

    public boolean isRunning() {
        return !this.effect.isComplete();
    }

    public void centerAtActor(Actor other) {
        this.setPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.effect.update(delta);

        if (this.effect.isComplete() && !this.effect.getEmitters().first().isContinuous()) {
            this.effect.dispose();
            this.remove();
        }
    }

}
