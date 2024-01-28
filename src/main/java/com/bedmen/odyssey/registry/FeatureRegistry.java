package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.bedmen.odyssey.world.gen.feature.AbandonedIronGolemFeature;
import com.bedmen.odyssey.world.gen.feature.SculkBulbFeature;
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
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TREASURE_CHEST = FEATURES.register("treasure_chest", () -> new TreasureChestFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SCULK_BULB = FEATURES.register("sculk_bulb", () -> new SculkBulbFeature(NoneFeatureConfiguration.CODEC));
}