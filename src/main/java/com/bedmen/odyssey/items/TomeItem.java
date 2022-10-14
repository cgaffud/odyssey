package com.bedmen.odyssey.items;

import com.bedmen.odyssey.client.gui.screens.TomeViewScreen;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
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
            new TomeResearchRequirement(1, List.of(ItemRegistry.OBSIDIAN_HAMMER.get(), ItemRegistry.BLUNT_SABRE.get()))
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

    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        Map<Enchantment, Integer> map = new LinkedHashMap<>();
        CompoundTag compoundTag = itemStack.getTag();
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

    public record TomeResearchRequirement(int countNeeded, List<Item> items) { }
}