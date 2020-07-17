package com.gaosworld.ruddybit.sprites;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gaosworld.ruddybit.tools.GameTools;

public class Pipe {

    private World world;
    private Body body;
    private boolean isLive;
    private float width;
    private float height;

    public Pipe(World world, float x, float y, float width) {
        this.world = world;
        this.width = width;
        body = createBody(x / GameTools.PPM, y / GameTools.PPM);
        isLive = true;
    }

    public void update(float delta) {
        body.setLinearVelocity(GameTools.VELX * delta, 0);
        if ( getPosition().x + width / 2 < 0 ) {
            isLive = false;
        }
    }

    private Body createBody(float x, float y) {
        height = GameTools.WORLD_HEIGHT * 0.6f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x + width / 2f, y + height / 2f);
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0;

        Body b = world.createBody(bodyDef);
        b.createFixture(fixtureDef).setUserData(this);
        return b;
    }

    public void setPostion(float x, float y) {
        body.setTransform(x, y, 0);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isLive() {
        return isLive;
    }
}
