package com.duckii.skinchanger.mixin;

import com.duckii.skinchanger.client.SkinChangerClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
    @Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
    private void onGetSkin(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;

        // Only apply to the local player
        if (player.isMainPlayer()) {
            // First check if Essential cosmetics has a skin override - respect Essential's cosmetics
            if (SkinChangerClient.isEssentialCosmeticsActive()) {
                Identifier essentialSkin = SkinChangerClient.getEssentialSkinOverride();
                if (essentialSkin != null) {
                    // Essential has a cosmetic skin, let Essential handle it
                    return;
                }
            }

            // Then check if we have a custom skin selected
            if (SkinChangerClient.getCurrentSkinUrl() != null) {
                Identifier customSkinId = SkinChangerClient.getSkinIdentifier(SkinChangerClient.getCurrentSkinUrl());
                if (customSkinId != null) {
                    // In 1.21.11, use SkinAssetInfo which implements TextureAsset
                    AssetInfo.TextureAsset bodyTexture = new AssetInfo.SkinAssetInfo(customSkinId, SkinChangerClient.getCurrentSkinUrl());
                    SkinTextures customTextures = SkinTextures.create(
                        bodyTexture,
                        null, // capeTexture
                        null, // elytraTexture
                        PlayerSkinType.WIDE // model
                    );
                    cir.setReturnValue(customTextures);
                }
            }
        }
    }
}