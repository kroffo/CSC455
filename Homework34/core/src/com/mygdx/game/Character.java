package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Character {
    private float maxVelocity;
    private Vector2 position, initialPosition;
    private Vector2 orientation, initialOrientation;
    private Vector2 velocity;
    private Sprite sprite;
    private float maxAcceleration = 0.02f, previousAngle = 0;
    

    public Character(Vector2 p, Vector2 o, float maxV) {
        position = p.cpy();
        initialPosition = p.cpy();
        orientation = o.nor().cpy();
        initialOrientation = orientation.cpy();
        velocity = new Vector2(0f,0f);
        maxVelocity = maxV;
    }

    public void reinitialize() {
        position = initialPosition.cpy();
        orientation = initialOrientation.cpy();
        sprite.setRotation(orientation.angle() - 90);
        zeroVelocity();
    }

    protected void setSprite(Sprite s) {
        sprite = s;
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        sprite.setPosition(position.x,position.y);
        sprite.rotate(orientation.angle() - 90);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getOrientation() {
        return orientation.cpy();
    }

    // direction < 0 : clockwise
    // direction > 0 : counter-clockwise
    public void rotate(int direction) {
        previousAngle = orientation.angle();
        if (direction == 0)
            return;
        float degrees;
        if (direction < 0)
            degrees = -3f;
        else
            degrees = 3f;
        orientation.rotate(degrees);
        sprite.rotate(degrees);
    }

    public void rotate(float degrees) {
        previousAngle = orientation.angle();
        orientation.rotate(degrees);
        sprite.rotate(degrees);
    }

    protected void setRotation(float degrees) {
        previousAngle = orientation.angle();
        orientation.setAngle(degrees);
        sprite.setRotation(degrees - 90);
    }
    
    public Vector2 getPosition() {
        return position.cpy();
    }

    public float getAccelerationRate() {
        return maxAcceleration;
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    protected void zeroVelocity() {
        velocity.setZero();
    }

    public void reverseVelocity(String axesString) {
        if (velocity.len() == 0) {
            setRotation(previousAngle);
        } else {
            String[] axes = axesString.split("");
            for (String axis : axes) {
                if (axis.equals("x")) {
                    if (Math.abs(velocity.x) < 1)
                        velocity.set(-1*Math.abs(velocity.x)/velocity.x,velocity.y);
                    else
                        velocity.set(-1*velocity.x,velocity.y);
                } else if (axis.equals("y")) {
                    if (Math.abs(velocity.y) < 1)
                        velocity.set(velocity.x,-1*Math.abs(velocity.y)/velocity.y);
                    else
                        velocity.set(velocity.x,-1*velocity.y);
                }
            }
        }
    }

    public void accelerate() {
        velocity.add(orientation.scl(maxAcceleration));
        orientation.nor();
        if (velocity.len() > maxVelocity)
            velocity.setLength(maxVelocity);
    }

    public void decelerate() {
        float initialV = velocity.len();
        velocity.sub(velocity.cpy().nor().scl(maxAcceleration));
        orientation.nor();
        if (initialV < velocity.len())
            velocity.setLength(0f);
    }

    public void accelerate(float acceleration) {
        velocity.add(orientation.scl(acceleration));
        orientation.nor();
        if (velocity.len() > maxVelocity)
            velocity.setLength(maxVelocity);
    }

    public void accelerate(Vector2 acceleration) {
        if (acceleration.len() > maxAcceleration)
            acceleration.setLength(maxAcceleration);
        velocity.add(acceleration);
        orientation.setAngle(velocity.angle());
        sprite.setRotation(orientation.angle()-90);
        if (velocity.len() > maxVelocity)
            velocity.setLength(maxVelocity);
    }
    
    public void step() {
        position.add(velocity);
        sprite.setPosition(position.x, position.y);
    }
}
