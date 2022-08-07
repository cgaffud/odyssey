package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.animal.OdysseyPolarBear;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBody;
import com.bedmen.odyssey.entity.boss.MineralLeviathanHead;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.entity.projectile.Boomerang;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.entity.projectile.SonicBoom;
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

    // Monsters

    // - Zombies
    public static final RegistryObject<EntityType<ZombieBrute>> ZOMBIE_BRUTE = ENTITY_TYPE.register("zombie_brute", () -> EntityType.Builder.<ZombieBrute>of(ZombieBrute::new, MobCategory.MONSTER).sized(0.9F, 2.85F).clientTrackingRange(12).build(new ResourceLocation(Odyssey.MOD_ID, "zombie_brute").toString()));
    public static final RegistryObject<EntityType<DungeonZombie>> MOON_TOWER_ZOMBIE = ENTITY_TYPE.register("moon_tower_zombie", () -> EntityType.Builder.<DungeonZombie>of(DungeonZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "moon_tower_zombie").toString()));

    // - Skeletons
    public static final RegistryObject<EntityType<OdysseySkeleton>> SKELETON = ENTITY_TYPE.register("skeleton", () -> EntityType.Builder.<OdysseySkeleton>of(OdysseySkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "skeleton").toString()));
    public static final RegistryObject<EntityType<DungeonSkeleton>> MOON_TOWER_SKELETON = ENTITY_TYPE.register("moon_tower_skeleton", () -> EntityType.Builder.<DungeonSkeleton>of(DungeonSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "moon_tower_skeleton").toString()));

    // - Creepers
    public static final RegistryObject<EntityType<BabyCreeper>> BABY_CREEPER = ENTITY_TYPE.register("baby_creeper", () -> EntityType.Builder.<BabyCreeper>of(BabyCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "baby_creeper").toString()));
    public static final RegistryObject<EntityType<CamoCreeper>> CAMO_CREEPER = ENTITY_TYPE.register("camo_creeper", () -> EntityType.Builder.<CamoCreeper>of(CamoCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "camo_creeper").toString()));

    // - Arthropods
    public static final RegistryObject<EntityType<BarnSpider>> BARN_SPIDER = ENTITY_TYPE.register("barn_spider", () -> EntityType.Builder.<BarnSpider>of(BarnSpider::new, MobCategory.MONSTER).sized(0.7F, 0.5F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "barn_spider").toString()));
    public static final RegistryObject<EntityType<Weaver>> WEAVER = ENTITY_TYPE.register("weaver", () -> EntityType.Builder.<Weaver>of(Weaver::new, MobCategory.MONSTER).sized(1.4F, 0.8F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "weaver").toString()));
    public static final RegistryObject<EntityType<BabyLeviathan>> BABY_LEVIATHAN = ENTITY_TYPE.register("baby_leviathan", () -> EntityType.Builder.<BabyLeviathan>of(BabyLeviathan::new, MobCategory.MONSTER).sized(0.75F, 0.2F).clientTrackingRange(8).build(new ResourceLocation(Odyssey.MOD_ID, "baby_leviathan").toString()));

    // - Wraith
    public static final RegistryObject<EntityType<Wraith>> WRAITH = ENTITY_TYPE.register("wraith", () -> EntityType.Builder.<Wraith>of(Wraith::new, MobCategory.MONSTER).sized(0.55F, 1.85F).clientTrackingRange(12).build(new ResourceLocation(Odyssey.MOD_ID, "wraith").toString()));

    // Animals
    public static final RegistryObject<EntityType<PassiveWeaver>> PASSIVE_WEAVER = ENTITY_TYPE.register("passive_weaver", () -> EntityType.Builder.<PassiveWeaver>of(PassiveWeaver::new, MobCategory.CREATURE).sized(1.4F, 0.8F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "passive_weaver").toString()));
    public static final RegistryObject<EntityType<OdysseyPolarBear>> POLAR_BEAR = ENTITY_TYPE.register("polar_bear", () -> EntityType.Builder.<OdysseyPolarBear>of(OdysseyPolarBear::new, MobCategory.CREATURE).immuneTo(Blocks.POWDER_SNOW).sized(1.4F, 1.4F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "polar_bear").toString()));

    // Bosses
    public static final RegistryObject<EntityType<AbandonedIronGolem>> ABANDONED_IRON_GOLEM = ENTITY_TYPE.register("abandoned_iron_golem", () -> EntityType.Builder.<AbandonedIronGolem>of(AbandonedIronGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(20).build(new ResourceLocation(Odyssey.MOD_ID, "abandoned_iron_golem").toString()));
    public static final RegistryObject<EntityType<MineralLeviathanHead>> MINERAL_LEVIATHAN = ENTITY_TYPE.register("mineral_leviathan", () -> EntityType.Builder.<MineralLeviathanHead>of(MineralLeviathanHead::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation(Odyssey.MOD_ID, "mineral_leviathan").toString()));
    public static final RegistryObject<EntityType<MineralLeviathanBody>> MINERAL_LEVIATHAN_BODY = ENTITY_TYPE.register("mineral_leviathan_body", () -> EntityType.Builder.<MineralLeviathanBody>of(MineralLeviathanBody::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation(Odyssey.MOD_ID, "mineral_leviathan_body").toString()));
//    public static final RegistryObject<EntityType<Permafrost>> PERMAFROST = ENTITY_TYPE.register("permafrost", () -> EntityType.Builder.<Permafrost>of(Permafrost::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation("permafrost").toString()));

    // Projectiles
    public static final RegistryObject<EntityType<Boomerang>> BOOMERANG = ENTITY_TYPE.register("boomerang", () -> EntityType.Builder.<Boomerang>of(Boomerang::new, MobCategory.MISC).sized(1.0f,0.0625f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(Odyssey.MOD_ID, "boomerang").toString()));
    public static final RegistryObject<EntityType<OdysseyArrow>> ARROW = ENTITY_TYPE.register("arrow", () -> EntityType.Builder.<OdysseyArrow>of(OdysseyArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(Odyssey.MOD_ID, "arrow").toString()));
    public static final RegistryObject<EntityType<SonicBoom>> SONIC_BOOM = ENTITY_TYPE.register("sonic_boom", () -> EntityType.Builder.<SonicBoom>of(SonicBoom::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation(Odyssey.MOD_ID, "sonic_boom").toString()));
//    public static final RegistryObject<EntityType<OdysseyTrident>> TRIDENT = ENTITY_TYPE.register("trident", () -> EntityType.Builder.<OdysseyTrident>of(OdysseyTrident::new, MobCategory.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("trident").toString()));
//    public static final RegistryObject<EntityType<PermafrostIcicle>> PERMAFROST_ICICLE = ENTITY_TYPE.register("permafrost_icicle", () -> EntityType.Builder.<PermafrostIcicle>of(PermafrostIcicle::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation("permafrost_icicle").toString()));

    //Boat
    public static final RegistryObject<EntityType<OdysseyBoat>> BOAT = ENTITY_TYPE.register("boat", () -> EntityType.Builder.<OdysseyBoat>of(OdysseyBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "boat").toString()));
}