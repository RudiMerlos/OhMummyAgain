package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.CrossPoint;
import org.rmc.entity.Direction;
import org.rmc.entity.Footprint;
import org.rmc.entity.GameOver;
import org.rmc.entity.Goal;
import org.rmc.entity.LevelPassDialog;
import org.rmc.entity.Mummy;
import org.rmc.entity.NewScoreDialog;
import org.rmc.entity.Player;
import org.rmc.entity.Solid;
import org.rmc.entity.block.Block;
import org.rmc.entity.block.Block.BlockType;
import org.rmc.entity.block.BlockEmpty;
import org.rmc.entity.block.BlockKey;
import org.rmc.entity.block.BlockMummy;
import org.rmc.entity.block.BlockRoyal;
import org.rmc.entity.block.BlockScroll;
import org.rmc.entity.block.BlockTreasure;
import org.rmc.entity.block.OpenBlock;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import org.rmc.framework.inputcontrol.InputGamepad;
import org.rmc.framework.inputcontrol.InputGamepadScreen;
import org.rmc.framework.tilemap.TilemapActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelScreen extends InputGamepadScreen {

    private Player player;
    private Goal goal;
    private GameOver gameOver;

    private static final int BLOCK_EMPTY = 6;
    private static final int BLOCK_TREASURE = 10;

    private int score;
    private int lives;
    private boolean key;
    private boolean royal;

    private boolean scroll;

    private boolean levelPassed;

    private boolean newScore;
    private StringBuilder nameMaxScore;
    private boolean inputCharacter;

    private Label scoreTitleLabel;
    private Label scoreLabel;
    private Label livesLabel;
    private Table livesTable;

    private Table levelPassTable;
    private Label pyramidTitleLabel;
    private Label pyramidLabel;
    private Label levelTitleLabel;
    private Label levelLabel;
    private Label levelPassLabel;

    private Table maxScoreTable;
    private Label nameScoreLabel;

    private Sound coinSound;
    private Sound dieSound;
    private Sound keySound;
    private Sound levelSound;

    @Override
    public void initialize() {
        TilemapActor tma = new TilemapActor("images/map.tmx", this.mainStage);

        // limits
        for (MapObject object : tma.getRectangleList("solid")) {
            MapProperties properties = object.getProperties();
            new Solid((float) properties.get("x"), (float) properties.get("y"),
                    (float) properties.get("width"), (float) properties.get("height"),
                    this.mainStage);
        }

        // direction
        for (MapObject object : tma.getRectangleList("direction")) {
            MapProperties properties = object.getProperties();
            boolean[] direction = new boolean[4];
            direction[0] = ((String) properties.get("north")).equals("true");
            direction[1] = ((String) properties.get("east")).equals("true");
            direction[2] = ((String) properties.get("south")).equals("true");
            direction[3] = ((String) properties.get("west")).equals("true");
            new Direction((float) properties.get("x"), (float) properties.get("y"),
                    (float) properties.get("width"), (float) properties.get("height"),
                    this.mainStage, direction);
        }

        // footprints
        for (MapObject object : tma.getRectangleList("footprint")) {
            MapProperties properties = object.getProperties();
            new Footprint((float) properties.get("x"), (float) properties.get("y"), this.mainStage,
                    ((String) properties.get("step")).equals("left"));
        }

        // player
        this.player = new Player(0, 0, this.mainStage);
        MapObject playerObject = tma.getRectangleList("start").get(0);
        MapProperties playerProperties = playerObject.getProperties();
        this.player.setPosition((float) playerProperties.get("x"),
                (float) playerProperties.get("y"));

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

        // blocks
        this.initializeBlocks(tma);

        // open blocks
        for (MapObject object : tma.getRectangleList("open_block")) {
            MapProperties properties = object.getProperties();
            new OpenBlock((float) properties.get("x"), (float) properties.get("y"),
                    (float) properties.get("width"), (float) properties.get("height"),
                    this.mainStage, Integer.parseInt((String) properties.get("id")));
        }

        // goal
        MapObject goalObject = tma.getRectangleList("goal").get(0);
        MapProperties goalProperties = goalObject.getProperties();
        this.goal = new Goal((float) goalProperties.get("x"), (float) goalProperties.get("y"),
                (float) goalProperties.get("width"), (float) goalProperties.get("height"),
                this.mainStage);

        this.gameOver = null;

        this.score = MainGame.getScore();
        this.lives = MainGame.getLives();

        this.key = false;
        this.royal = false;

        this.scroll = false;

        this.levelPassed = false;

        MainGame.winGame = false;

        this.newScore = false;
        this.nameMaxScore = new StringBuilder();
        this.inputCharacter = false;

        this.initializeTables();

        this.coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.ogg"));
        this.dieSound = Gdx.audio.newSound(Gdx.files.internal("sounds/die.ogg"));
        this.keySound = Gdx.audio.newSound(Gdx.files.internal("sounds/key.ogg"));
        this.levelSound = Gdx.audio.newSound(Gdx.files.internal("sounds/level.ogg"));
    }

    // create different types of blocks in random location
    private void initializeBlocks(TilemapActor tma) {
        Block[] blocks = new Block[20];
        BlockType[] types = fillTypes();

        int idBlock = 1;
        for (MapObject object : tma.getRectangleList("block")) {
            MapProperties properties = object.getProperties();
            int id = Integer.parseInt((String) properties.get("id")) - 1;
            if (types[id].equals(BlockType.EMPTY))
                blocks[id] = new BlockEmpty((float) properties.get("x"),
                        (float) properties.get("y"), this.mainStage, idBlock++);
            else if (types[id].equals(BlockType.TREASURE))
                blocks[id] = new BlockTreasure((float) properties.get("x"),
                        (float) properties.get("y"), this.mainStage, idBlock++);
            else if (types[id].equals(BlockType.KEY))
                blocks[id] = new BlockKey((float) properties.get("x"), (float) properties.get("y"),
                        this.mainStage, idBlock++);
            else if (types[id].equals(BlockType.ROYAL))
                blocks[id] = new BlockRoyal((float) properties.get("x"),
                        (float) properties.get("y"), this.mainStage, idBlock++);
            else if (types[id].equals(BlockType.MUMMY))
                blocks[id] = new BlockMummy((float) properties.get("x"),
                        (float) properties.get("y"), this.mainStage, idBlock++, this.player);
            else
                blocks[id] = new BlockScroll((float) properties.get("x"),
                        (float) properties.get("y"), this.mainStage, idBlock++);
        }
    }

    // helper method used by initializeBlocks to ramdomize the block types
    private static BlockType[] fillTypes() {
        int blockEmpty = 0;
        int blockTreasure = 0;
        boolean blockKey = false;
        boolean blockRoyal = false;
        boolean blockMummy = false;
        BlockType[] types = new BlockType[20];

        for (int i = 0; i < 20; i++) {
            int index = MathUtils.random(0, 19);
            while (types[index] != null)
                index = MathUtils.random(0, 19);

            if (blockEmpty < BLOCK_EMPTY) {
                types[index] = BlockType.EMPTY;
                blockEmpty++;
            } else if (blockTreasure < BLOCK_TREASURE) {
                types[index] = BlockType.TREASURE;
                blockTreasure++;
            } else if (!blockKey) {
                types[index] = BlockType.KEY;
                blockKey = true;
            } else if (!blockRoyal) {
                types[index] = BlockType.ROYAL;
                blockRoyal = true;
            } else if (!blockMummy) {
                types[index] = BlockType.MUMMY;
                blockMummy = true;
            } else {
                types[index] = BlockType.SCROLL;
            }
        }

        return types;
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
        for (int i = 0; i < MainGame.getNumberMummies(); i++) {
            if (i % 2 == 0) {
                if (mummyNumberLeft == 0) {
                    new Mummy((float) mummyLeft.get("x"), (float) mummyLeft.get("y"),
                            this.mainStage, this.player, MainGame.getMummyRange(), MainGame.EAST);
                } else if (mummyNumberLeft % 2 != 0) {
                    xLeft += 48;
                    new Mummy(xLeft, (float) mummyLeft.get("y"), this.mainStage, this.player,
                            MainGame.getMummyRange(), MainGame.EAST);
                } else {
                    yLeft += 48;
                    new Mummy((float) mummyLeft.get("x"), yLeft, this.mainStage, this.player,
                            MainGame.getMummyRange(), MainGame.NORTH);
                }
                mummyNumberLeft++;
            } else {
                if (mummyNumberRight == 0) {
                    new Mummy((float) mummyRight.get("x"), (float) mummyRight.get("y"),
                            this.mainStage, this.player, MainGame.getMummyRange(), MainGame.WEST);
                } else if (mummyNumberRight % 2 != 0) {
                    xRight -= 48;
                    new Mummy(xRight, (float) mummyRight.get("y"), this.mainStage, this.player,
                            MainGame.getMummyRange(), MainGame.WEST);
                } else {
                    yRight += 48;
                    new Mummy((float) mummyRight.get("x"), yRight, this.mainStage, this.player,
                            MainGame.getMummyRange(), MainGame.NORTH);
                }
                mummyNumberRight++;
            }
        }
    }

    private void initializeTables() {
        Color color = Color.valueOf(MainGame.TITLE_COLOR);
        this.scoreTitleLabel = new Label("SCORE", BaseGame.labelStyle);
        this.scoreTitleLabel.setColor(color);

        this.livesLabel = new Label("MEN", BaseGame.labelStyle);
        this.livesLabel.setColor(color);

        color = Color.valueOf(MainGame.SCORE_COLOR);
        this.scoreLabel = new Label(MainGame.getScoreString(this.score), BaseGame.labelStyle);
        this.scoreLabel.setColor(color);

        this.livesTable = new Table();
        for (int i = 0; i < this.lives; i++) {
            BaseActor liveIcon = new BaseActor(0, 0, this.uiStage);
            liveIcon.loadTexture("images/player_icon.png");
            this.livesTable.add(liveIcon);
        }

        this.uiTable.left().top();
        this.uiTable.pad(50).padLeft(80);
        this.uiTable.add(this.scoreTitleLabel).width(150);
        this.uiTable.add(this.scoreLabel).width(250);
        this.uiTable.add(this.livesLabel).width(100);
        this.uiTable.add(this.livesTable);

        this.levelPassTable = new Table();
        this.uiStage.addActor(this.levelPassTable);
        this.levelPassTable.setFillParent(true);
        this.levelPassTable.setVisible(false);

        color = Color.valueOf(MainGame.TITLE_COLOR);
        this.pyramidTitleLabel = new Label("Pyramid", BaseGame.labelStyle);
        this.pyramidTitleLabel.setColor(color);
        this.levelTitleLabel = new Label("Level", BaseGame.labelStyle);
        this.levelTitleLabel.setColor(color);

        color = Color.valueOf(MainGame.SCORE_COLOR);
        int pyramid = ((MainGame.getLevel() - 1) / 5) + 1;
        this.pyramidLabel = new Label(String.valueOf(pyramid), BaseGame.labelStyle);
        this.pyramidLabel.setColor(color);
        int level = MainGame.getLevel() % 5;
        this.levelLabel = new Label(String.valueOf(level == 0 ? 5 : level), BaseGame.labelStyle);
        this.levelLabel.setColor(color);

        color = Color.valueOf(MainGame.LEVEL_COLOR);
        this.levelPassLabel =
                new Label("    Level cleared.\n\nPress \"C\" to continue.", BaseGame.labelStyle);
        this.levelPassLabel.setColor(color);
        this.levelPassLabel.setFontScale(0.8f);

        this.levelPassTable.padTop(110);
        this.levelPassTable.add(this.pyramidTitleLabel).left().padLeft(60).padBottom(20);
        this.levelPassTable.add(this.pyramidLabel).left().padBottom(20);
        this.levelPassTable.row();
        this.levelPassTable.add(this.levelTitleLabel).left().padLeft(60).padBottom(20);
        this.levelPassTable.add(this.levelLabel).left().padBottom(20);
        this.levelPassTable.row();
        this.levelPassTable.add(this.levelPassLabel).colspan(2).center().padTop(20);

        this.maxScoreTable = new Table();
        this.uiStage.addActor(this.maxScoreTable);
        this.maxScoreTable.setFillParent(true);
        this.maxScoreTable.setVisible(false);

        this.nameScoreLabel = new Label("", BaseGame.labelStyle);
        this.nameScoreLabel.setColor(Color.BLACK);

        this.maxScoreTable.add(this.nameScoreLabel).left().padTop(180);
    }

    @Override
    public void update(float delta) {
        this.checkForBlocks();

        for (BaseActor solid : BaseActor.getList(this.mainStage, Solid.class)) {
            this.player.preventOverlap(solid);
        }

        for (BaseActor openBlock : BaseActor.getList(this.mainStage, OpenBlock.class)) {
            if (this.player.overlapsWithOffset(openBlock, 10))
                openBlock.remove();
        }

        for (BaseActor actor : BaseActor.getList(this.mainStage, Footprint.class)) {
            Footprint footprint = (Footprint) actor;
            if (this.player.overlapsWithOffset(footprint, 5)) {
                footprint.setDirection(this.player.getDirection());
                footprint.setVisible(true);
            }
        }

        for (BaseActor mummy : BaseActor.getList(this.mainStage, Mummy.class)) {
            if (this.player.overlaps(mummy, 0.8f)) {
                mummy.remove();
                MainGame.decrementNumberMummies();
                if (this.scroll) {
                    this.scroll = false;
                } else {
                    if (this.lives > 0) {
                        if (MainGame.getSoundsOn())
                            this.dieSound.play();
                        this.lives--;
                        this.livesTable.removeActorAt(this.lives, false);
                    }

                    if (this.lives == 0) {
                        this.player.setVisible(false);
                        this.player.setPosition(-10000, -10000);
                        this.gameOver = new GameOver(0, 0, this.mainStage);
                        this.player.remove();
                    }
                }
            }
        }

        if (this.player.overlaps(this.goal, 0.8f) && this.key && this.royal) {
            if (MainGame.getSoundsOn())
                this.levelSound.play();
            this.levelPassed = true;
            this.player.setVisible(false);
            this.player.setPosition(-10000, -10000);
            MainGame.setLives(this.lives);
            MainGame.setScore(this.score);
            if (MainGame.getLevel() < 25) {
                MainGame.incrementNumberMummies();
                MainGame.incrementLevel();
                new LevelPassDialog(0, 0, this.mainStage);
                this.levelPassTable.setVisible(true);
            } else {
                MainGame.winGame = true;
                BaseActor winActor = new BaseActor(0, 0, this.mainStage);
                winActor.loadTexture("images/win.png");
                winActor.setPosition(MainGame.WIDTH / 2 - winActor.getWidth() / 2,
                        MainGame.HEIGHT / 2 - winActor.getHeight() / 1.23f);
            }
        }

        if (this.gameOver != null && this.gameOver.isAnimationFinished()) {
            if (this.score > MainGame.getMinScore()) {
                this.newScore = true;
                this.inputCharacter = true;
                new NewScoreDialog(0, 0, this.mainStage);
                this.maxScoreTable.setVisible(true);
            } else {
                this.reset();
                BaseGame.setActiveScreen(new ScoreScreen());
            }
        }

        if ((this.gameOver != null || MainGame.winGame) && this.newScore) {
            this.nameScoreLabel.setText(this.nameMaxScore.toString());
        }
    }

    private void checkForBlocks() {
        for (BaseActor blockActor : BaseActor.getList(this.mainStage, Block.class)) {
            Block block = (Block) blockActor;
            this.player.preventOverlap(block);

            boolean discovered = false;
            if (!block.isDiscovered()) {
                discovered = true;
                for (BaseActor actor : BaseActor.getList(this.mainStage, OpenBlock.class)) {
                    OpenBlock openBlock = (OpenBlock) actor;
                    if (openBlock.getId() == block.getIdBlock())
                        discovered = false;
                }

                if (discovered) {
                    block.setDiscovered(true);
                    block.setAnimationPaused(false);
                    this.checkForBlockValue(block);
                }
            }
        }
    }

    private void checkForBlockValue(Block block) {
        if (block instanceof BlockTreasure) {
            if (MainGame.getSoundsOn())
                this.coinSound.play();
            this.score += 5;
            this.scoreLabel.setText(MainGame.getScoreString(this.score));
        } else if (block instanceof BlockKey) {
            if (MainGame.getSoundsOn())
                this.keySound.play();
            this.key = true;
        } else if (block instanceof BlockRoyal) {
            if (MainGame.getSoundsOn())
                this.keySound.play();
            this.score += 50;
            this.scoreLabel.setText(MainGame.getScoreString(this.score));
            this.royal = true;
        } else if (block instanceof BlockScroll) {
            this.scroll = true;
        }
    }


    private void reset() {
        MainGame.setLevel(MainGame.INITIAL_LEVEL);
        MainGame.setLives(MainGame.INITIAL_LIVES);
        MainGame.setScore(MainGame.INITIAL_SCORE);
        MainGame.setNumberMummies(MainGame.INITIAL_NUMBER_MUMMIES);
        MainGame.setMummyRange(MainGame.INITIAL_MUMMY_RANGE);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE) {
            this.paused = !this.paused;
            BaseScreen.sleep(100);
        }

        if (keycode == Keys.C) {
            if (MainGame.winGame) {
                if (this.score > MainGame.getMinScore()) {
                    this.newScore = true;
                    new NewScoreDialog(0, 0, this.mainStage);
                    this.maxScoreTable.setVisible(true);
                } else {
                    BaseGame.setActiveScreen(new ScoreScreen());
                }
            } else if (this.levelPassed) {
                if ((MainGame.getLevel() - 1) % 5 == 0) {
                    if ((MainGame.getLevel() % 10 - 1) == 0)
                        MainGame.setLives(this.lives + 1);
                    else
                        MainGame.setScore(this.score + 200);
                    MainGame.setNumberMummies(1);
                    MainGame.incrementMummyRange();
                    BaseGame.setActiveScreen(new LevelPass());
                } else {
                    BaseGame.setActiveScreen(new LevelScreen());
                }
            }
        }

        if (this.newScore && (this.gameOver != null || MainGame.winGame)) {
            if (keycode == Keys.ENTER) {
                MainGame.setScoreRecord(this.score, this.nameMaxScore.toString());
                this.reset();
                BaseGame.setActiveScreen(new ScoreScreen());
            } else if (keycode == Keys.BACKSPACE && this.nameMaxScore.length() > 0) {
                this.nameMaxScore.deleteCharAt(this.nameMaxScore.length() - 1);
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if ((this.gameOver != null || MainGame.winGame) && this.inputCharacter
                && this.nameMaxScore.length() < 12) {
            if (character != 8)
                this.nameMaxScore.append(character);
        } else if (this.newScore) {
            this.inputCharacter = true;
        }
        return false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == InputGamepad.getInstance().getButtonA()) {
            if (MainGame.winGame) {
                if (this.score > MainGame.getMinScore()) {
                    this.newScore = true;
                    new NewScoreDialog(0, 0, this.mainStage);
                    this.maxScoreTable.setVisible(true);
                } else {
                    BaseGame.setActiveScreen(new ScoreScreen());
                }
            } else if (this.levelPassed) {
                if ((MainGame.getLevel() - 1) % 5 == 0) {
                    if ((MainGame.getLevel() % 10 - 1) == 0)
                        MainGame.setLives(this.lives + 1);
                    else
                        MainGame.setScore(this.score + 200);
                    MainGame.setNumberMummies(1);
                    MainGame.incrementMummyRange();
                    BaseGame.setActiveScreen(new LevelPass());
                } else {
                    BaseGame.setActiveScreen(new LevelScreen());
                }
            }
        }

        if (buttonCode == InputGamepad.getInstance().getButtonStart()) {
            this.paused = !this.paused;
            BaseScreen.sleep(100);
        }

        return false;
    }

}
