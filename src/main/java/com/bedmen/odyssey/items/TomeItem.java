package com.bedmen.odyssey.items;

import com.bedmen.odyssey.client.gui.screens.TomeViewScreen;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.util.*;

public class TomeItem extends Item {
    // The map holds the enchantments for the tomes as keys
    // The index for the first list is the enchantment level
    // The List<TomeResearchRequirement> is the list of requirements for research
    public static final Map<Enchantment, List<List<TomeResearchRequirement>>> ARTHROPOD_RESEARCH_REQUIREMENTS = new HashMap<>();
    public static final List<TomeItem> TOMES = new ArrayList<>();

    public static void initTomes() {
        List<TomeResearchRequirement> baneOfArthropods1 = List.of(
            new TomeResearchRequirement(1, List.of(ItemRegistry.COPPER_HAMMER.get(), ItemRegistry.MINI_HAMMER.get())),
            new TomeResearchRequirement(2, List.of(ItemRegistry.CLOVER_STONE_HAMMER.get(), ItemRegistry.OBSIDIAN_HAMMER.get(), ItemRegistry.BLUNT_SABRE.get()))
        );
        List<TomeResearchRequirement> baneOfArthropods2 = List.of(
                new TomeResearchRequirement(2, List.of(ItemRegistry.ICE_DAGGER.get(), ItemRegistry.IRON_FIBER.get()))
        );
        List<TomeResearchRequirement> baneOfArthropods3 = List.of(
                new TomeResearchRequirement(1, List.of(ItemRegistry.RAIN_SWORD.get(), ItemRegistry.DIAMOND_AXE.get()))
        );

        ARTHROPOD_RESEARCH_REQUIREMENTS.put(Enchantments.BANE_OF_ARTHROPODS, List.of(baneOfArthropods1, baneOfArthropods2, baneOfArthropods3));
    }

    public static final String TOME_ENCHANTMENTS_TAG = "TomeEnchantments";
    public static final String TOME_ITEMS_TAG = "ResearchedItems";
    public static final String TOME_SAVED_PAGE_TAG = "SavedPage";

    public final Map<Enchantment, List<List<TomeResearchRequirement>>> tomeRequirements;
    public final int color;
    public TomeItem(Item.Properties properties, Map<Enchantment, List<List<TomeResearchRequirement>>> tomeRequirements, int color) {
        super(properties.stacksTo(1));
        this.color = color;
        this.tomeRequirements = tomeRequirements;
        TOMES.add(this);
    }

    public static ListTag getResearchedItemsListTag(ItemStack tome) {
        CompoundTag compoundTag = tome.getOrCreateTag();
        if(!compoundTag.contains(TOME_ITEMS_TAG)){
            compoundTag.put(TOME_ITEMS_TAG, new ListTag());
        }
        return compoundTag.getList(TOME_ITEMS_TAG, 10);
    }

