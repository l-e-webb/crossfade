package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class CrossFadeDialog extends Table {

    private Label headerLabel;
    private Label textLabel;
    private TextButton confirmButton;
    private TextButton cancelButton;

    CrossFadeDialog(Skin skin, Drawable background) {
        super();
        this.background(background);
        headerLabel = new Label("", skin);
        headerLabel.setWrap(true);
        headerLabel.setAlignment(Align.center);
        textLabel = new Label("", skin, "smallStyle");
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        confirmButton = new TextButton("", skin);
        cancelButton = new TextButton("", skin);
    }

    void setText(
            String headerText,
            String labelText,
            String confirmText,
            String cancelText
    ) {
        headerLabel.setText(headerText);
        textLabel.setText(labelText);
        confirmButton.setText(confirmText);
        cancelButton.setText(cancelText);
        rebuild();
    }

    void setHeaderText(String text) {
        headerLabel.setText(text);
        rebuild();
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
        boolean includeHeader = headerLabel.getText().length() > 0;
        boolean includeLabel = textLabel.getText().length > 0;
        pad(Dimensions.PADDING_LARGE);
        defaults().space(Dimensions.PADDING_LARGE);
        if (includeHeader) {
            row();
            add(headerLabel)
                    .growX().center().colspan(includeCancelButton ? 2 : 1);
        }
        if (includeLabel) {
            row();
            add(textLabel)
                    .grow().center().colspan(includeCancelButton ? 2 : 1);
        }
        row();
        add(confirmButton)
                .center()
                .height(Dimensions.PAUSE_BUTTON_HEIGHT);
        if (includeCancelButton) {
            add(cancelButton)
                    .center()
                    .height(Dimensions.PAUSE_BUTTON_HEIGHT);
        }
    }
}
