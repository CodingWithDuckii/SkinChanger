package com.duckii.localskinchanger.mixin;

import com.duckii.localskinchanger.LocalSkinChangerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
	@Inject(method = "getSkinTextureLocation", at = @At("RETURN"), cancellable = true)
	private void localSkinChanger$overrideSkin(CallbackInfoReturnable<ResourceLocation> cir) {
		Minecraft client = Minecraft.getInstance();
		if ((Object) this != client.player) {
			return;
		}

		ResourceLocation texture = LocalSkinChangerClient.selectedSkinTexture(client);
		if (texture != null) {
			cir.setReturnValue(texture);
		}
	}
}

