package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.LoadingScreen;
import com.tangledwebgames.crossfade.data.AssetLoader;

public class LoadingUiController extends UiStage {

    LoadingScreen loadingScreen;
    Container<Actor> loadingContainer;
    Container<CrossFadeDialog> dialogContainer;
    CrossFadeDialog loginDialog;
    CrossFadeDialog dataSharingDialog;

    public LoadingUiController(Viewport viewport, LoadingScreen loadingScreen) {
        super(viewport);
        this.loadingScreen = loadingScreen;
        initLoading();
    }

    private void initLoading() {
        BitmapFont font = AssetLoader.instance.uiFont;
        font.getData().setScale(Dimensions.TEXT_SCALE);
        font.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Dimensions.PRIMARY_COLOR);
        Label loading = new Label("Loading", labelStyle);
        loadingContainer = new Container<Actor>();
        loadingContainer.setFillParent(true);
        loadingContainer.center();
        loadingContainer.setActor(loading);
        loadingContainer.setVisible(false);
        addActor(loadingContainer);
    }

    public void initFull() {
        initStyle();
        dialogContainer = new Container<CrossFadeDialog>();
        dialogContainer.setFillParent(true);
        Value dialogMinHeight = Value.percentHeight(Dimensions.GENERIC_DIALOG_HEIGHT_RATIO, dialogContainer);
        Value dialogWidth = Value.percentWidth(Dimensions.PAUSE_TABLE_WIDTH_RATIO, dialogContainer);
        dialogContainer.width(dialogWidth);
        dialogContainer.minHeight(dialogMinHeight);
        addActor(dialogContainer);
        dialogContainer.setVisible(false);

        loginDialog = new CrossFadeDialog(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));
        dataSharingDialog = new CrossFadeDialog(
                skin,
                tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR),
                true
        );
        buildDataSharingDialog();
    }

    public void showLoading() {
        loadingContainer.setVisible(true);
    }

    public void hideLoading() {
        loadingContainer.setVisible(false);
    }

    public void showLoginPrompt() {
        showDialog(
                UiText.LOGIN_PROMPT_HEADER,
                UiText.LOGIN_PROMPT_BODY,
                UiText.SIGN_IN,
                UiText.LOGIN_PROMPT_CANCEL
        );
    }

    public void showErrorDialog(Boolean isNetworkError) {
        showDialog(
                UiText.GENERIC_ERROR_HEADER,
                (isNetworkError) ? UiText.LOGIN_PROMPT_NETWORK_ERROR_BODY : UiText.LOGIN_PROMPT_UNKNOWN_ERROR_BODY,
                UiText.LOGIN_PROMPT_TRY_AGAIN,
                UiText.LOGIN_PROMPT_CANCEL
        );
    }

    public void hideDialog() {
        dialogContainer.setActor(null);
        dialogContainer.setVisible(false);
    }

    public void showDataSharingDialog() {
        hideLoading();
        dialogContainer.setActor(dataSharingDialog);
        dialogContainer.setVisible(true);
    }

    private void buildDataSharingDialog() {
        dataSharingDialog.setText(
                UiText.DATA_PRIVACY_HEADER,
                UiText.DATA_PRIVACY_BODY,
                UiText.DATA_PRIVACY_AGREE,
                UiText.DATA_PRIVACY_DISAGREE
        );
        Label privacyPolicy = new Label(UiText.DATA_PRIVACY_POLICY, skin, "linkStyle");
        privacyPolicy.setTouchable(Touchable.enabled);
        dataSharingDialog.row();
        dataSharingDialog.add(privacyPolicy);
        dataSharingDialog.setConfirmButtonListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                loadingScreen.onDataSharingDialogAgree();
            }
        });
        dataSharingDialog.setCancelButtonListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                loadingScreen.onDataShoringDialogDisagree();
            }
        });
        privacyPolicy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                loadingScreen.onPrivacyPolicyClick();
            }
        });
    }

    private void showDialog(
            String headerText, String labelText, String confirmText, String cancelText
    ) {
        hideLoading();
        loginDialog.setText(headerText, labelText, confirmText, cancelText);
        loginDialog.setConfirmButtonListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                loadingScreen.onLoginButtonClicked();
            }
        });
        loginDialog.setCancelButtonListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                loadingScreen.onNoLoginButtonClicked();
            }
        });
        dialogContainer.setActor(loginDialog);
        dialogContainer.setVisible(true);
    }

    public boolean isDataSharingDialogVisible() {
        return dataSharingDialog != null &&
                dialogContainer.isVisible() &&
                dialogContainer.getActor() == dataSharingDialog;
    }
}