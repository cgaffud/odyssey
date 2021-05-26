package com.bedmen.odyssey.trades;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ModTrades {

    private static Method brewing_mixes;
    private static Map<VillagerProfession, Int2ObjectMap<VillagerTrades.ITrade[]>> trade_list;

    private static void addMix(Potion start, Item ingredient, Potion result) {
        if(brewing_mixes == null) {
            brewing_mixes = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addMix", Potion.class, Item.class, Potion.class);
            brewing_mixes.setAccessible(true);
        }

        try {
            brewing_mixes.invoke(null, start, ingredient, result);
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    private static void removeTrades(){
        if(trade_list == null) {
            trade_list = ObfuscationReflectionHelper.getPrivateValue(VillagerTrades.class, null, "TRADES");
        }

        trade_list.remove(VillagerProfession.FARMER);
        trade_list.remove(VillagerProfession.FISHERMAN);
        trade_list.remove(VillagerProfession.SHEPHERD);
        trade_list.remove(VillagerProfession.FLETCHER);
        trade_list.remove(VillagerProfession.LIBRARIAN);
        trade_list.remove(VillagerProfession.CARTOGRAPHER);
        trade_list.remove(VillagerProfession.CLERIC);
        trade_list.remove(VillagerProfession.ARMORER);
        trade_list.remove(VillagerProfession.WEAPONSMITH);
        trade_list.remove(VillagerProfession.TOOLSMITH);
        trade_list.remove(VillagerProfession.BUTCHER);
        trade_list.remove(VillagerProfession.LEATHERWORKER);
        trade_list.remove(VillagerProfession.MASON);
    }

    public static void addTrades() {
        removeTrades();

        trade_list.put(VillagerProfession.FARMER, gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Items.GOLDEN_APPLE, 50, 1, 2), new EmeraldForItemsTrade(Items.GOLDEN_APPLE, 40, 1, 2)})));
    }

    private static Int2ObjectMap<VillagerTrades.ITrade[]> gatAsIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap<>(p_221238_0_);
    }

    static class EmeraldForItemsTrade implements VillagerTrades.ITrade {
        private final Item tradeItem;
        private final int count;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EmeraldForItemsTrade(IItemProvider tradeItemIn, int countOut, int maxUsesIn, int xpValueIn) {
            this.tradeItem = tradeItemIn.asItem();
            this.count = countOut;
            this.maxUses = maxUsesIn;
            this.xpValue = xpValueIn;
            this.priceMultiplier = 0.00F;
        }

        public MerchantOffer getOffer(Entity trader, Random rand) {
            ItemStack itemstack = new ItemStack(this.tradeItem, 1);
            return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD, this.count), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForEmeraldsTrade implements VillagerTrades.ITrade {
        private final ItemStack sellingItem;
        private final int emeraldCount;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForEmeraldsTrade(Block sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, 12, xpValue);
        }

        public ItemsForEmeraldsTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsTrade(ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(sellingItem, emeraldCount, sellingItemCount, maxUses, xpValue, 0.05F);
        }

        public ItemsForEmeraldsTrade(ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingItem = sellingItem;
            this.emeraldCount = emeraldCount;
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class EnchantedItemForEmeraldsTrade implements VillagerTrades.ITrade {
        private final ItemStack sellingStack;
        private final int emeraldCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EnchantedItemForEmeraldsTrade(Item p_i50535_1_, int emeraldCount, int maxUses, int xpValue) {
            this(p_i50535_1_, emeraldCount, maxUses, xpValue, 0.05F);
        }

        public EnchantedItemForEmeraldsTrade(Item sellItem, int emeraldCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingStack = new ItemStack(sellItem);
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        public MerchantOffer getOffer(Entity trader, Random rand) {
            int i = 5 + rand.nextInt(15);
            ItemStack itemstack = EnchantmentHelper.enchantItem(rand, new ItemStack(this.sellingStack.getItem()), i, false);
            int j = Math.min(this.emeraldCount + i, 64);
            ItemStack itemstack1 = new ItemStack(Items.EMERALD, j);
            return new MerchantOffer(itemstack1, itemstack, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

}