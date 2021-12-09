package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuiverScreen extends AbstractContainerScreen<QuiverMenu> {
    private ResourceLocation guiTexture;

    public QuiverScreen(QuiverMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/quiver.png");
    }

    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.guiTexture);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int size = this.menu.getSize();
        for(int k = 0; k < size; k++){
            this.blit(matrixStack, i + 88 + k * 18 - size * 9, j + 33, 176, 0, 18, 18);
        }
    }
}