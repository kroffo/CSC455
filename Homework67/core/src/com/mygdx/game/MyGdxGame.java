package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Room currentRoom;
    float screenWidth, screenHeight;
    Creature player;

    String playerImage = "TestPlayer.png",
        chestImage = "TestChest.png",
        barrierImage = "TestBarrier.png";
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        currentRoom = new Room("TestTile.png", screenWidth, screenHeight);
        player = currentRoom.addPlayer(playerImage,4,0,"Squash");
        currentRoom.addChest(chestImage,4,1,"Wooden Chest",false,null);
        currentRoom.addBarrier(barrierImage,5,1);
        currentRoom.addBarrier(barrierImage,2,5);
        currentRoom.addEnemy("TestEnemy.png",1,1,"name",1,2,3,4,5,null,null,null,6,"rat");
    }
    
    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        processInput();
        for (Occupant o : currentRoom.getOccupants()) {
            if (o instanceof Creature)
                ((Creature)o).step();
        }
        currentRoom.draw(batch);
        batch.end();
    }

    private void processInput() {
        if (!player.transitioning()) {
            Tile location = player.getLocation();
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (location.neighborAvailable(3)) {
                    location.moveOccupant(3);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (location.neighborAvailable(1)) {
                    location.moveOccupant(1);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (location.neighborAvailable(2)) {
                    location.moveOccupant(2);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (location.neighborAvailable(0)) {
                    location.moveOccupant(0);
                }
            }
        }
    }
}
