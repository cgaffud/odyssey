package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.inventory.ArcaneGrindstoneMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ArcaneGrindstoneScreen extends AbstractContainerScreen<ArcaneGrindstoneMenu> {
    private static final ResourceLocation ARCANE_GRINDSTONE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/arcane_grindstone.png");
    private PageButton forwardButton;
    private PageButton backButton;

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
        RenderSystem.setShaderTexture(0, ARCANE_GRINDSTONE_LOCATION);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.showBigRedX()) {
            this.blit(poseStack, this.leftPos + 90, this.topPos + 29, this.imageWidth, 0, 22, 15);
        }
    }

    protected void renderLabels(PoseStack poseStack, int p_97809_, int p_97810_) {
        super.renderLabels(poseStack, p_97809_, p_97810_);
        Optional<Aspect> optionalAspect = this.menu.getSelectedAddedModifierAspect();
        poseStack.pushPose();
        float scale = 0.75f;
        poseStack.scale(scale, scale, 1.0f);
        optionalAspect.ifPresent(aspect -> this.font.draw(poseStack, new TranslatableComponent("container.oddc.remove_modifier", aspect.getName()), 77/scale, 49/scale, 4210752));
        poseStack.popPose();
    }

    protected void init() {
        super.init();
        this.createPageControlButtons();
    }

    protected void containerTick() {
        this.updateButtonVisibility();
    }

    protected void createPageControlButtons() {
        int topYForButtons = this.topPos + 65;
        this.forwardButton = this.addRenderableWidget(new PageButton(this.leftPos + 129, topYForButtons, true, (button) -> this.pageForward(), true));
        this.backButton = this.addRenderableWidget(new PageButton(this.leftPos + 74, topYForButtons, false, (button) -> this.pageBack(), true));
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = !this.menu.onLastPage();
        this.backButton.visible = !this.menu.onFirstPage();
    }

    protected void pageBack() {
        this.menu.movePageBackward();
        this.updateButtonVisibility();
    }

    protected void pageForward() {
        this.menu.movePageForward();
        this.updateButtonVisibility();
    }
}