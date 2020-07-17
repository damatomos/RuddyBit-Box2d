package com.gaosworld.ruddybit.sprites;

import com.badlogic.gdx.audio.Sound;

public class MySound {

    private Sound sound;
    private String name;
    public MySound(String name, Sound sound) {
        this.name = name;
        this.sound = sound;
    }

    public String getName() {
        return name;
    }

    public Sound getSound() {
        return sound;
    }
}
