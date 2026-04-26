package com.duckii.skinchanger.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkinSelectionScreen extends Screen {
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private String currentSkinName = "Default";

    public SkinSelectionScreen() {
        super(net.minecraft.text.Text.literal("Skin Changer"));
    }

    @Override
    protected void init() {
        List<String> skins = SkinChangerClient.getSkins();
        int centerX = this.width / 2;
        int previewY = this.height / 2 - 70;

        // Add skin buttons on the right
        for (int i = 0; i < skins.size(); i++) {
            final String skinUrl = skins.get(i);
            String skinName = (i == 0) ? "Blob Duckie" : "Inverted";

            this.addDrawableChild(ButtonWidget.builder(net.minecraft.text.Text.literal(skinName), button -> {
                SkinChangerClient.setCurrentSkinUrl(skinUrl);
                currentSkinName = skinName;
            })
            .dimensions(centerX + 50, previewY + i * (BUTTON_HEIGHT + 10), BUTTON_WIDTH, BUTTON_HEIGHT)
            .build());
        }

        // Remove skin button
        this.addDrawableChild(ButtonWidget.builder(net.minecraft.text.Text.literal("Remove Skin"), button -> {
            SkinChangerClient.setCurrentSkinUrl(null);
            currentSkinName = "Default";
        })
        .dimensions(centerX - 50 - BUTTON_WIDTH, previewY + (skins.size() + 1) * (BUTTON_HEIGHT + 10), BUTTON_WIDTH, BUTTON_HEIGHT)
        .build());

        // Done button
        this.addDrawableChild(ButtonWidget.builder(net.minecraft.text.Text.literal("Done"), button -> this.close())
                .dimensions(centerX - 50, previewY + (skins.size() + 2) * (BUTTON_HEIGHT + 10), BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        // Get current skin name
        updateCurrentSkinName();
    }

    private void updateCurrentSkinName() {
        String skinUrl = SkinChangerClient.getCurrentSkinUrl();
        if (skinUrl != null) {
            if (skinUrl.contains("blob")) {
                currentSkinName = "Blob Duckie";
            } else {
                currentSkinName = "Inverted";
            }
        } else {
            currentSkinName = "Default";
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dark background
        context.fill(0, 0, this.width, this.height, 0xCC000000);

        int centerX = this.width / 2;
        int previewX = centerX - 64;
        int previewY = this.height / 2 - 70;

        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, "Skin Changer", centerX, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Select a skin:", centerX, 45, 0xAAAAAA);

        // Draw skin preview box (simulated with color)
        context.fill(previewX - 4, previewY - 4, previewX + 132, previewY + 132, 0xFF555555);
        context.fill(previewX, previewY, previewX + 128, previewY + 128, 0xFF333333);

        // Show current skin name in preview box
        if (currentSkinName.equals("Default")) {
            context.drawCenteredTextWithShadow(this.textRenderer, "Default", centerX, previewY + 56, 0xAAAAAA);
            context.drawCenteredTextWithShadow(this.textRenderer, "Steve/Alex", centerX, previewY + 72, 0xAAAAAA);
        } else {
            // Show colored name based on skin
            int color = currentSkinName.equals("Blob Duckie") ? 0xFF66CC : 0x00FFFF;
            context.drawCenteredTextWithShadow(this.textRenderer, currentSkinName, centerX, previewY + 56, color);
            context.drawCenteredTextWithShadow(this.textRenderer, "Skin", centerX, previewY + 72, color);
        }

        // Update skin name in real-time
        updateCurrentSkinName();

        // Show current skin status
        String currentSkin = SkinChangerClient.getCurrentSkinUrl();
        if (currentSkin != null) {
            String skinName = currentSkin.contains("blob") ? "Blob Duckie" : "Inverted";
            context.drawCenteredTextWithShadow(this.textRenderer, "Equipped: " + skinName, centerX, previewY + 140, 0x00FF00);
            context.drawCenteredTextWithShadow(this.textRenderer, "Click Done to Confirm", centerX, previewY + 156, 0xFFFF00);
        } else {
            context.drawCenteredTextWithShadow(this.textRenderer, "Equipped: Default Skin", centerX, previewY + 140, 0x888888);
            context.drawCenteredTextWithShadow(this.textRenderer, "No custom skin", centerX, previewY + 156, 0x888888);
        }

        super.render(context, mouseX, mouseY, delta);
    }
}