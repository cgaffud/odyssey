package com.bedmen.odyssey.world.gen.biome.weather;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import com.bedmen.odyssey.world.BiomeUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;

public class OdysseyOverworldEffects extends DimensionSpecialEffects.OverworldEffects {
    
    private final Minecraft minecraft;
    private final LevelRenderer levelRenderer;
    
    public OdysseyOverworldEffects(Minecraft minecraft){
        this.minecraft = minecraft;
        this.levelRenderer = minecraft.levelRenderer;
    }

    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation SNOW_LOCATION = new ResourceLocation("textures/environment/snow.png");
    private static final ResourceLocation BLIZZARD_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/environment/blizzard.png");
    private static final int BLIZZARD_SPEED_MULTIPLIER = 8;

    public boolean renderSky(ClientLevel clientLevel, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        Entity cameraEntity = camera.getEntity();
        float blizzardFogScale = cameraEntity instanceof OdysseyPlayer odysseyPlayer ? odysseyPlayer.getBlizzardFogScale(partialTick) : 1.0f;
        
        setupFog.run();
        if (!isFoggy) {
            FogType fogtype = camera.getFluidInCamera();
            if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA && !doesMobEffectBlockSky(camera)) {
                if (this.minecraft.level.effects().skyType() == SkyType.NORMAL) {
                    RenderSystem.disableTexture();
                    Vec3 skyColor = clientLevel.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
                    float skyColorRed = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogRed, skyColor.x);
                    float skyColorGreen = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogGreen, skyColor.y);
                    float skyColorBlue = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogBlue, skyColor.z);
                    FogRenderer.levelFogColor();
                    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                    RenderSystem.depthMask(false);
                    RenderSystem.setShaderColor(skyColorRed, skyColorGreen, skyColorBlue, 1.0F);
                    ShaderInstance shaderinstance = RenderSystem.getShader();
                    this.levelRenderer.skyBuffer.bind();
                    this.levelRenderer.skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
                    VertexBuffer.unbind();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    float[] afloat = clientLevel.effects().getSunriseColor(clientLevel.getTimeOfDay(partialTick), partialTick);
                    if (afloat != null) {
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        RenderSystem.disableTexture();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        poseStack.pushPose();
                        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                        float f3 = Mth.sin(clientLevel.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(f3));
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
                        float f4 = afloat[0];
                        float f5 = afloat[1];
                        float f6 = afloat[2];
                        Matrix4f matrix4f = poseStack.last().pose();
                        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                        bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3] * blizzardFogScale).endVertex();
                        int i = 16;

                        for(int j = 0; j <= 16; ++j) {
                            float f7 = (float)j * ((float)Math.PI * 2F) / 16.0F;
                            float f8 = Mth.sin(f7);
                            float f9 = Mth.cos(f7);
                            bufferbuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
                        }

                        BufferUploader.drawWithShader(bufferbuilder.end());
                        poseStack.popPose();
                    }

                    RenderSystem.enableTexture();
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    poseStack.pushPose();
                    float f11 = 1.0F - clientLevel.getRainLevel(partialTick);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11 * blizzardFogScale);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(clientLevel.getTimeOfDay(partialTick) * 360.0F));
                    Matrix4f matrix4f1 = poseStack.last().pose();
                    float f12 = 30.0F;
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, SUN_LOCATION);
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
                    bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
                    bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
                    bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
                    BufferUploader.drawWithShader(bufferbuilder.end());
                    f12 = 20.0F;
                    RenderSystem.setShaderTexture(0, MOON_LOCATION);
                    int k = clientLevel.getMoonPhase();
                    int l = k % 4;
                    int i1 = k / 4 % 2;
                    float f13 = (float)(l + 0) / 4.0F;
                    float f14 = (float)(i1 + 0) / 2.0F;
                    float f15 = (float)(l + 1) / 4.0F;
                    float f16 = (float)(i1 + 1) / 2.0F;
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferbuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(f15, f16).endVertex();
                    bufferbuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(f13, f16).endVertex();
                    bufferbuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(f13, f14).endVertex();
                    bufferbuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(f15, f14).endVertex();
                    BufferUploader.drawWithShader(bufferbuilder.end());
                    RenderSystem.disableTexture();
                    float f10 = clientLevel.getStarBrightness(partialTick) * f11;
                    if (f10 > 0.0F) {
                        RenderSystem.setShaderColor(f10, f10, f10, f10 * blizzardFogScale);
                        FogRenderer.setupNoFog();
                        this.levelRenderer.starBuffer.bind();
                        this.levelRenderer.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
                        VertexBuffer.unbind();
                        setupFog.run();
                    }

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                    poseStack.popPose();
                    RenderSystem.disableTexture();
                    RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                    double d0 = this.minecraft.player.getEyePosition(partialTick).y - clientLevel.getLevelData().getHorizonHeight(clientLevel);
                    if (d0 < 0.0D) {
                        poseStack.pushPose();
                        poseStack.translate(0.0D, 12.0D, 0.0D);
                        this.levelRenderer.darkBuffer.bind();
                        this.levelRenderer.darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
                        VertexBuffer.unbind();
                        poseStack.popPose();
                    }

                    if (clientLevel.effects().hasGround()) {
                        float adjustedRed = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogRed, skyColor.x * 0.2F + 0.04F);
                        float adjustedGreen = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogGreen, skyColor.y * 0.2F + 0.04F);
                        float adjustedBlue = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogBlue, skyColor.z * 0.6F + 0.1F);
                        RenderSystem.setShaderColor(adjustedRed, adjustedGreen, adjustedBlue, 1.0F);
                    } else {
                        RenderSystem.setShaderColor(skyColorRed, skyColorGreen, skyColorBlue, 1.0F);
                    }

                    RenderSystem.enableTexture();
                    RenderSystem.depthMask(true);
                }
            }
        }
        return true;
    }
    
    public boolean tickRain(ClientLevel level, int ticks, Camera camera)
    {
        LocalPlayer localPlayer = minecraft.player;
        if(localPlayer != null && BiomeUtil.isInBlizzard(localPlayer)){
            RandomSource randomsource = RandomSource.create((long)ticks * 312987231L);
            if(randomsource.nextInt(20) < minecraft.levelRenderer.rainSoundTime++){
                minecraft.levelRenderer.rainSoundTime = 0;
                minecraft.level.playLocalSound(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ(), SoundEventRegistry.ARCTIC_WIND.get(), SoundSource.WEATHER, 1.0F, 1.0F, false);
            }
            return true;
        }
        return false;
    }
    
    public boolean renderSnowAndRain(ClientLevel clientLevel, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ)
    {
        float rainLevel = this.minecraft.level.getRainLevel(partialTick);
        lightTexture.turnOnLightLayer();
        Level level = this.minecraft.level;
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
        float f1 = (float)ticks + partialTick;
        RenderSystem.setShader(GameRenderer::getParticleShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int j1 = k - l; j1 <= k + l; ++j1) {
            for(int k1 = i - l; k1 <= i + l; ++k1) {
                int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                double d0 = (double)this.levelRenderer.rainSizeX[l1] * 0.5D;
                double d1 = (double)this.levelRenderer.rainSizeZ[l1] * 0.5D;
                blockpos$mutableblockpos.set((double)k1, camY, (double)j1);
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
                        RandomSource randomsource = RandomSource.create((long)(k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
                        blockpos$mutableblockpos.set(k1, j2, j1);
                        if (rainLevel > 0.0F && biome.warmEnoughToRain(blockpos$mutableblockpos)) {
                            if (i1 != 0) {
                                if (i1 >= 0) {
                                    tesselator.end();
                                }

                                i1 = 0;
                                RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                            }

                            int i3 = ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                            float f2 = -((float)i3 + partialTick) / 32.0F * (3.0F + randomsource.nextFloat());
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
                        } else if (rainLevel > 0.0F && biome.getPrecipitation() == Biome.Precipitation.SNOW)  { // Snow
                            if (i1 != 1) {
                                if (i1 >= 0) {
                                    tesselator.end();
                                }

                                i1 = 1;
                                RenderSystem.setShaderTexture(0, SNOW_LOCATION);
                                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                            }

                            float f5 = -((float)(ticks & 511) + partialTick) / 512.0F;
                            float f6 = (float)(randomsource.nextDouble() + (double)f1 * 0.01D * (double)((float)randomsource.nextGaussian()));
                            float f7 = (float)(randomsource.nextDouble() + (double)(f1 * (float)randomsource.nextGaussian()) * 0.001D);
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

                            float f5 = -((float)(ticks & ((512 / BLIZZARD_SPEED_MULTIPLIER)-1)) + partialTick) / (512.0F / (float) BLIZZARD_SPEED_MULTIPLIER);
                            float f6 = (float)(randomsource.nextDouble() + (double)f1 * (0.01D * (double)BLIZZARD_SPEED_MULTIPLIER) * (double)((float)randomsource.nextGaussian()));
                            float f7 = (float)(randomsource.nextDouble() + (double)(f1 * (float)randomsource.nextGaussian()) * 0.001D);
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
        return true;
    }

    private static boolean doesMobEffectBlockSky(Camera p_234311_) {
        Entity entity = p_234311_.getEntity();
        if (!(entity instanceof LivingEntity livingentity)) {
            return false;
        } else {
            return livingentity.hasEffect(MobEffects.BLINDNESS) || livingentity.hasEffect(MobEffects.DARKNESS);
        }
    }
    
}
