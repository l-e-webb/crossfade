package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class CrossFadeDialog extends Table {

    private Label textLabel;
    private TextButton confirmButton;
    private TextButton cancelButton;

    CrossFadeDialog(Skin skin, Drawable background) {
        super();
        this.background(background);
        textLabel = new Label("", skin);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        confirmButton = new TextButton("", skin);
        cancelButton = new TextButton("", skin);
    }

    void setLabelText(String text) {
        textLabel.setText(text);
        rebuild();
    }

    void setButtonText(String confirmText, String cancelText) {
        confirmButton.setText(confirmText);
        cancelButton.setText(cancelText);
        rebuild();
    }

    void setButtonText(String confirmText) {
        setButtonText(confirmText, "");
    }

    void setConfirmButtonListener(ClickListener listener) {
        confirmButton.clearListeners();
        confirmButton.addListener(listener);
    }

    void setCancelButtonListener(ClickListener listener) {
        cancelButton.clearListeners();
        cancelButton.addListener(listener);
    }

    private void rebuild() {
        clearChildren();

        //Layout
        boolean includeCancelButton = cancelButton.getText().length() > 0;
        pad(Dimensions.PADDING_LARGE);
        add(textLabel)
                .expand().fill().center()
                .colspan(includeCancelButton ? 2 : 1)
                .padBottom(Dimensions.PADDING_LARGE);
        row();
        add(confirmButton)
                .center()
                .height(Dimensions.PAUSE_BUTTON_HEIGHT);
        if (includeCancelButton) {
            add(cancelButton)
                    .center()
                    .height(Dimensions.PAUSE_BUTTON_HEIGHT);
        }
        row();
    }
}
