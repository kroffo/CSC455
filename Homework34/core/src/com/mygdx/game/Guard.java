package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Guard extends Character {

    public enum State {
        GUARD,
        CHASE
    }

    private State state;
    private Character target;
    private Vector2 guardPosition;
    private boolean inPosition, arriving = false, rotatingToStartPosition = false, rotatingLeft = true;
    private int rotationCounter;
    private float sightRange = 150f;
    private Rectangle avoidingObject = null;

    
    
    
    public Guard(Vector2 p, Vector2 o, Character t) {
        super(p, o, 1f);
        Sprite sprite = new Sprite(new Texture(Gdx.files.internal("Sprites/Guard.png")));
        sprite.setScale(0.5f);
        super.setSprite(sprite);
        state = State.GUARD;
        target = t;
        guardPosition = p.cpy();
        inPosition = true;
    }

    public void step(Sprite[] collidables) {
        if (state == State.GUARD) {
            if (targetSighted()) {
                    inPosition = false;
                    state = State.CHASE;
            } else {
                Vector2 velocity = this.getVelocity();
                Vector2 position = this.getPosition();
                Vector2 orientation = this.getOrientation();
                if (inPosition) {
                    if (rotatingToStartPosition) {
                        if (orientation.angle() < 93 && orientation.angle() > 87) {
                            this.setRotation(90f);
                            rotatingToStartPosition = false;
                            rotatingLeft = true;
                            rotationCounter = 0;
                        } else if (orientation.angle() > 90 && orientation.angle() < 270)
                            this.rotate(-1);
                        else
                            this.rotate(1);
                    } else { //rotate back and forth between 90 and 0 (360) degrees
                        if (rotatingLeft) {
                            this.rotate(0.5f);
                            if (++rotationCounter == 540)
                                rotatingLeft = false;
                        } else {
                            this.rotate(-0.5f);
                            if (--rotationCounter == 0)
                                rotatingLeft = true;
                        }
                    }
                } else { // Get to the choppa! err... I mean guard position...
                    Vector2 target = guardPosition.cpy().sub(position);
                    if (target.len() < 0.05f && velocity.len() < 0.2f) {
                        this.zeroVelocity();
                        inPosition = true;
                    } else {
                        if (velocity.isZero()) {
                            float angle = orientation.angle(target);
                            if (angle != 0) {
                                if (angle > 3)
                                    this.rotate(3);
                                else if (angle < -3)
                                    this.rotate(-3);
                                else
                                    this.rotate(angle);
                            } else {
                                this.accelerate();
                            }
                        } else {
                            float maxVelocity = this.getMaxVelocity();
                            float slowingDistance = maxVelocity*maxVelocity/(2*this.getAccelerationRate());
                            float distance = target.len();
                            float rampedSpeed = maxVelocity*(distance/slowingDistance);
                            float clippedSpeed = Math.min(rampedSpeed, maxVelocity);
                            Vector2 desiredVelocity = target.cpy().scl(clippedSpeed / distance);
                            Vector2 avoidance = avoidObstacles(collidables);
                            if (!avoidance.isZero())
                                desiredVelocity = desiredVelocity.add(avoidance);
                            this.accelerate(desiredVelocity.sub(velocity));
                                
                        }
                    }
                }
            }
        } else if (state == State.CHASE) {
            if (!targetSighted()) {
                state = State.GUARD;
                rotatingToStartPosition = true;
            } else {
                Vector2 desiredVelocity = target.getPosition().sub(this.getPosition()).nor().scl(this.getMaxVelocity());
                Vector2 avoidance = avoidObstacles(collidables);
                if (!avoidance.isZero())
                    desiredVelocity = desiredVelocity.add(avoidance);
                this.accelerate(desiredVelocity.sub(this.getVelocity()));
            }
        }
        super.step();
    }

    private Vector2 avoidObstacles(Sprite[] collidables) {
        Vector2 velocity = this.getVelocity();
        Vector2 position = this.getPosition();
        Vector2 sightStart = position.cpy();
        Vector2 sightEnd = position.cpy().add(velocity.cpy().nor().scl(sightRange));
        float displacement = 0;
        Rectangle avoiding = null;
        Vector2 intersection = null;
        for (Sprite collidable : collidables) {
            Rectangle r = collidable.getBoundingRectangle();
            Vector2 center = new Vector2(r.getX() + r.getWidth()/2, r.getY() + r.getHeight()/2);
            //Vector2 cushion = new Vector2(this.getSprite().getWidth(), this.getSprite().getHeight());
            // float cushion = this.getSprite().getWidth()/2;
            // // Add some cushioning so the guard doesn't scoot so close to a corner that it still collides due to its width!
            // r.setX(r.getX() - cushion);
            // r.setWidth(r.getWidth() + 2*cushion);
            // r.setY(r.getY() - cushion);
            // r.setHeight(r.getHeight() + 2*cushion);
            
            // get edges of rectangle, each defined by two vectors
            Vector2 edges[][] = new Vector2[4][2];
            // bottom edge
            edges[0][0] = new Vector2(r.getX(), r.getY());
            edges[0][1] = new Vector2(r.getX() + r.getWidth(), r.getY());
            // right edge
            edges[1][0] = new Vector2(r.getX() + r.getWidth(), r.getY());
            edges[1][1] = new Vector2(r.getX() + r.getWidth(), r.getY() + r.getHeight());
            // top edge
            edges[2][0] = new Vector2(r.getX(), r.getY() + r.getHeight());
            edges[2][1] = new Vector2(r.getX() + r.getWidth(), r.getY() + r.getHeight());
            // left edge
            edges[3][0] = new Vector2(r.getX(), r.getY());
            edges[3][1] = new Vector2(r.getX(), r.getY() + r.getHeight());
            
            for (Vector2 edge[] : edges) {
                Vector2 intersectionHere = new Vector2(0f,0f);
                if (Intersector.intersectSegments(sightStart, sightEnd, edge[0], edge[1], intersectionHere)) {
                    float displacementHere = intersectionHere.cpy().sub(position).len();
                    if (intersection == null) {
                        displacement = displacementHere;
                        avoiding = r;
                        intersection = intersectionHere;
                    } else if (displacementHere < displacement) {
                        displacement = displacementHere;
                        avoiding = r;
                        intersection = intersectionHere;
                    }
                }
            }
        }

        if (intersection != null) {
            avoidingObject = avoiding;
            Vector2 center = new Vector2(0f,0f);
            avoiding.getCenter(center);
            Vector2 avoidance = center.cpy().sub(position);
            float angle = standardizeAngle(velocity.cpy().angle(avoidance));
            float importance = 10/displacement;
            if (angle < 180)
                return velocity.cpy().rotate(-90).nor().scl(this.getMaxVelocity()*importance);
            else
                return velocity.cpy().rotate(90).nor().scl(this.getMaxVelocity()*importance);
        } else {
            return new Vector2(0f,0f);
        }
    }
    
    private boolean targetSighted() {
        Vector2 orientation = this.getOrientation();
        Vector2 targetOffset = target.getPosition().sub(this.getPosition());
        return ((orientation.angle(targetOffset) > -60 && orientation.angle(targetOffset) < 60) && targetOffset.len() < sightRange);
    }

    public void drawView(ShapeRenderer shapeRenderer) {
        Vector2 pos = this.getPosition();
        Sprite sprite = this.getSprite();
        float startAngle = this.getOrientation().angle() - 60;
        shapeRenderer.arc(pos.x + sprite.getWidth()/2,
                          pos.y + sprite.getHeight()/2,
                          sightRange,
                          startAngle,
                          120
                          );
        /*        if (avoidingObject != null)
            shapeRenderer.rect(avoidingObject.getX(),
                               avoidingObject.getY(),
                               avoidingObject.getWidth(),
                               avoidingObject.getHeight()
                               );
        */
    }

    public void reinitialize() {
        super.reinitialize();
        state = State.GUARD;
        inPosition = true;
        rotatingLeft = true;
        rotationCounter = 0;
    }

    private float standardizeAngle(float angle) {
        while (angle > 360)
            angle -= 360;
        while (angle < 0)
            angle += 360;
        return angle;
    }
}
