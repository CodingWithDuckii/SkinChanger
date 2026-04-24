package com.duckii.localskinchanger.screen;

import com.duckii.localskinchanger.LocalSkinChangerClient;
import com.duckii.localskinchanger.skin.AvailableSkin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class SkinChangerScreen extends Screen {
	private final Screen parent;

	public SkinChangerScreen(Screen parent) {
		super(Text.literal("Skins"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int buttonWidth = 200;
		int buttonHeight = 20;
		int spacing = 6;

		int centerX = this.width / 2;
		int topY = this.height / 4 + 20;

		this.addDrawableChild(
			ButtonWidget.builder(Text.literal("Equip: " + AvailableSkin.BLOB_DUCKIE.displayName()), button -> {
				LocalSkinChangerClient.selectSkin(MinecraftClient.getInstance(), AvailableSkin.BLOB_DUCKIE);
			}).dimensions(centerX - buttonWidth / 2, topY, buttonWidth, buttonHeight).build()
		);

		this.addDrawableChild(
			ButtonWidget.builder(Text.literal("Equip: " + AvailableSkin.INVERTED.displayName()), button -> {
				LocalSkinChangerClient.selectSkin(MinecraftClient.getInstance(), AvailableSkin.INVERTED);
			}).dimensions(centerX - buttonWidth / 2, topY + buttonHeight + spacing, buttonWidth, buttonHeight).build()
		);

		this.addDrawableChild(
			ButtonWidget.builder(Text.literal("Back"), button -> {
				MinecraftClient.getInstance().setScreen(parent);
			}).dimensions(centerX - buttonWidth / 2, topY + (buttonHeight + spacing) * 2 + 10, buttonWidth, buttonHeight).build()
		);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

		AvailableSkin selected = LocalSkinChangerClient.selectedSkin();
		context.drawCenteredTextWithShadow(
			this.textRenderer,
			Text.literal("Selected: " + selected.displayName()),
			this.width / 2,
			40,
			0xAAAAAA
		);

		drawPreview(context, AvailableSkin.BLOB_DUCKIE, this.width / 2 - 80, 60);
		drawPreview(context, AvailableSkin.INVERTED, this.width / 2 + 16, 60);

		super.render(context, mouseX, mouseY, delta);
	}

	private void drawPreview(DrawContext context, AvailableSkin skin, int x, int y) {
		Identifier texture = LocalSkinChangerClient.selectedSkinTexture(MinecraftClient.getInstance());
		if (LocalSkinChangerClient.selectedSkin() != skin) {
			texture = com.duckii.localskinchanger.skin.SkinRegistry.textureFor(MinecraftClient.getInstance(), skin);
		}
		if (texture != null) {
			context.drawTexture(texture, x, y, 0, 0, 64, 64, 64, 64);
		}
		context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(skin.displayName()), x + 32, y + 70, 0xFFFFFF);
	}
}

