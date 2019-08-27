package com.tangledwebgames.crossfade.sound;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.tangledwebgames.crossfade.Assets;
import com.tangledwebgames.crossfade.CrossFadeGame;
import com.tangledwebgames.crossfade.PreferenceWrapper;

/**
 *  Handles playing sound effects and music.
 */
public class SoundManager {

    private static final float MOVE_SOUND_MOD = 1.2f;
    private static final float BUTTON_SOUND_MOD = 1.2f;
    private static final float WIN_SOUND_MOD = 0.65f;
    private static boolean sfx = true;
    private static boolean music = true;
    private static float sfxVolume = 1f;
    private static float musicVolume = 1f;

    private SoundManager() {}

    private static void playSfx(Sound sound, float volumeMod) {
        if (sfx && sound != null) {
            sound.play(Math.min(sfxVolume * volumeMod, 1f));
        }
    }

    private static void playSfx(Sound sound) {
        playSfx(sound, 1f);
    }

    public static void moveSound() {
        playSfx(Assets.instance.moveSound, MOVE_SOUND_MOD);
    }

    public static void buttonSound() {
        playSfx(Assets.instance.buttonSound, BUTTON_SOUND_MOD);
    }

    public static void winSound() {
        playSfx(Assets.instance.winSound, WIN_SOUND_MOD);
    }

    public static void playMusic() {
        if (music && Assets.instance.backgroundMusic != null && !Assets.instance.backgroundMusic.isPlaying()) {
            Assets.instance.backgroundMusic.play();
        }
    }

    public static void stopMusic() {
        if (Assets.instance.backgroundMusic != null && Assets.instance.backgroundMusic.isPlaying()) {
            Assets.instance.backgroundMusic.stop();
        }
    }

    public static void init() {
        Preferences prefs = PreferenceWrapper.prefs;
        setMusic(prefs.getBoolean(
                PreferenceWrapper.MUSIC_ON_KEY,
                CrossFadeGame.APP_TYPE != Application.ApplicationType.WebGL
        ));
        setMusicVolume(prefs.getFloat(PreferenceWrapper.MUSIC_VOLUME_KEY, 1f));
        setSfx(prefs.getBoolean(PreferenceWrapper.SFX_ON_KEY, true));
        setSfxVolume(prefs.getFloat(PreferenceWrapper.SFX_VOLUME_KEY, 1f));
    }

    public static boolean isSfxOn() {
        return sfx;
    }

    public static void setSfx(boolean sfx) {
        SoundManager.sfx = sfx;
    }

    public static void turnOnSfx() {
        setSfx(true);
    }

    public static void turnOffSfx() {
        setSfx(false);
    }

    public static void toggleSfx() {
        setSfx(!sfx);
    }

    public static boolean isMusicOn() {
        return music;
    }

    public static void setMusic(boolean music) {
        SoundManager.music = music;
        if (music) playMusic(); else stopMusic();
    }

    public static void turnOnMusic() {
        setMusic(true);
    }

    public static void turnOffMusic() {
        setMusic(false);
    }

    public static void toggleMusic() {
        setMusic(!music);
    }

    public static float getSfxVolume() {
        return sfxVolume;
    }

    public static void setSfxVolume(float sfxVolume) {
        if (0 <= sfxVolume && sfxVolume <= 1) {
            SoundManager.sfxVolume = sfxVolume;
        }
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        if (0 <= musicVolume && musicVolume <= 1) {
            SoundManager.musicVolume = musicVolume;
            if (Assets.instance.backgroundMusic != null) {
                Assets.instance.backgroundMusic.setVolume(musicVolume);
            }
        }
    }
}
