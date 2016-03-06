package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class Teleporter extends Cell {

    private Teleporter link;
    private String teleporterId;
    private boolean teleported;
    private Sprite teleportedSprite;
    
    public Teleporter(String id) {
        super("T" + id);
        teleporterId = id;
        teleportedSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/teleported.png")));
    }

    /* returns the correct sprite to draw based on the last path found */
    @Override
    public Sprite getSprite() {
        if (super.selected)
            return super.selectedSprite;
        else if (!super.visited)
            return super.unvisitedSprite;
        else if (super.crossed)
            return super.crossedSprite;
        else if (teleported)
            return teleportedSprite;
        else
            return super.visitedSprite;
    }

    /* Sets positions of sprites for this cell */
    @Override
    public void setPosition(int row, int col, float maxHeight) {
        float y = maxHeight - (float)((row+1) * unvisitedSprite.getHeight());
        float x = (float)(col*unvisitedSprite.getWidth());
        teleportedSprite.setPosition(x, y);
        super.setPosition(row, col, maxHeight);
    }

    public void setLink(Teleporter link) {
        this.link = link;
    }
    
    public Teleporter getLink() {
        return link;
    }

    public String getId() {
        return teleporterId;
    }

    public void teleport() {
        teleported = true;
    }

    public boolean teleported() {
        return teleported;
    }

    public void reset() {
        teleported = false;
    }

    public int getTerrainValue() {
        return 0;
    }
}