    public static List<Item> getResearchedItemsList(ItemStack tome) {
        ListTag researchedItemsListTag = TomeItem.getResearchedItemsListTag(tome);
        return researchedItemsListTag.stream().map(tag -> ItemStack.of((CompoundTag) tag).getItem()).toList();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if(level.isClientSide) {
            Minecraft.getInstance().setScreen(new TomeViewScreen(new TomeViewScreen.TomeAccess(interactionHand, itemstack)));
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        if (itemStack.hasTag()) {
            Map<Enchantment, Integer> map = getEnchantments(itemStack);
            for(Map.Entry<Enchantment, Integer> entry: map.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int lvl = entry.getValue();
                componentList.add(enchantment.getFullname(lvl));
            }
        }
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack tome) {
        Map<Enchantment, Integer> map = new LinkedHashMap<>();
        CompoundTag compoundTag = tome.getTag();
        if(compoundTag == null || !compoundTag.contains(TOME_ENCHANTMENTS_TAG)) {
            return map;
        }
        ListTag enchantmentListTag = compoundTag.getList(TOME_ENCHANTMENTS_TAG, 10);
        for(int i = 0; i < enchantmentListTag.size(); ++i) {
            CompoundTag enchantmentTag = enchantmentListTag.getCompound(i);
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(EnchantmentHelper.getEnchantmentId(enchantmentTag));
            int level = EnchantmentHelper.getEnchantmentLevel(enchantmentTag);
            if (enchantment != null) {
                map.put(enchantment, level);
            }
        }
        return map;
    }

    @Nullable
    public static Tuple<Enchantment, Integer> getSavedPage(ItemStack tome) {
        CompoundTag compoundTag = tome.getOrCreateTag();
        Enchantment savedEnchantment = null;
        int savedLvl = -1;
        if(compoundTag.contains(TOME_SAVED_PAGE_TAG)){
            CompoundTag savedPageTag = compoundTag.getCompound(TOME_SAVED_PAGE_TAG);
            savedEnchantment = ForgeRegistries.ENCHANTMENTS.getValue(EnchantmentHelper.getEnchantmentId(savedPageTag));
            savedLvl = EnchantmentHelper.getEnchantmentLevel(savedPageTag);
        }
        if(savedEnchantment == null) {
            return null;
        }
        return new Tuple<>(savedEnchantment, savedLvl);
    }

    public boolean canBeResearched(ItemStack researchStack, ItemStack quill, ItemStack tome) {
        List<Item> researchedItemsList = getResearchedItemsList(tome);
        Item researchItem = researchStack.getItem();
        QuillItem quillItem = (QuillItem) quill.getItem();
        return this.getRequiredItems(quillItem.isCursed).contains(researchItem) && !researchedItemsList.contains(researchItem);
    }

    public void addResearchedItem(ItemStack researchStack, ItemStack tome) {
        ListTag researchedItemsListTag = getResearchedItemsListTag(tome);
        CompoundTag researchStackTag = new CompoundTag();
        researchStack.getItem().getDefaultInstance().save(researchStackTag);
        researchedItemsListTag.add(researchStackTag);
        updateEnchantments(tome);
    }

    private void updateEnchantments(ItemStack tome){
        ListTag enchantmentsTag = new ListTag();
        List<TomePage> tomePages = this.getTomePages(tome);
        Map<Enchantment, Integer> enchantmentLevels = new LinkedHashMap<>();
        for(TomePage tomePage: tomePages){
            if(tomePage.enchantmentFullyResearched) {
                if(enchantmentLevels.containsKey(tomePage.enchantment)){
                    int currentLvl = enchantmentLevels.get(tomePage.enchantment);
                    if(tomePage.lvl > currentLvl) {
                        enchantmentLevels.put(tomePage.enchantment, tomePage.lvl);
                    }
                } else {
                    enchantmentLevels.put(tomePage.enchantment, tomePage.lvl);
                }
            }
        }
        for(Map.Entry<Enchantment, Integer> entry: enchantmentLevels.entrySet()){
            CompoundTag enchantmentTag = new CompoundTag();
            enchantmentTag.putString("id", ForgeRegistries.ENCHANTMENTS.getKey(entry.getKey()).toString());
            enchantmentTag.putInt("lvl", entry.getValue());
            enchantmentsTag.add(enchantmentTag);
        }
        CompoundTag compoundTag = tome.getOrCreateTag();
        compoundTag.put(TOME_ENCHANTMENTS_TAG, enchantmentsTag);
    }

    public Set<Item> getRequiredItems(boolean isCurse){
        Set<Item> itemSet = new HashSet<>();
        for(Map.Entry<Enchantment, List<List<TomeResearchRequirement>>> entry: this.tomeRequirements.entrySet()){
            Enchantment enchantment = entry.getKey();
            if(enchantment.isCurse() == isCurse) {
                for(List<TomeResearchRequirement> tomeResearchRequirementList: entry.getValue()){
                    for(TomeResearchRequirement tomeResearchRequirement: tomeResearchRequirementList){
                        itemSet.addAll(tomeResearchRequirement.items());
                    }
                }
            }
        }
        return itemSet;
    }

    public List<TomePage> getTomePages(ItemStack tome) {
        List<Item> researchedItemList = TomeItem.getResearchedItemsList(tome);
        List<TomePage> tomePageList = new ArrayList<>();
        Map<Enchantment, Integer> tomeEnchantments = TomeItem.getEnchantments(tome);
        for(Map.Entry<Enchantment, List<List<TomeItem.TomeResearchRequirement>>> entry: this.tomeRequirements.entrySet()){
            Enchantment enchantment = entry.getKey();
            List<List<TomeItem.TomeResearchRequirement>> pageRequirementList = entry.getValue();
            int minLevel = enchantment.getMinLevel();
            int maxLevel = Integer.min(enchantment.getMaxLevel(), pageRequirementList.size() + minLevel - 1);
            int currentLevel = tomeEnchantments.get(enchantment) == null ? 0 : tomeEnchantments.get(enchantment);
            int numPagesForEnchantment = Mth.clamp(currentLevel - minLevel + 2, 1, maxLevel - minLevel + 1) ;
            for(int lvl = minLevel; lvl < minLevel + numPagesForEnchantment; lvl++){
                int i = lvl - minLevel;
                List<TomeRequirementStatus> tomeRequirementStatusList = new ArrayList<>();
                List<TomeItem.TomeResearchRequirement> pageRequirements = pageRequirementList.get(i);
                for(TomeItem.TomeResearchRequirement tomeResearchRequirement: pageRequirements){
                    List<Boolean> itemsAreResearched = tomeResearchRequirement.items().stream().map(researchedItemList::contains).toList();
                    int numItemsResearchedForRequirement = itemsAreResearched.stream().reduce(0,
                            (acc, isResearched) -> acc + (isResearched ? 1 : 0), Integer::sum);
                    boolean requirementSatisfied = numItemsResearchedForRequirement >= tomeResearchRequirement.countNeeded();
                    TomeRequirementStatus tomeRequirementStatus = new TomeRequirementStatus(tomeResearchRequirement, numItemsResearchedForRequirement, requirementSatisfied, itemsAreResearched);
                    tomeRequirementStatusList.add(tomeRequirementStatus);
                }
                int numRequirementsCompleted = tomeRequirementStatusList.stream().reduce(0,
                        (acc, tomeRequirementStatus) -> acc + (tomeRequirementStatus.requirementSatisfied ? 1 : 0), Integer::sum);
                boolean enchantmentFullyResearched = numRequirementsCompleted >= pageRequirements.size();
                TomePage tomePage = new TomePage(enchantment, lvl, numRequirementsCompleted, enchantmentFullyResearched, tomeRequirementStatusList);
                tomePageList.add(tomePage);
            }
        }
        return tomePageList;
    }

    public record TomeResearchRequirement(int countNeeded, List<Item> items) { }

    public record TomePage(Enchantment enchantment,
                           int lvl,
                           int numRequirementsCompleted,
                           boolean enchantmentFullyResearched,
                           List<TomeRequirementStatus> tomeRequirementStatusList){}

    public record TomeRequirementStatus(TomeItem.TomeResearchRequirement tomeResearchRequirement,
                                        int numItemsResearched,
                                        boolean requirementSatisfied,
                                        List<Boolean> itemsAreResearched){}
}