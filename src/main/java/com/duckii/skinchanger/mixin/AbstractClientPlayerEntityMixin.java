package com.duckii.skinchanger.mixin;

import com.duckii.skinchanger.client.SkinChangerClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void onGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        
        // Only apply to the local player
        if (player.isMainPlayer() && SkinChangerClient.getCurrentSkinUrl() != null) {
            // We need to return a SkinTextures object with our custom texture
            // For now, let's assume we have a way to get the Identifier for the URL
            Identifier customSkinId = SkinChangerClient.getSkinIdentifier(SkinChangerClient.getCurrentSkinUrl());
            if (customSkinId != null) {
                SkinTextures current = cir.getReturnValue();
                // If we don't have the current textures yet, we might need to wait or use defaults
                SkinTextures customTextures = new SkinTextures(
                    customSkinId,
                    null, // textureUrl
                    null, // capeTexture
                    null, // elytraTexture
                    SkinTextures.Model.WIDE, // model
                    true // secure
                );
                cir.setReturnValue(customTextures);
            }
        }
    }
}
