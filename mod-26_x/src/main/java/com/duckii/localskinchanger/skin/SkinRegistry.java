package com.duckii.localskinchanger.skin;

import com.duckii.localskinchanger.LocalSkinChangerClient;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.resources.ResourceLocation;

public final class SkinRegistry {
	private static final Map<AvailableSkin, ResourceLocation> RUNTIME_TEXTURES = new EnumMap<>(AvailableSkin.class);

	private SkinRegistry() {
	}

	public static ResourceLocation textureFor(Minecraft client, AvailableSkin skin) {
		ResourceLocation cached = RUNTIME_TEXTURES.get(skin);
		if (cached != null) {
			return cached;
		}

		ResourceLocation runtimeId = new ResourceLocation(LocalSkinChangerClient.MOD_ID, "runtime/skin/" + skin.id());
		Optional<Resource> resource = client.getResourceManager().getResource(skin.assetLocation());
		if (resource.isEmpty()) {
			return null;
		}

		try (InputStream in = resource.get().open()) {
			NativeImage image = NativeImage.read(in);
			DynamicTexture texture = new DynamicTexture(image);
			client.getTextureManager().register(runtimeId, texture);
			RUNTIME_TEXTURES.put(skin, runtimeId);
			return runtimeId;
		} catch (IOException e) {
			return null;
		}
	}
}

