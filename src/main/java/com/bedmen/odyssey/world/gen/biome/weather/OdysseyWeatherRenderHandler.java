package com.bedmen.odyssey.world.gen.biome.weather;

import com.bedmen.odyssey.Odyssey;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.IWeatherRenderHandler;

import java.util.Random;

public class OdysseyWeatherRenderHandler implements IWeatherRenderHandler {

    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation SNOW_LOCATION = new ResourceLocation("textures/environment/snow.png");
    private static final ResourceLocation BLIZZARD_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/environment/blizzard.png");
    private static final int BLIZZARD_SPEED = 8;

    public void render(int ticks, float partialTick, ClientLevel level, Minecraft minecraft, LightTexture lightTexture, double camX, double camY, double camZ) {
        float rainLevel = level.getRainLevel(partialTick);
        lightTexture.turnOnLightLayer();
        int i = Mth.floor(camX);
        int j = Mth.floor(camY);
        int k = Mth.floor(camZ);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int l = 5;
        if (Minecraft.useFancyGraphics()) {
            l = 10;
        }

        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        int i1 = -1;
        float floatTicks = (float)ticks + partialTick;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int j1 = k - l; j1 <= k + l; ++j1) {
            for(int k1 = i - l; k1 <= i + l; ++k1) {
                int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                double d0 = (double)minecraft.levelRenderer.rainSizeX[l1] * 0.5D;
                double d1 = (double)minecraft.levelRenderer.rainSizeZ[l1] * 0.5D;
                blockpos$mutableblockpos.set(k1, camY, j1);
                Biome biome = level.getBiome(blockpos$mutableblockpos).value();
                if (biome.getPrecipitation() != Biome.Precipitation.NONE) {
                    int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
                    int j2 = j - l;
                    int k2 = j + l;
                    if (j2 < i2) {
                        j2 = i2;
                    }

                    if (k2 < i2) {
                        k2 = i2;
                    }

                    int l2 = i2;
                    if (i2 < j) {
                        l2 = j;
                    }

                    if (j2 != k2) {
                        Random random = new Random(k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761);
                        blockpos$mutableblockpos.set(k1, j2, j1);
                        if (biome.warmEnoughToRain(blockpos$mutableblockpos) && rainLevel > 0.0F) { // Rain
                            if (i1 != 0) {
                                if (i1 >= 0) {
                                    tesselator.end();
                                }

                                i1 = 0;
                                RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                            }

                            int i3 = ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                            float f2 = -((float)i3 + partialTick) / 32.0F * (3.0F + random.nextFloat());
                            double d2 = (double)k1 + 0.5D - camX;
                            double d4 = (double)j1 + 0.5D - camZ;
                            float f3 = (float)Math.sqrt(d2 * d2 + d4 * d4) / (float)l;
                            float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * rainLevel;
                            blockpos$mutableblockpos.set(k1, l2, j1);
                            int j3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
                            bufferbuilder.vertex((double)k1 - camX - d0 + 0.5D, (double)k2 - camY, (double)j1 - camZ - d1 + 0.5D).uv(0.0F, (float)j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                            bufferbuilder.vertex((double)k1 - camX + d0 + 0.5D, (double)k2 - camY, (double)j1 - camZ + d1 + 0.5D).uv(1.0F, (float)j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                            bufferbuilder.vertex((double)k1 - camX + d0 + 0.5D, (double)j2 - camY, (double)j1 - camZ + d1 + 0.5D).uv(1.0F, (float)k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                            bufferbuilder.vertex((double)k1 - camX - d0 + 0.5D, (double)j2 - camY, (double)j1 - camZ - d1 + 0.5D).uv(0.0F, (float)k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                        } else if (rainLevel > 0.0F && biome.getPrecipitation() == Biome.Precipitation.SNOW) { // Snow
                            if (i1 != 1) {
                                if (i1 >= 0) {
                                    tesselator.end();
                                }

                                i1 = 1;
                                RenderSystem.setShaderTexture(0, SNOW_LOCATION);
                                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                            }

                            float f5 = -((float)(ticks & 511) + partialTick) / 512.0F;
                            float f6 = (float)(random.nextDouble() + (double)floatTicks * 0.01D * (double)((float)random.nextGaussian()));
                            float f7 = (float)(random.nextDouble() + (double)(floatTicks * (float)random.nextGaussian()) * 0.001D);
                            double d3 = (double)k1 + 0.5D - camX;
                            double d5 = (double)j1 + 0.5D - camZ;
                            float f8 = (float)Math.sqrt(d3 * d3 + d5 * d5) / (float)l;
                            float f9 = ((1.0F - f8 * f8) * 0.3F + 0.5F) * rainLevel;
                            blockpos$mutableblockpos.set(k1, l2, j1);
                            int k3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
                            int l3 = k3 >> 16 & '\uffff';
                            int i4 = k3 & '\uffff';
                            int j4 = (l3 * 3 + 240) / 4;
                            int k4 = (i4 * 3 + 240) / 4;
                            bufferbuilder.vertex((double)k1 - camX - d0 + 0.5D, (double)k2 - camY, (double)j1 - camZ - d1 + 0.5D).uv(0.0F + f6, (float)j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            bufferbuilder.vertex((double)k1 - camX + d0 + 0.5D, (double)k2 - camY, (double)j1 - camZ + d1 + 0.5D).uv(1.0F + f6, (float)j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            bufferbuilder.vertex((double)k1 - camX + d0 + 0.5D, (double)j2 - camY, (double)j1 - camZ + d1 + 0.5D).uv(1.0F + f6, (float)k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            bufferbuilder.vertex((double)k1 - camX - d0 + 0.5D, (double)j2 - camY, (double)j1 - camZ - d1 + 0.5D).uv(0.0F + f6, (float)k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                        } else if (biome.getPrecipitation() == OdysseyPrecipitation.BLIZZARD) { // Blizzard
                            if (i1 != 1) {
                                if (i1 >= 0) {
                                    tesselator.end();
                                }

                                i1 = 1;
                                RenderSystem.setShaderTexture(0, BLIZZARD_LOCATION);
                                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                            }

                            float f5 = -((float)(ticks & ((512 / BLIZZARD_SPEED)-1)) + partialTick) / (512.0F / (float)BLIZZARD_SPEED);
                            float f6 = (float)(random.nextDouble() + (double)floatTicks * (0.01D * (double)BLIZZARD_SPEED) * (double)((float)random.nextGaussian()));
                            float f7 = (float)(random.nextDouble() + (double)(floatTicks * (float)random.nextGaussian()) * 0.001D);
                            double d3 = (double)k1 + 0.5D - camX;
                            double d5 = (double)j1 + 0.5D - camZ;
                            float f8 = (float)Math.sqrt(d3 * d3 + d5 * d5) / (float)l;
                            float f9 = ((1.0F - f8 * f8) * 0.3F + 0.5F);
                            blockpos$mutableblockpos.set(k1, l2, j1);
                            int k3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
                            int l3 = k3 >> 16 & '\uffff';
                            int i4 = k3 & '\uffff';
                            int j4 = (l3 * 3 + 240) / 4;
                            int k4 = (i4 * 3 + 240) / 4;
                            bufferbuilder.vertex((double)k1 - camX - d0 + 0.5D, (double)k2 - camY, (double)j1 - camZ - d1 + 0.5D).uv(0.0F + f6, (float)j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            bufferbuilder.vertex((double)k1 - camX + d0 + 0.5D, (double)k2 - camY, (double)j1 - camZ + d1 + 0.5D).uv(1.0F + f6, (float)j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            bufferbuilder.vertex((double)k1 - camX + d0 + 0.5D, (double)j2 - camY, (double)j1 - camZ + d1 + 0.5D).uv(1.0F + f6, (float)k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            bufferbuilder.vertex((double)k1 - camX - d0 + 0.5D, (double)j2 - camY, (double)j1 - camZ - d1 + 0.5D).uv(0.0F + f6, (float)k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                        }
                    }
                }
            }
        }

        if (i1 >= 0) {
            tesselator.end();
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
    }
}
