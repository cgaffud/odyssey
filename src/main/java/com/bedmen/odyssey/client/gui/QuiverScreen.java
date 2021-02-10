package com.bedmen.odyssey.client.gui;


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.FletchingTableContainer;
import com.bedmen.odyssey.container.QuiverContainer;
import com.bedmen.odyssey.container.slots.NewSmithingTableSlot;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuiverScreen extends ContainerScreen<QuiverContainer> {
    private ResourceLocation guiTexture;

    public QuiverScreen(QuiverContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/quiver.png");

    }

    public void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.guiTexture);
        int i = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        int size = this.container.getSize();
        for(int k = 0; k < size; k++){
            this.blit(matrixStack, i + 88 + k * 18 - size * 9, j + 33, 176, 0, 18, 18);
        }
    }
}