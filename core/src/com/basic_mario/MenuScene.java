package com.basic_mario;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Scene;
import com.isoterik.racken._2d.scenes.transition.SceneTransitions;
import com.isoterik.racken.actor.ActorAnimation;

public class MenuScene extends Scene {
    public  MenuScene(){
        setBackgroundColor(Color.BLACK);
       // setRenderDebugLines(true);

        float width = gameWorldUnits.getScreenWidth();
        float height = gameWorldUnits.getScreenHeight();

        GameObject img = newSpriteObject(racken.assets.getTexture("bg/background.png"));
        addGameObject(img);

        Skin skin = racken.assets.getSkin("Skin/glassy-ui.json");
        TextButton textButton = new TextButton("Play", skin);

        textButton.addListener(new ChangeListener() {
          @Override
            public void changed(ChangeEvent event, Actor actor) {
          racken.setScene(new MapScene());
             }
        });

        TextButton textBtn = new TextButton("Option", skin);

        TextButton txtBtn = new TextButton("Close",skin,"small");

        Label label1 =new Label("A - Backward", skin, "black");
        Label label2 =new Label("D - Forward", skin, "black");
        Label label3 =new Label("W - Up", skin, "black");
        Label label4 =new Label("Arrow Keys", skin, "black");

        Window window = new Window("Instructions", skin);
        textBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ActorAnimation.instance().grow(window,0.5f, Interpolation.bounceIn);

                txtBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ActorAnimation.instance().slideOut(window,ActorAnimation.UP,2f
                                ,Interpolation.elastic);
                    }
                });

                uiCanvas.addActor(window);
            }

        });

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);

        table.row();
        table.add(textButton).colspan(2).padBottom(10);

        table.row();
        table.add(textBtn).colspan(2).padTop(10);

        window.row();
        window.add(label1).fillX();

        window.row();
        window.add(label2).fillX();

        window.row();
        window.add(label3).fillX();

        window.row();
        window.add(label4).fillX();


        window.row();
        window.add(txtBtn).padTop(30);

        window.setPosition((width- window.getWidth())/2, (height- window.getHeight())/2);
        window.setOrigin(window.getWidth(), window.getHeight());
        window.pack();
        window.setKeepWithinStage(false);

            uiCanvas.addActor(table);



    }
}
