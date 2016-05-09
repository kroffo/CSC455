package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemyMenu extends Menu {
    
    public EnemyMenu(Enemy e) {
        super(e.getName() + " the " + e.getRace());
        Selectable[] options = new Selectable[2];
        Menu stats = new Menu("Stats");
        
        Selectable[] statOpts = new MenuItem[4];
        Player p = Player.getPlayer();
        statOpts[0] = new MenuItem("Name: " + e.getName());
        statOpts[1] = new MenuItem("Health: " + e.getHealth());
        statOpts[2] = new MenuItem("Strength: " + e.getStrength());
        statOpts[3] = new MenuItem("Weapon: " + e.getEquippedWeaponName());
        stats.setOptions(statOpts);
        
        options[0] = stats;
        options[1] = new FightOption();
        super.setOptions(options);
    }

}
