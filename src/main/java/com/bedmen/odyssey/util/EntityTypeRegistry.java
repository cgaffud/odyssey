package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.TrooperEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegistry {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES , Odyssey.MOD_ID);

    public static void init() {
        ENTITY_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<EntityType<TrooperEntity>> TROOPER = ENTITY_TYPE.register("trooper", () -> EntityType.Builder.<TrooperEntity>create(TrooperEntity::new, EntityClassification.MONSTER).size(0.6f,1.7f).trackingRange(12).build(new ResourceLocation(Odyssey.MOD_ID, "trooper").toString()));

}