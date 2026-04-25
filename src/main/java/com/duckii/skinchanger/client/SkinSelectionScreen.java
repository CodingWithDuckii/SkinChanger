package com.duckii.skinchanger.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class SkinSelectionScreen extends Screen {
    private static final int SKINS_PER_ROW = 3;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PADDING = 10;

    public SkinSelectionScreen() {
        super(Text.translatable("gui.skinchanger.title"));
    }

    @Override
    protected void init() {
        List<String> skins = SkinChangerClient.getSkins();
        int x = (this.width - (SKINS_PER_ROW * (BUTTON_WIDTH + PADDING))) / 2;
        int y = 50;

        for (int i = 0; i < skins.size(); i++) {
            final String skinUrl = skins.get(i);
            int row = i / SKINS_PER_ROW;
            int col = i % SKINS_PER_ROW;

            this.addDrawableChild(ButtonWidget.builder(Text.literal("Skin " + (i + 1)), button -> {
                SkinChangerClient.setCurrentSkinUrl(skinUrl);
            })
            .dimensions(x + col * (BUTTON_WIDTH + PADDING), y + row * (BUTTON_HEIGHT + PADDING), BUTTON_WIDTH, BUTTON_HEIGHT)
            .build());
        }

        // Close button
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> this.close())
                .dimensions(this.width / 2 - 50, this.height - 40, 100, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
