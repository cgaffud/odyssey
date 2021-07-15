package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.PermafrostEntity;
import com.bedmen.odyssey.entity.monster.ArctihornEntity;
import com.bedmen.odyssey.entity.monster.WerewolfEntity;
import com.bedmen.odyssey.entity.projectile.NewTridentEntity;
import com.bedmen.odyssey.entity.projectile.PermafrostIcicleEntity;
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

    //Mobs
    public static final RegistryObject<EntityType<WerewolfEntity>> WEREWOLF = ENTITY_TYPE.register("werewolf", () -> EntityType.Builder.<WerewolfEntity>of(WerewolfEntity::new, ModBiomeEntitySpawn.WEREWOLF).sized(0.6F, 0.85F).clientTrackingRange(10).build(new ResourceLocation("werewolf").toString()));
    public static final RegistryObject<EntityType<ArctihornEntity>> ARCTIHORN = ENTITY_TYPE.register("arctihorn", () -> EntityType.Builder.<ArctihornEntity>of(ArctihornEntity::new, EntityClassification.MONSTER).sized(0.8F, 0.8F).clientTrackingRange(12).build(new ResourceLocation("arctihorn").toString()));

    //Bosses
    public static final RegistryObject<EntityType<PermafrostEntity>> PERMAFROST = ENTITY_TYPE.register("permafrost", () -> EntityType.Builder.<PermafrostEntity>of(PermafrostEntity::new, EntityClassification.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(10).build(new ResourceLocation("permafrost").toString()));

    //Projectiles
    public static final RegistryObject<EntityType<NewTridentEntity>> NEW_TRIDENT = ENTITY_TYPE.register("new_trident", () -> EntityType.Builder.<NewTridentEntity>of(NewTridentEntity::new, EntityClassification.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("new_trident").toString()));
    public static final RegistryObject<EntityType<SerpentTridentEntity>> SERPENT_TRIDENT = ENTITY_TYPE.register("serpent_trident", () -> EntityType.Builder.<SerpentTridentEntity>of(SerpentTridentEntity::new, EntityClassification.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("serpent_trident").toString()));
    public static final RegistryObject<EntityType<PermafrostIcicleEntity>> PERMAFROST_ICICLE = ENTITY_TYPE.register("permafrost_icicle", () -> EntityType.Builder.<PermafrostIcicleEntity>of(PermafrostIcicleEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation("permafrost_icicle").toString()));

}