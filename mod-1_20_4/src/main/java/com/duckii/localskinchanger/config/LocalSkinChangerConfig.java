package com.duckii.localskinchanger.config;

import com.duckii.localskinchanger.skin.AvailableSkin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class LocalSkinChangerConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("local_skin_changer.json");

	private String selectedSkinId = AvailableSkin.BLOB_DUCKIE.id();

	public static LocalSkinChangerConfig load() {
		if (!Files.exists(PATH)) {
			LocalSkinChangerConfig config = new LocalSkinChangerConfig();
			config.save();
			return config;
		}

		try (BufferedReader reader = Files.newBufferedReader(PATH)) {
			LocalSkinChangerConfig loaded = GSON.fromJson(reader, LocalSkinChangerConfig.class);
			if (loaded == null) {
				return new LocalSkinChangerConfig();
			}
			if (loaded.selectedSkinId == null || loaded.selectedSkinId.isBlank()) {
				loaded.selectedSkinId = AvailableSkin.BLOB_DUCKIE.id();
			}
			return loaded;
		} catch (IOException | JsonSyntaxException e) {
			return new LocalSkinChangerConfig();
		}
	}

	public void save() {
		try {
			Files.createDirectories(PATH.getParent());
		} catch (IOException e) {
			return;
		}

		try (BufferedWriter writer = Files.newBufferedWriter(PATH)) {
			GSON.toJson(this, writer);
		} catch (IOException ignored) {
		}
	}

	public AvailableSkin selectedSkin() {
		return AvailableSkin.byId(selectedSkinId).orElse(AvailableSkin.BLOB_DUCKIE);
	}

	public void setSelectedSkin(AvailableSkin skin) {
		this.selectedSkinId = skin.id();
	}
}

