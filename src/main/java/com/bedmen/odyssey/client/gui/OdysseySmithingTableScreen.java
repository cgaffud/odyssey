package com.bedmen.odyssey.client.gui;


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.OdysseySmithingTableContainer;
import com.bedmen.odyssey.container.slots.OdysseySmithingTableSlot;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseySmithingTableScreen extends ContainerScreen<OdysseySmithingTableContainer> {
    private ResourceLocation guiTexture;

    public OdysseySmithingTableScreen(OdysseySmithingTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/new_smith_table.png");

    }

    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void drawBoxes(MatrixStack matrixStack) {
        for(int i1 = 1; i1 < 10; ++i1) {
            OdysseySmithingTableSlot slot = (OdysseySmithingTableSlot)this.menu.slots.get(i1);
            if (!slot.shouldBeUsed()) {
                RenderSystem.disableDepthTest();
                int j1 = slot.x+this.leftPos-1;
                int k1 = slot.y+((this.height - this.imageHeight) / 2)-1;
                RenderSystem.colorMask(true, true, true, false);
                int slotColor = -1060714810;
                this.fillGradient(matrixStack, j1, k1, j1 + 18, k1 + 18, slotColor, slotColor);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();
            }
        }
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(this.guiTexture);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.drawBoxes(matrixStack);
    }
}