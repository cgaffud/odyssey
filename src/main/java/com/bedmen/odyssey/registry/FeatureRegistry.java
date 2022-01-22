package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.world.gen.feature.AbandonedIronGolemFeature;
import com.bedmen.odyssey.world.gen.feature.TreasureChestFeature;
import com.bedmen.odyssey.world.gen.feature.TriplePlantBlockFeature;
import com.bedmen.odyssey.world.gen.feature.tree.CornerLeafTreeFeature;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FeatureRegistry {

    public static DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Odyssey.MOD_ID);

    public static void init() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Feature<SimpleBlockConfiguration>> TRIPLE_PLANT_BLOCK = FEATURES.register("triple_plant_block", () -> new TriplePlantBlockFeature(SimpleBlockConfiguration.CODEC));
    public static final RegistryObject<Feature<TreeConfiguration>> CORNER_LEAF_TREE = FEATURES.register("corner_leaf_tree", () -> new CornerLeafTreeFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ABANDONED_IRON_GOLEM = FEATURES.register("abandoned_iron_golem", () -> new AbandonedIronGolemFeature(NoneFeatureConfiguration.CODEC));

    //Treasure Chests
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> COPPER_TREASURE_CHEST = FEATURES.register("copper_treasure_chest", () -> new TreasureChestFeature(NoneFeatureConfiguration.CODEC, TreasureChestMaterial.COPPER, 0, 56));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> STERLING_SILVER_TREASURE_CHEST = FEATURES.register("sterling_silver_treasure_chest", () -> new TreasureChestFeature(NoneFeatureConfiguration.CODEC, TreasureChestMaterial.STERLING_SILVER, -56, 0));
    public static final Map<TreasureChestMaterial, Feature<NoneFeatureConfiguration>> TREASURE_CHEST_MAP = new HashMap<>();

    public static void initTreasureChests(){
        TREASURE_CHEST_MAP.put(TreasureChestMaterial.COPPER, COPPER_TREASURE_CHEST.get());
        TREASURE_CHEST_MAP.put(TreasureChestMaterial.STERLING_SILVER, STERLING_SILVER_TREASURE_CHEST.get());
    }
}