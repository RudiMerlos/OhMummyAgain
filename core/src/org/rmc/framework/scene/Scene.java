package org.rmc.framework.scene;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Scene extends Actor {

    private List<SceneSegment> segmentList;
    private int index;

    public Scene() {
        super();
        this.segmentList = new ArrayList<>();
        this.index = -1;
    }

    public void addSegment(SceneSegment segment) {
        this.segmentList.add(segment);
    }

    public void clearSegments() {
        this.segmentList.clear();
    }

    public void start() {
        this.index = 0;
        this.segmentList.get(this.index).start();
    }

    public boolean isSegmentFinished() {
        return this.segmentList.get(this.index).isFinished();
    }

    public boolean isLastSegment() {
        return this.index >= this.segmentList.size() - 1;
    }

    public void loadNextSegment() {
        if (this.isLastSegment())
            return;

        this.segmentList.get(this.index).finish();
        this.index++;
        this.segmentList.get(this.index).start();
    }

    public boolean isSceneFinished() {
        return this.isLastSegment() && this.isSegmentFinished();
    }

    @Override
    public void act(float delta) {
        if (this.isSegmentFinished() && !this.isLastSegment())
            this.loadNextSegment();
    }

}
