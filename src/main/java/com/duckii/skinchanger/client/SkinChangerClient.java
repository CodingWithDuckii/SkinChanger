package com.duckii.skinchanger.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkinChangerClient implements ClientModInitializer {
    private static String currentSkinUrl = null;
    private static final List<String> SKINS = new ArrayList<>();
    private static final Map<String, Identifier> DOWNLOADED_SKINS = new HashMap<>();
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        // Initialize skins
        SKINS.add("https://www.minecraftskins.com/uploads/skins/2026/04/15/blob-duckie-23995388.png?v951");
        SKINS.add("https://www.minecraftskins.com/uploads/skins/2026/01/24/inverted-23814924.png?v951");

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.skinchanger.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.skinchanger"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                client.setScreen(new SkinSelectionScreen());
            }
        });
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

    private static void downloadSkin(String urlString) {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(urlString);
                try (InputStream is = url.openStream()) {
                    NativeImage image = NativeImage.read(is);
                    MinecraftClient.getInstance().execute(() -> {
                        Identifier id = Identifier.of("skinchanger", "skin_" + urlString.hashCode());
                        MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
                        DOWNLOADED_SKINS.put(urlString, id);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
