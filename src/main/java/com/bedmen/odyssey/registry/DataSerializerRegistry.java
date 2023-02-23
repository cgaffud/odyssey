package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DataSerializerRegistry {

    public static DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.DATA_SERIALIZERS, Odyssey.MOD_ID);

    public static void init() {
        DATA_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<DataSerializerEntry> INT_LIST = DATA_SERIALIZERS.register("int_list", () -> new DataSerializerEntry(OdysseyDataSerializers.INT_LIST));
    public static final RegistryObject<DataSerializerEntry> ASPECT_STRENGTH_MAP = DATA_SERIALIZERS.register("aspect_strength_map", () -> new DataSerializerEntry(OdysseyDataSerializers.ASPECT_STRENGTH_MAP));
    public static final RegistryObject<DataSerializerEntry> PERMABUFF_HOLDER = DATA_SERIALIZERS.register("permabuff_holder", () -> new DataSerializerEntry(OdysseyDataSerializers.PERMABUFF_HOLDER));
    public static final RegistryObject<DataSerializerEntry> FIRE_TYPE = DATA_SERIALIZERS.register("fire_type", () -> new DataSerializerEntry(OdysseyDataSerializers.FIRE_TYPE));
    public static final RegistryObject<DataSerializerEntry> COVENTYPE_INT_MAP = DATA_SERIALIZERS.register("coventype_int_map", () -> new DataSerializerEntry(OdysseyDataSerializers.COVENTYPE_INT_MAP));
}