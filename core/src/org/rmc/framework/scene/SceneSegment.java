package org.rmc.framework.scene;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SceneSegment {

    private Actor actor;
    private Action action;

    public SceneSegment(Actor actor, Action action) {
        this.actor = actor;
        this.action = action;
    }

    public void start() {
        this.actor.clearActions();
        this.actor.addAction(this.action);
    }

    public boolean isFinished() {
        return this.actor.getActions().size == 0;
    }

    public void finish() {
        // simulate 100000 seconds elapsed time to complete in-progress action
        if (this.actor.hasActions())
            this.actor.getActions().first().act(100000);

        // remove any remaining actions
        this.actor.clearActions();
    }

}
