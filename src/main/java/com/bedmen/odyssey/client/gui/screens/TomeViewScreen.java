package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.TomeItem2;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TomeViewScreen extends Screen {
    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 18;
    public static final int PAGE_TEXT_X_OFFSET = 36;
    public static final int PAGE_TEXT_Y_OFFSET = 30;
    public static final ResourceLocation TOME_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/tome.png");
    protected static final int TEXT_WIDTH = 114;
    protected static final int TEXT_HEIGHT = 128;
    protected static final int IMAGE_WIDTH = 192;
    protected static final int IMAGE_HEIGHT = 192;
    protected static final int IMAGE_TOP_Y = 2;
    protected static final int BACK_BUTTON_X_OFFSET = 43;
    protected static final int FORWARD_BUTTON_X_OFFSET = 116;
    protected static final int BUTTON_Y_OFFSET = 157;
    protected static final int ITEM_GRID_X_OFFSET = 60;
    protected static final int ITEM_GRID_Y_OFFSET = 32;
    protected static final int REQUIREMENT_TEXT_LEFT_X_SIDE = 34;
    protected static final int CHECK_MARK_X_POSITION = 0;
    protected static final int CHECK_MARK_Y_POSITION = 218;
    protected static final int CHECK_MARK_DIAMETER = 16;
//    protected static final int CIRCLE_X_POSITION = 16;
//    protected static final int CIRCLE_Y_POSITION = 218;
    protected static final int BOX_DIAMETER = 20;
    protected static final int BOX_X_POSITION = 46;
    protected static final int BOX_Y_POSITION = 219;
    protected static final int ITEMSTACK_ROW_COLUMN_SPACING = BOX_DIAMETER +2;
    private final TomeViewScreen.TomeAccess tomeAccess;
    private int currentPage;
    private PageButton forwardButton;
    private PageButton backButton;
    private final boolean playTurnSound;

    public TomeViewScreen(TomeViewScreen.TomeAccess tomeAccess) {
        this(tomeAccess, true);
    }

    private TomeViewScreen(TomeViewScreen.TomeAccess tomeAccess, boolean playTurnSound) {
        super(NarratorChatListener.NO_TITLE);
        this.tomeAccess = tomeAccess;
        this.playTurnSound = playTurnSound;
    }

    public int getImageLeftX(){
        return (this.width - IMAGE_WIDTH) / 2;
    }

    public boolean setPage(int pageNumber) {
        int i = Mth.clamp(pageNumber, 0, this.tomeAccess.getPageCount() - 1);
        if (i != this.currentPage) {
            this.currentPage = i;
            this.updateButtonVisibility();
            return true;
        } else {
            return false;
        }
    }

    protected boolean forcePage(int pageNumber) {
        return this.setPage(pageNumber);
    }

    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.addRenderableWidget(new Button(this.width / 2 - 100, 196, 200, 20, CommonComponents.GUI_DONE, (p_98299_) -> {
            this.minecraft.setScreen((Screen)null);
        }));
    }

    protected void createPageControlButtons() {
        int imageLeftX = getImageLeftX();
        this.forwardButton = this.addRenderableWidget(new PageButton(imageLeftX + FORWARD_BUTTON_X_OFFSET, IMAGE_TOP_Y + BUTTON_Y_OFFSET, true, (button) -> this.pageForward(), this.playTurnSound));
        this.backButton = this.addRenderableWidget(new PageButton(imageLeftX + BACK_BUTTON_X_OFFSET, IMAGE_TOP_Y + BUTTON_Y_OFFSET, false, (button) -> this.pageBack(), this.playTurnSound));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return this.tomeAccess.getPageCount();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            switch(keyCode) {
                case 266:
                    this.backButton.onPress();
                    return true;
                case 267:
                    this.forwardButton.onPress();
                    return true;
                default:
                    return false;
            }
        }
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TOME_LOCATION);
        int imageLeftX = getImageLeftX();
        this.blit(poseStack, imageLeftX, IMAGE_TOP_Y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        TomePage tomePage = this.tomeAccess.pages.get(this.currentPage);
        // Enchantment name and level header text
        MutableComponent enchantmentHeader = ((MutableComponent) tomePage.enchantmentComponent).withStyle(ChatFormatting.BLACK);
        int headerWidth = this.font.width(enchantmentHeader);
        this.font.draw(poseStack, enchantmentHeader, (float)(imageLeftX + (IMAGE_WIDTH - headerWidth)/2), 18.0F, 0);
        // Research completion text
        TextComponent textComponent = new TextComponent(tomePage.numRequirementsCompleted + "/" + tomePage.tomeRequirementStatusList.size() + " Requirements Completed").withStyle()

        for(int i = 0; i < tomePage.tomeRequirementStatusList.size(); i++) {
            int rowDistance = ITEMSTACK_ROW_COLUMN_SPACING * i;
            TomeRequirementStatus tomeRequirementStatus = tomePage.tomeRequirementStatusList.get(i);
            TomeItem2.TomeResearchRequirement tomeResearchRequirement = tomeRequirementStatus.tomeResearchRequirement;
            // Do green checkmark instead of x/y text if the requirement is satisfied
            if(tomeRequirementStatus.requirementSatisfied) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, TOME_LOCATION);
                this.blit(poseStack, imageLeftX + (ITEM_GRID_X_OFFSET + REQUIREMENT_TEXT_LEFT_X_SIDE - CHECK_MARK_DIAMETER)/2, IMAGE_TOP_Y + ITEM_GRID_Y_OFFSET + rowDistance + (BOX_DIAMETER - CHECK_MARK_DIAMETER)/2, CHECK_MARK_X_POSITION, CHECK_MARK_Y_POSITION, CHECK_MARK_DIAMETER, CHECK_MARK_DIAMETER);
            } else {
                FormattedText formattedText = FormattedText.of(tomeRequirementStatus.numItemsResearched+"/"+tomeResearchRequirement.countNeeded());
                for(FormattedCharSequence formattedcharsequence: this.font.split(formattedText, Integer.MAX_VALUE)){
                    this.font.draw(poseStack, formattedcharsequence, (float)(imageLeftX + (ITEM_GRID_X_OFFSET + REQUIREMENT_TEXT_LEFT_X_SIDE - CHECK_MARK_DIAMETER)/2), (float)(IMAGE_TOP_Y + ITEM_GRID_Y_OFFSET + rowDistance + (BOX_DIAMETER - this.font.lineHeight)/2), 0);
                }
            }
            List<ItemStack> itemStackList = tomeResearchRequirement.items().stream().map(Item::getDefaultInstance).toList();
            for(int j = 0; j < itemStackList.size(); j++) {
                ItemStack itemStack = itemStackList.get(j);
                boolean isResearched = tomeRequirementStatus.itemsAreResearched.get(j);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                int circleLeftX = imageLeftX + ITEM_GRID_X_OFFSET + j * ITEMSTACK_ROW_COLUMN_SPACING;
                int circleTopY = IMAGE_TOP_Y + ITEM_GRID_Y_OFFSET + rowDistance;
                int itemOffset = (BOX_DIAMETER - 16)/2;
                int itemLeftX = circleLeftX + itemOffset;
                int itemTopY = circleTopY + itemOffset;
                this.renderItemStack(poseStack, itemLeftX, itemTopY, itemStack);
//                if(isResearched){
//
//                }
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, TOME_LOCATION);
                this.blit(poseStack, circleLeftX, circleTopY, BOX_X_POSITION + (isResearched ? BOX_DIAMETER : 0), BOX_Y_POSITION, BOX_DIAMETER, BOX_DIAMETER);
                if (this.isHovering(itemLeftX, itemTopY, mouseX, mouseY)) {
                    int color = isResearched ? 0x8080FF80 : 0x80FFFFFF;
                    RenderSystem.disableDepthTest();
                    RenderSystem.colorMask(true, true, true, false);
                    fillGradient(poseStack, itemLeftX, itemTopY, itemLeftX + 16, itemTopY + 16, color, color,  this.getBlitOffset());
                    RenderSystem.colorMask(true, true, true, true);
                    RenderSystem.enableDepthTest();
                    this.renderTooltip(poseStack, itemStack, mouseX, mouseY);
                }
            }
        }

