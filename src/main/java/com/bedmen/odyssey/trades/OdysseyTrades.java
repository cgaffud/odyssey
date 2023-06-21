package com.bedmen.odyssey.trades;

import com.bedmen.odyssey.items.odyssey_versions.OdysseyMapItem;
import com.bedmen.odyssey.tags.OdysseyStructureTags;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;

public class OdysseyTrades {

    public static void addTrades() {
        VillagerTrades.TRADES.clear();
        VillagerTrades.TRADES.put(VillagerProfession.CARTOGRAPHER, toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new OdysseyTreasureMapForEmeralds(13, OdysseyStructureTags.ON_COVEN_HUT_MAPS, "filled_map.coven_hut", 12, 5)})));
    }

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> immutableMap) {
        return new Int2ObjectOpenHashMap<>(immutableMap);
    }

    static class OdysseyTreasureMapForEmeralds implements VillagerTrades.ItemListing {
        private final int emeraldCost;
        private final TagKey<Structure> destination;
        private final String displayName;
        private final int maxUses;
        private final int villagerXp;

        public OdysseyTreasureMapForEmeralds(int emeraldCost, TagKey<Structure> destination, String displayName, int maxUses, int villagerXp) {
            this.emeraldCost = emeraldCost;
            this.destination = destination;
            this.displayName = displayName;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
        }

        @Nullable
        public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
            if (!(entity.level instanceof ServerLevel serverlevel)) {
                return null;
            } else {
                BlockPos blockpos = serverlevel.findNearestMapStructure(this.destination, entity.blockPosition(), 100, true);
                if (blockpos != null) {
                    ItemStack itemstack = OdysseyMapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
                    MapItem.renderBiomePreviewMap(serverlevel, itemstack);
                    MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", OdysseyMapItem.DecorationType.COVEN_HUT);
                    itemstack.setHoverName(Component.translatable(this.displayName));
                    return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.villagerXp, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

}