package com.gaosworld.ruddybit.sprites;

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

public class Coin {

    private World world;
    private Body body;
    private boolean isLive;
    public float radius;
    private int mult;
    private Texture texture;
    private Animation<TextureRegion> animation;
    private Array<TextureRegion> frames;
    private float timer;

    public Coin(World world, float x, float y, int mult, Texture texture) {
        this.texture = texture;
        this.world = world;
        radius = 30;
        isLive = true;
        this.mult = mult;
        body = createBody(x, y);
        body.setLinearVelocity(-2 * (mult- 1), 0);

        TextureRegion[][] tiles = TextureRegion.split(texture, texture.getWidth() / 6, texture.getHeight());
        frames = generateSprites(tiles);
        animation = new Animation<TextureRegion>(0.1f, frames);
    }

    public void update(float delta) {
        timer += delta;
    }

    private Body createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y + radius/GameTools.PPM);
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / GameTools.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

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

    public void setPostion(float x, float y) {
        body.setTransform(x, y, 0);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }

    public int getMult() {
        return mult;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public TextureRegion getTexture() {
        return animation.getKeyFrame(timer, true);
    }
}
