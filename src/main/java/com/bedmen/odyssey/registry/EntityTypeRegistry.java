package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBodyEntity;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.bedmen.odyssey.entity.boss.PermafrostEntity;
import com.bedmen.odyssey.entity.item.OdysseyBoatEntity;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.entity.projectile.BoomerangEntity;
import com.bedmen.odyssey.entity.projectile.OdysseyArrowEntity;
import com.bedmen.odyssey.entity.projectile.OdysseyTridentEntity;
import com.bedmen.odyssey.entity.projectile.PermafrostIcicleEntity;
import com.bedmen.odyssey.world.spawn.OdysseyBiomeEntitySpawn;
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
    public static final RegistryObject<EntityType<LupineEntity>> LUPINE = ENTITY_TYPE.register("lupine", () -> EntityType.Builder.<LupineEntity>of(LupineEntity::new, OdysseyBiomeEntitySpawn.HARD_BIOME).sized(0.6F, 0.85F).clientTrackingRange(10).build(new ResourceLocation("lupine").toString()));
    public static final RegistryObject<EntityType<ArctihornEntity>> ARCTIHORN = ENTITY_TYPE.register("arctihorn", () -> EntityType.Builder.<ArctihornEntity>of(ArctihornEntity::new, EntityClassification.MONSTER).sized(0.8F, 0.8F).clientTrackingRange(12).build(new ResourceLocation("arctihorn").toString()));
    public static final RegistryObject<EntityType<BabySkeletonEntity>> BABY_SKELETON = ENTITY_TYPE.register("baby_skeleton", () -> EntityType.Builder.<BabySkeletonEntity>of(BabySkeletonEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation("baby_skeleton").toString()));
    public static final RegistryObject<EntityType<WeaverEntity>> WEAVER = ENTITY_TYPE.register("weaver", () -> EntityType.Builder.<WeaverEntity>of(WeaverEntity::new, EntityClassification.MONSTER).sized(1.4F, 0.8F).clientTrackingRange(10).build(new ResourceLocation("weaver").toString()));

    //Bosses
    public static final RegistryObject<EntityType<MineralLeviathanEntity>> MINERAL_LEVIATHAN = ENTITY_TYPE.register("mineral_leviathan", () -> EntityType.Builder.<MineralLeviathanEntity>of(MineralLeviathanEntity::new, EntityClassification.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation("mineral_leviathan").toString()));
    public static final RegistryObject<EntityType<MineralLeviathanBodyEntity>> MINERAL_LEVIATHAN_BODY = ENTITY_TYPE.register("mineral_leviathan_body", () -> EntityType.Builder.<MineralLeviathanBodyEntity>of(MineralLeviathanBodyEntity::new, EntityClassification.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation("mineral_leviathan_body").toString()));
    public static final RegistryObject<EntityType<PermafrostEntity>> PERMAFROST = ENTITY_TYPE.register("permafrost", () -> EntityType.Builder.<PermafrostEntity>of(PermafrostEntity::new, EntityClassification.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(10).build(new ResourceLocation("permafrost").toString()));

    //Projectiles
    public static final RegistryObject<EntityType<OdysseyTridentEntity>> TRIDENT = ENTITY_TYPE.register("trident", () -> EntityType.Builder.<OdysseyTridentEntity>of(OdysseyTridentEntity::new, EntityClassification.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("trident").toString()));
    public static final RegistryObject<EntityType<PermafrostIcicleEntity>> PERMAFROST_ICICLE = ENTITY_TYPE.register("permafrost_icicle", () -> EntityType.Builder.<PermafrostIcicleEntity>of(PermafrostIcicleEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation("permafrost_icicle").toString()));
    public static final RegistryObject<EntityType<OdysseyArrowEntity>> ARROW = ENTITY_TYPE.register("arrow", () -> EntityType.Builder.<OdysseyArrowEntity>of(OdysseyArrowEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("arrow").toString()));
    public static final RegistryObject<EntityType<BoomerangEntity>> BOOMERANG = ENTITY_TYPE.register("boomerang", () -> EntityType.Builder.<BoomerangEntity>of(BoomerangEntity::new, EntityClassification.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("boomerang").toString()));

    //Boat
    public static final RegistryObject<EntityType<OdysseyBoatEntity>> BOAT = ENTITY_TYPE.register("boat", () -> EntityType.Builder.<OdysseyBoatEntity>of(OdysseyBoatEntity::new, EntityClassification.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(new ResourceLocation("boat").toString()));

    //Vanilla
    public static final RegistryObject<EntityType<OdysseySkeletonEntity>> SKELETON = ENTITY_TYPE_VANILLA.register("skeleton", () -> EntityType.Builder.<OdysseySkeletonEntity>of(OdysseySkeletonEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation("skeleton").toString()));

}