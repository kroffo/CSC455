package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {

    private enum FightState {
        INPUT,
        OUTPUT,
        FLE,
        DONE
    }
    
    private SpriteBatch batch;
    private float screenWidth, screenHeight;
    private Player player;
    private Room startRoom;
    private PauseMenu pauseMenu;
    private LootMenu lootMenu;
    private EnemyMenu enemyMenu;
    private DoorMenu doorMenu;
    private ChestMenu chestMenu;
    private FightMenu fightMenu;
    private FightState fightState;
    private FightStatsBar statsBar;
    private ArrayList<String> fightOutput = new ArrayList<String>(0);
    private boolean actionPressed, readyForAction;
    private int winCount;
    
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
        Chest c = startRoom.addChest(chestImage,4,1,"Wooden Chest",true,new Key("Wooden Key"));
        Satchel s = c.getContents();
        s.addWeapon(new Weapon("Hammer",3,5));
        s.addWeapon(new Weapon("Sickle",5,3));
        s.addArmor(new Armor("Iron Wall",7,"Shield"));
        s.addKey(new Key("Blue Key"));
        startRoom.addBarrier(barrierImage,5,1);
        startRoom.addBarrier(barrierImage,2,5);
        startRoom.addEnemy("TestPlayer.png","TestEnemy.png",1,1,"name",1,2,3,4,5,null,null,null,1,"rat");
        Door d = new Door("Wooden Door", new Sprite(new Texture("TestChest.png")), startRoom, 0, 3, 3, secondRoom, 8, 3, 1, true, new Key("Blue Key"));
    }
    
    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        processInput();
        if (Player.getPlayer().getState() == Player.State.PLAY) {
            batch.begin();
            Room room = player.getRoom();
            for (Occupant o : room.getOccupants()) {
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
                        if (c.isLocked()) {
                            player.examineChest(c);
                            chestMenu = new ChestMenu(c);
                        } else {
                            player.lootChest(c);
                            lootMenu = new LootMenu(c.getName(), c.getContents());
                        }
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
        } else if (state == Player.State.CHEST) {
            chestMenu.processInput();
            chestMenu.draw(batch, screenWidth, screenHeight);
            if (chestMenu.exited()) {
                chestMenu.reinitialize();
                player.removeChest();
            }
        } else if (state == Player.State.PEEK) {
            
            batch.begin();
            for (Room r : player.getRoom().getNeighboringRooms())
                for (Occupant o : r.getOccupants())
                    if (o instanceof Creature)
                        ((Creature)o).step();
            player.getPeekRoom().draw(batch);
            batch.end();
            
        } else if (state == Player.State.FIGHT) {
            Enemy opponent = player.getExaminee();

            Sprite playerFS = player.getFightSprite();
            Sprite opponentFS = opponent.getFightSprite();
            
            batch.begin();
            playerFS.setPosition(100,125);
            playerFS.draw(batch);
            opponentFS.setPosition(450,275);
            opponentFS.draw(batch);
            batch.end();

            Random r = new Random();
            if (fightMenu == null) {
                fightMenu = new FightMenu();
                fightState = FightState.INPUT;
                statsBar = new FightStatsBar(player, opponent);
                
            }
            statsBar.draw(batch, screenWidth, screenHeight);
            if (fightState == FightState.INPUT) {
                fightOutput.clear();
                fightMenu.processInput();
                readyForAction = false;
                fightMenu.draw(batch, screenWidth, screenHeight);
                String input = player.getFightOption();
                
                if (input != null) {
                    player.resetFightOption();
                    fightState = FightState.OUTPUT;
                    
                    if (input.equals("Attack")) {
                        int inflicting = r.nextInt(player.getAttack()) + 1;
                        inflicting = inflicting - opponent.getDefense();
                        if (inflicting <= 0) {
                            inflicting = 1;
                        }
                        boolean crit = false;
                        if (r.nextInt(100) + 1 < player.getLuck()) {
                            inflicting *= 2;
                            crit = true;
                        }
                        fightOutput.add(player.generateAttackString(inflicting,crit));
                        opponent.takeDamage(inflicting);
                        
                        if (opponent.stumbled()) {
                            fightOutput.add(opponent.unstumble());
                        } else {
                            int inflicted = r.nextInt(opponent.getAttack()) + 1;
                            inflicted = inflicted - player.getDefense();
                            if (inflicted <= 0) {
                                inflicted = 1;
                            }
                            crit = false;
                            if (r.nextInt(100) + 1 < opponent.getLuck()) {
                                inflicted *= 2;
                                crit = true;
                            }
                            fightOutput.add(opponent.generateAttackString(inflicted,crit));
                            player.takeDamage(inflicted);
                        }
                        
                    } else if (input.equals("Block")) {
                        
                        if (opponent.stumbled()) {
                            fightOutput.add(opponent.unstumble());
                        } else {
                            int inflicted = r.nextInt(opponent.getAttack()) + 1;
                            inflicted = inflicted - player.getDefense();
                            if (inflicted <= 0) {
                                inflicted = 1;
                            }
                            boolean blockSuccessful = false;
                            if (r.nextInt(100) + 1 < (player.getStrength()/opponent.getStrength())*100) {
                                blockSuccessful = true;
                                inflicted /= 2;
                            }
                            boolean crit = false;
                            if (r.nextInt(100) + 1 < opponent.getLuck()) {
                                inflicted *= 2;
                                crit = true;
                            }
                            fightOutput.add(player.generateBlockString(blockSuccessful));
                            fightOutput.add(opponent.generateAttackString(inflicted,crit));
                            player.takeDamage(inflicted);
                        }
                        
                    } else if (input.equals("Dodge")) {

                        if (opponent.stumbled()) {
                            fightOutput.add(opponent.unstumble());
                            fightOutput.add(player.getName() + " dodges, but nothing happens!");
                        } else if (r.nextInt(100) + 1 < player.getAgility()) {
                            fightOutput.add(player.getName() + " dodges " + opponent.getName() + "'s attack!");
                            fightOutput.add(opponent.stumble());
                        } else {
                            fightOutput.add(player.getName() + " attempts to dodge, but is not fast enough!");
                            int inflicted = r.nextInt(opponent.getAttack()) + 1;
                            inflicted = inflicted - player.getDefense();
                            if (inflicted <= 0) {
                                inflicted = 1;
                            }
                            boolean crit = false;
                            if (r.nextInt(100) + 1 < opponent.getLuck()) {
                                inflicted *= 2;
                                crit = true;
                            }
                            fightOutput.add(opponent.generateAttackString(inflicted,crit));
                            player.takeDamage(inflicted);
                        }
                        
                    } else if (input.equals("Flee")) {
                        
                        fightState = FightState.FLE;
                        if (opponent.stumbled()) {
                            fightOutput.add(opponent.unstumble());
                        } else {
                            int inflicted = r.nextInt(opponent.getAttack()) + 1;
                            inflicted = inflicted - player.getDefense();
                            if (inflicted <= 0) {
                                inflicted = 1;
                            }
                            boolean crit = false;
                            if (r.nextInt(100) + 1 < opponent.getLuck()) {
                                inflicted *= 2;
                                crit = true;
                            }
                            fightOutput.add(opponent.generateAttackString(inflicted,crit));
                            player.takeDamage(inflicted);
                        }
                        fightOutput.add(player.generateFleeString());
                    }
                }
            } else if (fightState == FightState.OUTPUT) { /* Generate an output window like FightMenu and remove when space is pressed */
                new FightOutputDisplay(fightOutput).draw(batch, screenWidth, screenHeight);
                if (readyForAction) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                        actionPressed = true;
                    } else if (actionPressed) {
                        actionPressed = false;
                        fightState = FightState.INPUT;
                        readyForAction = false;
                    }
                } else {
                    readyForAction = !(Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER));
                }
            } else if (fightState == FightState.FLE) { /* Generate an output window like FightMenu and end when space is pressed */
                new FightOutputDisplay(fightOutput).draw(batch, screenWidth, screenHeight);
                if (readyForAction) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                        actionPressed = true;
                    } else if (actionPressed) {
                        actionPressed = false;
                        fightMenu = null;
                        readyForAction = false;
                        player.setState(Player.State.PLAY);
                    }
                } else {
                    readyForAction = !(Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER));
                }
            }
            if (player.getHealth() == 0) {
                player.setState(Player.State.GAMEOVER);
            } else if (opponent.getHealth() == 0) {
                fightState = FightState.DONE;
                fightOutput.clear();
                fightOutput.add(opponent.getName() + " falls to the ground, dead.");
                fightOutput.add(player.getName() + " feeds on the body, replenishing health...");
                if (winCount == 0) {
                    winCount = player.addWin();
                }
                if (winCount % 5 == 0) {
                    fightOutput.add("You have won " + player.getLevel()*5 + " battles! Level up!");	
                }
                new FightOutputDisplay(fightOutput).draw(batch, screenWidth, screenHeight);
                if (readyForAction) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                        actionPressed = true;
                    } else if (actionPressed) {
                        actionPressed = false;
                        fightMenu = null;
                        player.setState(Player.State.PLAY);
                        readyForAction = false;
                        winCount = 0;
                        opponent.kill();
                    }
                } else {
                    readyForAction = !(Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER));
                }
            }
        }
    }
}
/* 
 * 
 * Write the FIGHT
 * 
 * Write the world parser
 *
 */
