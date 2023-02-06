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
    public static final RegistryObject<DataSerializerEntry> PERMABUFF_MAP_SERIALIZER = DATA_SERIALIZERS.register("permabuff_map_serializer", () -> new DataSerializerEntry(OdysseyDataSerializers.PERMABUFF_MAP));
    public static final RegistryObject<DataSerializerEntry> COVENTYPE_INT_MAP_SERIALIZER = DATA_SERIALIZERS.register("coventype_int_map_serializer", () -> new DataSerializerEntry(OdysseyDataSerializers.COVENTYPE_INT_MAP));
    public static final RegistryObject<DataSerializerEntry> COVENTYPE_FLOAT_MAP_SERIALIZER = DATA_SERIALIZERS.register("coventype_float_map_serializer", () -> new DataSerializerEntry(OdysseyDataSerializers.COVENTYPE_FLOAT_MAP));

}