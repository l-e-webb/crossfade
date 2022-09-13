package com.tangledwebgames.crossfade.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.crossfade.data.AssetLoader;

public class UiStage extends Stage {

    protected Skin skin;
    protected NinePatchDrawable tile9Patch;
    protected NinePatchDrawable box9Patch;
    protected TextureRegionDrawable whiteBox;

    public UiStage(Viewport viewport) {
        super(viewport);
    }

    protected void initStyle() {
        //Font inits.
        BitmapFont titleFont = AssetLoader.instance.titleFont;
        titleFont.getData().setScale(Dimensions.TITLE_SCALE);
        titleFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );
        BitmapFont uiFont = AssetLoader.instance.uiFont;
        uiFont.getData().setScale(Dimensions.TEXT_SCALE);
        uiFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );
        BitmapFont smallFont = AssetLoader.instance.smallFont;
        smallFont.getData().setScale(Dimensions.SMALL_TEXT_SCALE);
        smallFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear
        );

        //Style inits.
        Label.LabelStyle labelStyle = new Label.LabelStyle(uiFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle smallStyle = new Label.LabelStyle(smallFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Dimensions.PRIMARY_COLOR);
        Label.LabelStyle highlightStyle = new Label.LabelStyle(
                uiFont,
                Dimensions.ACTIVE_BUTTON_COLOR
        );
        Label.LabelStyle deemphasisStyle = new Label.LabelStyle(uiFont, Dimensions.DARK_COLOR);
        Label.LabelStyle linkStyle = new Label.LabelStyle(uiFont, Dimensions.ACTIVE_BUTTON_COLOR);
        tile9Patch = new NinePatchDrawable(
                new NinePatch(AssetLoader.instance.tileSmall, 13, 13, 13, 13));
        NinePatchDrawable button9Patch = new NinePatchDrawable(tile9Patch);
        button9Patch.setLeftWidth(26);
        button9Patch.setRightWidth(26);
        box9Patch = new NinePatchDrawable(
                new NinePatch(AssetLoader.instance.greyBox, 3, 3, 3, 3));
        whiteBox = new TextureRegionDrawable(AssetLoader.instance.whiteBox);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = button9Patch.tint(Dimensions.BUTTON_COLOR);
        buttonStyle.down = button9Patch.tint(Dimensions.ACTIVE_BUTTON_COLOR);
        buttonStyle.fontColor = Dimensions.DARK_TEXT_COLOR;
        buttonStyle.downFontColor = Dimensions.DARK_TEXT_COLOR;
        buttonStyle.font = uiFont;
        TextureRegionDrawable checkbox = new TextureRegionDrawable(
                AssetLoader.instance.tileSmall
        );
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(
                checkbox.tint(Dimensions.OFF_CHECKBOX_COLOR),
                checkbox.tint(Dimensions.PRIMARY_COLOR),
                uiFont,
                Dimensions.PRIMARY_COLOR
        );
        TextureRegionDrawable sliderBackground = new TextureRegionDrawable(
                AssetLoader.instance.sliderBackground
        );
        Sprite sliderKnobSprite = new Sprite(AssetLoader.instance.sliderKnob);
        sliderKnobSprite.setSize(Dimensions.SLIDER_KNOB_WIDTH, Dimensions.SLIDER_KNOB_HEIGHT);
        SpriteDrawable sliderKnob = new SpriteDrawable(sliderKnobSprite);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(
                sliderBackground.tint(Dimensions.SLIDER_COLOR),
                sliderKnob.tint(Dimensions.PRIMARY_COLOR)
        );
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = box9Patch.tint(Dimensions.SLIDER_COLOR);


        //Skin init.
        skin = new Skin();
        skin.add("titleStyle", titleStyle);
        skin.add("smallStyle", smallStyle);
        skin.add("highlightStyle", highlightStyle);
        skin.add("deemphasisStyle", deemphasisStyle);
        skin.add("linkStyle", linkStyle);
        skin.add("default", labelStyle);
        skin.add("default", buttonStyle);
        skin.add("default", checkBoxStyle);
        skin.add("default-horizontal", sliderStyle);
        skin.add("default", scrollPaneStyle);
    }
}
