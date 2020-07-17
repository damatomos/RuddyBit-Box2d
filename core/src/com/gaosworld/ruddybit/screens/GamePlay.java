package com.gaosworld.ruddybit.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gaosworld.ruddybit.RuddyBitGame;
import com.gaosworld.ruddybit.data.Data;
import com.gaosworld.ruddybit.sprites.Base;
import com.gaosworld.ruddybit.sprites.Coin;
import com.gaosworld.ruddybit.sprites.DualPipe;
import com.gaosworld.ruddybit.sprites.MySound;
import com.gaosworld.ruddybit.sprites.Player;
import com.gaosworld.ruddybit.tools.GameAssets;
import com.gaosworld.ruddybit.tools.GameTools;
import com.gaosworld.ruddybit.tools.WorldContactListener;

import static java.lang.String.format;

public class GamePlay extends DefaultGameScreen {
    private RuddyBitGame game;
    private World world;
    private Box2DDebugRenderer renderer;

    private Player player;
    private Array<DualPipe> pipes;
    private ShapeRenderer shapeRenderer;
    private int generatePipeValue;
    private WorldContactListener worldContactListener;
    private Array<Base> bases;
    private float timeGameOver = 0;
    private float timeChangeVelX = 0;
    private Sound jumpSound;
    private MySound coin;
    private MySound loser;
    private Music background_song;

    private Hud hud;

    public GamePlay(RuddyBitGame game) {
        this.game = game;
        if ( game.handlerBanner != null ) {
            game.handlerBanner.showAds(false);
        }
    }

