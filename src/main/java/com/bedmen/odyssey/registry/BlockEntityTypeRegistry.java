package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.AlloyFurnaceBlockEntity;
import com.bedmen.odyssey.block.entity.HollowCoconutBlockEntity;
import com.bedmen.odyssey.block.entity.OdysseySignBlockEntity;
import com.bedmen.odyssey.potions.OdysseyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEntityTypeRegistry {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES , Odyssey.MOD_ID);

    public static void init() {
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<OdysseySignBlockEntity>> SIGN = BLOCK_ENTITY_TYPES.register("sign",() -> BlockEntityType.Builder.of(OdysseySignBlockEntity::new, BlockRegistry.PALM_SIGN.get(), BlockRegistry.PALM_WALL_SIGN.get()).build(null));
    public static final RegistryObject<BlockEntityType<HollowCoconutBlockEntity>> HOLLOW_COCONUT = BLOCK_ENTITY_TYPES.register("hollow_coconut", () -> BlockEntityType.Builder.of(HollowCoconutBlockEntity::new, BlockRegistry.HOLLOW_COCONUT.get()).build(null));
    public static final RegistryObject<BlockEntityType<AlloyFurnaceBlockEntity>> ALLOY_FURNACE = BLOCK_ENTITY_TYPES.register("alloy_furnace", () -> BlockEntityType.Builder.of(AlloyFurnaceBlockEntity::new, BlockRegistry.ALLOY_FURNACE.get()).build(null));
}