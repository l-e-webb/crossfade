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
    private TextButton button;

    CrossFadeDialog(Skin skin, Drawable background) {
        super();
        this.background(background);
        textLabel = new Label("", skin);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        button = new TextButton("", skin);

        //Layout
        pad(Dimensions.PADDING_LARGE);
        add(textLabel).expand().fill().center().padBottom(Dimensions.PADDING_LARGE);
        row();
        add(button).center().minWidth(Dimensions.PAUSE_BUTTON_MIN_WIDTH).height(Dimensions.PAUSE_BUTTON_HEIGHT);
        row();
    }

    CrossFadeDialog(Skin skin,
                    Drawable background,
                    String labelText,
                    String buttonText,
                    ClickListener clickListener) {
        this(skin, background);
        setLabelText(labelText);
        setButtonText(buttonText);;
        setButtonListener(clickListener);
    }

    void setLabelText(String text) {
        textLabel.setText(text);
        invalidateHierarchy();
    }

    void setButtonText(String text) {
        button.setText(text);
        invalidateHierarchy();
    }

    void setButtonListener(ClickListener listener) {
        button.clearListeners();
        button.addListener(listener);
    }
}
