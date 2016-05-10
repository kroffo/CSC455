package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;

public class Enemy extends Creature {
    private String race;
    private boolean searched;
    private Behavior behavior;
    private int randomCycles;

    public enum Behavior {
        FLEE,
        CHASE,
        RANDOMSLOW,
        RANDOMFAST,
        STATIONARY
    }

    public Enemy(Sprite s, Sprite fs, Tile l, String name, int startHealth, int startStrength, int startDefense, int startAgility,
                 int startLuck, Satchel satchel, int speed, String race, String behave) {
        super(s, fs, l, name, startHealth, startStrength, startDefense, startAgility, startLuck, satchel, speed);
        this.race = race;
        searched = false;
        if (behave.equalsIgnoreCase("flee"))
            behavior = Behavior.FLEE;
        else if (behave.equalsIgnoreCase("chase"))
            behavior = Behavior.CHASE;
        else if (behave.equalsIgnoreCase("randomslow"))
            behavior = Behavior.RANDOMSLOW;
        else if (behave.equalsIgnoreCase("randomfast"))
            behavior = Behavior.RANDOMFAST;
        else if (behave.equalsIgnoreCase("stationary"))
            behavior = Behavior.STATIONARY;
        else
            behavior = Behavior.RANDOMSLOW;
    }

    public String getRace() {
        return race;
    }
	
    public String toString() {
        String rv = race + ":\t" + getName();
        if (isDead()) {
            rv = rv + "\t[DEAD]";
            if (searched) {
                rv = rv + "  (Searched)";
            }
        }
        return rv;
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
            if (dist < traversalSpeed) {
                transitioning = false;
                sprite.setPosition(location.getSprite().getX(), location.getSprite().getY());
            }
        } else if (behavior == Behavior.RANDOMSLOW || Player.getPlayer().getRoom() != location.getRoom()) {
            Random r = new Random();
            if (r.nextInt(10000) < ++randomCycles) {
                randomCycles = 0;
                int neighborIndex = r.nextInt(8);
                if (neighborIndex < 4) {
                    if (location.neighborAvailable(neighborIndex)) {
                        this.setOrientation(neighborIndex);
                        location.moveOccupant(neighborIndex);
                    }
                } else {
                    this.setOrientation(neighborIndex - 4);
                }
            }
        } else if (behavior == Behavior.RANDOMFAST) {
            Random r = new Random();
            if (r.nextInt(1000) < ++randomCycles) {
                randomCycles = 0;
                int neighborIndex = r.nextInt(8);
                if (neighborIndex < 4) {
                    if (location.neighborAvailable(neighborIndex)) {
                        this.setOrientation(neighborIndex);
                        location.moveOccupant(neighborIndex);
                    }
                } else {
                    this.setOrientation(neighborIndex - 4);
                }
            }
        } else if (behavior == Behavior.FLEE) {
            Sprite pSprite = Player.getPlayer().getLocation().getSprite();
            Vector2 playerPosition = new Vector2(pSprite.getX(), pSprite.getY());
            int targetNeighbor = location.getFurthestAvailableNeighbor(playerPosition);
            if (targetNeighbor != -1) {
                this.setOrientation(targetNeighbor);
                location.moveOccupant(targetNeighbor);
            }
        } else if (behavior == Behavior.CHASE) {
            Sprite pSprite = Player.getPlayer().getLocation().getSprite();
            Vector2 playerPosition = new Vector2(pSprite.getX(), pSprite.getY());
            int targetNeighbor = location.getNextPursuitTile(Player.getPlayer().getLocation());
            if (targetNeighbor != -1) {
                this.setOrientation(targetNeighbor);
                if (location.neighborAvailable(targetNeighbor))
                    location.moveOccupant(targetNeighbor);
            }
        }
        return !transitioning;
    }
	
    public void searched() {
        searched = true;
    }

    public void kill() {
        unequipItems();
        location.removeOccupant();
        Chest c = new Chest(sprite, location, getName() + "'s Satchel", false, null, satchel);
        location.setOccupant(c);
        location.getRoom().removeOccupant(this);
        location.getRoom().addOccupant(c);
        c.fixPosition();
    }    
}
