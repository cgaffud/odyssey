package com.bedmen.odyssey.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnchantButton extends Button {
    public boolean notEnoughLapis;
    public boolean notEnoughLevelRequirement;
    public boolean notEnoughLevelCost;
    public ExclusiveFlag exclusiveFlag = new ExclusiveFlag();


    public EnchantButton(int x, int y, Button.IPressable onPress) {
        super(x, y, 96, 10, StringTextComponent.EMPTY, onPress);
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(OdysseyEnchantmentScreen.GUI_TEXTURE);
        int i = 0;
        int j = 166;
        if (this.isHovered()) {
            j += 10;
        }

        this.blit(matrixStack, this.x, this.y, i, j, 96, 10);
    }

    public void playDownSound(SoundHandler handler) {
        //handler.play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));

    }

    public class ExclusiveFlag{
        public int flag;
        // 0 if not excclusive
        // 1 if downgradde
        // 2 if equal
        // 3 if mutal exclusion with another enchantment
        public Enchantment enchantment;

        public void clear(){
            this.flag = 0;
            this.enchantment = null;
        }
    }
}
