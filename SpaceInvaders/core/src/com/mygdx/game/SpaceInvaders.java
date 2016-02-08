package com.mygdx.game;

import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;


public class SpaceInvaders extends ApplicationAdapter {
    Random randy = new Random();
    SpriteBatch batch;
    Sprite[][][] invaderSprites;
    Sprite playerShip;
    Sprite bullet;
    Sprite alienShip;
    Sprite[] numbers;
    Sprite score;
    int barriers[][];
    int leftMost = 0;
    int rightMost = 10;
    int bottomMostI = 4;
    int bottomMostJ = 0;
    int lives = 2;
    float displacementX;
    Sprite[] barrierLeftCorner;
    Sprite[] barrierRightCorner;
    Sprite[] barrierMidLeft;
    Sprite[] barrierMidRight;
    Sprite[] barrierSquare;
    Sprite[][][] barrierPosition;
    Sprite explosion, extraLife, shipsplosion, levelSprite, gameOverSprite,
        resetMessage, finalScore, redShip, greenShip, blueShip;
    boolean[][] invaders;
    Sprite[] invaderBullets;
    boolean[] invaderBulletsShot;
    Sprite[] autoModeBullets;
    boolean[] autoModeBulletsShot;
    float elapsedTime = 0, nextTime;
    int screenWidth, screenHeight, i = 1, state,
        currentFrame, horizontalBase = 120, verticalBase = 400,
        barrierHeight = 170, numberAlive = 55, stepSize = 5, maxAutoBullets = 150,
        displayExplosion, level, playerHeight = 80, shootRate,
        killedSinceIncreaseInShootRate, scoreValue = 0, displayShipsplosion;
    String currentAtlasKey = new String("0001");
    boolean headingLeft = true, bulletShot = false, alienCrossedLeft = false,
        alienTraveling = false, gameOver = false, shipDied = false, pause = false,
        autoMode = false;
    Sound shootSound, explosionSound, invaderShootSound, playerExplodeSound;
    
