package com.duckii.localskinchanger.mixin;

import com.duckii.localskinchanger.screen.SkinChangerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
	protected TitleScreenMixin(Component title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void localSkinChanger$addButton(CallbackInfo ci) {
		this.addRenderableWidget(
			Button.builder(Component.literal("Skins"), button -> {
				Minecraft.getInstance().setScreen(new SkinChangerScreen((Screen) (Object) this));
			}).bounds(5, this.height - 25, 80, 20).build()
		);
	}
}

