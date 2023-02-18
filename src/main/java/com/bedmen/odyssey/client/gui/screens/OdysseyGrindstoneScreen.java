package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.inventory.OdysseyGrindstoneMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class OdysseyGrindstoneScreen extends AbstractContainerScreen<OdysseyGrindstoneMenu> {
    private static final ResourceLocation GRINDSTONE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/grindstone.png");
    private PageButton forwardButton;
    private PageButton backButton;
    private static final float ASPECT_TEXT_SCALE = 0.75f;
    private static final int ASPECT_TEXT_LEFT_X = 81;
    private static final int ASPECT_TEXT_TOP_Y = 50;
    private static final int ASPECT_TEXT_RIGHT_X = 166;
    private static final int ASPECT_TEXT_BOTTOM_Y = 63;
    private static final int ASPECT_TEXT_BOX_WIDTH = ASPECT_TEXT_RIGHT_X - ASPECT_TEXT_LEFT_X + 1;

    public OdysseyGrindstoneScreen(OdysseyGrindstoneMenu odysseyGrindstoneMenu, Inventory inventory, Component component) {
        super(odysseyGrindstoneMenu, inventory, component);
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        this.renderBg(poseStack, partialTicks, mouseX, mouseY);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GRINDSTONE_LOCATION);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.showBigRedX()) {
            this.blit(poseStack, this.leftPos + 81, this.topPos + 29, this.imageWidth, 0, 22, 15);
        }
    }

    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);
        Optional<AspectInstance> optionalAspectInstance = this.menu.getSelectedAddedModifierAspect();
        poseStack.pushPose();
        poseStack.scale(ASPECT_TEXT_SCALE, ASPECT_TEXT_SCALE, 1.0f);
        if(optionalAspectInstance.isPresent()){
            List<FormattedCharSequence> formattedCharSequenceList = this.font.split(optionalAspectInstance.get().aspect.getComponent(), (int)(ASPECT_TEXT_BOX_WIDTH/ASPECT_TEXT_SCALE));
            float yOffset = 8.5f + -4.5f * formattedCharSequenceList.size();
            for(FormattedCharSequence formattedcharsequence : formattedCharSequenceList) {
                this.font.draw(poseStack, formattedcharsequence, (ASPECT_TEXT_LEFT_X + (ASPECT_TEXT_BOX_WIDTH - this.font.width(formattedcharsequence)*ASPECT_TEXT_SCALE)/2.0f)/ASPECT_TEXT_SCALE, (ASPECT_TEXT_TOP_Y + yOffset)/ASPECT_TEXT_SCALE, 4210752);
                yOffset += 9;
            }
        }
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
        int topYForButtons = this.topPos + ASPECT_TEXT_BOTTOM_Y + 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(this.leftPos + ASPECT_TEXT_RIGHT_X - 19, topYForButtons, true, (button) -> this.pageForward(), false));
        this.backButton = this.addRenderableWidget(new PageButton(this.leftPos + ASPECT_TEXT_LEFT_X - 3, topYForButtons, false, (button) -> this.pageBack(), false));
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