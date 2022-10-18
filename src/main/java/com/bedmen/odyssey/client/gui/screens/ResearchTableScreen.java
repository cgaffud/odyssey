package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.ResearchTableBlockEntity;
import com.bedmen.odyssey.inventory.ResearchTableMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/research_table.png");

    public ResearchTableScreen(ResearchTableMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
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

    protected void renderBg(PoseStack poseStack, float p_97854_, int p_97855_, int p_97856_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int inkPixels = this.menu.getInk() * 2;
        if (inkPixels > 0) {
            this.blit(poseStack, i + 39, j + 63 - inkPixels, 176, 10-inkPixels, 8, inkPixels);
        }

        int researchTicks = this.menu.getResearchTicks();
        if (researchTicks > 0) {
            int j1 = (int)(86.0F * (float)researchTicks / (float) ResearchTableBlockEntity.TOTAL_RESEARCH_TIME);
            if (j1 > 0) {
                this.blit(poseStack, i + 53, j + 50, 0, 166, j1, 16);
            }
            int color = this.menu.getCurse() ? 0xFF0000 : 0x00FFFF;
            FormattedText formattedText = EnchantmentNames.getInstance().getRandomName(this.font, 68);
            this.font.drawWordWrap(formattedText, i+70, j + 54, 68, color);
            drawBox(poseStack, Integer.max(i+53+14, i+53+j1), j+50, Integer.min(72, 86-j1));
        }
    }

    protected void drawBox(PoseStack poseStack, int leftX, int topY, int horizontalLength) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        int slotColor = 0xFF8B8B8B;
        this.fillGradient(poseStack, leftX, topY, leftX+horizontalLength, topY + 16, slotColor, slotColor);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }
}