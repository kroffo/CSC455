package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyGdxGame extends ApplicationAdapter {
    
    private SpriteBatch batch;
    private Room startRoom;
    private float screenWidth, screenHeight;
    private Player player;
    private PauseMenu pauseMenu;
    private LootMenu lootMenu;
    private EnemyMenu enemyMenu;
    private DoorMenu doorMenu;
    
    private String playerImage = "TestPlayer.png",
        chestImage = "TestChest.png",
        barrierImage = "TestBarrier.png";
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        
        startRoom = new Room("TestTile.png", screenWidth, screenHeight);
        Room secondRoom = new Room("TestTile.png", screenWidth, screenHeight);
        player = startRoom.addPlayer(playerImage,playerImage,4,0,"Squash");
        Chest c = startRoom.addChest(chestImage,4,1,"Wooden Chest",false,null);
        Satchel s = c.getContents();
        s.addWeapon(new Weapon("Hammer",3,5));
        s.addWeapon(new Weapon("Sickle",5,3));
        s.addArmor(new Armor("Iron Wall",7,"Shield"));
        s.addKey(new Key("Blue Key"));
        startRoom.addBarrier(barrierImage,5,1);
        startRoom.addBarrier(barrierImage,2,5);
        startRoom.addEnemy("TestEnemy.png","TestEnemy.png",1,1,"name",1,2,3,4,5,null,null,null,6,"rat");
        Door d = new Door("Wooden Door", new Sprite(new Texture("TestChest.png")), startRoom, 0, 3, 3, secondRoom, 8, 3, 1, true, new Key("Blue Key"));
        
    }
    
    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        processInput();
        if (Player.getPlayer().getState() == Player.State.PLAY) {
            batch.begin();
            for (Occupant o : player.getRoom().getOccupants()) {
                if (o instanceof Creature)
                    ((Creature)o).step();
            }
            player.getRoom().draw(batch);
            batch.end();
        }
    }

    private void processInput() {
        Player.State state = Player.getPlayer().getState();
        if (state == Player.State.PLAY) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                Player.getPlayer().setState(Player.State.PAUSE);
                pauseMenu = new PauseMenu();
                return;
            }
            if (!player.transitioning()) {
                Tile location = player.getLocation();
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    if (location.chestAt(player.getOrientation())) { /* Loot that chest (or body) */
                        Chest c = (Chest)(location.getNeighboringOccupant(player.getOrientation()));
                        player.lootChest(c);
                        lootMenu = new LootMenu(c.getName(), c.getContents());
                    } else if (location.enemyAt(player.getOrientation())) {
                        Enemy e = (Enemy)(location.getNeighboringOccupant(player.getOrientation()));
                        player.examineEnemy(e);
                        enemyMenu = new EnemyMenu(e);
                    } else if (location.doorAt(player.getOrientation())) {
                        Door d = location.getDoor();
                        player.examineDoor(d);
                        doorMenu = new DoorMenu(d);
                    }
                }
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    player.setOrientation(3);
                    if (location.neighborAvailable(3)) {
                        location.moveOccupant(3);
                    }
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    player.setOrientation(1);
                    if (location.neighborAvailable(1)) {
                        location.moveOccupant(1);
                    }
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    player.setOrientation(2);
                    if (location.neighborAvailable(2)) {
                        location.moveOccupant(2);
                    }
                } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    player.setOrientation(0);
                    if (location.neighborAvailable(0)) {
                        location.moveOccupant(0);
                    }
                } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    player.setOrientation(3);
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    player.setOrientation(1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    player.setOrientation(2);
                } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    player.setOrientation(0);
                }
            }
        } else if (state == Player.State.PAUSE) {
            pauseMenu.processInput();
            pauseMenu.draw(batch, screenWidth, screenHeight);
            if (pauseMenu.exited()) {
                pauseMenu.reinitialize();
                player.setState(Player.State.PLAY);
            }
        } else if (state == Player.State.LOOT) {
            lootMenu.processInput();
            lootMenu.draw(batch, screenWidth, screenHeight);
            if (lootMenu.exited()) {
                lootMenu.reinitialize();
                player.removeLootChest();
            }
        } else if (state == Player.State.EXAMINE) {
            enemyMenu.processInput();
            enemyMenu.draw(batch, screenWidth, screenHeight);
            if (enemyMenu.exited()) {
                enemyMenu.reinitialize();
                player.removeExaminee();
            }
        } else if (state == Player.State.DOOR) {
            doorMenu.processInput();
            doorMenu.draw(batch, screenWidth, screenHeight);
            if (doorMenu.exited()) {
                doorMenu.reinitialize();
                player.removeDoor();
            }
        } else if (state == Player.State.PEEK) {
            batch.begin();
            player.getPeekRoom().draw(batch);
            batch.end();
        } else if (state == Player.State.FIGHT) {
            /* 
             * Write behaviors for Enemies
             * 
             * Write the FIGHT
             * 
             * Write the world parser
             *
             */
        }
    }
}
