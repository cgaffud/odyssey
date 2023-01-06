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
    public static final RegistryObject<DataSerializerEntry> FLOAT_LIST = DATA_SERIALIZERS.register("float_list", () -> new DataSerializerEntry(OdysseyDataSerializers.FLOAT_LIST));

}