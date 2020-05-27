package com.tangledwebgames.crossfade;

import com.tangledwebgames.crossfade.sound.SoundManager;
import com.tangledwebgames.crossfade.ui.UiReceiver;

public class UiEventHandler implements UiReceiver {

    private MainController mainController;

    UiEventHandler() {
        this.mainController = MainController.instance;
    }

    // Main UI

    @Override
    public void onPreviousButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        mainController.goToPreviousLevel();
        SoundManager.buttonSound();
    }

    @Override
    public void onNextButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        mainController.goToNextLevel();
        SoundManager.buttonSound();
    }

    @Override
    public void onResetButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        mainController.resetLevel();
        SoundManager.buttonSound();
    }

    @Override
    public void onLevelSelectButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        mainController.showLevelSelectMenu();
        SoundManager.buttonSound();
    }

    @Override
    public void onMenuButtonClicked() {
        if (!isMainUiTableEnabled()) return;
        mainController.showMainMenu();
        SoundManager.buttonSound();
    }

    @Override
    public boolean isMainUiTableEnabled() {
        return mainController.inGame();
    }


    // Pause menu

    @Override
    public void onSfxCheckboxChanged(boolean isChecked) {
        SettingsManager.setIsSfxOn(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onSfxVolumeSliderChanged(float value) {
        SettingsManager.setSfxVolume(value);
    }

    @Override
    public void onMusicCheckboxChanged(boolean isChecked) {
        SettingsManager.setIsMusicOn(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onMusicVolumeSliderChanged(float value) {
        SettingsManager.setMusicVolume(value);
    }

    @Override
    public void onAnimateTilesCheckboxChanged(boolean isChecked) {
        SettingsManager.setAnimateTiles(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onHighlightTilesCheckboxChanged(boolean isChecked) {
        SettingsManager.setHighlightTiles(isChecked);
        SoundManager.buttonSound();
    }

    @Override
    public void onPauseMenuContinueButtonClicked() {
        mainController.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onPauseMenuBuyButtonClicked() {
        mainController.showPurchaseDialog(false);
        SoundManager.buttonSound();
    }


    // Win dialog

    @Override
    public void onWinContinueButtonClicked() {
        mainController.goToNextLevel();
        SoundManager.buttonSound();
    }


    // Level select menu

    @Override
    public void onLevelSelectContinueButtonClicked() {
        mainController.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onLevelSelected(int level) {
        mainController.goToLevel(level);
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
        mainController.unpauseGame();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseFailedConfirm() {
        mainController.showPurchaseFailedDialog();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseSuccessConfirm() {
        mainController.showPurchaseSuccessDialog();
        SoundManager.buttonSound();
    }

    @Override
    public void onPurchaseNoRestoreConfirm() {
        mainController.showPurchaseNoRestoreDialog();
        SoundManager.buttonSound();
    }

}
