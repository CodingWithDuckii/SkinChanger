package com.duckii.skinchanger.mixin;

import com.duckii.skinchanger.client.SkinSelectionScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("HEAD"))
    private void onInitWidgets(CallbackInfo ci) {
        // Add skin button to pause menu
        this.addDrawableChild(ButtonWidget.builder(Text.literal("👕"), button -> {
            this.client.setScreen(new SkinSelectionScreen());
        })
        .dimensions(this.width / 2 + 104, this.height / 4 + 48 + -16, 20, 20)
        .build());
    }
}