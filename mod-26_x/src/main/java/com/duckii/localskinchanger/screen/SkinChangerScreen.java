package com.duckii.localskinchanger.screen;

import com.duckii.localskinchanger.LocalSkinChangerClient;
import com.duckii.localskinchanger.skin.AvailableSkin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class SkinChangerScreen extends Screen {
	private final Screen parent;

	public SkinChangerScreen(Screen parent) {
		super(Component.literal("Skins"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int buttonWidth = 200;
		int buttonHeight = 20;
		int spacing = 6;

		int centerX = this.width / 2;
		int topY = this.height / 4 + 20;

		this.addRenderableWidget(
			Button.builder(Component.literal("Equip: " + AvailableSkin.BLOB_DUCKIE.displayName()), button -> {
				LocalSkinChangerClient.selectSkin(Minecraft.getInstance(), AvailableSkin.BLOB_DUCKIE);
			}).bounds(centerX - buttonWidth / 2, topY, buttonWidth, buttonHeight).build()
		);

		this.addRenderableWidget(
			Button.builder(Component.literal("Equip: " + AvailableSkin.INVERTED.displayName()), button -> {
				LocalSkinChangerClient.selectSkin(Minecraft.getInstance(), AvailableSkin.INVERTED);
			}).bounds(centerX - buttonWidth / 2, topY + buttonHeight + spacing, buttonWidth, buttonHeight).build()
		);

		this.addRenderableWidget(
			Button.builder(Component.literal("Back"), button -> {
				Minecraft.getInstance().setScreen(parent);
			}).bounds(centerX - buttonWidth / 2, topY + (buttonHeight + spacing) * 2 + 10, buttonWidth, buttonHeight).build()
		);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(graphics);
		graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

		AvailableSkin selected = LocalSkinChangerClient.selectedSkin();
		graphics.drawCenteredString(this.font, "Selected: " + selected.displayName(), this.width / 2, 40, 0xAAAAAA);

		drawPreview(graphics, AvailableSkin.BLOB_DUCKIE, this.width / 2 - 80, 60);
		drawPreview(graphics, AvailableSkin.INVERTED, this.width / 2 + 16, 60);

		super.render(graphics, mouseX, mouseY, delta);
	}

	private void drawPreview(GuiGraphics graphics, AvailableSkin skin, int x, int y) {
		Minecraft client = Minecraft.getInstance();
		ResourceLocation texture = LocalSkinChangerClient.selectedSkinTexture(client);
		if (LocalSkinChangerClient.selectedSkin() != skin) {
			texture = com.duckii.localskinchanger.skin.SkinRegistry.textureFor(client, skin);
		}
		if (texture != null) {
			graphics.blit(texture, x, y, 0, 0, 64, 64, 64, 64);
		}
		graphics.drawCenteredString(this.font, skin.displayName(), x + 32, y + 70, 0xFFFFFF);
	}
}

