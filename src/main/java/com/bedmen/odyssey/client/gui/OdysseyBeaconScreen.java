package com.bedmen.odyssey.client.gui;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.OdysseyBeaconContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class OdysseyBeaconScreen extends ContainerScreen<OdysseyBeaconContainer> {
    private static final ResourceLocation BEACON_GUI_TEXTURES = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/beacon.png");
    private static int redFlash1 = 0;
    private static int redFlash2 = 0;
    private static final int[] nonAmp = {12,13,14,15,16,18,24,28,33};
    private static Map<Integer, ResourceLocation> EFFECT_TEXTURES = new java.util.HashMap<>();

    public OdysseyBeaconScreen(final OdysseyBeaconContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(this.BEACON_GUI_TEXTURES);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int e = this.menu.getEffect();

        if(e >= 0){
            ResourceLocation effect_texture = EFFECT_TEXTURES.get(e);
            if(effect_texture == null){
                String s = Effect.byId(e).getDescriptionId();
                s = s.substring(s.indexOf('.')+1);
                int i1 = s.indexOf('.');
                String s1 = s.substring(0,i1);
                String s2 = s.substring(i1+1);
                if(s1.equals("minecraft")) effect_texture = new ResourceLocation("textures/mob_effect/"+s2+".png");
                else effect_texture = new ResourceLocation(s1,"textures/mob_effect/"+s2+".png");
                EFFECT_TEXTURES.put(e,effect_texture);
            }

            this.minecraft.getTextureManager().bind(effect_texture);
            blit(matrixStack, i+115, j+16, 0, (float)0, (float)0, 18, 18, 18, 18);


            this.minecraft.getTextureManager().bind(this.BEACON_GUI_TEXTURES);
            int amp = this.menu.getAmplifier();
            if(this.menu.getBlocks() < 9 && amp > 0) amp = 0;

            boolean b1 = true;
            for(int i1 : nonAmp) if(e == i1) {
                b1 = false;
                break;
            }

            if((amp == 0 || amp == 1) && b1) {
                this.blit(matrixStack, i+125, j+26, 176, 16+10*amp, 10, 10);
            }
        }

        if(this.redFlash1 > 0) this.redFlash1--;
        if(this.redFlash2 > 0) this.redFlash2--;

        if(this.menu.getCompletionError1()){
            this.menu.setCompletionError1(false);
            this.redFlash1 = 16;
        }
        if(this.menu.getCompletionError2()){
            this.menu.setCompletionError2(false);
            this.redFlash2 = 16;
        }

        for(int i1 = 0; i1 < 9; i1++){
            int x1 = i1 % 3;
            int y1 = i1 / 3;
            if(this.menu.getCompletion(i1) == 1) this.blit(matrixStack, i+46+x1*16, j+19+y1*16, 176, 0, 16, 16);
            else if(this.redFlash2 > 0){
                flashRed(matrixStack,i+46+x1*16,j+19+y1*16, this.redFlash2);
            }
            if(i1 == 4 && this.redFlash1 > 0 && this.menu.getCompletion(4) == 0){
                flashRed(matrixStack,i+46+x1*16,j+19+y1*16, this.redFlash1);
            }
        }
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public void flashRed(MatrixStack matrixStack, int x, int y, int intensity){
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        int redColor = ((intensity/2) << 28)+0x00FF0000;
        this.fillGradient(matrixStack, x, y, x+16, y+16, redColor, redColor);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }
}
