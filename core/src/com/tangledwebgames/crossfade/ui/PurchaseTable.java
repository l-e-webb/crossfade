package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tangledwebgames.crossfade.CrossFadePurchaseManager;
import com.tangledwebgames.crossfade.MainScreen;

class PurchaseTable extends Table {

    private Label textLabel;
    private TextButton buyButton;
    private TextButton restoreButton;
    private TextButton cancelButton;

    PurchaseTable(Skin skin, Drawable drawable) {
        super();
        background(drawable);
        Label headingLabel = new Label(UiText.BUY_FULL_VERSION, skin);
        textLabel = new Label("", skin, "smallStyle");
        textLabel.setWrap(true);
        buyButton = new TextButton("", skin);
        restoreButton = new TextButton(UiText.RESTORE, skin);
        cancelButton = new TextButton(UiText.CANCEL, skin);

        final PurchaseTable purchaseTable = this;
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchaseTable.waitForTransaction();
                CrossFadePurchaseManager.buyFullVersion();
            }
        });
        restoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchaseTable.waitForTransaction();
                CrossFadePurchaseManager.restore();
            }
        });
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainScreen.instance.unpauseGame();
            }
        });

        //Layout
        pad(Dimensions.PADDING_LARGE);
        add(headingLabel).padBottom(Dimensions.PADDING_LARGE).center();
        row();
        add(textLabel).padBottom(Dimensions.PADDING_LARGE).expand().fill();
        row();
        add(buyButton).center().height(Dimensions.PAUSE_BUTTON_HEIGHT).minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH).padBottom(Dimensions.PADDING_MEDIUM);
        row();
        add(restoreButton).center().height(Dimensions.PAUSE_BUTTON_HEIGHT).minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH).padBottom(Dimensions.PADDING_MEDIUM);
        row();
        add(cancelButton).center().height(Dimensions.PAUSE_BUTTON_HEIGHT).minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH).padBottom(Dimensions.PADDING_MEDIUM);
        row();
    }

    void update(boolean fromAttemptToAccessUnavailableContent) {
        buyButton.setText(UiText.BUY + " (" + CrossFadePurchaseManager.getLocalPrice() + ")");
        String mainText = fromAttemptToAccessUnavailableContent ?
                UiText.CONTENT_UNAVAILABLE : CrossFadePurchaseManager.getLocalDescription();
        mainText += " " + UiText.RESTORE_PROMPT;
        Gdx.app.log("FIND ME", mainText);
        textLabel.setText(mainText);
        invalidateHierarchy();
    }

    void enableAll() {
        buyButton.setDisabled(false);
        restoreButton.setDisabled(false);
        cancelButton.setDisabled(false);
    }

    void waitForTransaction() {
        buyButton.setDisabled(true);
        restoreButton.setDisabled(true);
        cancelButton.setDisabled(true);
    }

}
