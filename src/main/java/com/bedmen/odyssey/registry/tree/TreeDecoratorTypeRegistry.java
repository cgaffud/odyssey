package com.bedmen.odyssey.registry.tree;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.feature.tree.GreatFoliagePlacer;
import com.bedmen.odyssey.world.gen.feature.tree.LeaveGreatwoodVineDecorator;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TreeDecoratorTypeRegistry {

    public static DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = DeferredRegister.create(Registry.TREE_DECORATOR_TYPE_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        TREE_DECORATOR_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<TreeDecoratorType<LeaveGreatwoodVineDecorator>> LEAVE_GREATWOOD_VINE = TREE_DECORATOR_TYPE.register("leave_greatwood_vine", () -> new TreeDecoratorType<>(LeaveGreatwoodVineDecorator.CODEC));
    }
