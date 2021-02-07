package com.bedmen.odyssey.client.gui;


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.InfuserContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfuserScreen extends ContainerScreen<InfuserContainer> {
    private ResourceLocation guiTexture;

    public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/infuser.png");
        this.xSize = 176;
        this.ySize = 198;
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

        int l = this.container.getCookProgressionScaled();
        l = MathHelper.clamp(l, 0, 30);
        this.blit(matrixStack, i + 31, j + 28, 176, 0, 78-l, l);
        this.blit(matrixStack, i + 31 + 78 - l, j + 28, 176 + 78 - l, 0, l, 78-l);
        this.blit(matrixStack, i + 31, j + 28 + l, 176, l, l, 78-l);
        this.blit(matrixStack, i + 31 + l, j + 28 + 78 - l, 176 + l, 78 - l, 78 - l, l);

        if(this.container.hasLight()) this.blit(matrixStack, i + 71, j + 19, 176, 78, 58, 71);
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY+32, 4210752);
    }
}