package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.inventory.ArcaneGrindstoneMenu;
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
public class ArcaneGrindstoneScreen extends AbstractContainerScreen<ArcaneGrindstoneMenu> {
    private static final ResourceLocation GRINDSTONE_LOCATION = new ResourceLocation("textures/gui/container/grindstone.png");

    public ArcaneGrindstoneScreen(ArcaneGrindstoneMenu arcaneGrindstoneMenu, Inventory inventory, Component component) {
        super(arcaneGrindstoneMenu, inventory, component);
    }

    public void render(PoseStack poseStack, int p_98792_, int p_98793_, float p_98794_) {
        this.renderBackground(poseStack);
        this.renderBg(poseStack, p_98794_, p_98792_, p_98793_);
        super.render(poseStack, p_98792_, p_98793_, p_98794_);
        this.renderTooltip(poseStack, p_98792_, p_98793_);
    }

    protected void renderBg(PoseStack poseStack, float p_98787_, int p_98788_, int p_98789_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GRINDSTONE_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem()) {
            this.blit(poseStack, i + 92, j + 31, this.imageWidth, 0, 28, 21);
        }

    }
}