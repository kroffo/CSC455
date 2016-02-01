package com.mygdx.helloworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    //Texture texture;
    //Pixmap pixmap;
    TextureAtlas textureAtlas;
    //BitmapFont font;
    Sprite sprite;
    int screenWidth, screenHeight, i = 1, position = 0,
        currentFrame = 0;
    String currentAtlasKey = new String("0001");
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("Sprites/Invader1.atlas"));
        
        AtlasRegion region = textureAtlas.findRegion("0001");
        sprite = new Sprite(region);
        sprite.setPosition(120,100);
        sprite.scale(2.5f);
        Timer.schedule(new Task() {
                @Override
                public void run() {
                    if (++currentFrame > 19)
                        currentFrame = 0;
                    currentAtlasKey = "000" + (currentFrame/10 + 1);
                    sprite.setRegion(textureAtlas.findRegion(currentAtlasKey));
                }
            },0,1/30.0f);
        // font = new BitmapFont();
        // font.setColor(Color.RED);
    }

    @Override
    public void dispose() {
        batch.dispose();
        //font.dispose();
        //texture.dispose();
        textureAtlas.dispose();
    }
    
    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }
    
    @Override
    public void pause() {}

    @Override
    public void resume() {}
    
}
