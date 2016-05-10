package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Creature {

    public static enum State {
        PLAY,
        PAUSE,
        LOOT,
        FIGHT,
        EXAMINE,
        DOOR,
        CHEST,
        PEEK,
        GAMEOVER
    }
    
    private static Player player;
    private int wins;
    private int maxHealth;
    private State state = State.PLAY;
    private Room room;
    private Room peekRoom;
    private int peekFrames;
    private Chest lootChest;
    private Chest lockedChest;
    private Enemy examinee;
    private Door door;
    private String fightOption;

    private Player(Sprite s, Sprite fs, Tile l, Room r, String name, Satchel satchel) {
        super(s, fs, l, name, 10, 4, 2, 20, 20, satchel, 2);
        room = r;
        orientation = 0;
    }

    public static void createPlayer(Sprite s, Sprite fs, Tile l, Room r, String name, Satchel satchel) {
        if (Player.getPlayer() == null) {
            player = new Player(s, fs, l, r, name, satchel);
        }
    }

    public static Player getPlayer() {
        return player;
    }

    public boolean step() {
        if (transitioning) {
            Vector2 target = new Vector2(location.getSprite().getX(), location.getSprite().getY());
            Vector2 position = new Vector2(sprite.getX(), sprite.getY());
            Vector2 velocity = target.sub(position).nor().scl(traversalSpeed);
            sprite.translate(velocity.x, velocity.y);
            target = new Vector2(location.getSprite().getX(), location.getSprite().getY());
            position = new Vector2(sprite.getX(), sprite.getY());
            float dist = (target.sub(position)).len();
            if (dist < 1)
                transitioning = false;
        } else {
            sprite.setPosition(location.getSprite().getX(), location.getSprite().getY());
        }
        return !transitioning;
    }

    public Room getRoom() {
        return room;
    }

    public State getState() {
        return state;
    }

    public void setState(State s) {
        state = s;
    }
    
    public void lootChest(Chest c) {
        lootChest = c;
        state = State.LOOT;
    }

    public void removeLootChest() {
        lootChest = null;
        state = State.PLAY;
    }

    public Chest getLootChest() {
        return lootChest;
    }

    public void examineEnemy(Enemy e) {
        examinee = e;
        state = State.EXAMINE;
    }

    public void removeExaminee() {
        examinee = null;
        state = State.PLAY;
    }

    public Enemy getExaminee() {
        return examinee;
    }

    public void examineDoor(Door d) {
        door = d;
        state = State.DOOR;
    }

    public void removeDoor() {
        door = null;
        state = State.PLAY;
    }
    
    public Door getDoor() {
        return door;
    }

    public void examineChest(Chest c) {
        lockedChest = c;;
        state = State.CHEST;
    }

    public void removeChest() {
        lockedChest = null;;
        state = State.PLAY;
    }
    
    public Chest getChest() {
        return lockedChest;
    }

    public void startFight() {
        if (examinee == null)
            state = State.PLAY;
        else
            state = State.FIGHT;
    }

    public void select(MenuItem m) {
        Item i = m.getItem();
        if (i instanceof Weapon) {
            if (state == State.PAUSE) {
                if (i.isEquipped())
                    unequipWeapon(i.getName());
                else 
                    equipWeapon(i.getName());
            }
        } else if (i instanceof Armor) {
            if (state == State.PAUSE) {
                if (i.isEquipped())
                    unequipArmor(i.getName());
                else
                    equipArmor(i.getName());
            } 
        } else if (state == State.DOOR) {
            if (m.toString().equalsIgnoreCase("Enter")) {
                boolean doorUnlocked = false;
                if (door.isLocked()) {
                    if (satchel.containsKey(door.getCorrectKeyName())) {
                        Key k = satchel.removeKey(door.getCorrectKeyName());
                        door.unlock(k);
                        doorUnlocked = true;
                    }
                } else {
                    doorUnlocked = true;
                }
                if (doorUnlocked) {
                    Tile newLocation = door.getOtherTile(room);
                    Room newRoom = newLocation.getRoom();
                    location.removeOccupant();
                    newLocation.setOccupant(this);
                    room.removeOccupant(this);
                    newRoom.addOccupant(this);
                    location = newLocation;
                    room = newRoom;
                }
                state = State.PLAY;
            } else if (m.toString().equalsIgnoreCase("Peek")) {
                boolean doorUnlocked = false;
                if (door.isLocked()) {
                    if (satchel.containsKey(door.getCorrectKeyName())) {
                        Key k = satchel.removeKey(door.getCorrectKeyName());
                        door.unlock(k);
                        doorUnlocked = true;
                    }
                } else {
                    doorUnlocked = true;
                }
                if (doorUnlocked) {
                    peekRoom = door.getOtherTile(room).getRoom();
                    peekFrames = 0;
                    state = State.PEEK;
                } else {
                    state = State.PLAY;
                }
            }
        } else if (state == State.CHEST) {
            if (m.toString().equalsIgnoreCase("Unlock")) {
                boolean chestUnlocked = false;
                if (lockedChest.isLocked()) {
                    if (satchel.containsKey(lockedChest.getCorrectKeyName())) {
                        Key k = satchel.removeKey(lockedChest.getCorrectKeyName());
                        lockedChest.unlock(k);
                        chestUnlocked = true;
                    }
                } else {
                    chestUnlocked = true;
                }
                state = State.PLAY;
            }
        }
    }
    
    public void select(LootItem m) {
        Item i = m.getItem();
        if (i instanceof Weapon) {
            if (state == State.LOOT) {
                takeWeaponFromChest(lootChest, i.getName());
            }
        } else if (i instanceof Armor) {
            if (state == State.LOOT) {
                takeArmorFromChest(lootChest, i.getName());
            }
        } else if (i instanceof Key) {
            if (state == State.LOOT) {
                takeKeyFromChest(lootChest, i.getName());
            }
        }
    }

    public Room getPeekRoom() {
        if (++peekFrames == 300) {
            state = State.PLAY;
        }
        return peekRoom;
    }
    
    public void takeWeaponFromChest(Chest chest, String weaponName) {
        if (chest.containsWeapon(weaponName)) { //See if chest has the item
            satchel.addWeapon(chest.getContents().removeWeapon(weaponName));
        }
    }
	
    public void takeArmorFromChest(Chest chest, String armorName) {
        if (chest.containsArmor(armorName)) { //See if chest has the item
            satchel.addArmor(chest.getContents().removeArmor(armorName));
        }
    }
	
    public void takeKeyFromChest(Chest chest, String keyName) {
        if (chest.containsKey(keyName)) { //See if chest has the item
            satchel.addKey(chest.getContents().removeKey(keyName));
        }
    }

    public int getLevel() {
        return wins/5 + 1;
    }
    
    public void selectFightOption(String name) {
        fightOption = name;
    }

    public void resetFightOption() {
        fightOption = null;
    }

    public String getFightOption() {
        return fightOption;
    }

    public int addWin() {
        if (++wins % 5 == 0) {
            levelUp();
        }
        increaseHealth(examinee.getMaxHealth()/10);
        return wins;
    }
}
