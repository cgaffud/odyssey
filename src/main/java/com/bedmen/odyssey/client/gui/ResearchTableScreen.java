package com.bedmen.odyssey.client.gui;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.ResearchTableContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ResearchTableScreen extends ContainerScreen<ResearchTableContainer> {
    private static final ResourceLocation RESEARCH_TABLE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/research_table.png");
    private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};

    public ResearchTableScreen(ResearchTableContainer p_i51097_1_, PlayerInventory p_i51097_2_, ITextComponent p_i51097_3_) {
        super(p_i51097_1_, p_i51097_2_, p_i51097_3_);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(matrixStack, p_230430_2_, p_230430_3_);
    }

    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(RESEARCH_TABLE_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = this.menu.getFuel() * 2;
        if (k > 0) {
            this.blit(matrixStack, i + 39, j + 63 - k, 176, 10-k, 8, k);
        }

        int i1 = this.menu.getBrewingTicks();
        if (i1 > 0) {
            int j1 = (int)(86.0F * (1.0F - (float)i1 / 400.0F));
            if (j1 > 0) {
                this.blit(matrixStack, i + 53, j + 50, 0, 166, j1, 16);
            }
            int color = this.menu.getCurse() ? 0xFF0000 : 0x00FFFF;
            ITextProperties itextproperties = EnchantmentNameParts.getInstance().getRandomName(this.font, 68);
            this.font.drawWordWrap(itextproperties, i+70, j + 54, 68, color);
            drawBox(matrixStack, Integer.max(i+53+14, i+53+j1), j+50, Integer.min(72, 86-j1));
        }

    }

    protected void drawBox(MatrixStack matrixStack, int i, int j, int k) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        int slotColor = 0xFF8B8B8B;
        this.fillGradient(matrixStack, i, j, i+k, j + 16, slotColor, slotColor);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }
}