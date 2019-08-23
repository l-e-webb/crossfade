package com.tangledwebgames.crossfade.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 *  Handles playing sound effects and music.
 */
public class SoundManager implements Disposable {

    private static SoundManager instance;

    private Music backgroundMusic;
    private Sound moveSound;
    private Sound buttonSound;
    private Sound winSound;

    private static boolean sfx = true;
    private static boolean music = true;

    private static float sfxVolume = 1f;
    private static float musicVolume = 1f;

    private SoundManager() {}

    private static void playSfx(Sound sound) {
        if (sfx && sound != null) {
            sound.play(sfxVolume);
        }
    }

    public static void moveSound() {
        playSfx(instance.moveSound);
    }

    public static void buttonSound() {
        playSfx(instance.buttonSound);
    }

    public static void winSound() {
        playSfx(instance.winSound);
    }

    public static void playMusic() {
        if (music && instance.backgroundMusic != null && !instance.backgroundMusic.isPlaying()) {
            instance.backgroundMusic.play();
        }
    }

    public static void stopMusic() {
        if (instance.backgroundMusic != null && instance.backgroundMusic.isPlaying()) {
            instance.backgroundMusic.stop();
        }
    }

    public static void init() {
        instance = new SoundManager();
        initSounds();
        initMusic();
    }

    private static void initSounds() {
        instance.moveSound = Gdx.audio.newSound(Gdx.files.internal("move_sound.wav"));
        instance.buttonSound = instance.moveSound;
        instance.winSound = Gdx.audio.newSound(Gdx.files.internal("win_sound.mp3"));
    }

    private static void initMusic() {
        instance.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("test_bgm.mp3"));
        instance.backgroundMusic.setLooping(true);
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
            if (instance.backgroundMusic != null) {
                instance.backgroundMusic.setVolume(musicVolume);
            }
        }
    }

    @Override
    public void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
        if (moveSound != null) moveSound.dispose();
        if (buttonSound != null) buttonSound.dispose();
        if (winSound != null) winSound.dispose();
    }
}
