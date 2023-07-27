package com.bedmen.odyssey.client.gui;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.Optional;

public class OdysseyIngameGui extends ForgeGui
{
    private static final ResourceLocation COCONUT_BLUR_LOCATION = new ResourceLocation(Odyssey.MOD_ID , "textures/misc/coconutblur.png");
    public static final ResourceLocation ODYSSEY_GUI_ICONS_LOCATION = new ResourceLocation(Odyssey.MOD_ID ,"textures/gui/icons.png");

    public OdysseyIngameGui(Minecraft mc) {
        super(mc);
    }

    public Minecraft getMinecraft(){
        return this.minecraft;
    }

    protected void renderHearts(PoseStack poseStack, Player player, int left, int top, int rowHeight, int regen, float healthMax, int health, int healthLast, int absorb, boolean highlight) {
        HeartType heartType = Gui.HeartType.forPlayer(player);
        Optional<OdysseyHeartType> optionalOdysseyHeartType = OdysseyHeartType.forPlayer(player);
        Either<HeartType, OdysseyHeartType> eitherHeartType = heartType == HeartType.NORMAL && optionalOdysseyHeartType.isPresent() ? Either.right(optionalOdysseyHeartType.get()) : Either.left(heartType);
        int yTexturePosVanilla = 9 * (player.level.getLevelData().isHardcore() ? 5 : 0);
        int yTexturePosOdyssey = 9 * (player.level.getLevelData().isHardcore() ? 1 : 0);
        int j = Mth.ceil((double)healthMax / 2.0D);
        int k = Mth.ceil((double)absorb / 2.0D);
        int l = j * 2;

        for(int i1 = j + k - 1; i1 >= 0; --i1) {
            int j1 = i1 / 10;
            int k1 = i1 % 10;
            int xScreenPos = left + k1 * 8;
            int yScreenPos = top - j1 * rowHeight;
            if (health + absorb <= 4) {
                yScreenPos += this.random.nextInt(2);
            }

            if (i1 < j && i1 == regen) {
                yScreenPos -= 2;
            }

            this.renderHeart(poseStack, Either.left(Gui.HeartType.CONTAINER), xScreenPos, yScreenPos, yTexturePosVanilla, yTexturePosOdyssey, highlight, false);
            int j2 = i1 * 2;
            boolean flag = i1 >= j;
            if (flag) {
                int k2 = j2 - l;
                if (k2 < absorb) {
                    boolean flag1 = k2 + 1 == absorb;
                    this.renderHeart(poseStack, heartType == Gui.HeartType.WITHERED ? eitherHeartType : Either.left(Gui.HeartType.ABSORBING), xScreenPos, yScreenPos, yTexturePosVanilla, yTexturePosOdyssey, false, flag1);
                }
            }

            if (highlight && j2 < healthLast) {
                boolean flag2 = j2 + 1 == healthLast;
                this.renderHeart(poseStack, eitherHeartType, xScreenPos, yScreenPos, yTexturePosVanilla, yTexturePosOdyssey, true, flag2);
            }

            if (j2 < health) {
                boolean flag3 = j2 + 1 == health;
                this.renderHeart(poseStack, eitherHeartType, xScreenPos, yScreenPos, yTexturePosVanilla, yTexturePosOdyssey, false, flag3);
            }
        }

    }

    public void renderHeart(PoseStack poseStack, Either<HeartType, OdysseyHeartType> heartType, int xScreenPos, int yScreenPos, int yTexturePosVanilla, int yTexturePosOdyssey, boolean blink, boolean half) {
        heartType.ifLeft(heartType1 -> {
            RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
            this.blit(poseStack, xScreenPos, yScreenPos, heartType1.getX(half, blink), yTexturePosVanilla, 9, 9);
        });
        heartType.ifRight(odysseyHeartType -> {
            RenderSystem.setShaderTexture(0, ODYSSEY_GUI_ICONS_LOCATION);
            this.blit(poseStack, xScreenPos, yScreenPos, odysseyHeartType.getX(half, blink), yTexturePosOdyssey, 9, 9);
        });
    }

    public enum OdysseyHeartType {
        HOT(0, false);

        private final int index;
        private final boolean canBlink;

        OdysseyHeartType(int index, boolean canBlink) {
            this.index = index;
            this.canBlink = canBlink;
        }

        public int getX(boolean half, boolean blink) {
            int i;
            int j = half ? 1 : 0;
            int k = this.canBlink && blink ? 2 : 0;
            i = j + k;
            return 45 + (this.index * 2 + i) * 9;
        }

        static Optional<OdysseyHeartType> forPlayer(Player player) {
            if(player instanceof OdysseyLivingEntity odysseyLivingEntity && odysseyLivingEntity.isOverheating()){
                return Optional.of(HOT);
            }
            return Optional.empty();
        }
    }

