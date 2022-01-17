package com.bedmen.odyssey.client.gui;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class OdysseyIngameGui extends ForgeIngameGui
{
    private static final ResourceLocation COCONUT_BLUR_LOCATION = new ResourceLocation(Odyssey.MOD_ID , "textures/misc/coconutblur.png");

    public OdysseyIngameGui(Minecraft mc) {
        super(mc);
    }

    protected void renderArmor(PoseStack mStack, int width, int height)
    {
        minecraft.getProfiler().push("armor");

        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - left_height;

        int level = minecraft.player.getArmorValue();
        int imageYOffset = 122;
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