//        for(int k = 0; k < this.menu.slots.size(); ++k) {
//            Slot slot = this.menu.slots.get(k);
//            if (slot.isActive()) {
//                RenderSystem.setShader(GameRenderer::getPositionTexShader);
//                this.renderSlot(poseStack, slot);
//            }
//
//            if (this.isHovering(slot, (double)p_97796_, (double)p_97797_) && slot.isActive()) {
//                this.hoveredSlot = slot;
//                int l = slot.x;
//                int i1 = slot.y;
//                renderSlotHighlight(p_97795_, l, i1, this.getBlitOffset(), this.getSlotColor(k));
//            }
//        }


//        int k = Math.min(128 / 9, this.cachedPageComponents.size());
//
//        for(int l = 0; l < k; ++l) {
//            FormattedCharSequence formattedcharsequence = this.cachedPageComponents.get(l);
//            this.font.draw(poseStack, formattedcharsequence, (float)(i + 36), (float)(32 + l * 9), 0);
//        }

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    protected boolean isHovering(int itemStackLeftX, int itemStackTopY, double mouseX, double mouseY) {
        return mouseX >= (double)(itemStackLeftX - 1) && mouseX < (double)(itemStackLeftX + 16 + 1) && mouseY >= (double)(itemStackTopY - 1) && mouseY < (double)(itemStackTopY + 16 + 1);
    }

