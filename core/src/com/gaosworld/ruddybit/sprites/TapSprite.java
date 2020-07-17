package com.gaosworld.ruddybit.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gaosworld.ruddybit.tools.GameTools;

public class TapSprite {

    private Vector2 position;
    private Texture texture;
    private float timer;
    private Animation<TextureRegion> animation;
    private Array<TextureRegion> frames;
    private boolean ocult;

    public TapSprite(Texture texture) {
        this.texture = texture;

        position = new Vector2(0, 0);
        TextureRegion[][] tiles = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight());
        frames = generateSprites(tiles);
        animation = new Animation<TextureRegion>(0.1f, frames);
        ocult = true;
    }

    private Array<TextureRegion> generateSprites(TextureRegion[][] regions) {
        Array<TextureRegion> frames = new Array<>();

        for ( int i = 0; i < regions[0].length; i++) {
            frames.add(regions[0][i]);
        }

        return frames;
    }

    public void update(float delta) {
        timer += delta;

        if ( timer > 3) {
            ocult = false;
        }

    }

    public TextureRegion getTexture() {
        return ocult ? null : animation.getKeyFrame(timer, true);
    }

    public float getTimer() {
        return timer;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return frames.get(0).getRegionWidth();
    }

    public float getHeight() {
        return frames.get(0).getRegionHeight();
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

}
