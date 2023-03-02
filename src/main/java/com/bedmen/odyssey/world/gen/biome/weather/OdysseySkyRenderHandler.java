package com.bedmen.odyssey.world.gen.biome.weather;

import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.event_listeners.ForgeBusClientEvents;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ISkyRenderHandler;

public class OdysseySkyRenderHandler implements ISkyRenderHandler {

    private static final ResourceLocation MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
    
    public void render(int ticks, float partialTick, PoseStack poseStack, ClientLevel level, Minecraft minecraft) {
        LevelRenderer levelRenderer = minecraft.levelRenderer;
        Matrix4f starmatrix4f = RenderSystem.getProjectionMatrix();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPosition = camera.getPosition();
        Entity cameraEntity = camera.getEntity();
        double cameraX = cameraPosition.x();
        double cameraY = cameraPosition.y();
        float blizzardFogScale = cameraEntity instanceof OdysseyPlayer odysseyPlayer ? odysseyPlayer.getBlizzardFogScale(partialTick) : 1.0f;
        boolean isFoggy = minecraft.level.effects().isFoggyAt(Mth.floor(cameraX), Mth.floor(cameraY)) || minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        if (!isFoggy) {
            FogType fogtype = camera.getFluidInCamera();
            if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA) {
                if (cameraEntity instanceof LivingEntity cameraLivingEntity) {
                    if (cameraLivingEntity.hasEffect(MobEffects.BLINDNESS)) {
                        return;
                    }
                }
                if (minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.END) {
                    levelRenderer.renderEndSky(poseStack);
                } else if (minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.NORMAL) {
                    RenderSystem.disableTexture();
                    Vec3 skyColor = level.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);

                    float skyColorRed = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogRed, skyColor.x);
                    float skyColorGreen = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogGreen, skyColor.y);
                    float skyColorBlue = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogBlue, skyColor.z);
                    FogRenderer.levelFogColor();
                    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                    RenderSystem.depthMask(false);
                    RenderSystem.setShaderColor(skyColorRed, skyColorGreen, skyColorBlue, 1.0f);
                    ShaderInstance shaderinstance = RenderSystem.getShader();
                    levelRenderer.skyBuffer.drawWithShader(poseStack.last().pose(), starmatrix4f, shaderinstance);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
                    if (afloat != null) {
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        RenderSystem.disableTexture();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
                        poseStack.pushPose();
                        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                        float f2 = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(f2));
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
                        float f3 = afloat[0];
                        float f4 = afloat[1];
                        float f5 = afloat[2];
                        Matrix4f matrix4f = poseStack.last().pose();
                        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                        bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f3, f4, f5, afloat[3] * blizzardFogScale).endVertex();
                        int i = 16;

                        for(int j = 0; j <= 16; ++j) {
                            float f6 = (float)j * ((float)Math.PI * 2F) / 16.0F;
                            float f7 = Mth.sin(f6);
                            float f8 = Mth.cos(f6);
                            bufferbuilder.vertex(matrix4f, f7 * 120.0F, f8 * 120.0F, -f8 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
                        }

                        bufferbuilder.end();
                        BufferUploader.end(bufferbuilder);
                        poseStack.popPose();
                    }

                    RenderSystem.enableTexture();
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    poseStack.pushPose();
                    float f11 = 1.0F - level.getRainLevel(partialTick);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11 * blizzardFogScale);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
                    Matrix4f matrix4f1 = poseStack.last().pose();
                    float f12 = 30.0F;
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, SUN_LOCATION);
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
                    bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
                    bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
                    bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
                    bufferbuilder.end();
                    BufferUploader.end(bufferbuilder);
                    f12 = 20.0F;
                    RenderSystem.setShaderTexture(0, MOON_LOCATION);
                    int k = level.getMoonPhase();
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
                    bufferbuilder.end();
                    BufferUploader.end(bufferbuilder);
                    RenderSystem.disableTexture();
                    float f9 = level.getStarBrightness(partialTick) * f11;
                    if (f9 > 0.0F) {
                        RenderSystem.setShaderColor(f9, f9, f9, f9 * blizzardFogScale);
                        FogRenderer.setupNoFog();
                        levelRenderer.starBuffer.drawWithShader(poseStack.last().pose(), starmatrix4f, GameRenderer.getPositionShader());
                        FogRenderer.setupFog(camera, FogRenderer.FogMode.FOG_SKY, minecraft.gameRenderer.getRenderDistance(), isFoggy, partialTick);
                    }

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
                    RenderSystem.disableBlend();
                    poseStack.popPose();
                    RenderSystem.disableTexture();
                    RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0f);
                    double d0 = minecraft.player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
                    if (d0 < 0.0D) {
                        poseStack.pushPose();
                        poseStack.translate(0.0D, 12.0D, 0.0D);
                        levelRenderer.darkBuffer.drawWithShader(poseStack.last().pose(), starmatrix4f, shaderinstance);
                        poseStack.popPose();
                    }

                    if (level.effects().hasGround()) {
                        float adjustedRed = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogRed, skyColor.x * 0.2F + 0.04F);
                        float adjustedGreen = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogGreen, skyColor.y * 0.2F + 0.04F);
                        float adjustedBlue = (float) Mth.lerp(blizzardFogScale, FogRenderer.fogBlue, skyColor.z * 0.6F + 0.1F);
                        RenderSystem.setShaderColor(adjustedRed, adjustedGreen, adjustedBlue, 1.0f);
                    } else {
                        RenderSystem.setShaderColor(skyColorRed, skyColorGreen, skyColorBlue, 1.0f);
                    }

                    RenderSystem.enableTexture();
                    RenderSystem.depthMask(true);
                }
            }
        }
    }
}
