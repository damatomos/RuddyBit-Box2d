package com.gaosworld.ruddybit.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.gaosworld.ruddybit.RuddyBitGame;
import com.gaosworld.ruddybit.data.Data;
import com.gaosworld.ruddybit.sprites.TapSprite;
import com.gaosworld.ruddybit.sprites.TitleSprite;
import com.gaosworld.ruddybit.tools.GameAssets;
import com.gaosworld.ruddybit.tools.GameTools;

import java.io.IOException;

public class MenuScreen extends DefaultGameScreen {

    private RuddyBitGame game;
    private TitleSprite titleSprite;
    private TapSprite tapSprite;
    private boolean isGameover;
    private boolean isCallGameplay;
    private Music background_song;
    private int score = 0;
    private Hud hud;

    public MenuScreen(RuddyBitGame game, boolean isGameover) {
        this.game = game;
        Gdx.app.log("MENU", "init");
        if ( Gdx.app.getType() == Application.ApplicationType.Android) {
            game.handlerBanner.showAds(true);
        }
        this.isGameover = isGameover;
        isCallGameplay = false;
        hud = new Hud(game.manager);

        try {
            score = ( (Data) game.gameData.load()).getScore();
            Gdx.app.log("MENU", "score: " + score);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public MenuScreen(RuddyBitGame game) {
        this(game, false);
    }

    @Override
    public void show() {
        super.show();
        titleSprite = new TitleSprite((Texture) game.manager.get(isGameover ? GameAssets.GAMEOVER : GameAssets.TITLE));
        titleSprite.setPosition((viewport.getWorldWidth() - titleSprite.getTexture().getRegionWidth() / GameTools.PPM) / 2f, viewport.getWorldHeight());

        tapSprite = new TapSprite((Texture) game.manager.get(GameAssets.TAP));
        tapSprite.setPosition((viewport.getWorldWidth() - tapSprite.getWidth() / GameTools.PPM) / 2f,
                (viewport.getWorldHeight() - tapSprite.getHeight() / GameTools.PPM) / 2f);

        background_song = Gdx.audio.newMusic(Gdx.files.internal("songs/background_song.ogg"));
        background_song.setVolume(40f/100f);
        background_song.setLooping(true);
        if ( GameTools.WITHSOUND) {
            background_song.play();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();

        hud.drawStage(delta);

        if ( Gdx.input.justTouched() && !hud.getSound().isPressed() ) {
            isCallGameplay = true;
        }

        if ( background_song.isPlaying() && !GameTools.WITHSOUND ) {
            background_song.pause();
        } else if ( !background_song.isPlaying() && GameTools.WITHSOUND ) {
            background_song.play();
        }

        if ( isCallGameplay ) {
            isCallGameplay = false;
            setScreen(game, new GamePlay(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        hud.dispose();
        background_song.dispose();
    }

    private void update(float delta) {
        titleSprite.update(delta);
        tapSprite.update(delta);

        if ( Gdx.input.isTouched() ) {
            //isCallGameplay = true;
        }
    }

    private void draw() {
        batch.begin();
        drawBackground();
        drawTitle();
        drawTap();
        batch.end();
        // hud
        drawScore();
    }

    private void drawBackground() {
        batch.draw((Texture) game.manager.get(GameAssets.BACKGROUND), 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw((Texture) game.manager.get(GameAssets.BASE), 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight() * 0.25f);
    }

    private void drawTitle() {
        batch.draw(titleSprite.getTexture(),
                titleSprite.getX(), titleSprite.getY(),
                titleSprite.getTexture().getRegionWidth() / GameTools.PPM,
                titleSprite.getTexture().getRegionHeight() / GameTools.PPM);
    }

    private void drawTap() {
        if ( tapSprite.getTexture() != null ) {
            batch.draw(tapSprite.getTexture(), tapSprite.getX(), tapSprite.getY(), tapSprite.getTexture().getRegionWidth() / GameTools.PPM,
                    tapSprite.getTexture().getRegionHeight() / GameTools.PPM);
        }
    }

    private void drawScore() {
        hud.drawText("[#9c070f]HIGHSCORE", hud.font32, GameTools.WIDTH / 2f, (GameTools.HEIGHT * 0.25f) * 0.60f, true);
        hud.drawText("[GOLD]" + score, hud.font32, GameTools.WIDTH / 2f, (GameTools.HEIGHT * 0.25f) * 0.40f, true);
    }

}

