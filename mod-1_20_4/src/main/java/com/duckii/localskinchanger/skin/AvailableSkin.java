package com.duckii.localskinchanger.skin;

import com.duckii.localskinchanger.LocalSkinChangerClient;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.util.Identifier;

public enum AvailableSkin {
	BLOB_DUCKIE(
		"blob_duckie",
		"Blob Duckie",
		"skins/blob_duckie.png",
		"https://www.minecraftskins.com/uploads/skins/2026/04/15/blob-duckie-23995388.png?v951"
	),
	INVERTED(
		"inverted",
		"Inverted",
		"skins/inverted.png",
		"https://www.minecraftskins.com/uploads/skins/2026/01/24/inverted-23814924.png?v951"
	);

	private final String id;
	private final String displayName;
	private final String assetPath;
	private final String sourceUrl;

	AvailableSkin(String id, String displayName, String assetPath, String sourceUrl) {
		this.id = id;
		this.displayName = displayName;
		this.assetPath = assetPath;
		this.sourceUrl = sourceUrl;
	}

	public String id() {
		return id;
	}

	public String displayName() {
		return displayName;
	}

	public String assetPath() {
		return assetPath;
	}

	public String sourceUrl() {
		return sourceUrl;
	}

	public Identifier assetIdentifier() {
		return new Identifier(LocalSkinChangerClient.MOD_ID, assetPath);
	}

	public static Optional<AvailableSkin> byId(String id) {
		return Arrays.stream(values()).filter(s -> s.id.equals(id)).findFirst();
	}
}

