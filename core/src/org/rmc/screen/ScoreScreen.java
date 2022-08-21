package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.CrossPoint;
import org.rmc.entity.Mummy;
import org.rmc.entity.Solid;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import org.rmc.framework.tilemap.TilemapActor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScoreScreen extends BaseScreen {

    private Table scoreTable;

    @Override
    public void initialize() {
        TilemapActor tma = new TilemapActor("images/score.tmx", this.mainStage);

        // limits
        for (MapObject object : tma.getRectangleList("solid")) {
            MapProperties properties = object.getProperties();
            new Solid((float) properties.get("x"), (float) properties.get("y"),
                    (float) properties.get("width"), (float) properties.get("height"),
                    this.mainStage);
        }

        // cross point
        for (MapObject object : tma.getRectangleList("cross")) {
            MapProperties properties = object.getProperties();
            boolean[] direction = new boolean[4];
            direction[0] = ((String) properties.get("north")).equals("true");
            direction[1] = ((String) properties.get("east")).equals("true");
            direction[2] = ((String) properties.get("south")).equals("true");
            direction[3] = ((String) properties.get("west")).equals("true");
            new CrossPoint((float) properties.get("x"), (float) properties.get("y"),
                    (float) properties.get("width"), (float) properties.get("height"),
                    this.mainStage, direction);
        }

        // mummy
        this.initializeMummies(tma);

        this.scoreTable = new Table();
        BaseGame.fontParameters.size = 22;
        BaseGame.setLabelStyleFont();
        this.setScoreTable();

        this.uiTable.padTop(40);
        this.uiTable.add(this.scoreTable);
    }

    private void initializeMummies(TilemapActor tma) {
        MapProperties mummyLeft = tma.getRectangleList("mummy_start_left").get(0).getProperties();
        MapProperties mummyRight = tma.getRectangleList("mummy_start_right").get(0).getProperties();
        float xLeft = (float) mummyLeft.get("x");
        float yLeft = (float) mummyLeft.get("y");
        float xRight = (float) mummyRight.get("x");
        float yRight = (float) mummyRight.get("y");
        int mummyNumberLeft = 0;
        int mummyNumberRight = 0;
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                if (mummyNumberLeft == 0) {
                    new Mummy((float) mummyLeft.get("x"), (float) mummyLeft.get("y"),
                            this.mainStage, null, MainGame.getMummyRange(), MainGame.EAST);
                } else if (mummyNumberLeft % 2 != 0) {
                    xLeft += 48;
                    new Mummy(xLeft, (float) mummyLeft.get("y"), this.mainStage, null,
                            MainGame.getMummyRange(), MainGame.EAST);
                } else {
                    yLeft += 48;
                    new Mummy((float) mummyLeft.get("x"), yLeft, this.mainStage, null,
                            MainGame.getMummyRange(), MainGame.NORTH);
                }
                mummyNumberLeft++;
            } else {
                if (mummyNumberRight == 0) {
                    new Mummy((float) mummyRight.get("x"), (float) mummyRight.get("y"),
                            this.mainStage, null, MainGame.getMummyRange(), MainGame.WEST);
                } else if (mummyNumberRight % 2 != 0) {
                    xRight -= 48;
                    new Mummy(xRight, (float) mummyRight.get("y"), this.mainStage, null,
                            MainGame.getMummyRange(), MainGame.WEST);
                } else {
                    yRight += 48;
                    new Mummy((float) mummyRight.get("x"), yRight, this.mainStage, null,
                            MainGame.getMummyRange(), MainGame.NORTH);
                }
                mummyNumberRight++;
            }
        }
    }

    private void setScoreTable() {
        for (int i = 0; i < 5; i++) {
            Label label = new Label(MainGame.getScoreRecord(i), BaseGame.labelStyle);
            label.setColor(Color.BLACK);
            this.scoreTable.add(label).padBottom(20).expandX().left();
            this.scoreTable.row();
        }
    }

    @Override
    public void update(float delta) {
        // not used
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.P)
            BaseGame.setActiveScreen(new LevelScreen());
        else if (keycode == Keys.I)
            BaseGame.setActiveScreen(new InstructionsScreen());
        return false;
    }

}
