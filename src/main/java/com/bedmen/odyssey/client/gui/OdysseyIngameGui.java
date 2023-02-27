package com.bedmen.odyssey.client.gui;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class OdysseyIngameGui extends ForgeIngameGui
{
    private static final ResourceLocation COCONUT_BLUR_LOCATION = new ResourceLocation(Odyssey.MOD_ID , "textures/misc/coconutblur.png");
    public static final ResourceLocation ODYSSEY_GUI_ICONS_LOCATION = new ResourceLocation(Odyssey.MOD_ID ,"textures/gui/icons.png");

    public OdysseyIngameGui(Minecraft mc) {
        super(mc);
    }

    public Minecraft getMinecraft(){
        return this.minecraft;
    }

    public void renderArmor(PoseStack mStack, int width, int height)
    {
        RenderSystem.setShaderTexture(0, ODYSSEY_GUI_ICONS_LOCATION);
        minecraft.getProfiler().push("armor");

        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - left_height;

        int level = minecraft.player == null ? 80 : minecraft.player.getArmorValue();
        int imageYOffset = 9;
        if(level < 0){
            level *= -1;
            imageYOffset -= 9;
        }
        if(level > 40){
            level -= 40;
            imageYOffset += 9;
        }
        for (int i = 0; level > 0 && i < 10; i++)
        {
            if (i*4+3 < level)
            {
                blit(mStack, left, top, 36, imageYOffset, 9, 9);
            }
            else if (i*4+3 == level)
            {
                blit(mStack, left, top, 27, imageYOffset, 9, 9);
            }
            else if (i*4+2 == level)
            {
                blit(mStack, left, top, 18, imageYOffset, 9, 9);
            }
            else if (i*4+1 == level)
            {
                blit(mStack, left, top, 9, imageYOffset, 9, 9);
            }
            else if (i*4+1 > level)
            {
                blit(mStack, left, top, 0, imageYOffset, 9, 9);
            }
            left += 8;
        }
        left_height += 10;

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }

    public void renderFlight(int width, int height, PoseStack mStack)
    {
        RenderSystem.setShaderTexture(0, ODYSSEY_GUI_ICONS_LOCATION);
        minecraft.getProfiler().push("flight");
        Player player = (Player)this.minecraft.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - right_height;

        if (player instanceof OdysseyLivingEntity odysseyLivingEntity)
        {
            int maxFlight = odysseyLivingEntity.getMaxFlight();
            if(maxFlight > 0){
                double flight = odysseyLivingEntity.getFlightValue();
                int full = Mth.ceil((flight - 1.99d) * 10.0D / (double)maxFlight);
                int partial = Mth.ceil(flight * 10.0D / (double)maxFlight) - full;

                for (int i = 0; i < full + partial; ++i)
                {
                    int x = (i < full ? 0 : 9);
                    blit(mStack, left - i * 8 - 9, top, x, 27, 9, 9);
                }
                right_height += 10;
            }
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }

    public void renderOdysseyHelmet(float partialTicks, PoseStack poseStack)
    {
        ItemStack itemstack = this.minecraft.player.getInventory().getArmor(3);

        if (this.minecraft.options.getCameraType().isFirstPerson() && !itemstack.isEmpty())
        {
            Item item = itemstack.getItem();
            if (item == ItemRegistry.HOLLOW_COCONUT.get()){
                renderTextureOverlay(COCONUT_BLUR_LOCATION, 1.0F);
            }
            else
            {
                RenderProperties.get(item).renderHelmetOverlay(itemstack, minecraft.player, this.screenWidth, this.screenHeight, partialTicks);
            }
        }
    }

    public void renderSearingOverlay(float temperature)
    {
        int i = this.minecraft.getWindow().getGuiScaledWidth();
        int j = this.minecraft.getWindow().getGuiScaledHeight();
        temperature = Mth.clamp(temperature, 0.0f, 1.0f);
        double d0 = Mth.lerp(temperature, 2.0D, 1.0D);
        float f = 0.4F * temperature;
        float f1 = 0.2F * temperature;
        float f2 = 0.2F * temperature;
        double d1 = (double)i * d0;
        double d2 = (double)j * d0;
        double d3 = ((double)i - d1) / 2.0D;
        double d4 = ((double)j - d2) / 2.0D;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RenderSystem.setShaderColor(f, f1, f2, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, GameRenderer.NAUSEA_LOCATION);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(d3, d4 + d2, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(d3 + d1, d4 + d2, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(d3 + d1, d4, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(d3, d4, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}