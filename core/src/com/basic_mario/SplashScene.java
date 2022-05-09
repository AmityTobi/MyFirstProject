package com.basic_mario;

import com.badlogic.gdx.graphics.Color;
import com.isoterik.racken.Component;
import com.isoterik.racken.GameObject;
import com.isoterik.racken.Scene;

public class SplashScene extends Scene {

    public  SplashScene(){
        setBackgroundColor(Color.BLACK);
        GameObject loader = GameObject.newInstance();
        loader.addComponent(new Component(){
            @Override
            public void postUpdate(float deltaTime) {
                racken.assets.update();
            }
        });
        addGameObject(loader);
    }
}