    @Override
    public void show() {
        super.show();
        world = new World(new Vector2(0, -25), false);
        coin = new MySound("coin", Gdx.audio.newSound(Gdx.files.internal("songs/coinsound.wav")));
        loser = new MySound("loser", Gdx.audio.newSound(Gdx.files.internal("songs/losesong.wav")));
        worldContactListener = new WorldContactListener(coin, loser);
        world.setContactListener(worldContactListener);
        shapeRenderer = new ShapeRenderer();
        renderer = new Box2DDebugRenderer();
        player = new Player(world, GameTools.WIDTH * 0.2f, GameTools.HEIGHT / 2f, (Texture) game.manager.get(GameAssets.PLAYER));
        pipes = new Array<>();
        bases = new Array<Base>();
        createBase();
        createPipe();
        generatePipeValue = MathUtils.random(0, 1);

        hud = new Hud(game.manager, true);
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("songs/salto.wav"));
        background_song = Gdx.audio.newMusic(Gdx.files.internal("songs/background_song02.ogg"));
        background_song.setVolume(2/100f);
        background_song.setLooping(true);
        if ( GameTools.WITHSOUND ) {
            background_song.play();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //debug();
        timeChangeVelX += delta;

        if ( timeChangeVelX > 5 && player.isLive() ) {
            GameTools.VELX -= 1;
            timeChangeVelX = 0;
        }

        controller();

        if ( !GameTools.PAUSE) {
            update(delta);

            renderer.render(world, camera.combined);
        }
        draw();

        if ( !GameTools.PAUSE) {
            world.step(1/60f, 6, 2);
        }

        hud.drawStage(delta);
        if ( !player.isLive()) {
            timeGameOver += delta;
            if ( timeGameOver > 3) {

                game.gameData.save(new Data(GameTools.SCORE));
                GameTools.SCORE = 0;
                GameTools.PAUSE = true;
                GameTools.VELX = GameTools.INITIALVELX;
                if ( Gdx.app.getType() == Application.ApplicationType.Android ) {
                    game.handlerBanner.showInterstitial(true);
                }

                setScreen(game, new MenuScreen(game, true));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
        GameTools.PAUSE = true;
    }

    @Override
    public void resume() {
        super.resume();
        //GameTools.PAUSE = false;
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        renderer.dispose();
        shapeRenderer.dispose();
        hud.dispose();
        coin.getSound().dispose();
        loser.getSound().dispose();
        jumpSound.dispose();
        background_song.dispose();
    }

    private void update(float delta) {

        player.update(delta);
        if (Gdx.input.justTouched() && player.isLive() && !hud.getPause().isPressed() ) {
            jumpSound.play(1f);
            player.jump();
        }

        for ( Base base: bases) {
            base.update(delta);
        }
        createBase();
        createPipe();
        for ( DualPipe pipe: pipes ) {
            pipe.update(delta);
        }
        checkPipeIsLive();
        checkBaseIsLive();
    }

    private void draw() {

        batch.begin();
        batch.draw((Texture) game.manager.get(GameAssets.BACKGROUND), 0, 0, GameTools.WORLD_WIDTH, GameTools.WORLD_HEIGHT);

        for ( DualPipe pipe: pipes) {

            if ( pipe.getWidth() == pipe.widths[0] ) {
                drawPipe(pipe, GameAssets.TUBE_TOP, GameAssets.TUBE_BOTTOM);
            } else {
                //drawPipe(pipe, GameAssets.BOXRED, GameAssets.BOXRED);

                drawPipe(pipe, GameAssets.TUBE_TOP, GameAssets.TUBE_BOTTOM);
            }

            for(Coin coin: pipe.getCoins()) {
                batch.draw(coin.getTexture(),
                        coin.getPosition().x - coin.radius/GameTools.PPM,
                        coin.getPosition().y - coin.radius/GameTools.PPM,
                        coin.radius*2/GameTools.PPM, coin.radius*2/GameTools.PPM);
            }

        }
        for ( Base base: bases) {
            batch.draw((Texture) game.manager.get(GameAssets.BASE),
                    base.getPosition().x - base.getWidth() / 2f,
                    base.getPosition().y - base.getHeight() / 2f, base.getWidth(), base.getHeight());
        }

        batch.draw( player.isLive() ? player.getTexture() : new TextureRegion( (Texture) game.manager.get(GameAssets.PLAYER_DEAD)),
                player.getPosition().x - player.radius/GameTools.PPM,
                player.getPosition().y - player.radius / GameTools.PPM,
                player.radius *2 / GameTools.PPM, player.radius *2 / GameTools.PPM);

        batch.end();

        hud.drawText("" +GameTools.SCORE, hud.font32, GameTools.WIDTH / 2f, GameTools.HEIGHT - 50, true);
    }

    private void drawPipe(DualPipe pipe, String textureTop, String textureBottom) {
        batch.draw((Texture) game.manager.get(textureTop),
                pipe.getTop().getPosition().x -  pipe.getTop().getWidth()/2f, pipe.getTop().getPosition().y - pipe.getTop().getHeight()/2f,
                pipe.getTop().getWidth(), pipe.getTop().getHeight());
        batch.draw((Texture) game.manager.get(textureBottom),
                pipe.getBottom().getPosition().x -  pipe.getBottom().getWidth()/2f, pipe.getBottom().getPosition().y - pipe.getBottom().getHeight()/2f,
                pipe.getBottom().getWidth(), pipe.getBottom().getHeight());
    }

    private void controller() {

        if ( Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom += 1 / GameTools.PPM;
        }

        if ( Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            GameTools.PAUSE = !GameTools.PAUSE;
        }

        camera.update();

    }

    private void createBase() {

        if ( bases.size == 0 ) {
            bases.add(new Base(world, 2, 0));
        } else if ( bases.size > 0 && (bases.get(bases.size - 1).getPosition().x + bases.get(bases.size - 1).getWidth() / 2f - 15/GameTools.PPM < GameTools.WORLD_WIDTH) ) {
            bases.add(new Base(world, GameTools.WORLD_WIDTH, 0));
        }

    }

    private void createPipe() {
        boolean canCreate = true;
        float[] postionsToCreate = new float[] {
                GameTools.WORLD_WIDTH * 0.7f,
                GameTools.WORLD_WIDTH * 0.5f
        };

        float[] yPositions = new float[] {
                 0,
                -GameTools.HEIGHT * 0.2f,
                -GameTools.HEIGHT * 0.4f
        };
        if ( pipes.size > 0 ) {

            if ( !(pipes.get(pipes.size - 1).getPositionOfBig().x + pipes.get(pipes.size - 1).getWidth() / 2f < postionsToCreate[generatePipeValue]) ) {
                canCreate = false;
            } else  {
                generatePipeValue = MathUtils.random(0, 1);
            }
        }
        if ( canCreate ) {
            pipes.add(new DualPipe(world, GameTools.WIDTH, yPositions[MathUtils.random(0, yPositions.length - 1)], game.manager));
        }
    }

    private void checkBaseIsLive() {
        for ( Base base: bases) {
            if ( !base.isLive() ) {
                world.destroyBody(base.getBody());
                bases.removeValue(base, true);
            }
        }
    }

    private void checkPipeIsLive() {
        for ( DualPipe pipe: pipes ) {
            if ( !pipe.isLive() ) {
                pipe.destroyBody();
                pipes.removeValue(pipe, true);
            }
        }
    }

    private void debug() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(0, 0, 0, GameTools.WORLD_HEIGHT);
        shapeRenderer.line(GameTools.WORLD_WIDTH / 2f, 0, GameTools.WORLD_WIDTH / 2f, GameTools.WORLD_HEIGHT);
        shapeRenderer.line(GameTools.WORLD_WIDTH * 0.8f, 0, GameTools.WORLD_WIDTH * 0.8f, GameTools.WORLD_HEIGHT);
        shapeRenderer.line(GameTools.WORLD_WIDTH, 0, GameTools.WORLD_WIDTH, GameTools.WORLD_HEIGHT);
        shapeRenderer.line(0, 0, GameTools.WORLD_WIDTH, 0);
        shapeRenderer.line(0, GameTools.WORLD_HEIGHT, GameTools.WORLD_WIDTH, GameTools.WORLD_HEIGHT);

        if ( pipes.size > 0) {
            shapeRenderer.setColor(Color.SKY);
            shapeRenderer.line(pipes.get(pipes.size - 1).getPositionOfBig().x + pipes.get(pipes.size - 1).getWidth() / 2f, 0,
                    pipes.get(pipes.size - 1).getPositionOfBig().x + pipes.get(pipes.size - 1).getWidth() / 2f, GameTools.WORLD_HEIGHT);
        }

        shapeRenderer.end();
    }
}
