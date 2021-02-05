package com.bedmen.odyssey.client.gui;


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.NewEnchantmentContainer;
import com.bedmen.odyssey.network.CUpdateEnchantPacket;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.BeaconScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CUpdateBeaconPacket;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class NewEnchantmentScreen extends ContainerScreen<NewEnchantmentContainer> {
    public final static ResourceLocation GUI_TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/enchanting_table.png");;
    private ChangeEnchantPageButton buttonNextPage;
    private ChangeEnchantPageButton buttonPreviousPage;
    private EnchantButton[] enchantButtons = new EnchantButton[5];
    private int currPage = 1;
    private int numPages = this.container.numPages();
    public static final int ENCHANT_PER_PAGE = 5;
    public List<Enchantment> enchantmentList;
    public List<Integer> levelList;
    public List<Integer> costList;
    private int size;

    public NewEnchantmentScreen(NewEnchantmentContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 176;
        this.ySize = 166;
    }

    public void init() {
        super.init();
        this.addChangePageButtons();
        this.addEnchantButtons();
        this.enchantmentList = this.container.getEnchantmentList();
        this.levelList = this.container.getLevelList();
        this.costList = this.container.getCostList();
        this.size = this.enchantmentList.size();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if(this.buttonPreviousPage.visible) this.buttonPreviousPage.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        if(this.buttonNextPage.visible) this.buttonNextPage.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        for(int i = 0; i < ENCHANT_PER_PAGE; i++){
            if(this.numPages <= 0) this.enchantButtons[i].visible = false;
            else if (this.size <= index(i)) this.enchantButtons[i].visible = false;
            else if ((this.container.inventorySlots.get(1).getStack().getCount() < this.costList.get(index(i)) || playerInventory.player.experienceLevel < this.levelList.get(index(i))*10) && !playerInventory.player.isCreative()) this.enchantButtons[i].visible = false;
            else this.enchantButtons[i].visible = true;

            if(this.enchantButtons[i].visible) this.enchantButtons[i].renderButton(matrixStack, mouseX, mouseY, partialTicks);
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        for(int i = 0; i < ENCHANT_PER_PAGE; i++) {
            if (this.isPointInRegion(72, 18 + 10*i, 96, 10, mouseX, mouseY) && index(i) < this.size) {
                List<ITextComponent> list = Lists.newArrayList();
                int level = this.levelList.get(index(i));
                int experienceLevel = playerInventory.player.experienceLevel;
                int cost = this.costList.get(index(i));
                if(experienceLevel < level*10 && !playerInventory.player.isCreative()){
                    list.add((new TranslationTextComponent("container.enchant.level.requirement", level*10)).mergeStyle(TextFormatting.RED));
                }
                if(experienceLevel < cost && !playerInventory.player.isCreative()){
                    if(cost == 1){
                        list.add((new TranslationTextComponent("container.enchant.level.one")).mergeStyle(TextFormatting.RED));
                    }
                    else{
                        list.add((new TranslationTextComponent("container.enchant.level.many", cost)).mergeStyle(TextFormatting.RED));
                    }
                }
                if(this.container.inventorySlots.get(1).getStack().getCount() < cost && !playerInventory.player.isCreative()){
                    list.add((new TranslationTextComponent("container.enchant.lapis.many", cost)).mergeStyle(TextFormatting.RED));
                }
                this.func_243308_b(matrixStack, list, mouseX, mouseY);
                break;
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.GUI_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }

    protected void renderPageText(MatrixStack matrixStack) {
        ITextComponent pageText =  new TranslationTextComponent("book.pageIndicator", this.currPage, Math.max(this.numPages, 1));
        int pageTextX = 120 - (this.font.getStringPropertyWidth(pageText) / 2);
        this.font.func_243248_b(matrixStack, pageText, (float)pageTextX, (float)(this.playerInventoryTitleY), 0x404040);
    }

    protected void renderEnchantmentText(MatrixStack matrixStack) {
        for(int i = 0; i < ENCHANT_PER_PAGE && this.size > ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i; i++){
            Enchantment e = this.enchantmentList.get(ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i);
            String s = e.getName();
            IFormattableTextComponent enchantmentText =  new TranslationTextComponent(s);
            if(e.getMaxLevel() > 1) enchantmentText.appendString(" ").append(new TranslationTextComponent("enchantment.level." + this.levelList.get(index(i))));
            float scale = (1.0f/1.5f);
            float x1 = 124.0f - ((float)(this.font.getStringPropertyWidth(enchantmentText)) / 2.0f * scale);

            GlStateManager.pushMatrix();
            GlStateManager.scalef(scale, scale, 1.0f);
            GlStateManager.translatef(x1*(1.0f/scale), (20.5f+10*i)*(1.0f/scale), 0.0f);
            if(!this.enchantButtons[i].visible) this.font.func_243248_b(matrixStack, enchantmentText, 0, 0, 0x000000);
            else if(this.enchantButtons[i].isHovered()) this.font.func_243248_b(matrixStack, enchantmentText, 0, 0, 0xe0ca9f);
            else  this.font.func_243248_b(matrixStack, enchantmentText, 0, 0, 0x404040);
            GlStateManager.popMatrix();

            s = ""+this.levelList.get(index(i))*10;
            GlStateManager.pushMatrix();
            GlStateManager.scalef(scale, scale, 1.0f);
            GlStateManager.translatef((80.0f-(this.font.getStringWidth(s)/2.0f))*(1.0f/scale), (20.5f+10*i)*(1.0f/scale), 0.0f);
            if(!this.enchantButtons[i].visible) this.font.drawStringWithShadow(matrixStack, s, 0, 0, 0x408010);
            else this.font.drawStringWithShadow(matrixStack, s, 0, 0, 0x80FF20);
            GlStateManager.popMatrix();
        }
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
        if(this.numPages > 0) {
            renderPageText(matrixStack);
            renderEnchantmentText(matrixStack);
        }

        for(int i = 0; i < ENCHANT_PER_PAGE; i++){
            if(this.enchantButtons[i].isHovered() && this.enchantButtons[i].visible){
                int j = 17;
                int k = 47;
                this.minecraft.getTextureManager().bindTexture(this.GUI_TEXTURE);
                this.blit(matrixStack, j, k, 96, 166, 16, 16);
                this.blit(matrixStack, j+6, k+8, -11+11*this.costList.get(index(i)), 186, 11, 9);
            }
        }
    }
    protected void addChangePageButtons() {
        int i = this.guiLeft+116;
        int j = this.guiTop;
        this.buttonNextPage = this.addButton(new ChangeEnchantPageButton(i + 41, j+this.playerInventoryTitleY, true, (p_214159_1_) -> { this.nextPage(); }));
        this.buttonPreviousPage = this.addButton(new ChangeEnchantPageButton(i - 41, j+this.playerInventoryTitleY, false, (p_214158_1_) -> { this.previousPage(); }));
        this.updateButtons();
    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currPage < this.numPages && this.numPages > 0;
        this.buttonPreviousPage.visible = this.currPage > 1 && this.numPages > 0;
    }

    protected void previousPage() {
        if (this.currPage > 1) {
            --this.currPage;
        } else {
            this.currPage = 1;
        }

        this.updateButtons();
    }

    protected void nextPage() {
        if (this.currPage < this.numPages) {
            ++this.currPage;
        } else {
            this.currPage = this.numPages;
        }

        this.updateButtons();
    }

    public void tick(){
        this.numPages = this.container.numPages();
        this.enchantmentList = this.container.getEnchantmentList();
        this.levelList = this.container.getLevelList();
        this.costList = this.container.getCostList();
        this.size = this.enchantmentList.size();
        if(this.currPage > this.numPages) this.currPage = this.numPages;
        if(this.numPages == 0) this.currPage = 1;
        this.updateButtons();
    }

    protected void addEnchantButtons() {
        int i = this.guiLeft+72;
        int j = this.guiTop+18;
        for(int k = 0; k < ENCHANT_PER_PAGE; k++){
            int finalK = k;
            this.enchantButtons[k] = this.addButton(new EnchantButton(i, j+k*10, (p_214159_1_) -> { addEnchantment(finalK); }));
        }
        this.updateButtons();
    }

    private void addEnchantment(final int k) {
        int level = this.levelList.get(index(k));
        int cost = this.costList.get(index(k));
        Enchantment e = this.enchantmentList.get(index(k));
        PlayerEntity player = playerInventory.player;
        boolean b1 = this.container.inventorySlots.get(1).getStack().getCount() >= cost || player.isCreative();
        boolean b2 = player.experienceLevel >= level*10 || player.isCreative();
        if(b1 && b2){
            NewEnchantmentScreen.this.minecraft.getConnection().sendPacket(new CUpdateEnchantPacket(level, EnchantmentUtil.enchantmentToInt(e), cost));
            this.playerInventory.player.onEnchant(ItemStack.EMPTY, cost);
            this.minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f));
        }
    }

    private int index(int i){
        return ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i;
    }
}