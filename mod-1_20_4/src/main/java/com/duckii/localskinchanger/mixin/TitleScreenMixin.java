package com.duckii.localskinchanger.mixin;

import com.duckii.localskinchanger.screen.SkinChangerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void localSkinChanger$addButton(CallbackInfo ci) {
		this.addDrawableChild(
			ButtonWidget.builder(Text.literal("Skins"), button -> {
				MinecraftClient.getInstance().setScreen(new SkinChangerScreen((Screen) (Object) this));
			}).dimensions(5, this.height - 25, 80, 20).build()
		);
	}
}

