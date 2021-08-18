package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.IPlayerPermanentBuffs;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends DisplayEffectsScreen<PlayerContainer> implements IRecipeShownListener {

    public MixinInventoryScreen(PlayerContainer p_i51091_1_, PlayerInventory p_i51091_2_, ITextComponent p_i51091_3_) {
        super(p_i51091_1_, p_i51091_2_, p_i51091_3_);
    }

    @Inject(method = "renderBg", at = @At(value = "TAIL"))
    private void onRenderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_, CallbackInfo ci) {
        int i1 = this.leftPos;
        int j1 = this.topPos;
        if(((IPlayerPermanentBuffs) this.inventory.player).getNetherImmune()){
            this.minecraft.getTextureManager().bind(INVENTORY_LOCATION);
            this.blit(matrixStack, i1+78, j1+9, 176, 37, 14, 14);
        }
    }
}


