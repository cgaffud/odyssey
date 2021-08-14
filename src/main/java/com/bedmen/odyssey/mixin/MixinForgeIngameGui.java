package com.bedmen.odyssey.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
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

        ClientPlayerEntity player = minecraft.player;
        int level = player.getArmorValue();
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

        int resLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.RESPIRATION, player);
        int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, player);
        int blastLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, player);
        int featherLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, player);

        if(resLevel + fireLevel + blastLevel + featherLevel > 0){
            top -= 5;
            left = width / 2 - 91;
            blit(mStack, left, top, 0, 140, 81, 4);
            top += 1;
            left += 1;
            int total = 0;
            for (int i = 0; i < resLevel && total < 20; i++){
                blit(mStack, left, top, 0, 144, 3, 2);
                left += 4;
                total++;
            }
            for (int i = 0; i < fireLevel && total < 20; i++){
                blit(mStack, left, top, 0, 146, 3, 2);
                left += 4;
                total++;
            }
            for (int i = 0; i < blastLevel && total < 20; i++){
                blit(mStack, left, top, 0, 148, 3, 2);
                left += 4;
                total++;
            }
            for (int i = 0; i < featherLevel && total < 20; i++){
                blit(mStack, left, top, 0, 150, 3, 2);
                left += 4;
                total++;
            }
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
        post(ARMOR, mStack);
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
        if (player.isEyeInFluid(FluidTags.LAVA)) {
            int full = MathHelper.ceil((double) (air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceil((double) air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; ++i) {
                blit(mStack, left - i * 8 - 9, top, (i < full ? 0 : 9), 152, 9, 9);
            }
            right_height += 10;
        } else if (player.isEyeInFluid(FluidTags.WATER) || air < 300) {
            int full = MathHelper.ceil((double) (air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceil((double) air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; ++i) {
                blit(mStack, left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
            }
            right_height += 10;
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
        post(AIR, mStack);
    }

}
