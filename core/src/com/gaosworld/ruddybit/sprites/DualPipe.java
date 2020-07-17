package com.gaosworld.ruddybit.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gaosworld.ruddybit.tools.GameAssets;
import com.gaosworld.ruddybit.tools.GameTools;

public class DualPipe {

    private Pipe top;
    private Pipe bottom;
    public static final float SPACE = 250;
    private World world;
    private Array<Coin> coins;
    private int countCoint;
    private AssetManager manager;
    public float[] widths;
    public DualPipe(World world, float x, float y, AssetManager manager) {
        this.world = world;
        this.manager = manager;
        widths = new float[] {
                GameTools.WORLD_WIDTH * 0.2f,
                GameTools.WORLD_WIDTH * 0.5f,
                GameTools.WORLD_WIDTH
        };
        float width = widths[MathUtils.random(0, widths.length - 1)];
        bottom = new Pipe(world, x, y, width);
        top = new Pipe(world, x, bottom.getPosition().y * GameTools.PPM + (bottom.getHeight()/2f) * GameTools.PPM + SPACE, width);

        countCoint = width == widths[widths.length -1] ? 3 : width == widths[widths.length -2] ? 2 : 1;
        coins = new Array<>();
        for ( int c = 1; c <= countCoint; c++) {
            createCoin(countCoint, c);
        }

    }

    public void update(float delta) {
        top.update(delta);
        bottom.update(delta);
        updateCoin(delta);
    }

    public void setLinearVel(float x, float y) {
        top.getBody().setLinearVelocity(x, y);
        bottom.getBody().setLinearVelocity(x, y);
    }

    public Vector2 getPosition() {
        return new Vector2(Math.min(top.getPosition().x, bottom.getPosition().x), bottom.getPosition().y);

    }

    public Vector2 getPositionOfBig() {
        return new Vector2( getWidth() == top.getWidth() ? top.getPosition().x : bottom.getPosition().x, bottom.getPosition().y);
    }

    public boolean isLive() {
        return top.isLive() || bottom.isLive();
    }

    public float getWidth() {
        return Math.max(top.getWidth(), bottom.getWidth());
    }

    public float getHeight() {
        return top.getHeight() + bottom.getHeight() + SPACE;
    }

    public void destroyBody() {
        world.destroyBody(top.getBody());
        world.destroyBody(bottom.getBody());
        for ( Coin coin: coins) {
            world.destroyBody(coin.getBody());
            coins.removeValue(coin, true);
        }
    }

    public Pipe getTop() {
        return top;
    }

    public Pipe getBottom() {
        return bottom;
    }

    private void createCoin(int countCoin, int mult) {
        float coinX = 0, coinY;

        coinY = MathUtils.random(getBottom().getPosition().y + getBottom().getHeight() / 2f + 20/GameTools.PPM,
                getBottom().getPosition().y + getBottom().getHeight() / 2f + (SPACE - 80)/GameTools.PPM) ;
        coinX = getBottom().getPosition().x;

        coins.add(new Coin(world, coinX, coinY, mult, (Texture) manager.get(GameAssets.REDCOIN)));
    }

    public void updateCoin(float delta) {
        float coinX = 0;
        coinX = getBottom().getPosition().x;

        for ( Coin coin: coins) {
            coin.update(delta);
            coin.getBody().setTransform(coinX + coin.getBody().getLinearVelocity().x, coin.getPosition().y, 0);
            if ( !coin.isLive() || !isLive()) {
                world.destroyBody(coin.getBody());
                coins.removeValue(coin, true);
            }
        }
    }

    public Array<Coin> getCoins() {
        return coins;
    }
}