    @Override
    public void create () {
        batch = new SpriteBatch();
        shootSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/shoot.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/explosion.wav"));
        invaderShootSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/invaderShoot.wav"));
        playerExplodeSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/playerExplode.wav"));
        score = new Sprite(new Texture(Gdx.files.internal("Sprites/Score/Score.png")));
        score.setScale(2,2);
        finalScore = new Sprite(new Texture(Gdx.files.internal("Sprites/Score/Score.png")));
        finalScore.setScale(3,3);
        gameOverSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/GameOver.png")));
        gameOverSprite.setScale(4,4);
        resetMessage = new Sprite(new Texture(Gdx.files.internal("Sprites/ResetMessage.png")));
        resetMessage.setScale(2,2);
        shipsplosion = new Sprite(new Texture(Gdx.files.internal("Sprites/PlayerShip/Explosion.png")));
        shipsplosion.setScale(2,2);
        levelSprite = new Sprite(new Texture(Gdx.files.internal("Sprites/Score/Level.png")));
        levelSprite.setScale(2,2);
        numbers = new Sprite[10];
        for (int i = 0; i < 10; i++) {
            numbers[i] = new Sprite(new Texture(Gdx.files.internal("Sprites/Score/" + i + ".png")));
            numbers[i].setScale(2,2);
        }
        greenShip = new Sprite(new Texture(Gdx.files.internal("Sprites/PlayerShip/PlayerShip.png")));
        redShip = new Sprite(new Texture(Gdx.files.internal("Sprites/PlayerShip/RedShip.png")));
        blueShip = new Sprite(new Texture(Gdx.files.internal("Sprites/PlayerShip/BlueShip.png")));
        playerShip = greenShip;
        extraLife = new Sprite(new Texture(Gdx.files.internal("Sprites/PlayerShip/PlayerShip.png")));
        extraLife.setScale(2,2);
        explosion = new Sprite(new Texture(Gdx.files.internal("Sprites/Explosion.png")));
        explosion.setScale(2,2);
        explosion.setOrigin(4,4);
        greenShip.setScale(2,2);
        greenShip.setOrigin(15,5);
        redShip.setScale(2,2);
        redShip.setOrigin(15,5);
        blueShip.setScale(2,2);
        blueShip.setOrigin(15,5);
        bullet = new Sprite(new Texture(Gdx.files.internal("Sprites/Bullet.png")));
        alienShip = new Sprite(new Texture(Gdx.files.internal("Sprites/SpaceShip/SpaceShip.png")));
        alienShip.setScale(2,2);
        barriers = new int[4][10];
        barrierPosition = new Sprite[4][10][4];
        barrierLeftCorner = new Sprite[4];
        barrierRightCorner = new Sprite[4];
        barrierMidLeft = new Sprite[4];
        barrierMidRight = new Sprite[4];
        barrierSquare = new Sprite[4];        
        for (int i = 0; i < 4; i++) { // add barrier sprites
            for (int j = 0; j < 4; j++) {
                barrierPosition[i][0][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/LeftCorner/000" + (j+1) + ".png")));
                barrierPosition[i][3][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/RightCorner/000" + (j+1) + ".png")));
                barrierPosition[i][6][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/MidLeft/000" + (j+1) + ".png")));
                barrierPosition[i][5][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/MidRight/000" + (j+1) + ".png")));
                barrierPosition[i][1][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/Square/000" + (j+1) + ".png")));
                barrierPosition[i][2][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/Square/000" + (j+1) + ".png")));
                barrierPosition[i][4][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/Square/000" + (j+1) + ".png")));
                barrierPosition[i][7][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/Square/000" + (j+1) + ".png")));;
                barrierPosition[i][8][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/Square/000" + (j+1) + ".png")));
                barrierPosition[i][9][j] = new Sprite(new Texture(Gdx.files.internal("Sprites/Barrier/Square/000" + (j+1) + ".png")));
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 4; k++) {
                    barrierPosition[i][j][k].setScale(2,2);
                }
            }
        }
        invaderSprites = new Sprite[5][11][2];
        invaders = new boolean[5][11];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 11; j++) {
                invaderSprites[i][j][0] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader3/0001.png")));
                invaderSprites[i][j][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader3/0002.png")));
                invaderSprites[i][j][0].setScale(2f,2f);
                invaderSprites[i][j][0].setOrigin(invaderSprites[i][j][0].getWidth()/2, invaderSprites[i][j][0].getHeight()/2);
                invaderSprites[i][j][1].setScale(2f,2f);
                invaderSprites[i][j][1].setOrigin(invaderSprites[i][j][1].getWidth()/2, invaderSprites[i][j][1].getHeight()/2);
                invaders[i][j] = true;
            }
        }
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                invaderSprites[i][j][0] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader1/0001.png")));
                invaderSprites[i][j][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader1/0002.png")));
                invaderSprites[i][j][0].setScale(2f,2f);
                invaderSprites[i][j][0].setOrigin(invaderSprites[i][j][0].getWidth()/2, invaderSprites[i][j][0].getHeight()/2);
                invaderSprites[i][j][1].setScale(2f,2f);
                invaderSprites[i][j][1].setOrigin(invaderSprites[i][j][1].getWidth()/2, invaderSprites[i][j][1].getHeight()/2);
                invaders[i][j] = true;
            }
        }
        for (int i = 3; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                invaderSprites[i][j][0] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader2/0001.png")));
                invaderSprites[i][j][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Invader2/0002.png")));
                invaderSprites[i][j][0].setScale(2f,2f);
                invaderSprites[i][j][0].setOrigin(invaderSprites[i][j][0].getWidth()/2, invaderSprites[i][j][0].getHeight()/2);
                invaderSprites[i][j][1].setScale(2f,2f);
                invaderSprites[i][j][1].setOrigin(invaderSprites[i][j][1].getWidth()/2, invaderSprites[i][j][1].getHeight()/2);
                invaders[i][j] = true;
            }
        }
        invaderBullets = new Sprite[10];
        invaderBulletsShot = new boolean[10];
        for (int i = 0; i < 10; i++) {
            invaderBullets[i] = new Sprite(new Texture(Gdx.files.internal("Sprites/Bullet.png")));
            invaderBulletsShot[i] = false;
        }
        autoModeBullets = new Sprite[maxAutoBullets];
        autoModeBulletsShot = new boolean[maxAutoBullets];
        for (int i = 0; i < maxAutoBullets; i++) {
            autoModeBullets[i] = new Sprite(new Texture(Gdx.files.internal("Sprites/Bullet.png")));
            autoModeBulletsShot[i] = false;
        }
        displacementX = invaderSprites[0][0][0].getWidth()*2;
        initializeLevel();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shootSound.dispose();
        explosionSound.dispose();
    }
    
    @Override
    public void render () {
        if (numberAlive == 0) initializeLevel();
        if (currentFrame == 0) {
            playerShip.setPosition(screenWidth/2, playerHeight);
            score.setPosition(20, screenHeight - 20);
            levelSprite.setPosition(20, 10);
            gameOverSprite.setPosition(screenWidth/2, screenHeight/2);
            resetMessage.setPosition(screenWidth/2 - 35, screenHeight/2 - 40);
            finalScore.setPosition(screenWidth/2 - 25, screenHeight/2 + 40);
        }
        currentFrame++;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (pause) {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                pause = false;
            }
            playerShip.draw(batch);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 11; j++) {
                    if (invaders[i][j]) {
                        invaderSprites[i][j][state].setPosition(horizontalBase + j*(invaderSprites[0][0][0].getWidth()*2), verticalBase - (invaderSprites[0][0][0].getHeight()*2 + 10)*i);
                        invaderSprites[i][j][state].draw(batch);
                    }
                }
            }
            alienShip.setPosition(-30,420);
            alienTraveling = false;
            score.draw(batch);
            levelSprite.draw(batch);
            String levelString = "" + level;
            for (int i = 0; i < levelString.length(); i++) {
                Sprite digit = numbers[Integer.parseInt(levelString.substring(i,i+1))];
                digit.setPosition(80 + i*10, levelSprite.getY());
                digit.draw(batch);
            }
            String scoreString = "" + scoreValue;
            for (int i = 0; i < scoreString.length(); i++) {
                Sprite digit = numbers[Integer.parseInt(scoreString.substring(i,i+1))];
                digit.setPosition(80 + i*10, score.getY());
                digit.draw(batch);
            }
            for (int i = 0; i < lives; i++) {
                extraLife.setPosition(screenWidth - 200 + (5 + extraLife.getWidth()*2)*i, score.getY());
                extraLife.draw(batch);
            }
            for (int i = 0; i < barriers.length; i++) {
                for (int j = 0; j < 10; j++) {
                    if (barriers[i][j] < 4) {
                        int offsetX = 0, offsetY = 0;
                        if (j < 9)
                            offsetX = (j%4)*8*2;
                        else
                            offsetX = 24*2;
                        if (j > 7)
                            offsetY = 16*2;
                        else if (j > 3)
                            offsetY = 8*2;
                        barrierPosition[i][j][barriers[i][j]].setPosition(150*i + 50+offsetX, barrierHeight - offsetY);
                        barrierPosition[i][j][barriers[i][j]].draw(batch);
                    }
                }
            }
        } else {
            if (displayShipsplosion > 0) {
                shipsplosion.draw(batch);
                if (displayShipsplosion++ > 100) displayShipsplosion = 0;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 11; j++) {
                        if (invaders[i][j]) {
                            invaderSprites[i][j][state].setPosition(horizontalBase + j*(invaderSprites[0][0][0].getWidth()*2), verticalBase - (invaderSprites[0][0][0].getHeight()*2 + 10)*i);
                            invaderSprites[i][j][state].draw(batch);
                        }
                    }
                }
                alienShip.setPosition(-30,420);
                alienTraveling = false;
                score.draw(batch);
                levelSprite.draw(batch);
                String levelString = "" + level;
                for (int i = 0; i < levelString.length(); i++) {
                    Sprite digit = numbers[Integer.parseInt(levelString.substring(i,i+1))];
                    digit.setPosition(80 + i*10, levelSprite.getY());
                    digit.draw(batch);
                }
                String scoreString = "" + scoreValue;
                for (int i = 0; i < scoreString.length(); i++) {
                    Sprite digit = numbers[Integer.parseInt(scoreString.substring(i,i+1))];
                    digit.setPosition(80 + i*10, score.getY());
                    digit.draw(batch);
                }
                for (int i = 0; i < lives; i++) {
                    extraLife.setPosition(screenWidth - 200 + (5 + extraLife.getWidth()*2)*i, score.getY());
                    extraLife.draw(batch);
                }
                for (int i = 0; i < 10; i++) {
                    invaderBulletsShot[i] = false;
                }
                for (int i = 0; i < maxAutoBullets; i++) {
                    autoModeBulletsShot[i] = false;
                }
                bulletShot = false;
                for (int i = 0; i < barriers.length; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (barriers[i][j] < 4) {
                            int offsetX = 0, offsetY = 0;
                            if (j < 9)
                                offsetX = (j%4)*8*2;
                            else
                                offsetX = 24*2;
                            if (j > 7)
                                offsetY = 16*2;
                            else if (j > 3)
                                offsetY = 8*2;
                            barrierPosition[i][j][barriers[i][j]].setPosition(150*i + 50+offsetX, barrierHeight - offsetY);
                            barrierPosition[i][j][barriers[i][j]].draw(batch);
                        }
                    }
                }
            } else {
                if (!gameOver) {
                    if (displayExplosion > 0) {
                        explosion.draw(batch);
                        if (displayExplosion++ > 3) displayExplosion = 0;
                    }
                    elapsedTime += Gdx.graphics.getDeltaTime();
                    if (elapsedTime > nextTime) {
                        if (++state == 2) state = 0;
                        if (headingLeft) {
                            horizontalBase -= stepSize;
                            if (horizontalBase + displacementX*leftMost < 20) {
                                horizontalBase += stepSize;
                                headingLeft = false;
                                verticalBase -= 13;
                                if (invaderSprites[bottomMostI][bottomMostJ][state].getY() < 170) gameOver = true;
                            }
                        } else {
                            horizontalBase += stepSize;
                            if (horizontalBase + (displacementX*rightMost) > screenWidth - 20) {
                                horizontalBase -= stepSize;
                                headingLeft = true;
                                verticalBase -= 13;
                                if (invaderSprites[bottomMostI][bottomMostJ][state].getY() < 170) gameOver = true;
                            }
                        }
                        nextTime += getSpeed();
                    }
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 11; j++) {
                            if (invaders[i][j]) {
                                invaderSprites[i][j][state].setPosition(horizontalBase + j*(invaderSprites[0][0][0].getWidth()*2), verticalBase - (invaderSprites[0][0][0].getHeight()*2 + 10)*i);
                                invaderSprites[i][j][state].draw(batch);
                            }
                        }
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        if (playerShip.getX() > 20)
                            playerShip.translateX(-2f);
                    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        if (playerShip.getX() < screenWidth - 20)
                            playerShip.translateX(2f);
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                        if (!autoMode) {
                            if (!bulletShot) {
                                bullet.setPosition(playerShip.getX(), playerShip.getY());
                                bulletShot = true;
                                shootSound.play(0.5f);
                            }
                        } else {
                            for (int i = 0; i < maxAutoBullets; i++) {
                                if (!autoModeBulletsShot[i]) {
                                    autoModeBulletsShot[i] = true;
                                    autoModeBullets[i].setPosition(playerShip.getX(), playerShip.getY());
                                    shootSound.play(0.5f);
                                    break;
                                }
                            }
                        }
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                            if (Gdx.input.isKeyPressed(Input.Keys.U)) {
                                if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                                    changeColor("b");
                                }
                            }
                        }
                    } 
                    if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                                changeColor("r");
                            }
                        }
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                                if (Gdx.input.isKeyPressed(Input.Keys.N)) {
                                    changeColor("g");
                                }
                            }
                        }
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
                            if (Gdx.input.isKeyPressed(Input.Keys.N)) {
                                autoMode = true;
                            }
                        }
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
                            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                                autoMode = false;
                            }
                        }
                    }
                    for (int k = 0; k < 10; k++) {
                        if (invaderBulletsShot[k]) {
                            Sprite collided = spriteShotByInvader(invaderBullets[k]);
                            if (collided != null) {
                                if (collided == playerShip) {
                                    removeAllBullets();
                                    playerExplodeSound.play();
                                    lives--;
                                    if (lives < 0) gameOver = true;
                                    displayShipsplosion = 1;
                                    shipsplosion.setPosition(collided.getX(), collided.getY());
                                } else {
                                    for (int i = 0; i < 4; i++) {
                                        for (int j = 0; j < 10; j++) {
                                            if (collided == barrierPosition[i][j][0]) {
                                                barriers[i][j]++;
                                                invaderBulletsShot[k] = false;
                                                explosionSound.play(1.0f);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!autoMode) {
                        if (bulletShot) {
                            Sprite collided = spriteShotByPlayer(bullet, state);
                            if (collided != null) {
                                if (collided == alienShip) {
                                    int thousands = scoreValue / 1000;
                                    scoreValue += 50;
                                    if (thousands < scoreValue / 1000) lives++;
                                    alienTraveling = false;
                                    displayExplosion++;
                                    explosion.setPosition(collided.getX(), collided.getY());
                                    explosion.draw(batch);
                                    alienShip.setPosition(-30,420);
                                    bulletShot = false;
                                    explosionSound.play(1.0f);
                                }
                                for (int i = 0; i < 4; i++) {
                                    for (int j = 0; j < 10; j++) {
                                        if (collided == barrierPosition[i][j][0]) {
                                            barriers[i][j]++;
                                            bulletShot = false;
                                            explosionSound.play(1.0f);
                                        }
                                    }
                                }
                                for (int i = 0; i < 5; i++) {
                                    for (int j = 0; j < 11; j++) {
                                        if (collided == invaderSprites[i][j][state]) {
                                            increaseScore(i);
                                            invaders[i][j] = false;
                                            bulletShot = false;
                                            numberAlive--;
                                            displayExplosion++;
                                            explosionSound.play(1.0f);
                                            explosion.setPosition(collided.getX(), collided.getY());
                                            explosion.draw(batch);
                                            if (j == leftMost) calculateLeftMost();
                                            else if (j == rightMost) calculateRightMost();
                                            if (collided == invaderSprites[bottomMostI][bottomMostJ][state]) calculateBottomMost();
                                            break;
                                        }
                                    }
                                }
                            } else {
                                bullet.translateY(4f);
                                if (bullet.getY() > 440)
                                    bulletShot = false;
                                else
                                    bullet.draw(batch);
                            }
                        }
                    } else {
                        for (int k = 0; k < maxAutoBullets; k++) {
                            if (autoModeBulletsShot[k]) {
                                Sprite collided = spriteShotByPlayer(autoModeBullets[k], state);
                                if (collided != null) {
                                    if (collided == alienShip) {
                                        int thousands = scoreValue / 1000;
                                        scoreValue += 50;
                                        if (thousands < scoreValue / 1000) lives++;
                                        alienTraveling = false;
                                        displayExplosion++;
                                        explosion.setPosition(collided.getX(), collided.getY());
                                        explosion.draw(batch);
                                        alienShip.setPosition(-30,420);
                                        autoModeBulletsShot[k] = false;
                                        explosionSound.play(1.0f);
                                    }
                                    for (int i = 0; i < 4; i++) {
                                        for (int j = 0; j < 10; j++) {
                                            if (collided == barrierPosition[i][j][0]) {
                                                barriers[i][j]++;
                                                autoModeBulletsShot[k] = false;
                                                explosionSound.play(1.0f);
                                            }
                                        }
                                    }
                                    for (int i = 0; i < 5; i++) {
                                        for (int j = 0; j < 11; j++) {
                                            if (collided == invaderSprites[i][j][state]) {
                                                increaseScore(i);
                                                invaders[i][j] = false;
                                                autoModeBulletsShot[k] = false;
                                                numberAlive--;
                                                displayExplosion++;
                                                explosionSound.play(1.0f);
                                                explosion.setPosition(collided.getX(), collided.getY());
                                                explosion.draw(batch);
                                                if (j == leftMost) calculateLeftMost();
                                                else if (j == rightMost) calculateRightMost();
                                                if (collided == invaderSprites[bottomMostI][bottomMostJ][state]) calculateBottomMost();
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    autoModeBullets[k].translateY(4f);
                                    if (autoModeBullets[k].getY() > 440)
                                        autoModeBulletsShot[k] = false;
                                    else
                                        autoModeBullets[k].draw(batch);
                                }
                            }
                        }
                    }
                    playerShip.draw(batch);
                    if (currentFrame % 1500 == 0) {
                        alienTraveling = true;
                        if (alienCrossedLeft)
                            alienShip.setPosition(-30,420);
                        else
                            alienShip.setPosition(screenWidth + 30, 420);
                        alienCrossedLeft = !alienCrossedLeft;
                    }
                    for (int i = 0; i < 10; i++) {
                        if (invaderBulletsShot[i]) {
                            invaderBullets[i].translateY(-4f);
                            if (invaderBullets[i].getY() < 0)
                                invaderBulletsShot[i] = false;
                            else
                                invaderBullets[i].draw(batch);
                        }
                    }
                    if (alienTraveling) {
                        boolean stopAlien = false;
                        if (!alienCrossedLeft) {
                            alienShip.translateX(1.0f);
                            stopAlien = alienShip.getX() > screenWidth;
                        } else {
                            alienShip.translateX(-1.0f);
                            stopAlien = alienShip.getX() < 0;
                        }
                        if (stopAlien) {
                            alienTraveling = false;
                        }
                        alienShip.draw(batch);
                    }
                    for (int i = 0; i < barriers.length; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (barriers[i][j] < 4) {
                                int offsetX = 0, offsetY = 0;
                                if (j < 9)
                                    offsetX = (j%4)*8*2;
                                else
                                    offsetX = 24*2;
                                if (j > 7)
                                    offsetY = 16*2;
                                else if (j > 3)
                                    offsetY = 8*2;
                                barrierPosition[i][j][barriers[i][j]].setPosition(150*i + 50+offsetX, barrierHeight - offsetY);
                                barrierPosition[i][j][barriers[i][j]].draw(batch);
                            }
                        }
                    }
            
                    if (getNumberOfAlienBullets() < 10 && randy.nextInt(20000) < shootRate) makeAlienShoot(state);
                    score.draw(batch);
                    levelSprite.draw(batch);
                    String levelString = "" + level;
                    for (int i = 0; i < levelString.length(); i++) {
                        Sprite digit = numbers[Integer.parseInt(levelString.substring(i,i+1))];
                        digit.setPosition(80 + i*10, levelSprite.getY());
                        digit.draw(batch);
                    }
                    String scoreString = "" + scoreValue;
                    for (int i = 0; i < scoreString.length(); i++) {
                        Sprite digit = numbers[Integer.parseInt(scoreString.substring(i,i+1))];
                        digit.setPosition(80 + i*10, score.getY());
                        digit.draw(batch);
                    }
                    for (int i = 0; i < lives; i++) {
                        extraLife.setPosition(screenWidth - 200 + (5 + extraLife.getWidth()*2)*i, score.getY());
                        extraLife.draw(batch);
                    }
                } else {
                    finalScore.draw(batch);
                    String scoreString = "" + scoreValue;
                    for (int i = 0; i < scoreString.length(); i++) {
                        Sprite digit = numbers[Integer.parseInt(scoreString.substring(i,i+1))];
                        digit.setScale(3,3);
                        digit.setPosition(finalScore.getX() + 80 + i*15, finalScore.getY());
                        digit.draw(batch);
                        digit.setScale(2,2);
                    }
                    gameOverSprite.draw(batch);
                    resetMessage.draw(batch);
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                        scoreValue = 0;
                        level = 0;
                        lives = 2;
                        initializeLevel();
                        gameOver = false;
                        resetBarriers();
                        removeAllBullets();
                    }
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.P)) {
                pause = true;
            }
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = /*width*/640;
        screenHeight = height;
    }
    
    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private float getSpeed() {
        if (++killedSinceIncreaseInShootRate == 11) {
            shootRate += 10;
            killedSinceIncreaseInShootRate = 0;
        }
        return numberAlive/(55.0f + 20*level);
    }

    private Sprite spriteShotByPlayer(Sprite bulletShot, int state) {
        Rectangle bulletBox = bulletShot.getBoundingRectangle();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                if (invaders[i][j] && collisionCheck(invaderSprites[i][j][state].getBoundingRectangle(),bulletBox)) {
                    return invaderSprites[i][j][state];
                }
            }
        }
        if (collisionCheck(alienShip.getBoundingRectangle(),bulletBox)) {
            return alienShip;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                if (barriers[i][j] < 4 && collisionCheck(barrierPosition[i][j][0].getBoundingRectangle(),bulletBox)) {
                    return barrierPosition[i][j][0];
                }
            }
        }
        return null;
    }

    private Sprite spriteShotByInvader(Sprite bulletShot) {
        Rectangle bulletBox = bulletShot.getBoundingRectangle();
        if (collisionCheck(playerShip.getBoundingRectangle(),bulletBox)) {
            return playerShip;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                if (barriers[i][j] < 4 && collisionCheck(barrierPosition[i][j][0].getBoundingRectangle(),bulletBox)) {
                    return barrierPosition[i][j][0];
                }
            }
        }
        return null;
    }

    private boolean collisionCheck(Rectangle a, Rectangle b) {
        double x1 = a.getX(), y1 = a.getY(), x2 = a.getX() + a.getWidth(), y2 = a.getY() + a.getHeight();
        if (x1 < b.getX()) x1 = b.getX();
        if (y1 < b.getY()) y1 = b.getY();
        if (x2 > (b.getX() + b.getWidth())) x2 = b.getX() + b.getWidth();
        if (y2 > (b.getY() + b.getHeight())) y2 = b.getY() + b.getHeight();
        return x1 < x2 && y1 < y2;
    }

    private void calculateLeftMost() {
        for (int j = leftMost; j <= rightMost; j++) {
            for (int i = 0; i < 5; i++) {
                if (invaders[i][j]) {
                    leftMost = j;
                    return;
                }
            }
        }
    }

    private void calculateRightMost() {
        for (int j = rightMost; j >= leftMost; j--) {
            for (int i = 0; i < 5; i++) {
                if (invaders[i][j]) {
                    rightMost = j;
                    return;
                }
            }
        }
    }

    private void calculateBottomMost() {
        for (int i = 4; i >=0; i--) {
            for (int j = leftMost; j < rightMost; j++) {
                if (invaders[i][j]) {
                    bottomMostI = i;
                    bottomMostJ = j;
                    return;
                }
            }
        }
    }

    private void initializeLevel() {
        horizontalBase = 120;
        verticalBase = 400;
        numberAlive = 55;
        displayExplosion = 0;
        leftMost = 0;
        rightMost = 10;
        bottomMostI = 4;
        bottomMostJ = 0;
        elapsedTime = 0;
        headingLeft = true;
        bulletShot = false;
        alienCrossedLeft = false;
        alienTraveling = false;
        gameOver = false;
        for (int j = 0; j < 10; j++) {
            invaderBulletsShot[i] = false;
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                invaders[i][j] = true;
            }
        }
        nextTime = getSpeed();
        level++;
        shootRate = level*10;
        if (shootRate > 50) shootRate = 50;
    }

    private int getNumberOfAlienBullets() {
        int count = 0;
        for (boolean shot : invaderBulletsShot) {
            if (shot) count++;
        }
        return count;
    }

    private void makeAlienShoot(int state) {
        int i = randy.nextInt(5);
        int j = randy.nextInt(11);
        while (!invaders[i][j]) {
            i = randy.nextInt(5);
            j = randy.nextInt(11);
        }
        Sprite shooter = invaderSprites[i][j][state];
        for (int k = 0; k < 10; k++) {
            if (!invaderBulletsShot[k]) {
                invaderBulletsShot[k] = true;
                invaderBullets[k].setPosition(shooter.getX(), shooter.getY());
                invaderShootSound.play();
                break;
            }
        }
    }

    private void changeColor(String color) {
        if (color.equals("r")) {
            redShip.setPosition(playerShip.getX(), playerShip.getY());
            playerShip = redShip;
        } else if (color.equals("g")) {
            greenShip.setPosition(playerShip.getX(), playerShip.getY());
            playerShip = greenShip;
        } else if (color.equals("b")) {
            blueShip.setPosition(playerShip.getX(), playerShip.getY());
            playerShip = blueShip;
        }
    }

    private void increaseScore(int row) {
        int thousands = scoreValue / 1000;
        if (row == 0) {
            scoreValue += 30;
        } else if (row == 1 || row == 2) {
            scoreValue += 20;
        } else {
            scoreValue += 10;
        }
        if (thousands < scoreValue / 1000) lives++;
    }

    private void resetBarriers() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                barriers[i][j] = 0;
            }
        }
    }

    private void removeAllBullets() {
        bulletShot = false;
        for (int i = 0; i < 10; i++)
            invaderBulletsShot[i] = false;
        for (int i = 0; i < maxAutoBullets; i++)
            autoModeBulletsShot[i] = false;
    }
}
