package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.LoadingScreen;
import com.tangledwebgames.crossfade.data.AssetLoader;

public class LoadingUiController extends UiStage {

    LoadingScreen loadingScreen;
    Table loadingTable;
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
        loadingTable = new Table();
        loadingTable.setFillParent(true);
        loadingTable.add(loading).center();
        loadingTable.setVisible(false);
        addActor(loadingTable);
    }

    public void initFull() {
        initStyle();
        loginDialog = new CrossFadeDialog(skin, tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR));
        addActor(loginDialog);
        dataSharingDialog = new CrossFadeDialog(
                skin,
                tile9Patch.tint(Dimensions.UI_BACKGROUND_COLOR),
                true
        );
        buildDataSharingDialog();
        addActor(dataSharingDialog);
        updatePositions();
        hideLoginDialog();
        hideDataSharingDialog();
    }

    public void showLoading() {
        loadingTable.setVisible(true);
    }

    public void hideLoading() {
        loadingTable.setVisible(false);
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

    public void hideLoginDialog() {
        loginDialog.setVisible(false);
    }

    public void showDataSharingDialog() {
        hideLoading();
        hideLoginDialog();
        dataSharingDialog.setVisible(true);
    }

    public void hideDataSharingDialog() {
        dataSharingDialog.setVisible(false);
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
        hideDataSharingDialog();
        loginDialog.setVisible(true);
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
    }

    public void updatePositions() {
        float worldWidth = getViewport().getWorldWidth();
        float worldHeight = getViewport().getWorldHeight();
        loginDialog.setSize(
                worldWidth * Dimensions.PAUSE_TABLE_WIDTH_RATIO,
                getViewport().getWorldHeight() * Dimensions.GENERIC_DIALOG_HEIGHT_RATIO
        );
        loginDialog.setPosition(
                worldWidth / 2,
                worldHeight / 2,
                Align.center
        );
        dataSharingDialog.setSize(
                worldWidth * Dimensions.PAUSE_TABLE_WIDTH_RATIO,
                getViewport().getWorldHeight() * Dimensions.GENERIC_DIALOG_HEIGHT_RATIO
        );
        dataSharingDialog.setPosition(
                worldWidth / 2,
                worldHeight / 2,
                Align.center
        );
    }

    public boolean isDataSharingDialogVisible() {
        return dataSharingDialog != null && dataSharingDialog.isVisible();
    }
}