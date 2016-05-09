package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseMenu extends Menu {

    public PauseMenu() {
        super("Pause Menu");
        Selectable[] options = new Selectable[4];
        Menu stats = new Menu("Stats");
        
        Selectable[] statOpts = new MenuItem[9];
        Player p = Player.getPlayer();
        statOpts[0] = new MenuItem("Name: " + p.getName());
        statOpts[1] = new MenuItem("Level: " + p.getLevel());
        statOpts[2] = new MenuItem("Health: " + p.getHealth() + "/" + p.getMaxHealth());
        String weaponString = "Weapon: " + p.getEquippedWeaponName();
        if (p.getEquippedWeapon() != null)
            weaponString = weaponString + " (+" + p.getEquippedWeapon().getAttack() + ")";
        statOpts[3] = new MenuItem(weaponString);
        statOpts[4] = new MenuItem("Attack: " + p.getAttack());
        statOpts[5] = new MenuItem("Defense: " + p.getDefense());
        statOpts[6] = new MenuItem("Strength: " + p.getStrength());
        statOpts[7] = new MenuItem("Agility: " + p.getAgility());
        statOpts[8] = new MenuItem("Luck: " + p.getLuck());
        stats.setOptions(statOpts);

         Menu weaps = new Menu("Weapons");
        
        
        options[0] = stats;
        options[1] = weaps;
        options[2] = new Menu("Armor");
        options[3] = new Menu("Keys");
        super.setOptions(options);
    }
}
