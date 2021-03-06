package com.tangledwebgames.crossfade.sound;

import com.badlogic.gdx.audio.Sound;
import com.tangledwebgames.crossfade.data.AssetLoader;
import com.tangledwebgames.crossfade.data.SettingsManager;

/**
 * Handles playing sound effects and music.
 */
public class SoundManager {

    private static final float MOVE_SOUND_MOD = 1.2f;
    private static final float BUTTON_SOUND_MOD = 1.2f;
    private static final float WIN_SOUND_MOD = 0.65f;
    private static boolean sfx;
    private static boolean music;
    private static float sfxVolume;
    private static float musicVolume;

    private SoundManager() {
    }

    public static void updateFromSettings() {
        setMusic(SettingsManager.isMusicOn());
        setMusicVolume(SettingsManager.getMusicVolume());
        setSfx(SettingsManager.isSfxOn());
        setSfxVolume(SettingsManager.getSfxVolume());
    }

    private static void playSfx(Sound sound, float volumeMod) {
        if (sfx && sound != null) {
            sound.play(Math.min(sfxVolume * volumeMod, 1f));
        }
    }

    private static void playSfx(Sound sound) {
        playSfx(sound, 1f);
    }

    public static void moveSound() {
        playSfx(AssetLoader.instance.moveSound, MOVE_SOUND_MOD);
    }

    public static void buttonSound() {
        playSfx(AssetLoader.instance.buttonSound, BUTTON_SOUND_MOD);
    }

    public static void winSound() {
        playSfx(AssetLoader.instance.winSound, WIN_SOUND_MOD);
    }

    public static void playMusic() {
        if (music && AssetLoader.instance.backgroundMusic != null && !AssetLoader.instance.backgroundMusic.isPlaying()) {
            AssetLoader.instance.backgroundMusic.play();
        }
    }

    public static void stopMusic() {
        if (AssetLoader.instance.backgroundMusic != null && AssetLoader.instance.backgroundMusic.isPlaying()) {
            AssetLoader.instance.backgroundMusic.stop();
        }
    }

    private static void setSfx(boolean sfx) {
        SoundManager.sfx = sfx;
    }

    private static void setMusic(boolean music) {
        SoundManager.music = music;
        if (music) playMusic();
        else stopMusic();
    }

    private static void setSfxVolume(float sfxVolume) {
        if (0 <= sfxVolume && sfxVolume <= 1) {
            SoundManager.sfxVolume = sfxVolume;
        }
    }

    private static void setMusicVolume(float musicVolume) {
        if (0 <= musicVolume && musicVolume <= 1) {
            SoundManager.musicVolume = musicVolume;
            if (AssetLoader.instance.backgroundMusic != null) {
                AssetLoader.instance.backgroundMusic.setVolume(musicVolume);
            }
        }
    }
}
