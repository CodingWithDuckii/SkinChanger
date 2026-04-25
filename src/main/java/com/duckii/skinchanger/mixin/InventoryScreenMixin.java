package com.duckii.skinchanger.mixin;

import com.duckii.skinchanger.client.SkinSelectionScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends Screen {
    protected InventoryScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        int x = (this.width - 176) / 2;
        int y = (this.height - 166) / 2;
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("👕"), button -> {
            this.client.setScreen(new SkinSelectionScreen());
        })
        .dimensions(x + 150, y + 60, 20, 20)
        .build());
    }
}
