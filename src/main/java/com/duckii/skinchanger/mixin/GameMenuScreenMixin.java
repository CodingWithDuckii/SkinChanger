package com.duckii.skinchanger.mixin;

import com.duckii.skinchanger.client.SkinSelectionScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
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
        // Change skin button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("👕"), button -> {
            this.client.setScreen(new SkinSelectionScreen());
        })
        .dimensions(this.width / 2 + 104, this.height / 4 + 48 + -16, 20, 20)
        .build());
    }

    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        // Render player preview
        int x = this.width / 2 + 114;
        int y = this.height / 4 + 48 + 40;
        InventoryScreen.drawEntity(context, x - 10, y - 20, x + 10, y + 20, 30, 0.0625F, mouseX, mouseY, this.client.player);
    }
}
