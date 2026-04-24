package com.duckii.localskinchanger.skin;

import com.duckii.localskinchanger.LocalSkinChangerClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

public final class SkinRegistry {
	private static final Map<AvailableSkin, Identifier> RUNTIME_TEXTURES = new EnumMap<>(AvailableSkin.class);

	private SkinRegistry() {
	}

	public static Identifier textureFor(MinecraftClient client, AvailableSkin skin) {
		Identifier cached = RUNTIME_TEXTURES.get(skin);
		if (cached != null) {
			return cached;
		}

		Identifier runtimeId = Identifier.of(LocalSkinChangerClient.MOD_ID, "runtime/skin/" + skin.id());
		Optional<Resource> resource = client.getResourceManager().getResource(skin.assetIdentifier());
		if (resource.isEmpty()) {
			return null;
		}

		try (InputStream in = resource.get().getInputStream()) {
			NativeImage image = NativeImage.read(in);
			NativeImageBackedTexture texture = new NativeImageBackedTexture(() -> LocalSkinChangerClient.MOD_ID + "/" + skin.id(), image);
			client.getTextureManager().registerTexture(runtimeId, texture);
			RUNTIME_TEXTURES.put(skin, runtimeId);
			return runtimeId;
		} catch (IOException e) {
			return null;
		}
	}
}
