package com.tangledwebgames.crossfade;

import com.tangledwebgames.crossfade.data.SettingsManager;
import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.UiReceiver;

public class UiEventHandler implements UiReceiver {

    // Main UI

    @Override
    public void onPreviousButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        MainController.instance.goToPreviousLevel();
        SoundManager.buttonSound();
    }

    @Override
    public void onNextButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        MainController.instance.goToNextLevel();
        SoundManager.buttonSound();
    }

    @Override
    public void onResetButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        MainController.instance.resetLevel();
        SoundManager.buttonSound();
    }

    @Override
    public void onLevelSelectButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        MainController.instance.showLevelSelectMenu();
        SoundManager.buttonSound();
    }

    @Override
    public void onMenuButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        MainController.instance.showMainMenu();
        SoundManager.buttonSound();
    }

    @Override
    public boolean isMainUiTableEnabled() {
        return MainController.instance.inGame();
    }


    // Pause menu

    @Override
    public void onSfxCheckboxChanged(boolean isChecked) {
        com.tangledwebgames.crossfade.data.SettingsManager.setIsSfxOn(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onSfxVolumeSliderChanged(float value) {
        com.tangledwebgames.crossfade.data.SettingsManager.setSfxVolume(value);
    }

    @Override
    public void onMusicCheckboxChanged(boolean isChecked) {
        com.tangledwebgames.crossfade.data.SettingsManager.setIsMusicOn(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onMusicVolumeSliderChanged(float value) {
        com.tangledwebgames.crossfade.data.SettingsManager.setMusicVolume(value);
    }

    @Override
    public void onAnimateTilesCheckboxChanged(boolean isChecked) {
        com.tangledwebgames.crossfade.data.SettingsManager.setAnimateTiles(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onHighlightTilesCheckboxChanged(boolean isChecked) {
        SettingsManager.setHighlightTiles(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onPauseMenuContinueButtonClicked() {
        MainController.instance.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onPauseMenuBuyButtonClicked() {
        MainController.instance.showPurchaseDialog(false);
        SoundManager.buttonSound();
    }


    // Win dialog

    @Override
    public void onWinContinueButtonClicked() {
        MainController.instance.goToNextLevel();
        SoundManager.buttonSound();
    }


    // Level select menu

    @Override
    public void onLevelSelectContinueButtonClicked() {
        MainController.instance.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onLevelSelected(int level) {
        MainController.instance.goToLevel(level);
        SoundManager.buttonSound();
    }

    // Purchase menu

    @Override
    public void onPurchaseTableBuyButtonClicked() {
        CrossFadePurchaseManager.buyFullVersion();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseTableRestoreButtonClicked() {
        CrossFadePurchaseManager.restore();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseTableCancelButtonClicked() {
        MainController.instance.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseFailedConfirm() {
        MainController.instance.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseSuccessConfirm() {
        MainController.instance.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseNoRestoreConfirm() {
        MainController.instance.unpauseGame();
        SoundManager.buttonSound();
    }

}
