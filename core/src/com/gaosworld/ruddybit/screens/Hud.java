package com.gaosworld.ruddybit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gaosworld.ruddybit.tools.GameAssets;
import com.gaosworld.ruddybit.tools.GameTools;

public class Hud implements Disposable {

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    public BitmapFont font32;
    public BitmapFont font16;
    public BitmapFont font8;
    private GlyphLayout layout;
    private Stage stage;
    private TextureRegionDrawable noSound;
    private TextureRegionDrawable withSound;
    private TextureRegionDrawable isPause;
    private TextureRegionDrawable isPlay;
    private Button sound;
    private Button pause;

    public Hud(AssetManager manager, boolean isPlaying) {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameTools.WIDTH, GameTools.HEIGHT, camera);
        batch = new SpriteBatch();
        layout = new GlyphLayout();
        font32 = (BitmapFont) manager.get(GameAssets.FONT32);
        font32.getData().markupEnabled = true;
        font16 = (BitmapFont) manager.get(GameAssets.FONT16);
        font16.getData().markupEnabled = true;
        font8 = (BitmapFont) manager.get(GameAssets.FONT8);
        font8.getData().markupEnabled = true;

        stage = new Stage(viewport);

        withSound = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/withSound.png"))));;
        noSound = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/noSound.png"))));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = GameTools.WITHSOUND ? withSound : noSound;
        sound = new Button(style);
        sound.setPosition(20, 50);
        sound.setSize(64, 64);
        sound.setName(GameTools.WITHSOUND ? "true" : "false");
        sound.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                sound.getStyle().up = sound.getStyle().up == withSound ? noSound : withSound;
                sound.setName(sound.getName().equals("true") ? "false": "true");
                GameTools.WITHSOUND = !GameTools.WITHSOUND;
                return true;
            }
        });

        isPause = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/pause.png"))));;
        isPlay = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/play.png"))));

        Button.ButtonStyle stylePause = new Button.ButtonStyle();
        stylePause.up = GameTools.PAUSE ? isPlay : isPause;
        pause = new Button(stylePause);
        pause.setPosition(20, 50);
        pause.setSize(50, 50);
        pause.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pause.getStyle().up = pause.getStyle().up == isPause ? isPlay : isPause;
                GameTools.PAUSE = !GameTools.PAUSE;
                return true;
            }
        });


        if ( isPlaying ) {
            stage.addActor(pause);
        } else {
            stage.addActor(sound);
        }
        Gdx.input.setInputProcessor(stage);
    }

    public Hud(AssetManager manager) {
        this(manager, false);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void drawText(String text, BitmapFont font, float x, float y, boolean center) {
        layout.setText(font, text);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, text, center ? x - layout.width / 2f: x, center ? y + layout.height / 2f : y);
        batch.end();
    }

    public void drawStage(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public Button getSound() {
        return sound;
    }

    public Button getPause() {
        return pause;
    }
}