package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.CrossPoint;
import org.rmc.entity.Direction;
import org.rmc.entity.Footprint;
import org.rmc.entity.Mummy;
import org.rmc.entity.Player;
import org.rmc.entity.Solid;
import org.rmc.entity.block.Block;
import org.rmc.entity.block.Block.BlockType;
import org.rmc.entity.block.BlockEmpty;
import org.rmc.entity.block.BlockKey;
import org.rmc.entity.block.BlockRoyal;
import org.rmc.entity.block.BlockScroll;
import org.rmc.entity.block.BlockTreasure;
import org.rmc.entity.block.OpenBlock;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseScreen;
import org.rmc.framework.tilemap.TilemapActor;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;

public class LevelScreen extends BaseScreen {

    private Player player;
    private Solid goal;

    private static final int BLOCK_EMPTY = 7;
    private static final int BLOCK_TREASURE = 10;

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

        // blocks
        this.initializeBlocks(tma);

        // open blocks
        for (MapObject object : tma.getRectangleList("open_block")) {
            MapProperties properties = object.getProperties();
            new OpenBlock((float) properties.get("x"), (float) properties.get("y"),
                    (float) properties.get("width"), (float) properties.get("height"),
                    this.mainStage, Integer.parseInt((String) properties.get("id")));
        }

        // footprints
        for (MapObject object : tma.getRectangleList("footprint")) {
            MapProperties properties = object.getProperties();
            new Footprint((float) properties.get("x"), (float) properties.get("y"), this.mainStage,
                    ((String) properties.get("step")).equals("left"));
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
        MapProperties mummyLeft = tma.getRectangleList("mummy_start_left").get(0).getProperties();
        MapProperties mummyRight = tma.getRectangleList("mummy_start_right").get(0).getProperties();
        for (int i = 0; i < MainGame.getNumberMummies(); i++) {
            if (i % 2 == 0)
                new Mummy((float) mummyLeft.get("x"), (float) mummyLeft.get("y"), this.mainStage,
                        this.player, MainGame.getMummySpeed(), MainGame.getMummyRange());
            else
                new Mummy((float) mummyRight.get("x"), (float) mummyRight.get("y"), this.mainStage,
                        this.player, MainGame.getMummySpeed(), MainGame.getMummySpeed());
        }

        // goal
        MapObject goalObject = tma.getRectangleList("goal").get(0);
        MapProperties goalProperties = goalObject.getProperties();
        this.goal = new Solid((float) goalProperties.get("x"), (float) goalProperties.get("y"),
                (float) goalProperties.get("width"), (float) goalProperties.get("height"),
                this.mainStage);
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
            } else {
                types[index] = BlockType.SCROLL;
            }
        }

        return types;
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

        if (this.player.overlaps(this.goal)) {
            // TODO check if the player has got key and royal to pass to the next level
        }
    }

    private void checkForBlocks() {
        for (BaseActor blockActor : BaseActor.getList(this.mainStage, Block.class)) {
            Block block = (Block) blockActor;
            this.player.preventOverlap(block);

            if (!block.isDiscovered()) {
                block.setDiscovered(true);
                for (BaseActor actor : BaseActor.getList(this.mainStage, OpenBlock.class)) {
                    OpenBlock openBlock = (OpenBlock) actor;
                    if (openBlock.getId() == block.getIdBlock())
                        block.setDiscovered(false);
                }

                if (block.isDiscovered())
                    block.setAnimationPaused(false);
            }
        }
    }

}
