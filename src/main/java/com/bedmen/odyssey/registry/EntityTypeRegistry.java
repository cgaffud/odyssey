package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.animal.OdysseyPolarBear;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.bedmen.odyssey.entity.boss.coven.*;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.*;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.entity.projectile.*;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeRegistry {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES , Odyssey.MOD_ID);

    public static void init() {
        ENTITY_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Mobs
//    public static final RegistryObject<EntityType<Lupine>> LUPINE = ENTITY_TYPE.register("lupine", () -> EntityType.Builder.<Lupine>of(Lupine::new, OdysseyBiomeEntitySpawn.HARD_BIOME).sized(0.6F, 0.85F).clientTrackingRange(10).build(new ResourceLocation("lupine").toString()));
//    public static final RegistryObject<EntityType<Arctihorn>> ARCTIHORN = ENTITY_TYPE.register("arctihorn", () -> EntityType.Builder.<Arctihorn>of(Arctihorn::new, MobCategory.MONSTER).sized(0.8F, 0.8F).clientTrackingRange(12).build(new ResourceLocation("arctihorn").toString()));

    // # Monsters

    // ## Zombies
    public static final RegistryObject<EntityType<ZombieBrute>> ZOMBIE_BRUTE = ENTITY_TYPE.register("zombie_brute", () -> EntityType.Builder.of(ZombieBrute::new, MobCategory.MONSTER).sized(0.9F, 2.85F).clientTrackingRange(12).build(new ResourceLocation(Odyssey.MOD_ID, "zombie_brute").toString()));
    public static final RegistryObject<EntityType<DungeonZombie>> MOON_TOWER_ZOMBIE = ENTITY_TYPE.register("moon_tower_zombie", () -> EntityType.Builder.<DungeonZombie>of(DungeonZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "moon_tower_zombie").toString()));
    public static final RegistryObject<EntityType<Forgotten>> FORGOTTEN = ENTITY_TYPE.register("forgotten", () -> EntityType.Builder.of(Forgotten::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "forgotten").toString()));
    public static final RegistryObject<EntityType<EncasedZombie>> ENCASED_ZOMBIE = ENTITY_TYPE.register("encased_zombie", () -> EntityType.Builder.of(EncasedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "encased_zombie").toString()));

    // ## Skeletons
    public static final RegistryObject<EntityType<OdysseySkeleton>> SKELETON = ENTITY_TYPE.register("skeleton", () -> EntityType.Builder.of(OdysseySkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "skeleton").toString()));
    public static final RegistryObject<EntityType<DungeonSkeleton>> MOON_TOWER_SKELETON = ENTITY_TYPE.register("moon_tower_skeleton", () -> EntityType.Builder.<DungeonSkeleton>of(DungeonSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "moon_tower_skeleton").toString()));
    public static final RegistryObject<EntityType<EncasedSkeleton>> ENCASED_SKELETON = ENTITY_TYPE.register("encased_skeleton", () -> EntityType.Builder.of(EncasedSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "encased_skeleton").toString()));

    // ## Creepers
    public static final RegistryObject<EntityType<BabyCreeper>> BABY_CREEPER = ENTITY_TYPE.register("baby_creeper", () -> EntityType.Builder.of(BabyCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "baby_creeper").toString()));
    public static final RegistryObject<EntityType<CamoCreeper>> CAMO_CREEPER = ENTITY_TYPE.register("camo_creeper", () -> EntityType.Builder.of(CamoCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "camo_creeper").toString()));
    public static final RegistryObject<EntityType<DripstoneCreeper>> DRIPSTONE_CREEPER = ENTITY_TYPE.register("dripstone_creeper", () -> EntityType.Builder.of(DripstoneCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "dripstone_creeper").toString()));


    // ## Arthropods
    public static final RegistryObject<EntityType<BarnSpider>> BARN_SPIDER = ENTITY_TYPE.register("barn_spider", () -> EntityType.Builder.of(BarnSpider::new, MobCategory.MONSTER).sized(0.7F, 0.5F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "barn_spider").toString()));
    public static final RegistryObject<EntityType<Weaver>> WEAVER = ENTITY_TYPE.register("weaver", () -> EntityType.Builder.of(Weaver::new, MobCategory.MONSTER).sized(1.4F, 0.8F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "weaver").toString()));
    public static final RegistryObject<EntityType<BabyLeviathan>> BABY_LEVIATHAN = ENTITY_TYPE.register("baby_leviathan", () -> EntityType.Builder.of(BabyLeviathan::new, MobCategory.MONSTER).sized(0.75F, 0.2F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "baby_leviathan").toString()));
    public static final RegistryObject<EntityType<BladeSpider>> BLADE_SPIDER = ENTITY_TYPE.register("blade_spider", () -> EntityType.Builder.of(BladeSpider::new, MobCategory.MONSTER).sized(1.7F, 1.3F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "blade_spider").toString()));


    // ## Wraith
    public static final RegistryObject<EntityType<Wraith>> WRAITH = ENTITY_TYPE.register("wraith", () -> EntityType.Builder.of(Wraith::new, MobCategory.MONSTER).sized(0.55F, 1.85F).clientTrackingRange(12).build(new ResourceLocation(Odyssey.MOD_ID, "wraith").toString()));
    public static final RegistryObject<EntityType<WraithStalker>> WRAITH_STALKER = ENTITY_TYPE.register("wraith_stalker", () -> EntityType.Builder.of(WraithStalker::new, MobCategory.MONSTER).sized(0.6F, 2.9F).clientTrackingRange(12).build(new ResourceLocation(Odyssey.MOD_ID, "wraith_stalker").toString()));
    public static final RegistryObject<EntityType<WraithAmalgam>> WRAITH_AMALGAM = ENTITY_TYPE.register("wraith_amalgam", () -> EntityType.Builder.of(WraithAmalgam::new, MobCategory.MONSTER).sized(0.8F, 2.3F).clientTrackingRange(12).build(new ResourceLocation(Odyssey.MOD_ID, "wraith_amalgam").toString()));

    // ## Illager
    public static final RegistryObject<EntityType<Bandit>> BANDIT = ENTITY_TYPE.register("bandit", () -> EntityType.Builder.of(Bandit::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "bandit").toString()));

    // # Animals
    public static final RegistryObject<EntityType<PassiveWeaver>> PASSIVE_WEAVER = ENTITY_TYPE.register("passive_weaver", () -> EntityType.Builder.of(PassiveWeaver::new, MobCategory.CREATURE).sized(1.4F, 0.8F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "passive_weaver").toString()));
    public static final RegistryObject<EntityType<OdysseyPolarBear>> POLAR_BEAR = ENTITY_TYPE.register("polar_bear", () -> EntityType.Builder.of(OdysseyPolarBear::new, MobCategory.CREATURE).immuneTo(Blocks.POWDER_SNOW).sized(1.4F, 1.4F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "polar_bear").toString()));

    // # Bosses
    public static final RegistryObject<EntityType<AbandonedIronGolem>> ABANDONED_IRON_GOLEM = ENTITY_TYPE.register("abandoned_iron_golem", () -> EntityType.Builder.of(AbandonedIronGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(20).build(new ResourceLocation(Odyssey.MOD_ID, "abandoned_iron_golem").toString()));

    // ## Mineral Leviathan
    public static final RegistryObject<EntityType<MineralLeviathanMaster>> MINERAL_LEVIATHAN_MASTER = ENTITY_TYPE.register("mineral_leviathan_master", () -> EntityType.Builder.<MineralLeviathanMaster>of(MineralLeviathanMaster::new, MobCategory.MONSTER).sized(0.0F, 0.0F).fireImmune().clientTrackingRange(32).build(new ResourceLocation(Odyssey.MOD_ID, "mineral_leviathan_master").toString()));
    public static final RegistryObject<EntityType<MineralLeviathanHead>> MINERAL_LEVIATHAN_HEAD = ENTITY_TYPE.register("mineral_leviathan_head", () -> EntityType.Builder.<MineralLeviathanHead>of(MineralLeviathanHead::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(32).build(new ResourceLocation(Odyssey.MOD_ID, "mineral_leviathan_head").toString()));
    public static final RegistryObject<EntityType<MineralLeviathanBody>> MINERAL_LEVIATHAN_BODY = ENTITY_TYPE.register("mineral_leviathan_body", () -> EntityType.Builder.<MineralLeviathanBody>of(MineralLeviathanBody::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(32).build(new ResourceLocation(Odyssey.MOD_ID, "mineral_leviathan_body").toString()));

    // ## Coven Witch
    public static final RegistryObject<EntityType<CovenMaster>> COVEN_MASTER = ENTITY_TYPE.register("coven_master", () -> EntityType.Builder.<CovenMaster>of(CovenMaster::new, MobCategory.MONSTER).sized(0.0F, 0.0F).fireImmune().clientTrackingRange(32).build(new ResourceLocation(Odyssey.MOD_ID, "coven_master").toString()));
    public static final RegistryObject<EntityType<EnderWitch>> ENDER_WITCH = ENTITY_TYPE.register("ender_witch", () -> EntityType.Builder.<EnderWitch>of(EnderWitch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).fireImmune().clientTrackingRange(32).build(new ResourceLocation(Odyssey.MOD_ID, "ender_witch").toString()));
    public static final RegistryObject<EntityType<NetherWitch>> NETHER_WITCH = ENTITY_TYPE.register("nether_witch", () -> EntityType.Builder.<NetherWitch>of(NetherWitch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).fireImmune().clientTrackingRange(32).build(new ResourceLocation(Odyssey.MOD_ID, "nether_witch").toString()));
    public static final RegistryObject<EntityType<OverworldWitch>> OVERWORLD_WITCH = ENTITY_TYPE.register("overworld_witch", () -> EntityType.Builder.<OverworldWitch>of(OverworldWitch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).fireImmune().clientTrackingRange(32).build(new ResourceLocation(Odyssey.MOD_ID, "overworld_witch").toString()));
    public static final RegistryObject<EntityType<CovenRootEntity>> COVEN_ROOT_ENTITY = ENTITY_TYPE.register("coven_root_entity", () -> EntityType.Builder.<CovenRootEntity>of(CovenRootEntity::new, MobCategory.MISC).sized(0.5F, 0.8F).clientTrackingRange(6).updateInterval(2).build(new ResourceLocation(Odyssey.MOD_ID, "coven_root_entity").toString()));

    //    public static final RegistryObject<EntityType<Permafrost>> PERMAFROST = ENTITY_TYPE.register("permafrost", () -> EntityType.Builder.<Permafrost>of(Permafrost::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation("permafrost").toString()));

    // # Projectiles
    public static final RegistryObject<EntityType<Boomerang>> BOOMERANG = ENTITY_TYPE.register("boomerang", () -> EntityType.Builder.<Boomerang>of(Boomerang::new, MobCategory.MISC).sized(1.0f,0.0625f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(Odyssey.MOD_ID, "boomerang").toString()));
    public static final RegistryObject<EntityType<OdysseyArrow>> ARROW = ENTITY_TYPE.register("arrow", () -> EntityType.Builder.<OdysseyArrow>of(OdysseyArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(Odyssey.MOD_ID, "arrow").toString()));
    public static final RegistryObject<EntityType<SonicBoom>> SONIC_BOOM = ENTITY_TYPE.register("sonic_boom", () -> EntityType.Builder.<SonicBoom>of(SonicBoom::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation(Odyssey.MOD_ID, "sonic_boom").toString()));
    public static final RegistryObject<EntityType<ThrownSpear>> THROWN_SPEAR = ENTITY_TYPE.register("thrown_spear", () -> EntityType.Builder.<ThrownSpear>of(ThrownSpear::new, MobCategory.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(Odyssey.MOD_ID, "thrown_spear").toString()));
    public static final RegistryObject<EntityType<WraithAmalgamProjectile>> WRAITH_AMALGAM_PROJECTILE = ENTITY_TYPE.register("wraith_amalgam_projectile", () -> EntityType.Builder.<WraithAmalgamProjectile>of(WraithAmalgamProjectile::new, MobCategory.MISC).sized(0.4f,0.4f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(Odyssey.MOD_ID, "wraith_amalgam_projectile").toString()));
    public static final RegistryObject<EntityType<DripstoneShard>> DRIPSTONE_SHARD = ENTITY_TYPE.register("dripstone_shard", () -> EntityType.Builder.<DripstoneShard>of(DripstoneShard::new, MobCategory.MISC).sized(0.25F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(Odyssey.MOD_ID, "dripstone_shard").toString()));

    //    public static final RegistryObject<EntityType<OdysseyTrident>> TRIDENT = ENTITY_TYPE.register("trident", () -> EntityType.Builder.<OdysseyTrident>of(OdysseyTrident::new, MobCategory.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("trident").toString()));
//    public static final RegistryObject<EntityType<PermafrostIcicle>> PERMAFROST_ICICLE = ENTITY_TYPE.register("permafrost_icicle", () -> EntityType.Builder.<PermafrostIcicle>of(PermafrostIcicle::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation("permafrost_icicle").toString()));

    // # Boat
    public static final RegistryObject<EntityType<OdysseyBoat>> BOAT = ENTITY_TYPE.register("boat", () -> EntityType.Builder.<OdysseyBoat>of(OdysseyBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "boat").toString()));
}