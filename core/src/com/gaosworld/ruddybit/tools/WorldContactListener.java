package com.gaosworld.ruddybit.tools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gaosworld.ruddybit.sprites.Base;
import com.gaosworld.ruddybit.sprites.Coin;
import com.gaosworld.ruddybit.sprites.MySound;
import com.gaosworld.ruddybit.sprites.Pipe;
import com.gaosworld.ruddybit.sprites.Player;

public class WorldContactListener implements ContactListener {

    private Sound coin;
    private Sound loser;

    public WorldContactListener(MySound... sounds) {
        for ( MySound s: sounds) {
            if (s.getName().equals("coin")) coin = s.getSound();
            else if ( s.getName().equals("loser")) loser = s.getSound();
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        checkPlayerContactWithPipeOrBase(fixA, fixB);
        checkPlayerContactWithCoin(fixA, fixB);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


    private void checkPlayerContactWithCoin(Fixture a, Fixture b) {
        if ( a.getUserData() instanceof Player && b.getUserData() instanceof Coin ) {
            ( (Coin) (b.getUserData())).setLive(false);
            GameTools.SCORE += MathUtils.random(100, 200);
            if ( coin != null ) {
                coin.play(50/100f);
            }

        } else if ( b.getUserData() instanceof Player && a.getUserData() instanceof Coin) {
            ( (Coin) (a.getUserData())).setLive(false);
            GameTools.SCORE += MathUtils.random(100, 200);
            if ( coin != null ) {
                coin.play(50/100f);
            }
        }

    }

    private void checkPlayerContactWithPipeOrBase(Fixture a, Fixture b) {
        if ( a.getUserData() instanceof Player && (b.getUserData() instanceof Pipe || b.getUserData() instanceof Base)) {
            ( (Player) (a.getUserData())).setLive(false);
            if ( loser != null ) {
                loser.play(50/100f);
            }
        } else if ( b.getUserData() instanceof Player && ( a.getUserData() instanceof Pipe || a.getUserData() instanceof Base) ) {
            ( (Player) (b.getUserData())).setLive(false);
            if ( loser != null ) {
                loser.play(50/100f);
            }
        }

    }
}
