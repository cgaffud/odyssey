package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.enchantment.EnchantmentUtil;
import com.bedmen.odyssey.util.FogUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@OnlyIn(Dist.CLIENT)
@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {

    @Overwrite
    public static void setupFog(ActiveRenderInfo activeRenderInfoIn, FogRenderer.FogType fogTypeIn, float farPlaneDistance, boolean nearFog, float partialTicks) {
        FluidState fluidstate = activeRenderInfoIn.getFluidInCamera();
        Entity entity = activeRenderInfoIn.getEntity();
        float hook = net.minecraftforge.client.ForgeHooksClient.getFogDensity(fogTypeIn, activeRenderInfoIn, partialTicks, 0.1F);
        if (hook >= 0) RenderSystem.fogDensity(hook);
        else
        if (fluidstate.is(FluidTags.WATER)) {
            float f = 0.05F;
            if (entity instanceof ClientPlayerEntity) {

                ClientPlayerEntity clientplayerentity = (ClientPlayerEntity)entity;
                f -= clientplayerentity.getWaterVision() * clientplayerentity.getWaterVision() * 0.03F;
                Biome biome = clientplayerentity.level.getBiome(clientplayerentity.blockPosition());
                if (biome.getBiomeCategory() == Biome.Category.SWAMP) {
                    f += 0.005F;
                }
            }

            RenderSystem.fogDensity(f);
            RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
        } else {
            float f2;
            float f3;
            int i2 = FogUtil.inFog((PlayerEntity)entity);
            if (fluidstate.is(FluidTags.LAVA)) {
                if(entity instanceof LivingEntity && EnchantmentUtil.hasMoltenAffinity((LivingEntity)entity)){
                    f2 = 0.0F;
                    f3 = 20.0F;
                } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasEffect(Effects.FIRE_RESISTANCE)) {
                    f2 = 0.0F;
                    f3 = 3.0F;
                } else {
                    f2 = 0.25F;
                    f3 = 1.0F;
                }
            } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasEffect(Effects.BLINDNESS)) {
                int i = ((LivingEntity) entity).getEffect(Effects.BLINDNESS).getDuration();
                float f1 = MathHelper.lerp(Math.min(1.0F, (float) i / 20.0F), farPlaneDistance, 5.0F);
                if (fogTypeIn == net.minecraft.client.renderer.FogRenderer.FogType.FOG_SKY) {
                    f2 = 0.0F;
                    f3 = f1 * 0.8F;
                } else {
                    f2 = f1 * 0.25F;
                    f3 = f1;
                }
            } else if (i2 > 0) {
                float f1 = MathHelper.lerp(1.0F, farPlaneDistance, 5.0F*(float)(9-i2));
                if (fogTypeIn == net.minecraft.client.renderer.FogRenderer.FogType.FOG_SKY) {
                    f2 = 0.0F;
                    f3 = f1 * 0.8F;
                } else {
                    f2 = f1 * 0.25F;
                    f3 = f1;
                }
            } else if (nearFog) {
                f2 = farPlaneDistance * 0.05F;
                f3 = Math.min(farPlaneDistance, 192.0F) * 0.5F;
            } else if (fogTypeIn == FogRenderer.FogType.FOG_SKY) {
                f2 = 0.0F;
                f3 = farPlaneDistance;
            } else {
                f2 = farPlaneDistance * 0.75F;
                f3 = farPlaneDistance;
            }

            RenderSystem.fogStart(f2);
            RenderSystem.fogEnd(f3);
            RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
            RenderSystem.setupNvFogDistance();
            net.minecraftforge.client.ForgeHooksClient.onFogRender(fogTypeIn, activeRenderInfoIn, partialTicks, f3);
        }

    }
}
