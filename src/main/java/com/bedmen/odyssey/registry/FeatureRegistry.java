package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.bedmen.odyssey.world.gen.feature.AbandonedIronGolemFeature;
import com.bedmen.odyssey.world.gen.feature.TreasureChestFeature;
import com.bedmen.odyssey.world.gen.feature.TriplePlantBlockFeature;
import com.bedmen.odyssey.world.gen.feature.tree.CornerLeafTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class FeatureRegistry {

    public static DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Odyssey.MOD_ID);

    public static void init() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Feature<SimpleBlockConfiguration>> TRIPLE_PLANT_BLOCK = FEATURES.register("triple_plant_block", () -> new TriplePlantBlockFeature(SimpleBlockConfiguration.CODEC));
    public static final RegistryObject<Feature<TreeConfiguration>> CORNER_LEAF_TREE = FEATURES.register("corner_leaf_tree", () -> new CornerLeafTreeFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ABANDONED_IRON_GOLEM = FEATURES.register("abandoned_iron_golem", () -> new AbandonedIronGolemFeature(NoneFeatureConfiguration.CODEC));

    //Treasure Chests
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> COPPER_TREASURE_CHEST = FEATURES.register("copper_treasure_chest", () -> new TreasureChestFeature(NoneFeatureConfiguration.CODEC, TreasureChestType.COPPER, 0, 56));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> STERLING_SILVER_TREASURE_CHEST = FEATURES.register("sterling_silver_treasure_chest", () -> new TreasureChestFeature(NoneFeatureConfiguration.CODEC, TreasureChestType.STERLING_SILVER, -64, 0));
    public static final Map<TreasureChestType, Feature<NoneFeatureConfiguration>> TREASURE_CHEST_MAP = new HashMap<>();

    public static void initTreasureChests(){
        TREASURE_CHEST_MAP.put(TreasureChestType.COPPER, COPPER_TREASURE_CHEST.get());
        TREASURE_CHEST_MAP.put(TreasureChestType.STERLING_SILVER, STERLING_SILVER_TREASURE_CHEST.get());
    }
}