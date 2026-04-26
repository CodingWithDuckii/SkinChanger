package com.duckii.skinchanger.mixin;

import com.duckii.skinchanger.client.SkinSelectionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        // Add skin changer button - position it next to the "Mods" button
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("skinchanger.button.title"), button -> {
                    MinecraftClient.getInstance().setScreen(new SkinSelectionScreen());
                })
                .dimensions(this.width - 104, 4, 100, 20)
                .build());
    }
}