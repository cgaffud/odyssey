package com.bedmen.odyssey.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Map;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.AIR;

@OnlyIn(Dist.CLIENT)
@Mixin(ForgeIngameGui.class)
public abstract class MixinForgeIngameGui extends IngameGui{

    public MixinForgeIngameGui(Minecraft mcIn) {
        super(mcIn);
    }

    private static boolean renderProt = false;

    @Shadow
    private boolean pre(RenderGameOverlayEvent.ElementType type, MatrixStack mStack){return false;}
    @Shadow
    public static int left_height;
    @Shadow
    public static int right_height;
    @Shadow
    private void post(RenderGameOverlayEvent.ElementType type, MatrixStack mStack){}

    protected void renderArmor(MatrixStack mStack, int width, int height)
    {
        if (pre(ARMOR, mStack)) return;
        minecraft.getProfiler().push("armor");

        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - left_height;

        int level = minecraft.player.getArmorValue();
        int j = 113;
        if(level > 40){
            level -= 40;
            j += 9;
        }
        for (int i = 0; level > 0 && i < 10; i++)
        {
            if (i*4+3 < level)
            {
                blit(mStack, left, top, 36, j, 9, 9);
            }
            else if (i*4+3 == level)
            {
                blit(mStack, left, top, 27, j, 9, 9);
            }
            else if (i*4+2 == level)
            {
                blit(mStack, left, top, 18, j, 9, 9);
            }
            else if (i*4+1 == level)
            {
                blit(mStack, left, top, 9, j, 9, 9);
            }
            else if (i*4+1 > level)
            {
                blit(mStack, left, top, 0, j, 9, 9);
            }
            left += 8;
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
        post(ARMOR, mStack);
    }

    public void renderFood(int width, int height, MatrixStack mStack)
    {
        if (pre(FOOD, mStack)) return;
        minecraft.getProfiler().push("food");

        PlayerEntity player = (PlayerEntity)this.minecraft.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - right_height;
        right_height += 10;
        boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic

        FoodStats stats = minecraft.player.getFoodData();
        int level = stats.getFoodLevel();

        int y = top;

        for (int i = 0; i < 10; ++i)
        {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int icon = 16;
            byte background = 0;

            if (minecraft.player.hasEffect(Effects.HUNGER))
            {
                icon += 36;
                background = 13;
            }
            if (unused) background = 1; //Probably should be a += 1 but vanilla never uses this

            if (player.getFoodData().getSaturationLevel() <= 0.0F && tickCount % (level * 3 + 1) == 0)
            {
                y = top + (random.nextInt(3) - 1);
            }

            blit(mStack, x, y, 16 + background * 9, 27, 9, 9);

            if (idx < level)
                blit(mStack, x, y, icon + 36, 27, 9, 9);
            else if (idx == level)
                blit(mStack, x, y, icon + 45, 27, 9, 9);
        }


        level = 0;
        renderProt = false;

        for(ItemStack itemStack : player.getArmorSlots()){
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
            for(Enchantment e : map.keySet()){
                if(e == Enchantments.ALL_DAMAGE_PROTECTION){
                    level += map.get(e);
                }
            }
        }

        if(level > 0) renderProt = true;

        y -= 10;

        for (int i = 0; level > 0 && i < 5; i++)
        {
            int x = left + i * 8 - 81;
            if (i*4+3 < level)
            {
                blit(mStack, x, y, 36, 140, 9, 9);
            }
            else if (i*4+3 == level)
            {
                blit(mStack, x, y, 27, 140, 9, 9);
            }
            else if (i*4+2 == level)
            {
                blit(mStack, x, y, 18, 140, 9, 9);
            }
            else if (i*4+1 == level)
            {
                blit(mStack, x, y, 9, 140, 9, 9);
            }
            else if (i*4+1 > level)
            {
                blit(mStack, x, y, 0, 140, 9, 9);
            }
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
        post(FOOD, mStack);
    }

    protected void renderAir(int width, int height, MatrixStack mStack)
    {
        if (pre(AIR, mStack)) return;
        minecraft.getProfiler().push("air");
        PlayerEntity player = (PlayerEntity)this.minecraft.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - right_height;

        if(renderProt) top -= 10;

        int air = player.getAirSupply();
        if (player.isEyeInFluid(FluidTags.WATER) || air < 300)
        {
            int full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; ++i)
            {
                blit(mStack, left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
            }
            right_height += 10;
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
        post(AIR, mStack);
    }

}
