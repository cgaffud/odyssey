package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectStrengthMap;
import com.bedmen.odyssey.aspect.encapsulator.PermabuffHolder;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.entity.boss.coven.CovenType;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class EntityDataSerializerRegistry {

    public static DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Odyssey.MOD_ID);

    public static void init() {
        ENTITY_DATA_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<EntityDataSerializer<IntList>> INT_LIST = ENTITY_DATA_SERIALIZERS.register("int_list", () -> OdysseyDataSerializers.INT_LIST);
    public static final RegistryObject<EntityDataSerializer<AspectStrengthMap>> ASPECT_STRENGTH_MAP = ENTITY_DATA_SERIALIZERS.register("aspect_strength_map", () -> OdysseyDataSerializers.ASPECT_STRENGTH_MAP);
    public static final RegistryObject<EntityDataSerializer<PermabuffHolder>> PERMABUFF_HOLDER = ENTITY_DATA_SERIALIZERS.register("permabuff_holder", () -> OdysseyDataSerializers.PERMABUFF_HOLDER);
    public static final RegistryObject<EntityDataSerializer<FireType>> FIRE_TYPE = ENTITY_DATA_SERIALIZERS.register("fire_type", () -> OdysseyDataSerializers.FIRE_TYPE);
    public static final RegistryObject<EntityDataSerializer<Map<CovenType, Integer>>> COVENTYPE_INT_MAP = ENTITY_DATA_SERIALIZERS.register("coventype_int_map", () -> OdysseyDataSerializers.COVENTYPE_INT_MAP);
}