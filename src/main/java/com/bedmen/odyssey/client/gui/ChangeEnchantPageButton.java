package com.bedmen.odyssey.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChangeEnchantPageButton extends Button {
    private final boolean isForward;

    public ChangeEnchantPageButton(int x, int y, boolean isForward, Button.IPressable onPress) {
        super(x, y, 8, 8, StringTextComponent.EMPTY, onPress);
        this.isForward = isForward;
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(NewEnchantmentScreen.GUI_TEXTURE);
        int i = 176;
        int j = 8;
        if (this.isHovered()) {
            i += 8;
        }

        if (!this.isForward) {
            j -= 8;
        }

        this.blit(matrixStack, this.x, this.y, i, j, 8, 8);
    }

    public void playDownSound(SoundHandler handler) {
        handler.play(SimpleSound.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));

    }
}
