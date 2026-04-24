package com.duckii.localskinchanger;

import com.duckii.localskinchanger.config.LocalSkinChangerConfig;
import com.duckii.localskinchanger.skin.AvailableSkin;
import com.duckii.localskinchanger.skin.SkinRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public final class LocalSkinChangerClient implements ClientModInitializer {
	public static final String MOD_ID = "local_skin_changer";

	private static LocalSkinChangerConfig config;

	@Override
	public void onInitializeClient() {
		config = LocalSkinChangerConfig.load();
	}

	public static LocalSkinChangerConfig config() {
		return config;
	}

	public static AvailableSkin selectedSkin() {
		return config.selectedSkin();
	}

	public static Identifier selectedSkinTexture(MinecraftClient client) {
		return SkinRegistry.textureFor(client, selectedSkin());
	}

	public static void selectSkin(MinecraftClient client, AvailableSkin skin) {
		config.setSelectedSkin(skin);
		config.save();
		SkinRegistry.textureFor(client, skin);
	}
}

