package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DataSerializerRegistry {

    public static DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS , Odyssey.MOD_ID);

    public static void init() {
        DATA_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<DataSerializerEntry> UUID_LIST = DATA_SERIALIZERS.register("uuid_list", () -> new DataSerializerEntry(OdysseyDataSerializers.UUID_LIST));

}