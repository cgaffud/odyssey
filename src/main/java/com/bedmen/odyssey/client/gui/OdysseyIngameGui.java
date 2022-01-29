package com.bedmen.odyssey.client.gui;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.IOdysseyLivingEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

        if (player instanceof IOdysseyLivingEntity odysseyLivingEntity && odysseyLivingEntity.getMaxGlidingTicks() > 0)
        {
            int maxGlidingTicks = odysseyLivingEntity.getMaxGlidingTicks();
            int glidingTicksLeft = maxGlidingTicks - odysseyLivingEntity.getGlidingTicks();
            int full = Mth.ceil((double)(glidingTicksLeft - 2) * 10.0D / maxGlidingTicks);
            int partial = Mth.ceil((double)glidingTicksLeft * 10.0D / maxGlidingTicks) - full;

            for (int i = 0; i < full + partial; ++i)
            {
                int x = (i < full ? 0 : 9);
                blit(mStack, left - i * 8 - 9, top, x, 27, 9, 9);
            }
            right_height += 10;
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }

    public void renderOdysseyHelmet(float partialTicks, PoseStack mStack)
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
}