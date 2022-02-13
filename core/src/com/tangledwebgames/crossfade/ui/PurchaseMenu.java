package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tangledwebgames.crossfade.CrossFadePurchaseManager;

class PurchaseMenu extends Table {

    private Label textLabel;
    private TextButton buyButton;
    private TextButton restoreButton;
    private TextButton cancelButton;

    PurchaseMenu(Skin skin, UiReceiver receiver, Drawable drawable) {
        super();
        background(drawable);
        Label headingLabel = new Label(UiText.BUY_FULL_VERSION, skin);
        textLabel = new Label("", skin, "smallStyle");
        textLabel.setWrap(true);
        buyButton = new TextButton("", skin);
        restoreButton = new TextButton(UiText.RESTORE, skin);
        cancelButton = new TextButton(UiText.CANCEL, skin);

        final PurchaseMenu purchaseMenu = this;
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchaseMenu.waitForTransaction();
                receiver.onPurchaseTableBuyButtonClicked();
            }
        });
        restoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchaseMenu.waitForTransaction();
                receiver.onPurchaseTableRestoreButtonClicked();
            }
        });
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receiver.onPurchaseTableCancelButtonClicked();
            }
        });

        //Layout
        pad(Dimensions.PADDING_LARGE);
        add(headingLabel).padBottom(Dimensions.PADDING_LARGE).center();
        row();
        add(textLabel).padBottom(Dimensions.PADDING_LARGE).expand().fill();
        row();
        add(buyButton).center()
                .height(Dimensions.PAUSE_BUTTON_HEIGHT)
                .minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH)
                .padBottom(Dimensions.PADDING_MEDIUM);
        row();
        add(restoreButton).center()
                .height(Dimensions.PAUSE_BUTTON_HEIGHT)
                .minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH)
                .padBottom(Dimensions.PADDING_MEDIUM);
        row();
        add(cancelButton).center()
                .height(Dimensions.PAUSE_BUTTON_HEIGHT)
                .minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH)
                .padBottom(Dimensions.PADDING_MEDIUM);
        row();
    }

    void update(boolean fromAttemptToAccessUnavailableContent) {
        String localPrice = CrossFadePurchaseManager.getLocalPrice();
        String buyText = UiText.BUY;
        if (localPrice != null) {
            buyText += "(" + localPrice + ")";
        }
        buyButton.setText(buyText);

        String description = CrossFadePurchaseManager.getLocalDescription();
        String mainText;
        if (fromAttemptToAccessUnavailableContent) {
            mainText = UiText.CONTENT_UNAVAILABLE;
        } else if (description != null) {
            mainText = description;
        } else {
            mainText = "";
        };
        mainText += " " + UiText.RESTORE_PROMPT;
        textLabel.setText(mainText);
        invalidateHierarchy();
    }

    void enableAll() {
        buyButton.setDisabled(false);
        restoreButton.setDisabled(false);
        cancelButton.setDisabled(false);
    }

    private void waitForTransaction() {
        buyButton.setDisabled(true);
        restoreButton.setDisabled(true);
        cancelButton.setDisabled(true);
    }

}
