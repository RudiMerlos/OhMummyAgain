package org.rmc.framework.tilemap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TilemapActor extends Actor {

    private TiledMap tiledMap;
    private OrthographicCamera tiledCamera;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;

    public TilemapActor(String filename, Stage stage) {
        // set up tile map
        this.tiledMap = new TmxMapLoader().load(filename);

        int tileWidth = (int) this.tiledMap.getProperties().get("tilewidth");
        int tileHeight = (int) this.tiledMap.getProperties().get("tileheight");
        int numTilesHorizontal = (int) this.tiledMap.getProperties().get("width");
        int numTilesVertical = (int) this.tiledMap.getProperties().get("height");
        int mapWidth = tileWidth * numTilesHorizontal;
        int mapHeight = tileHeight * numTilesVertical;

        BaseActor.setWorldBounds(mapWidth, mapHeight);

        // set up renderer
        this.tiledMapRenderer = new OrthoCachedTiledMapRenderer(this.tiledMap);
        this.tiledMapRenderer.setBlending(true);

        // set up camera
        this.tiledCamera = new OrthographicCamera();
        this.tiledCamera.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);
        this.tiledCamera.update();

        stage.addActor(this);
    }

    public List<MapObject> getRectangleList(String propertyName) {
        List<MapObject> list = new ArrayList<>();

        for (MapLayer layer : this.tiledMap.getLayers()) {
            for (MapObject object : layer.getObjects()) {
                if (!(object instanceof RectangleMapObject))
                    continue;

                MapProperties properties = object.getProperties();

                if (properties.containsKey("name") && properties.get("name").equals(propertyName))
                    list.add(object);
            }
        }

        return list;
    }

    public List<MapObject> getTileList(String propertyName) {
        List<MapObject> list = new ArrayList<>();

        for (MapLayer layer : this.tiledMap.getLayers()) {
            for (MapObject object : layer.getObjects()) {
                if (!(object instanceof TiledMapTileMapObject))
                    continue;

                MapProperties properties = object.getProperties();

                // Default MapProperties are stored within associated Tile object
                // Instance-specific overrides are stored in MapObject
                TiledMapTileMapObject tmtmo = (TiledMapTileMapObject) object;
                TiledMapTile t = tmtmo.getTile();
                MapProperties defaultProperties = t.getProperties();

                if (defaultProperties.containsKey("name")
                        && defaultProperties.get("name").equals(propertyName))
                    list.add(object);

                // get list of default property keys
                getDefaultProperties(properties, defaultProperties);
            }
        }

        return list;
    }

    private static void getDefaultProperties(MapProperties properties,
            MapProperties defaultProperties) {
        Iterator<String> propertyKeys = defaultProperties.getKeys();

        // iterate over keys; copy default values into properties if needed
        while (propertyKeys.hasNext()) {
            String key = propertyKeys.next();

            // check if value already exists; if not, create property with default value
            if (!properties.containsKey(key)) {
                Object value = defaultProperties.get(key);
                properties.put(key, value);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // adjust tilemap camera to stay in sync with main camera
        Camera mainCamera = this.getStage().getCamera();
        this.tiledCamera.position.x = mainCamera.position.x;
        this.tiledCamera.position.y = mainCamera.position.y;
        this.tiledCamera.update();
        this.tiledMapRenderer.setView(this.tiledCamera);

        // need the following code to force batch order, otherwise it is batched and rendered last
        batch.end();
        this.tiledMapRenderer.render();
        batch.begin();
    }

}
