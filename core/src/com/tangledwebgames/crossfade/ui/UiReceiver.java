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
    void onShareUsageDataCheckboxChanged(boolean isChecked);
    void onPauseMenuPrivacyPolicyClicked();
    void onPauseMenuContinueButtonClicked();
    void onPauseMenuBuyButtonClicked();
    void onSignOutButtonClicked();
    void onSignInButtonClicked();

    // Win menu
    void onWinContinueButtonClicked();

    // Level select menu
    void onLevelSelectContinueButtonClicked();

    void onLevelSelected(int level);

    // Purchase table
    void onPurchaseTableBuyButtonClicked();
    void onPurchaseTableRestoreButtonClicked();
    void onPurchaseTableCancelButtonClicked();

    // Purchase outcome dialog
    void onPurchaseFailedConfirm();
    void onPurchaseSuccessConfirm();
    void onPurchaseNoRestoreConfirm();
    void onPurchaseUnavailableConfirm();

    // Login dialog
    void onLoginSuccessConfirm();
    void onLoginTryAgain();
    void onLoginCancel();
    void onLogoutConfirm();
}