//    protected void renderTooltip(PoseStack poseStack, ItemStack itemStack, int mouseX, int mouseY) {
//        tooltipStack = itemStack;
//        this.renderTooltip(poseStack, this.getTooltipFromItem(itemStack), itemStack.getTooltipImage(), mouseX, mouseY);
//        tooltipStack = ItemStack.EMPTY;
//    }

    private void renderItemStack(PoseStack poseStack, int slotLeftX, int slotTopY, ItemStack itemStack) {
        String s = null;

        this.setBlitOffset(100);
        this.itemRenderer.blitOffset = 100.0F;

        RenderSystem.enableDepthTest();
        this.itemRenderer.renderAndDecorateItem(this.minecraft.player, itemStack, slotLeftX, slotTopY, slotLeftX + slotTopY * IMAGE_WIDTH);
        this.itemRenderer.renderGuiItemDecorations(this.font, itemStack, slotLeftX, slotTopY, s);

        this.itemRenderer.blitOffset = 0.0F;
        this.setBlitOffset(0);
    }

    public boolean handleComponentClicked(Style style) {
        ClickEvent clickevent = style.getClickEvent();
        if (clickevent == null) {
            return false;
        } else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String s = clickevent.getValue();

            try {
                int i = Integer.parseInt(s) - 1;
                return this.forcePage(i);
            } catch (Exception exception) {
                return false;
            }
        }
        return false;
    }

    public static class TomeAccess {

        private final List<TomePage> pages;

        public TomeAccess(ItemStack itemStack) {
            this.pages = readPages(itemStack);
        }

        public int getPageCount() {
            return this.pages.size();
        }

        private static List<TomePage> readPages(ItemStack itemStack) {
            TomeItem2 tomeItem2 = (TomeItem2) itemStack.getItem();
            ListTag researchedItemsListTag = TomeItem2.getResearchedItemsListTag(itemStack);
            List<Item> researchedItemList = researchedItemsListTag.stream().map(tag -> ItemStack.of((CompoundTag) tag).getItem()).toList();
            List<TomePage> tomePageList = new ArrayList<>();
            Map<Enchantment, Integer> tomeEnchantments = TomeItem2.getEnchantments(itemStack);
            for(Map.Entry<Enchantment, List<List<TomeItem2.TomeResearchRequirement>>> entry: tomeItem2.tomeRequirements.entrySet()){
                Enchantment enchantment = entry.getKey();
                List<List<TomeItem2.TomeResearchRequirement>> pageRequirementList = entry.getValue();
                int minLevel = enchantment.getMinLevel();
                int maxLevel = Integer.min(enchantment.getMaxLevel(), pageRequirementList.size() + minLevel - 1);
                int currentLevel = tomeEnchantments.get(enchantment) == null ? 0 : tomeEnchantments.get(enchantment);
                int numPagesForEnchantment = Mth.clamp(currentLevel - minLevel + 2, 1, maxLevel - minLevel + 1) ;
                for(int lvl = minLevel; lvl < minLevel + numPagesForEnchantment; lvl++){
                    int i = lvl - minLevel;
                    Component enchantmentComponent = enchantment.getFullname(lvl);
                    List<TomeRequirementStatus> tomeRequirementStatusList = new ArrayList<>();
                    List<TomeItem2.TomeResearchRequirement> pageRequirements = pageRequirementList.get(i);
                    for(TomeItem2.TomeResearchRequirement tomeResearchRequirement: pageRequirements){
                        List<Boolean> itemsAreResearched = tomeResearchRequirement.items().stream().map(researchedItemList::contains).toList();
                        int numItemsResearchedForRequirement = itemsAreResearched.stream().reduce(0,
                                (acc, isResearched) -> acc + (isResearched ? 1 : 0), Integer::sum);
                        boolean requirementSatisfied = numItemsResearchedForRequirement >= tomeResearchRequirement.countNeeded();
                        TomeRequirementStatus tomeRequirementStatus = new TomeRequirementStatus(tomeResearchRequirement, numItemsResearchedForRequirement, requirementSatisfied, itemsAreResearched);
                        tomeRequirementStatusList.add(tomeRequirementStatus);
                    }
                    int numRequirementsCompleted = tomeRequirementStatusList.stream().reduce(0,
                            (acc, tomeRequirementStatus) -> acc + (tomeRequirementStatus.requirementSatisfied ? 1 : 0), Integer::sum);
                    boolean enchantmentFullyResearched = numRequirementsCompleted >= pageRequirementList.size();
                    TomePage tomePage = new TomePage(enchantmentComponent, numRequirementsCompleted, enchantmentFullyResearched, tomeRequirementStatusList);
                    tomePageList.add(tomePage);
                }
            }
            return tomePageList;
        }
    }

    public record TomePage(Component enchantmentComponent,
                           int numRequirementsCompleted,
                           boolean enchantmentFullyResearched,
                           List<TomeRequirementStatus> tomeRequirementStatusList){}

    public record TomeRequirementStatus(TomeItem2.TomeResearchRequirement tomeResearchRequirement,
                                        int numItemsResearched,
                                        boolean requirementSatisfied,
                                        List<Boolean> itemsAreResearched){}
}