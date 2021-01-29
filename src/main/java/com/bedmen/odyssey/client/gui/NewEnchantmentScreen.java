package com.bedmen.odyssey.client.gui;


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.NewEnchantmentContainer;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
    public List<String> idList;
    public List<Integer> levelList;

    public NewEnchantmentScreen(NewEnchantmentContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 176;
        this.ySize = 166;
    }

    public void init() {
        super.init();
        this.addChangePageButtons();
        this.addEnchantButtons();
        this.idList = this.container.getIdList();
        this.levelList = this.container.getLevelList();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if(this.buttonPreviousPage.visible) this.buttonPreviousPage.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        if(this.buttonNextPage.visible) this.buttonNextPage.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        for(int i = 0; i < ENCHANT_PER_PAGE; i++){
            if(this.numPages <= 0) this.enchantButtons[i].visible = false;
            else if (this.container.getIdList().size() <= ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i) this.enchantButtons[i].visible = false;
            else this.enchantButtons[i].visible = true;

            if(this.enchantButtons[i].visible) this.enchantButtons[i].renderButton(matrixStack, mouseX, mouseY, partialTicks);
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

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
        int pageTextX = 124 - (this.font.getStringPropertyWidth(pageText) / 2);
        this.font.func_243248_b(matrixStack, pageText, (float)pageTextX, (float)(this.playerInventoryTitleY), 4210752);
    }

    protected void renderEnchantmentText(MatrixStack matrixStack) {
        for(int i = 0; i < ENCHANT_PER_PAGE && idList.size() > ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i; i++){
            String s = this.idList.get(ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i);
            s = s.replace(':','.');
            ITextComponent enchantmentText =  new TranslationTextComponent("enchantment."+s);
            ITextComponent levelText =  new TranslationTextComponent("enchantment.level."+this.levelList.get(ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i));
            ITextComponent spaceText =  new TranslationTextComponent(" ");
            float w = (float)(this.font.getStringPropertyWidth(enchantmentText) + this.font.getStringPropertyWidth(spaceText) + this.font.getStringPropertyWidth(levelText));
            float scale = (1.0f/1.5f);
            float x1 = 124.0f - (w / 2.0f * scale);
            float x2 = x1 + (this.font.getStringPropertyWidth(enchantmentText) + this.font.getStringPropertyWidth(spaceText))*scale;
            GlStateManager.pushMatrix();
            GlStateManager.scalef(scale, scale, 1.0f);
            GlStateManager.translatef(x1*(1.0f/scale), (20.5f+10*i)*(1.0f/scale), 0.0f);
            if(this.enchantButtons[i].isHovered()) this.font.func_243248_b(matrixStack, enchantmentText, 0, 0, 0xe0ca9f);
            else  this.font.func_243248_b(matrixStack, enchantmentText, 0, 0, 0x404040);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(scale, scale, 1.0f);
            GlStateManager.translatef(x2*(1.0f/scale), (20.5f+10*i)*(1.0f/scale), 0.0f);
            if(this.enchantButtons[i].isHovered()) this.font.func_243248_b(matrixStack, levelText, 0, 0, 0xe0ca9f);
            else this.font.func_243248_b(matrixStack, levelText, 0, 0, 0x404040);
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

        ItemRenderer itemRenderer = this.minecraft.getItemRenderer();
        for(int i = 0; i < ENCHANT_PER_PAGE; i++){
            if(this.enchantButtons[i].isHovered() && this.enchantButtons[i].visible){
                int index = ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i;
                int j = 35;
                int k = 27;
                if(!this.container.inventorySlots.get(1).getHasStack()){
                    ItemStack itemStack = EnchantmentUtil.stringToItem(this.idList.get(index));
                    AbstractGui.fill(matrixStack, j, k, j + 16, k + 16, 0x30FF0000);
                    itemRenderer.renderItemAndEffectIntoGuiWithoutEntity(itemStack, j, k);
                }
                k = 47;
                ItemStack itemStack1 = this.container.inventorySlots.get(2).getStack();
                if(itemStack1.getCount() < this.levelList.get(index)){
                    AbstractGui.fill(matrixStack, j, k, j + 16, k + 16, 0x30FF0000);
                    this.minecraft.getTextureManager().bindTexture(this.GUI_TEXTURE);
                    this.blit(matrixStack, j, k, 70+18*this.levelList.get(index), 166, 18, 18);
                }
                j = 15;
                if(playerInventory.player.experienceLevel < this.levelList.get(index)){
                    AbstractGui.fill(matrixStack, j, k, j + 16, k + 16, 0x30FF0000);
                }
                this.minecraft.getTextureManager().bindTexture(this.GUI_TEXTURE);
                this.blit(matrixStack, j, k, -16+16*this.levelList.get(index), 186, 16, 16);
            }
        }
    }
    protected void addChangePageButtons() {
        int i = this.guiLeft+120;
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
        this.idList = this.container.getIdList();
        this.levelList = this.container.getLevelList();
        if(this.currPage > this.numPages) this.currPage = this.numPages;
        if(this.numPages == 0) this.currPage = 1;
        this.updateButtons();
    }

    protected void addEnchantButtons() {
        int i = this.guiLeft+80;
        int j = this.guiTop+18;
        for(int k = 0; k < ENCHANT_PER_PAGE; k++){
            int finalK = k;
            this.enchantButtons[k] = this.addButton(new EnchantButton(i, j+k*10, (p_214159_1_) -> { addEnchantment(finalK); }));
        }
        this.updateButtons();
    }

    private void addEnchantment(final int k) {
        int index = ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+k;
        int level = this.levelList.get(index);
        String id = this.idList.get(index);
        boolean b1 = this.container.inventorySlots.get(1).getStack().getItem() == EnchantmentUtil.stringToItem(id).getItem();
        boolean b2 = this.container.inventorySlots.get(2).getStack().getCount() >= level;
        boolean b3 = playerInventory.player.experienceLevel >= level;
        if(b1 && b2 && b3){
            this.container.inventorySlots.get(1).decrStackSize(1);
            this.container.inventorySlots.get(2).decrStackSize(level);
            playerInventory.player.addExperienceLevel(-1*level);
            ItemStack itemStack = this.container.inventorySlots.get(0).getStack();
            itemStack.addEnchantment(EnchantmentUtil.stringToEnchantment(id), level);
        }
    }
}