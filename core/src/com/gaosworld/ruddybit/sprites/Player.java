package com.gaosworld.ruddybit.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gaosworld.ruddybit.tools.GameTools;

public class Player {

    private World world;
    private Body body;
    private boolean isLive;
    private Texture texture;
    private Array<TextureRegion> frames;
    public float radius = 25;
    private float timer;
    private Animation<TextureRegion> animation;

    public Player(World world, float x, float y, Texture texture) {
        this.world = world;
        body = createBody(x / GameTools.PPM, y / GameTools.PPM);
        isLive = true;

        TextureRegion[][] tiles = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight());
        frames = generateSprites(tiles);
        animation = new Animation<TextureRegion>(0.2f, frames);
        body.setFixedRotation(true);
    }

    public void update(float delta) {
        timer += delta;
    }

    public void jump() {
        body.setLinearVelocity(0, 0);
        body.applyLinearImpulse(new Vector2(0, 6), body.getWorldCenter(), false);
    }

    private Body createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / GameTools.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0;

        Body b = world.createBody(bodyDef);
        b.createFixture(fixtureDef).setUserData(this);
        return b;
    }

    private Array<TextureRegion> generateSprites(TextureRegion[][] regions) {
        Array<TextureRegion> frames = new Array<>();

        for ( int i = 0; i < regions[0].length; i++) {
            frames.add(regions[0][i]);
        }

        return frames;
    }

    public TextureRegion getTexture() {
        return animation.getKeyFrame(timer, true);
    }

    public void setPostion(float x, float y) {
        body.setTransform(x, y, 0);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public boolean isLive() {
        return isLive;
    }

    public float getRotation() {
        return body.getAngle();
    }

    public Body getBody() {
        return body;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

}