    public void renderArmor(PoseStack mStack, int width, int height)
    {
        RenderSystem.setShaderTexture(0, ODYSSEY_GUI_ICONS_LOCATION);
        minecraft.getProfiler().push("armor");

        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - leftHeight;

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
        leftHeight += 10;

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }

    public void renderShieldMeter(int width, int height, PoseStack poseStack, float partialTicks)
    {
        RenderSystem.setShaderTexture(0, ODYSSEY_GUI_ICONS_LOCATION);
        minecraft.getProfiler().push("shield meter");
        Player player = (Player)this.minecraft.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 - 116;
        int top = height - rightHeight;

        if (player instanceof OdysseyLivingEntity odysseyLivingEntity)
        {
            float shieldMeter = odysseyLivingEntity.getShieldMeter();
            float shieldMeterO = odysseyLivingEntity.getShieldMeterO();
            float lerpShieldMeter = Mth.lerp(partialTicks, shieldMeterO, shieldMeter);
            if(lerpShieldMeter > 1.0f){
                blit(poseStack, left, top, 14, 36, 14, 16);
                int goldenShieldHeight = Mth.ceil((lerpShieldMeter - 1.0f) * (1.0f / (WeaponUtil.SHIELD_METER_MAX - 1.0f)) * 16f);
                int inverseGoldenShieldHeight = 16 - goldenShieldHeight;
                blit(poseStack, left, top + inverseGoldenShieldHeight, 28, 36+inverseGoldenShieldHeight, 14, goldenShieldHeight);
            } else {
                blit(poseStack, left, top, 0, 36, 14, 16);
                int shieldHeight = Mth.ceil(lerpShieldMeter * 16f);
                int inverseShieldHeight = 16 - shieldHeight;
                blit(poseStack, left, top + inverseShieldHeight, 14, 36+inverseShieldHeight, 14, shieldHeight);
            }
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }

    public void renderFlight(int width, int height, PoseStack poseStack)
    {
        RenderSystem.setShaderTexture(0, ODYSSEY_GUI_ICONS_LOCATION);
        minecraft.getProfiler().push("flight");
        Player player = (Player)this.minecraft.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - rightHeight;

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
                    blit(poseStack, left - i * 8 - 9, top, x, 27, 9, 9);
                }
                rightHeight += 10;
            }
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }

    public void renderLightBar(int width, int height, PoseStack poseStack) {
        rightHeight -= 4;
        RenderSystem.setShaderTexture(0, ODYSSEY_GUI_ICONS_LOCATION);
        minecraft.getProfiler().push("light_bar");
        Player player = (Player) this.minecraft.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91 - 51;
        int top = height - rightHeight;

        ItemStack itemStack = player.getMainHandItem();

        if (AspectUtil.getItemStackAspectStrength(itemStack, Aspects.SOLAR_STRENGTH) > 0 || AspectUtil.getItemStackAspectStrength(itemStack, Aspects.LUNAR_STRENGTH) > 0) {
            int barAdjustment = AspectUtil.getItemStackAspectStrength(itemStack, Aspects.LUNAR_STRENGTH) > 0 ? 10 : 0;
            int charge = itemStack.getOrCreateTag().getInt(AspectUtil.STORED_BOOST_TAG);
            // Fill entire bar
            charge = (charge == 50) ? 51 : charge;
            blit(poseStack, left, top, 0, 52 + barAdjustment, 51, 5);
            if (charge > 0) blit(poseStack, left, top, 0, 57 + barAdjustment, charge, 5);
            rightHeight += 9;
        } else if (AspectUtil.getItemStackAspectStrength(itemStack, Aspects.ABSORBENT_GROWTH) > 0) {
            float progress = itemStack.getOrCreateTag().getFloat(AspectUtil.DAMAGE_GROWTH_TAG);
            // These values are temp as we need item texture to determine bar color
            blit(poseStack, left, top, 0, 62, 51, 5);
            if (progress > 0) blit(poseStack, left, top, 0, 67, (int) (progress*51), 5);
            rightHeight += 9;
        } else {
            rightHeight += 4;
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
                IClientItemExtensions.of(item).renderHelmetOverlay(itemstack, minecraft.player, this.screenWidth, this.screenHeight, partialTicks);
            }
        }
    }

    public void renderFrostbite()
    {
        if(this.minecraft.player instanceof OdysseyLivingEntity odysseyLivingEntity){
            if(odysseyLivingEntity.isCold()){
                this.renderTextureOverlay(POWDER_SNOW_OUTLINE_LOCATION, this.minecraft.player.getPercentFrozen());
            }
        }
    }

    public void renderRoastingOverlay()
    {
        if(this.minecraft.player instanceof OdysseyLivingEntity odysseyLivingEntity){
            if(odysseyLivingEntity.isHot()){
                float temperature = odysseyLivingEntity.getPercentOverheated();
                int i = this.minecraft.getWindow().getGuiScaledWidth();
                int j = this.minecraft.getWindow().getGuiScaledHeight();
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
    }
}