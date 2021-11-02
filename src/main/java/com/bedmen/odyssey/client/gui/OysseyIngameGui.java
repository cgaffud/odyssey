package com.bedmen.odyssey.client.gui;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*;

public class OysseyIngameGui extends ForgeIngameGui {

    private static final ResourceLocation COCONUT_BLUR_LOCATION = new ResourceLocation(Odyssey.MOD_ID , "textures/misc/coconutblur.png");
    private Field eventParentField;

    {
        try {
            eventParentField = ForgeIngameGui.class.getDeclaredField("eventParent");
            eventParentField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public OysseyIngameGui(Minecraft mc) {
        super(mc);
    }

    //Helper macros
    private boolean pre(RenderGameOverlayEvent.ElementType type, MatrixStack mStack)
    {
        try {
            return MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Pre(mStack, (RenderGameOverlayEvent)eventParentField.get(this), type));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void post(RenderGameOverlayEvent.ElementType type, MatrixStack mStack)
    {
        try {
            MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(mStack, (RenderGameOverlayEvent)eventParentField.get(this), type));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    private void bind(ResourceLocation res)
    {
        minecraft.getTextureManager().bind(res);
    }

    private int fourShift(int healthRemaining){
        if(healthRemaining >= 4)
            return 0;
        return 9*(4-healthRemaining);
    }

    private int twoShift(int healthRemaining){
        if(healthRemaining != 1)
            return 0;
        return 18;
    }

    public void renderHealth(int width, int height, MatrixStack mStack)
    {
        bind(GUI_ICONS_LOCATION);
        if (pre(HEALTH, mStack)) return;
        minecraft.getProfiler().push("health");
        RenderSystem.enableBlend();

        PlayerEntity player = (PlayerEntity)this.minecraft.getCameraEntity();
        int health = MathHelper.ceil(player.getHealth());
        int healthRemaining = health;
        boolean highlight = healthBlinkTime > (long)tickCount && (healthBlinkTime - (long)tickCount) / 3L %2L == 1L;

        if (health < this.lastHealth && player.invulnerableTime > 0)
        {
            this.lastHealthTime = Util.getMillis();
            this.healthBlinkTime = (long)(this.tickCount + 20);
        }
        else if (health > this.lastHealth && player.invulnerableTime > 0)
        {
            this.lastHealthTime = Util.getMillis();
            this.healthBlinkTime = (long)(this.tickCount + 10);
        }

        if (Util.getMillis() - this.lastHealthTime > 1000L)
        {
            this.lastHealth = health;
            this.displayHealth = health;
            this.lastHealthTime = Util.getMillis();
        }

        this.lastHealth = health;
        int healthLast = this.displayHealth;


        ModifiableAttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = (float)attrMaxHealth.getValue();
        int absorb = MathHelper.ceil(player.getAbsorptionAmount());
        int absorptionHearts =  MathHelper.ceil((absorb) / 2.0F);
        int twoHearts;
        int fourHearts;
        if(healthMax <= 20.0f)
            twoHearts = MathHelper.ceil((healthMax) / 2.0F);
        else
            twoHearts = Math.max(20 - MathHelper.ceil((healthMax) / 2.0F), 0);
        if(healthMax <= 40.0f)
            fourHearts = MathHelper.ceil((healthMax-20.0f) / 2.0F);
        else
            fourHearts = MathHelper.ceil(healthMax / 4.0F);
        int totalHearts = fourHearts + twoHearts + absorptionHearts;
        int fourHearts1 = fourHearts;
        int twoHearts1 = twoHearts;

        int healthRows = ((totalHearts - 1) / 10) + 1;
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        this.random.setSeed((long)(tickCount * 312871));

        int left = width / 2 - 91;
        int top = height - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;

        int regen = -1;
        if (player.hasEffect(Effects.REGENERATION))
        {
            regen = tickCount % 25;
        }

        final int TOP =  113 + (minecraft.level.getLevelData().isHardcore() ? 27 : 0);
        int BACKGROUND = (highlight ? 72 : 63);
        int MARGIN = 81;
        if (player.hasEffect(Effects.WITHER)) MARGIN += 72;
        else if (player.hasEffect(Effects.POISON))      MARGIN += 36;

        int absorbRemaining = absorb;
        int shake[] = new int[2];

        // iterates backwards and displays hearts containers correctly
        for(int i = healthRows-1; i >= 0; i--) {
            for (int j = ((i*10+9) < totalHearts) ? (totalHearts-1) % 10 : 9; j >= 0; j--) {
                int x = left + j * 8;
                int y = top - i * rowHeight;
                int heartNum = i*10+j;

                if (health <= 4) {
                    if (heartNum < 2) {
                        shake[heartNum] = random.nextInt(2);
                        y += shake[heartNum];
                    }
                    else y += random.nextInt(2);
                }

                if (i == regen) y -= 2;

                BACKGROUND = ((fourHearts == i*10+j+1) && !highlight) ? BACKGROUND-9 : BACKGROUND;
                blit(mStack, x, y, BACKGROUND, TOP+18, 9, 9);
            }
        }

        for(int i = 0; i < healthRows; i++){
            for(int j = 0; j < 10 && i*10+j < totalHearts; j++){
                int x = left + j * 8;
                int y = top - i * rowHeight;
                int heartNum = i*10+j;

                if ((health <= 4) && (heartNum < 2)) y += shake[heartNum];
                if (i == regen) y -= 2;

//                int TBACKGROUND = ((!highlight) && (fourHearts > 0)) ? BACKGROUND-9 : BACKGROUND;
//                blit(mStack, x, y, TBACKGROUND, TOP+18, 9, 9);

                if (highlight)
                {
                    if(fourHearts1 > 0){
                        if(healthLast > 0)
                            blit(mStack, x, y, MARGIN+fourShift(healthLast), TOP+9, 9, 9);
                        fourHearts1--;
                        healthLast -= 4;
                    } else if(twoHearts1 > 0){
                        if(healthLast > 0)
                            blit(mStack, x, y, MARGIN+twoShift(healthLast), TOP+9, 9, 9);
                        twoHearts1--;
                        healthLast -= 2;
                    }
                }

                if(fourHearts > 0){
                    if(healthRemaining > 0)
                        blit(mStack, x, y, MARGIN+fourShift(healthRemaining), TOP+18, 9, 9);
                    fourHearts--;
                    healthRemaining -= 4;
                } else if(twoHearts > 0){
                    if(healthRemaining > 0)
                        blit(mStack, x, y, MARGIN+twoShift(healthRemaining), TOP, 9, 9);
                    twoHearts--;
                    healthRemaining -= 2;
                } else {
                    if(absorbRemaining > 0)
                        blit(mStack, x, y, MARGIN+twoShift(absorbRemaining)+108, TOP, 9, 9);
                    //absorptionHearts--;
                    absorbRemaining -= 2;
                }
            }
        }
        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
        post(HEALTH, mStack);
    }

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

        int air = player.getAirSupply();
        if (player.isEyeInFluid(FluidTags.LAVA)) {
            int full = MathHelper.ceil((double) (air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceil((double) air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; ++i) {
                blit(mStack, left - i * 8 - 9, top, (i < full ? 0 : 9), 140, 9, 9);
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

    private void renderHelmet(float partialTicks, MatrixStack mStack)
    {
        if (pre(HELMET, mStack)) return;

        ItemStack itemstack = this.minecraft.player.inventory.getArmor(3);

        if (this.minecraft.options.getCameraType().isFirstPerson() && !itemstack.isEmpty())
        {
            Item item = itemstack.getItem();
            if (item == Blocks.CARVED_PUMPKIN.asItem())
            {
                renderPumpkin();
            } else if (item == ItemRegistry.HOLLOW_COCONUT.get()) {
                renderCoconut();
            }
            else
            {
                item.renderHelmetOverlay(itemstack, minecraft.player, this.screenWidth, this.screenHeight, partialTicks);
            }
        }

        post(HELMET, mStack);
    }

    protected void renderCoconut() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableAlphaTest();
        this.minecraft.getTextureManager().bind(COCONUT_BLUR_LOCATION);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(0.0D, (double)this.screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex((double)this.screenWidth, (double)this.screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex((double)this.screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
