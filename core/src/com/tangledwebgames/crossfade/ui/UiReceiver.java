package com.tangledwebgames.crossfade.ui;

public interface UiReceiver {

    // Main UI table
    void onPreviousButtonClicked();
    void onNextButtonClicked();
    void onResetButtonClicked();
    void onLevelSelectButtonClicked();
    void onMenuButtonClicked();
    boolean isMainUiTableEnabled();

    // Pause menu
    void onSfxCheckboxChanged(boolean isChecked);
    void onSfxVolumeSliderChanged(float value);
    void onMusicCheckboxChanged(boolean isChecked);
    void onMusicVolumeSliderChanged(float value);
    void onAnimateTilesCheckboxChanged(boolean isChecked);
    void onHighlightTilesCheckboxChanged(boolean isChecked);
    void onPauseMenuContinueButtonClicked();
    void onPauseMenuBuyButtonClicked();

    // Win menu
    void onWinContinueButtonClicked();

    // Level select menu
    void onLevelSelectContinueButtonClicked();
    void onLevelSelected(int level);

    //Purchase table
    void onPurchaseTableBuyButtonClicked();
    void onPurchaseTableRestoreButtonClicked();
    void onPurchaseTableCancelButtonClicked();

    //Dialog
    void onPurchaseFailedConfirm();
    void onPurchaseSuccessConfirm();
    void onPurchaseNoRestoreConfirm();

}
