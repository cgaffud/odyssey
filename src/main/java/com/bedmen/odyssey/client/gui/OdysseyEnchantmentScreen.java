package com.bedmen.odyssey.client.gui;


import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.container.OdysseyEnchantmentContainer;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.UpdateEnchantPacket;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class OdysseyEnchantmentScreen extends ContainerScreen<OdysseyEnchantmentContainer> {
    public final static ResourceLocation GUI_TEXTURE = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/container/enchanting_table.png");;
    private ChangeEnchantPageButton buttonNextPage;
    private ChangeEnchantPageButton buttonPreviousPage;
    private EnchantButton[] enchantButtons = new EnchantButton[5];
    private int currPage = 1;
    private int numPages = this.menu.numPages();
    public static final int ENCHANT_PER_PAGE = 5;
    public List<Enchantment> enchantmentList;
    public List<Integer> levelList;
    public List<Integer> costList;
    private int size;
    private Map<Enchantment, Integer> itemEnchantments = new HashMap<>();
    private int discount = 0;

    public OdysseyEnchantmentScreen(OdysseyEnchantmentContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void init() {
        super.init();
        this.addChangePageButtons();
        this.addEnchantButtons();
        this.enchantmentList = this.menu.getEnchantmentList();
        this.levelList = this.menu.getLevelList();
        this.costList = this.menu.getCostList();
        this.size = this.enchantmentList.size();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if(this.buttonPreviousPage.visible) this.buttonPreviousPage.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        if(this.buttonNextPage.visible) this.buttonNextPage.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        for(int i = 0; i < ENCHANT_PER_PAGE; i++){
            int index = index(i);
            this.enchantButtons[i].visible = true;
            this.enchantButtons[i].notEnoughLapis = false;
            this.enchantButtons[i].notEnoughLevelRequirement = false;
            this.enchantButtons[i].notEnoughLevelCost = false;
            this.enchantButtons[i].exclusiveFlag.clear();
            if(this.numPages <= 0 ||  this.size <= index){
                this.enchantButtons[i].visible = false;
            } else {
                int cost = this.costList.get(index);
                if(!this.inventory.player.isCreative()){
                    if((this.menu.slots.get(1).getItem().getCount() < cost)){
                        this.enchantButtons[i].visible = false;
                        this.enchantButtons[i].notEnoughLapis = true;
                    }
                    if(this.inventory.player.experienceLevel < cost){
                        this.enchantButtons[i].visible = false;
                        this.enchantButtons[i].notEnoughLevelCost = true;
                    }
                    if(this.inventory.player.experienceLevel < levelRequirement(this.levelList.get(index))){
                        this.enchantButtons[i].visible = false;
                        this.enchantButtons[i].notEnoughLevelRequirement = true;
                    }
                }
                EnchantmentUtil.getExclusiveFlag(this.enchantmentList.get(index), this.levelList.get(index), this.itemEnchantments, this.enchantButtons[i].exclusiveFlag);
                if(this.enchantButtons[i].exclusiveFlag.flag > 0){
                    this.enchantButtons[i].visible = false;
                }
            }
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);

        for(int i = 0; i < ENCHANT_PER_PAGE; i++) {
            int index = index(i);
            if (this.isHovering(72, 18 + 10*i, 96, 10, mouseX, mouseY) && index < this.size) {
                List<ITextComponent> list = Lists.newArrayList();
                int level = this.levelList.get(index);
                int cost = this.costList.get(index);
                if(this.enchantButtons[i].exclusiveFlag.flag  == 1){
                    list.add((new TranslationTextComponent("container.enchant.upgrade_already_applied")).withStyle(TextFormatting.RED));
                } else if(this.enchantButtons[i].exclusiveFlag.flag  == 2){
                    list.add((new TranslationTextComponent("container.enchant.already_applied")).withStyle(TextFormatting.RED));
                } else if(this.enchantButtons[i].exclusiveFlag.flag == 3){
                    TranslationTextComponent translationTextComponent = new TranslationTextComponent(this.enchantButtons[i].exclusiveFlag.enchantment.getDescriptionId());
                    list.add((new TranslationTextComponent("container.enchant.mutual_exclusion")).append(translationTextComponent).withStyle(TextFormatting.RED));
                } else {
                    if(this.enchantButtons[i].notEnoughLevelRequirement){
                        list.add((new TranslationTextComponent("container.enchant.level.requirement", levelRequirement(level))).withStyle(TextFormatting.RED));
                    }
                    if(this.enchantButtons[i].notEnoughLevelCost){
                        if(cost == 1){
                            list.add((new TranslationTextComponent("container.enchant.level.one")).withStyle(TextFormatting.RED));
                        }
                        else{
                            list.add((new TranslationTextComponent("container.enchant.level.many", cost)).withStyle(TextFormatting.RED));
                        }
                    }
                    if(this.enchantButtons[i].notEnoughLapis){
                        if(cost == 1){
                            list.add((new TranslationTextComponent("container.enchant.lapis.one")).withStyle(TextFormatting.RED));
                        }
                        else{
                            list.add((new TranslationTextComponent("container.enchant.lapis.many", cost)).withStyle(TextFormatting.RED));
                        }
                    }
                }
                this.renderComponentTooltip(matrixStack, list, mouseX, mouseY);
                break;
            }
        }
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(this.GUI_TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderPageText(MatrixStack matrixStack) {
        ITextComponent pageText =  new TranslationTextComponent("book.pageIndicator", this.currPage, Math.max(this.numPages, 1));
        int pageTextX = 120 - (this.font.width(pageText) / 2);
        this.font.draw(matrixStack, pageText, (float)pageTextX, (float)(this.inventoryLabelY), 0x404040);
    }

    protected void renderEnchantmentText(MatrixStack matrixStack) {
        for(int i = 0; i < ENCHANT_PER_PAGE && this.size > ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i; i++){
            Enchantment e = this.enchantmentList.get(ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i);
            String s = e.getDescriptionId();
            IFormattableTextComponent enchantmentText =  new TranslationTextComponent(s);
            if(e.getMaxLevel() > 1) enchantmentText.append(" ").append(new TranslationTextComponent("enchantment.level." + this.levelList.get(index(i))));
            float scale = (1.0f/1.5f);
            float x1 = 124.0f - ((float)(this.font.width(enchantmentText)) / 2.0f * scale);

            GlStateManager._pushMatrix();
            GlStateManager._scalef(scale, scale, 1.0f);
            GlStateManager._translatef(x1*(1.0f/scale), (20.5f+10*i)*(1.0f/scale), 0.0f);
            if(!this.enchantButtons[i].visible) this.font.draw(matrixStack, enchantmentText, 0, 0, 0x000000);
            else if(this.enchantButtons[i].isHovered()) this.font.draw(matrixStack, enchantmentText, 0, 0, 0xe0ca9f);
            else  this.font.draw(matrixStack, enchantmentText, 0, 0, 0x404040);
            GlStateManager._popMatrix();

            int index = index(i);
            s = ""+levelRequirement(this.levelList.get(index));
            GlStateManager._pushMatrix();
            GlStateManager._scalef(scale, scale, 1.0f);
            GlStateManager._translatef((80.0f-(this.font.width(s)/2.0f))*(1.0f/scale), (20.5f+10*i)*(1.0f/scale), 0.0f);
            if(!this.enchantButtons[i].visible) this.font.drawShadow(matrixStack, s, 0, 0, 0x408010);
            else this.font.drawShadow(matrixStack, s, 0, 0, 0x80FF20);
            GlStateManager._popMatrix();
        }
    }

    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
        if(this.numPages > 0) {
            renderPageText(matrixStack);
            renderEnchantmentText(matrixStack);
        }

        for(int i = 0; i < ENCHANT_PER_PAGE; i++){
            if(this.enchantButtons[i].isHovered() && this.enchantButtons[i].visible){
                int j = 17;
                int k = 47;
                this.minecraft.getTextureManager().bind(this.GUI_TEXTURE);
                this.blit(matrixStack, j, k, 96, 166, 16, 16);
                this.blit(matrixStack, j+6, k+8, -11+11*this.costList.get(index(i)), 186, 11, 9);
            }
        }
    }
    protected void addChangePageButtons() {
        int i = this.leftPos+116;
        int j = this.topPos;
        this.buttonNextPage = this.addButton(new ChangeEnchantPageButton(i + 41, j+this.inventoryLabelY, true, (p_214159_1_) -> { this.nextPage(); }));
        this.buttonPreviousPage = this.addButton(new ChangeEnchantPageButton(i - 41, j+this.inventoryLabelY, false, (p_214158_1_) -> { this.previousPage(); }));
        this.updateButtons();
    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currPage < this.numPages && this.numPages > 0;
        this.buttonPreviousPage.visible = this.currPage > 1 && this.numPages > 0;
    }

    private void updateItemStack() {
        ItemStack itemStack = this.menu.getSlot(0).getItem();
        this.itemEnchantments = EnchantmentHelper.getEnchantments(itemStack);
        Item item = itemStack.getItem();
        if(item instanceof TieredItem){
            this.discount = ((TieredItem) item).getTier().getEnchantmentValue();
        } else if(item instanceof ArmorItem){
            this.discount = ((ArmorItem) item).getMaterial().getEnchantmentValue();
        } else {
            this.discount = 0;
        }
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
        this.numPages = this.menu.numPages();
        this.enchantmentList = this.menu.getEnchantmentList();
        this.levelList = this.menu.getLevelList();
        this.costList = this.menu.getCostList();
        this.size = this.enchantmentList.size();
        if(this.currPage > this.numPages) this.currPage = this.numPages;
        if(this.numPages == 0) this.currPage = 1;
        this.updateButtons();
        this.updateItemStack();
    }

    protected void addEnchantButtons() {
        int i = this.leftPos+72;
        int j = this.topPos+18;
        for(int k = 0; k < ENCHANT_PER_PAGE; k++){
            int finalK = k;
            this.enchantButtons[k] = this.addButton(new EnchantButton(i, j+k*10, (p_214159_1_) -> { addEnchantment(finalK); }));
        }
        this.updateButtons();
    }

    private void addEnchantment(final int k) {
        int index = index(k);
        int level = this.levelList.get(index);
        int cost = this.costList.get(index);
        Enchantment e = this.enchantmentList.get(index);
        PlayerEntity player = inventory.player;
        boolean b1 = this.menu.slots.get(1).getItem().getCount() >= cost || player.isCreative();
        boolean b2 = player.experienceLevel >= levelRequirement(level) || player.isCreative();
        if(b1 && b2){
            OdysseyNetwork.CHANNEL.sendToServer(new UpdateEnchantPacket(level, EnchantmentUtil.enchantmentToInt(e), cost));
            this.minecraft.getSoundManager().play(SimpleSound.forUI(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0f));
        }
    }

    public int levelRequirement(int level){
        return Integer.max(level * 10 - this.discount, 1);
    }

    private int index(int i){
        return ENCHANT_PER_PAGE * this.currPage-ENCHANT_PER_PAGE+i;
    }
}