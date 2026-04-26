package com.duckii.skinchanger.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SkinChangerClient implements ClientModInitializer {
    private static String currentSkinUrl = null;
    private static final List<String> SKINS = new ArrayList<>();
    private static final Map<String, Identifier> DOWNLOADED_SKINS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        // Initialize skins
        SKINS.add("https://www.minecraftskins.com/uploads/skins/2026/04/15/blob-duckie-23995388.png?v951");
        SKINS.add("https://www.minecraftskins.com/uploads/skins/2026/01/24/inverted-23814924.png?v951");

        System.out.println("[SkinChanger] Client initialized successfully!");
    }

    public static String getCurrentSkinUrl() {
        return currentSkinUrl;
    }

    public static void setCurrentSkinUrl(String url) {
        currentSkinUrl = url;
        if (url != null && !DOWNLOADED_SKINS.containsKey(url)) {
            downloadSkin(url);
        }
    }

    public static List<String> getSkins() {
        return SKINS;
    }

    public static Identifier getSkinIdentifier(String url) {
        return DOWNLOADED_SKINS.get(url);
    }

    /**
     * Check if Essential mod is available and has cosmetics enabled
     */
    public static boolean isEssentialCosmeticsActive() {
        try {
            Class<?> essentialApi = Class.forName("gg.essential.api.cosmetics.EssentialCosmeticsApi");
            Object instance = essentialApi.getMethod("getInstance").invoke(null);
            return instance != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the current skin override from Essential if available
     */
    public static Identifier getEssentialSkinOverride() {
        try {
            Class<?> essentialApi = Class.forName("gg.essential.api.cosmetics.EssentialCosmeticsApi");
            Object api = essentialApi.getMethod("getInstance").invoke(null);
            java.lang.reflect.Method getCosmetiSkin = essentialApi.getMethod("getCurrentSkin");
            Object skinResult = getCosmetiSkin.invoke(api);
            if (skinResult instanceof Identifier) {
                return (Identifier) skinResult;
            }
        } catch (Exception e) {
            // Essential not available or no cosmetic skin
        }
        return null;
    }

    private static void downloadSkin(String urlString) {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = URI.create(urlString).toURL();
                try (InputStream is = url.openStream()) {
                    NativeImage image = NativeImage.read(is);
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.execute(() -> {
                        Identifier id = Identifier.of("skinchanger", "skin_" + (urlString.hashCode() & 0x7FFFFFFF));
                        Supplier<String> nameSupplier = () -> "skinchanger:skin_" + urlString.hashCode();
                        client.getTextureManager().registerTexture(id, new NativeImageBackedTexture(nameSupplier, image));
                        DOWNLOADED_SKINS.put(urlString, id);
                    });
                }
            } catch (Exception e) {
                System.err.println("[SkinChanger] Failed to download skin: " + urlString);
                e.printStackTrace();
            }
        });
    }
}