package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.WerewolfEntity;
import com.bedmen.odyssey.entity.projectile.NewTridentEntity;
import com.bedmen.odyssey.entity.projectile.SerpentTridentEntity;
import com.bedmen.odyssey.world.spawn.ModBiomeEntitySpawn;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegistry {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES , Odyssey.MOD_ID);
    public static DeferredRegister<EntityType<?>> ENTITY_TYPE_VANILLA = DeferredRegister.create(ForgeRegistries.ENTITIES , "minecraft");

    public static void init() {
        ENTITY_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITY_TYPE_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<EntityType<WerewolfEntity>> WEREWOLF = ENTITY_TYPE.register("werewolf", () -> EntityType.Builder.<WerewolfEntity>create(WerewolfEntity::new, ModBiomeEntitySpawn.WEREWOLF).size(0.6F, 0.85F).trackingRange(10).build(new ResourceLocation("werewolf").toString()));
    public static final RegistryObject<EntityType<NewTridentEntity>> NEW_TRIDENT = ENTITY_TYPE.register("new_trident", () -> EntityType.Builder.<NewTridentEntity>create(NewTridentEntity::new, EntityClassification.MISC).size(0.5f,0.5f).trackingRange(4).func_233608_b_(20).build(new ResourceLocation("new_trident").toString()));
    public static final RegistryObject<EntityType<SerpentTridentEntity>> SERPENT_TRIDENT = ENTITY_TYPE.register("serpent_trident", () -> EntityType.Builder.<SerpentTridentEntity>create(SerpentTridentEntity::new, EntityClassification.MISC).size(0.5f,0.5f).trackingRange(4).func_233608_b_(20).build(new ResourceLocation("serpent_trident").toString()));

}