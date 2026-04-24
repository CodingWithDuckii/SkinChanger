package com.duckii.localskinchanger.mixin;

import com.duckii.localskinchanger.LocalSkinChangerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerMixin {
	@Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
	private void localSkinChanger$overrideSkin(CallbackInfoReturnable<SkinTextures> cir) {
		MinecraftClient client = MinecraftClient.getInstance();
		if ((Object) this != client.player) {
			return;
		}

		Identifier texture = LocalSkinChangerClient.selectedSkinTexture(client);
		if (texture == null) {
			return;
		}

		SkinTextures original = cir.getReturnValue();
		cir.setReturnValue(new SkinTextures(texture, original.textureUrl(), original.capeTexture(), original.elytraTexture(), original.model(), original.secure()));
	}
}

