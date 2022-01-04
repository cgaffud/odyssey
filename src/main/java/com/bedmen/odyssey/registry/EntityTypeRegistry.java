package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBody;
import com.bedmen.odyssey.entity.boss.MineralLeviathanHead;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.entity.projectile.Boomerang;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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
    public static final RegistryObject<EntityType<BabySkeleton>> BABY_SKELETON = ENTITY_TYPE.register("baby_skeleton", () -> EntityType.Builder.<BabySkeleton>of(BabySkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(new ResourceLocation("baby_skeleton").toString()));
    public static final RegistryObject<EntityType<BabyCreeper>> BABY_CREEPER = ENTITY_TYPE.register("baby_creeper", () -> EntityType.Builder.<BabyCreeper>of(BabyCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build(new ResourceLocation("baby_creeper").toString()));
    public static final RegistryObject<EntityType<CamoCreeper>> CAMO_CREEPER = ENTITY_TYPE.register("camo_creeper", () -> EntityType.Builder.<CamoCreeper>of(CamoCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).build(new ResourceLocation("camo_creeper").toString()));
    public static final RegistryObject<EntityType<Weaver>> WEAVER = ENTITY_TYPE.register("weaver", () -> EntityType.Builder.<Weaver>of(Weaver::new, MobCategory.MONSTER).sized(1.4F, 0.8F).clientTrackingRange(10).build(new ResourceLocation("weaver").toString()));
    public static final RegistryObject<EntityType<PassiveWeaver>> PASSIVE_WEAVER = ENTITY_TYPE.register("passive_weaver", () -> EntityType.Builder.<PassiveWeaver>of(PassiveWeaver::new, MobCategory.CREATURE).sized(1.4F, 0.8F).clientTrackingRange(10).build(new ResourceLocation("passive_weaver").toString()));
    public static final RegistryObject<EntityType<BabyLeviathan>> BABY_LEVIATHAN = ENTITY_TYPE.register("baby_leviathan", () -> EntityType.Builder.<BabyLeviathan>of(BabyLeviathan::new, MobCategory.MONSTER).sized(0.75F, 0.2F).clientTrackingRange(8).build(new ResourceLocation("baby_leviathan").toString()));

    //Bosses
//    public static final RegistryObject<EntityType<AbandonedIronGolem>> ABANDONED_IRON_GOLEM = ENTITY_TYPE.register("abandoned_iron_golem", () -> EntityType.Builder.<AbandonedIronGolem>of(AbandonedIronGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(20).build(new ResourceLocation("abandoned_iron_golem").toString()));
    public static final RegistryObject<EntityType<MineralLeviathanHead>> MINERAL_LEVIATHAN = ENTITY_TYPE.register("mineral_leviathan", () -> EntityType.Builder.<MineralLeviathanHead>of(MineralLeviathanHead::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation("mineral_leviathan").toString()));
    public static final RegistryObject<EntityType<MineralLeviathanBody>> MINERAL_LEVIATHAN_BODY = ENTITY_TYPE.register("mineral_leviathan_body", () -> EntityType.Builder.<MineralLeviathanBody>of(MineralLeviathanBody::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation("mineral_leviathan_body").toString()));
//    public static final RegistryObject<EntityType<Permafrost>> PERMAFROST = ENTITY_TYPE.register("permafrost", () -> EntityType.Builder.<Permafrost>of(Permafrost::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(20).build(new ResourceLocation("permafrost").toString()));

    //Projectiles
    public static final RegistryObject<EntityType<Boomerang>> BOOMERANG = ENTITY_TYPE.register("boomerang", () -> EntityType.Builder.<Boomerang>of(Boomerang::new, MobCategory.MISC).sized(1.0f,0.0625f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("boomerang").toString()));
    public static final RegistryObject<EntityType<OdysseyArrow>> ARROW = ENTITY_TYPE.register("arrow", () -> EntityType.Builder.<OdysseyArrow>of(OdysseyArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("arrow").toString()));
//    public static final RegistryObject<EntityType<OdysseyTrident>> TRIDENT = ENTITY_TYPE.register("trident", () -> EntityType.Builder.<OdysseyTrident>of(OdysseyTrident::new, MobCategory.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation("trident").toString()));
//    public static final RegistryObject<EntityType<PermafrostIcicle>> PERMAFROST_ICICLE = ENTITY_TYPE.register("permafrost_icicle", () -> EntityType.Builder.<PermafrostIcicle>of(PermafrostIcicle::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation("permafrost_icicle").toString()));

    //Boat
    public static final RegistryObject<EntityType<OdysseyBoat>> BOAT = ENTITY_TYPE.register("boat", () -> EntityType.Builder.<OdysseyBoat>of(OdysseyBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(new ResourceLocation("boat").toString()));
}