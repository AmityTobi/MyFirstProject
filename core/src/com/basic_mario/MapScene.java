package com.basic_mario;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.Component;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Scene;
import com.isoterik.racken._2d.components.renderer.TiledMapRenderer;
import com.isoterik.racken.animation.Animator;
import com.isoterik.racken.animation.FrameAnimation;
import com.isoterik.racken.animation.ICondition;
import com.isoterik.racken.animation.conditions.NumericCompoundCondition;
import com.isoterik.racken.audio.AudioManager;
import com.isoterik.racken.input.GestureEventData;
import com.isoterik.racken.input.GestureTrigger;
import com.isoterik.racken.input.IGestureListener;
import com.isoterik.racken.input.IKeyListener;
import com.isoterik.racken.input.KeyEventData;
import com.isoterik.racken.input.KeyTrigger;
import com.isoterik.racken.physics2d.Physics2d;
import com.isoterik.racken.physics2d.PhysicsManager2d;
import com.isoterik.racken.physics2d.RigidBody2d;
import com.isoterik.racken.physics2d.colliders.BoxCollider;

import org.w3c.dom.Text;

import javax.swing.DefaultListSelectionModel;

public class MapScene extends Scene {
//    ICondition standCondition;
//    ICondition.DataSource<Float> horizontalSpeedSource, verticalSpeedSource;
//    NumericCompoundCondition walkCondition;

    public MapScene() {
        Music music = racken.assets.getMusic("Sfx/Mario.mp3");
        AudioManager.instance().playMusic(music, 1f, true);

        setBackgroundColor(Color.BLACK);

        TiledMap tiledMap = racken.assets.getAsset("Map/map.tmx", TiledMap.class);
        TiledMapRenderer renderer = new TiledMapRenderer(tiledMap,
                1 / gameWorldUnits.getPixelsPerUnit());

        GameObject gameManager = GameObject.newInstance();
        gameManager.addComponent(renderer);
        addGameObject(gameManager);

        PhysicsManager2d physicsManager2d = new PhysicsManager2d(new Vector2(0, -7.5f));
        physicsManager2d.setRenderPhysicsDebugLines(true);
        gameManager.addComponent(physicsManager2d);

//        gameManager.addComponent(new Component() {
//            @Override
//            public void update(float deltaTime) {
//                float speed = 6f;
//                if (input.isKeyDown(Input.Keys.RIGHT)) {
//                    mainCamera.getCamera().translate(speed * deltaTime, 0, 0);
//
//                } else if (input.isKeyDown(Input.Keys.LEFT))
//                    mainCamera.getCamera().translate(-speed * deltaTime, 0, 0);
//
//                float ww = gameWorldUnits.getWorldWidth();
//                float mapW = gameWorldUnits.toWorldUnit(renderer.mapWidth);
//
//                mainCamera.getCamera().position.x = MathUtils.clamp(mainCamera.getCamera().position.x,
//                        ww / 2, mapW - (ww / 2));
//            }

//            @Override
//            public void postUpdate(float deltaTime) {
//                Camera camera = mainCamera.getCamera();
//                gameObject.transform.setPosition(camera.position.x, camera.position.y);
//            }
//        });

        MapLayers mapLayers = tiledMap.getLayers();
        MapLayer objectLayer = mapLayers.get("Objects");
        MapLayer marioLayer = mapLayers.get("Mario layer");
        MapLayer platformLayer = mapLayers.get("Platform layer");

        Array<TiledMapTileMapObject> tiledMapTileMapObjects = renderer.getTileObjects(objectLayer);

        for (TiledMapTileMapObject tiledMapTileMapObject : tiledMapTileMapObjects) {
            TiledMapTile tile = tiledMapTileMapObject.getTile();

            GameObject gameObject = newSpriteObject(tile.getTextureRegion());
            setupTileObjectTransform(tiledMapTileMapObject, gameObject);

            addGameObject(gameObject);
        }

        Array<TiledMapTileMapObject> marioLayerMapObjects = renderer.getTileObjects(marioLayer);
        TiledMapTileMapObject tiledMapTileMapObject = marioLayerMapObjects.first();



        Array<RectangleMapObject> platformLayerMapObjects = renderer.getRectangleObjects(platformLayer);
        for (RectangleMapObject platformMapObject: platformLayerMapObjects){
            GameObject gameObject = GameObject.newInstance("PLATFORM");
            setupTileObjectTransform(platformMapObject,gameObject);

            gameObject.addComponent(new RigidBody2d(BodyDef.BodyType.StaticBody,physicsManager2d));
            gameObject.addComponent(new BoxCollider());
            addGameObject(gameObject);

        }

        GameObject mario = newSpriteObject(tiledMapTileMapObject.getTile().getTextureRegion());
                setupTileObjectTransform(tiledMapTileMapObject,mario);
                RigidBody2d rigidBody2d = new RigidBody2d(BodyDef.BodyType.DynamicBody,physicsManager2d);
                mario.addComponent(rigidBody2d);
                mario.addComponent(new BoxCollider());
                mario.addComponent(new marioController());
                addGameObject(mario);

                rigidBody2d.getBody().setLinearDamping(3);

                rigidBody2d.getBody().setFixedRotation(true);
    }

    private void setupTileObjectTransform(MapObject tile, GameObject gameObject) {
        MapProperties mapProperties = tile.getProperties();
        float x = gameWorldUnits.toWorldUnit(mapProperties.get("x", float.class));
        float y = gameWorldUnits.toWorldUnit(mapProperties.get("y", float.class));
        float w = gameWorldUnits.toWorldUnit(mapProperties.get("width", float.class));
        float h = gameWorldUnits.toWorldUnit(mapProperties.get("height", float.class));
        gameObject.transform.setPosition(x, y);
        gameObject.transform.setSize(w, h);
    }

    private  static  class marioController extends Physics2d{

        @Override
        public void fixedUpdate2d(float timeStep) {
            RigidBody2d rigidBody2d = getComponent(RigidBody2d.class);
            if (rigidBody2d == null)
                return;

            float maxHorizontalForce = 2000;
            float maxVerticalForce = 100000;
            float horizontalForce = 0;
            float verticalForce = 0;

            if (input.isKeyDown(Input.Keys.RIGHT))
                horizontalForce = maxHorizontalForce;

            if (input.isKeyDown(Input.Keys.LEFT))
                horizontalForce = -maxHorizontalForce;

            if (input.isKeyJustPressed(Input.Keys.UP))
                verticalForce = maxVerticalForce;

            rigidBody2d.getBody().applyForceToCenter(horizontalForce * timeStep, verticalForce * timeStep,true);
        }
    }


}