package com.bedmen.odyssey.items;

import java.util.*;
import javax.annotation.Nullable;

import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class TomeItem2 extends Item {
    public static final Map<Enchantment, List<List<TomeResearchRequirement>>> ARTHROPOD_RESEARCH_REQUIREMENTS = new HashMap<>();
    public static final List<TomeItem2> TOMES = new ArrayList<>();

    public static void initTomes() {
        List<TomeResearchRequirement> baneOfArthropods1 = List.of(
            new TomeResearchRequirement(1, List.of(ItemRegistry.COPPER_HAMMER.get(), ItemRegistry.MINI_HAMMER.get())),
            new TomeResearchRequirement(1, List.of(ItemRegistry.OBSIDIAN_HAMMER.get(), ItemRegistry.BLUNT_SABRE.get()))
        );

        ARTHROPOD_RESEARCH_REQUIREMENTS.put(Enchantments.BANE_OF_ARTHROPODS, List.of(baneOfArthropods1));
    }

    public static final String TOME_ENCHANTMENTS_TAG = "TomeEnchantments";
    public static final String TOME_ITEMS_TAG = "ResearchedItems";

    public final int color;
    public TomeItem2(Item.Properties properties, int color) {
        super(properties);
        this.color = color;
        TOMES.add(this);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if(level.isClientSide) {
            Minecraft.getInstance().setScreen(new BookViewScreen(new BookViewScreen.WrittenBookAccess(itemstack)));
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

    public void addResearchedItem(ItemStack itemStack, Item item) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        ListTag researchedItems = compoundTag.contains(TOME_ITEMS_TAG) ? compoundTag.getList(TOME_ITEMS_TAG, 10) : new ListTag();
        researchedItems.add(StringTag.valueOf(Registry.ITEM.getKey(item).toString()));
        compoundTag.put(TOME_ITEMS_TAG, researchedItems);
    }

//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
//        ItemStack itemstack = player.getItemInHand(interactionHand);
//        player.openItemGui(itemstack, interactionHand);
//        player.awardStat(Stats.ITEM_USED.get(this));
//        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
//    }

    public record TomeResearchRequirement(int countNeeded, List<Item> items) { }
}