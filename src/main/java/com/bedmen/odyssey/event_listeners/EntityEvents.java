package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.items.odyssey_versions.AspectArmorItem;
import com.bedmen.odyssey.items.odyssey_versions.AspectShieldItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.FatalHitAnimatePacket;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.combat.SmackPush;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    @SubscribeEvent
    public static void onLivingUpdateEvent(final LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            // Perform Smack
            if(odysseyLivingEntity.getSmackPush().shouldPush){
                WeaponUtil.smackTarget(odysseyLivingEntity.getSmackPush());
                odysseyLivingEntity.setSmackPush(new SmackPush());
            }
        }

        if (!livingEntity.level.isClientSide && livingEntity.isAlive()) {
            int bloodLossStrength = AspectUtil.getIntegerAspectStrengthAllSlots(livingEntity, Aspects.BLOOD_LOSS);
            int weightStrength = AspectUtil.getIntegerAspectStrengthAllSlots(livingEntity, Aspects.WEIGHT);
            int oxygenDeprivationStrength = AspectUtil.getIntegerAspectStrengthAllSlots(livingEntity, Aspects.OXYGEN_DEPRIVATION);

            if (bloodLossStrength > 0)
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.BLEEDING.get(), 2,
                        bloodLossStrength-1,false, false, false));
            if (weightStrength > 0)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, weightStrength-1
                ,false, false, false));
            if (oxygenDeprivationStrength > 0)
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.DROWNING.get(), 2, oxygenDeprivationStrength-1
                        ,false,false,false));
        }

        // Frost Walker
        BlockPos blockPos = livingEntity.blockPosition();
        if(AspectUtil.hasBooleanAspectOnArmor(livingEntity, Aspects.FROST_WALKER) && !blockPos.equals(livingEntity.lastPos)){
            FrostWalkerEnchantment.onEntityMoved(livingEntity, livingEntity.level, blockPos, 1);
        }
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(final LivingHurtEvent event){
        float amount = event.getAmount();
        LivingEntity hurtLivingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        Entity damageSourceEntity = damageSource.getDirectEntity();

        if (damageSourceEntity instanceof LivingEntity damageSourceLivingEntity) {
            ItemStack mainHandItemStack = damageSourceLivingEntity.getMainHandItem();
            // Smite, Bane of Arthropods, Hydro Damage
            amount += AspectUtil.getTargetConditionalAspectStrength(mainHandItemStack, hurtLivingEntity);
            // Poison Damage
            int poisonStrength = AspectUtil.getIntegerAspectStrength(mainHandItemStack, Aspects.POISON_DAMAGE);
            if(poisonStrength > 0){
                hurtLivingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + (int)(12 * poisonStrength), 1));
            }
            // Cobweb Chance
            float cobwebChance = AspectUtil.getFloatAspectStrength(mainHandItemStack, Aspects.COBWEB_CHANCE);
            if(cobwebChance > damageSourceLivingEntity.getRandom().nextFloat()){
                BlockPos blockPos = new BlockPos(hurtLivingEntity.getPosition(1f));
                if (hurtLivingEntity.level.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    hurtLivingEntity.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
                }
            }
            // Melee Larceny
            float larcenyChance = AspectUtil.getFloatAspectStrength(mainHandItemStack, Aspects.LARCENY_CHANCE);
            WeaponUtil.tryLarceny(larcenyChance, damageSourceLivingEntity, hurtLivingEntity);

            // Melee Knockback
            if(hurtLivingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
                odysseyLivingEntity.setNextKnockbackAspect(AspectUtil.getFloatAspectStrength(mainHandItemStack, Aspects.KNOCKBACK));
            }

            // Thorns
            float thornsStrength = AspectUtil.getFloatAspectValueFromArmor(hurtLivingEntity, Aspects.THORNS);
            if(thornsStrength > 0.0f && 0.25f >= hurtLivingEntity.getRandom().nextFloat()){
                damageSourceLivingEntity.hurt(DamageSource.thorns(hurtLivingEntity), thornsStrength);
            }

        } else if (damageSourceEntity instanceof OdysseyAbstractArrow odysseyAbstractArrow && hurtLivingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            // Ranged Knockback
            odysseyLivingEntity.setNextKnockbackAspect(odysseyAbstractArrow.getAspectStrength(Aspects.PROJECTILE_KNOCKBACK));
            // Ranged Larceny
            WeaponUtil.tryLarceny(odysseyAbstractArrow.getAspectStrength(Aspects.PROJECTILE_LARCENY_CHANCE), odysseyAbstractArrow.getOwner(), hurtLivingEntity);
        }

        // Break Coconut
        if(amount >= 10.0f && hurtLivingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemRegistry.HOLLOW_COCONUT.get() && damageSource != DamageSource.FALL){
            Consumer<LivingEntity> consumer = (p_233653_0_) -> {
                p_233653_0_.broadcastBreakEvent(EquipmentSlot.HEAD);
            };
            ItemStack itemStack = hurtLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
            Item item = itemStack.getItem();
            item.damageItem(itemStack, 100, hurtLivingEntity, consumer);
            consumer.accept(hurtLivingEntity);
            itemStack.shrink(1);
            if(hurtLivingEntity instanceof Player player){
                player.awardStat(Stats.ITEM_BROKEN.get(item));
            }
        }
        event.setAmount(amount);
    }

    @SubscribeEvent
    public static void onLivingDamageEvent(final LivingDamageEvent event){
        float amount = event.getAmount();
        LivingEntity hurtLivingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        Entity damageSourceEntity = damageSource.getEntity();
        if (damageSourceEntity instanceof LivingEntity damageSourceLivingEntity) {
            ItemStack mainHandItemStack = damageSourceLivingEntity.getMainHandItem();
            float fatalDamage = AspectUtil.getFloatAspectStrength(mainHandItemStack, Aspects.FATAL_HIT);
            float currentHealth = hurtLivingEntity.getHealth();
            float newHealth = currentHealth - amount;
            if(newHealth > 0.0f && newHealth < fatalDamage){
                float deathChance = (fatalDamage - newHealth) / fatalDamage;
                if(deathChance > hurtLivingEntity.getRandom().nextFloat()) {
                    event.setAmount(currentHealth);
                    OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> hurtLivingEntity), new FatalHitAnimatePacket(hurtLivingEntity));
                }
            }
        }
    }

    public static final Set<Integer> IDS = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(final EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide){
            Entity entity = event.getEntity();
            if(!event.loadedFromDisk()){
                if(entity.getType() == EntityType.SKELETON){
                    IDS.add(entity.getId());
                }
            }

            //Todo lifefruits
            //Update Max Health Attribute for Player
//            if(entity instanceof IOdysseyPlayer){
//                IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)entity;
//                Player playerEntity = (Player)entity;
//                PacketDistributor.PacketTarget packetTarget = PacketDistributor.PLAYER.with(() -> (ServerPlayer) entity);
//                int lifeFruits = playerPermanentBuffs.getLifeFruits();
//                //Sync Client
//                OdysseyNetwork.CHANNEL.send(packetTarget, new PermanentBuffsPacket(lifeFruits));
//                //Increase max health
//                AttributeInstance modifiableattributeinstance = playerEntity.getAttributes().getInstance(Attributes.MAX_HEALTH);
//                if (modifiableattributeinstance != null) {
//                    modifiableattributeinstance.setBaseValue(20.0d + 2.0d*lifeFruits);
//                }
//            }
        }
    }

    private interface EntityReplacementFunction {
        Optional<EntityType<?>> call(Mob mob, Random random);
    }

    private static Map<EntityType<?>, EntityReplacementFunction> ENTITY_REPLACEMENT_MAP;

    public static void initEntityMap(){
        ENTITY_REPLACEMENT_MAP = Map.of(
                EntityType.SKELETON, EntityEvents::skeletonReplace,
                EntityType.CREEPER, EntityEvents::creeperReplace,
                EntityType.ZOMBIE, EntityEvents::zombieReplace,
                EntityType.SPIDER, EntityEvents::spiderReplace,
                EntityType.DROWNED, EntityEvents::zombieReplace,
                EntityType.POLAR_BEAR, EntityEvents::polarBearReplace);
    }

    @SubscribeEvent
    public static void onLivingSpawnEvent$CheckSpawn(final LivingSpawnEvent.CheckSpawn event){
        Entity entity = event.getEntity();
        Random random = entity.level.random;
        EntityType<?> entityType = entity.getType();
        MobSpawnType mobSpawnType = event.getSpawnReason();

        if(mobSpawnType == MobSpawnType.SPAWNER){
            return;
        }

        if(ENTITY_REPLACEMENT_MAP.containsKey(entityType) && entity instanceof Mob mob){
            EntityReplacementFunction entityReplacementFunction = ENTITY_REPLACEMENT_MAP.get(entityType);
            Optional<EntityType<?>> oEntityType = entityReplacementFunction.call(mob, random);
            if(oEntityType.isPresent()){
                EntityType<?> entityType1 = oEntityType.get();
                if(entityType1 != entityType){
                    entityType1.spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), mobSpawnType, true, true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    private static Optional<EntityType<?>> skeletonReplace(Mob mob, Random random){
        return Optional.of(EntityTypeRegistry.SKELETON.get());
    }

    private static Optional<EntityType<?>> creeperReplace(Mob mob, Random random){
        if(isCamo(random, mob.level.getDifficulty())){
            return Optional.of(EntityTypeRegistry.CAMO_CREEPER.get());
        }
        else if(isBaby(mob)){
            return Optional.of(EntityTypeRegistry.BABY_CREEPER.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> zombieReplace(Mob mob, Random random){
        if(inPrairieBiome(mob)){
            mob.setBaby(true);
            return Optional.empty();
        }
        if(random.nextFloat() < 0.05f){
            return Optional.of(EntityTypeRegistry.ZOMBIE_BRUTE.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> spiderReplace(Mob mob, Random random){
        if(inPrairieBiome(mob)){
            return Optional.of(EntityTypeRegistry.BARN_SPIDER.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> polarBearReplace(Mob mob, Random random){
        return Optional.of(EntityTypeRegistry.POLAR_BEAR.get());
    }

    public static boolean isBaby(Entity entity){
        return inPrairieBiome(entity) || entity.level.random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get();
    }

    public static boolean inPrairieBiome(Entity entity){
        return entity.level.getBiome(entity.blockPosition()).is(BiomeRegistry.PRAIRIE_RESOURCE_KEY);
    }

    public static final Map<Difficulty, Float> DIFFICULTY_MAP = Map.of(
            Difficulty.HARD, 1f,
            Difficulty.NORMAL, 0.5f,
            Difficulty.EASY, 0.25f,
            Difficulty.PEACEFUL, 0.0f);

    public static boolean isCamo(Random random, Difficulty difficulty){
        return random.nextFloat() < DIFFICULTY_MAP.get(difficulty);
    }

    @SubscribeEvent
    public static void onLivingSpawnEvent$SpecialSpawn(final LivingSpawnEvent.SpecialSpawn event){
        Entity entity = event.getEntity();
        Random random = entity.level.random;
        EntityType<?> entityType = entity.getType();
        if(entity instanceof Zombie zombie && entityType == EntityType.ZOMBIE && zombie.getMainHandItem().isEmpty() && random.nextFloat() < 0.01){
            zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.SLEDGEHAMMER.get()));
            zombie.setDropChance(EquipmentSlot.MAINHAND, 0.5f);
        }
    }

    @SubscribeEvent
    public static void onLootingLevelEvent(final LootingLevelEvent event){
        DamageSource damageSource = event.getDamageSource();
        if(damageSource != null){
            Entity directEntity = damageSource.getDirectEntity();
            if(directEntity instanceof OdysseyAbstractArrow){
                event.setLootingLevel((int) ((OdysseyAbstractArrow) directEntity).getAspectStrength(Aspects.PROJECTILE_LOOTING_LUCK));
            } else if (directEntity instanceof LivingEntity livingEntity) {
                ItemStack itemStack = livingEntity.getMainHandItem();
                int looting = AspectUtil.getIntegerAspectStrength(itemStack, Aspects.LOOTING_LUCK);
                if(looting > 0){
                    event.setLootingLevel(looting);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onShieldBlockEvent(final ShieldBlockEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        ItemStack shield = livingEntity.getUseItem();
        if(shield.getItem() instanceof AspectShieldItem aspectShieldItem){
            DamageSource damageSource = event.getDamageSource();
            float damageBlockMultiplier = 1.0f;
            if(damageSource.isProjectile()){
                Entity damageSourceEntity = damageSource.getDirectEntity();
                if(damageSourceEntity instanceof OdysseyAbstractArrow odysseyAbstractArrow){
                    float piercingAspect = odysseyAbstractArrow.getAspectStrength(Aspects.PIERCING);
                    float impenetrabilityAspect = AspectUtil.getFloatAspectStrength(shield, Aspects.IMPENETRABILITY);
                    if(piercingAspect > impenetrabilityAspect){
                        damageBlockMultiplier = impenetrabilityAspect / piercingAspect;
                    }
                }
            }
            event.setBlockedDamage(damageBlockMultiplier * aspectShieldItem.getDamageBlock(shield, livingEntity.level.getDifficulty(), damageSource));
        }
    }

    @SubscribeEvent
    public static void onLivingKnockBackEvent(final LivingKnockBackEvent event){
        LivingEntity target = event.getEntityLiving();
        if(target instanceof OdysseyLivingEntity odysseyLivingEntity){
            event.setStrength(event.getOriginalStrength() * (1.0f + odysseyLivingEntity.getNextKnockbackAspect()));
            odysseyLivingEntity.setNextKnockbackAspect(0.0f);
        }
    }

    @SubscribeEvent
    public static void onLivingEquipmentChangeEvent(final LivingEquipmentChangeEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        ItemStack oldItemStack = event.getFrom();
        ItemStack newItemStack = event.getTo();
        EquipmentSlot equipmentSlot = event.getSlot();
        Multimap<Attribute, AttributeModifier> oldMultimap = HashMultimap.create();
        Multimap<Attribute, AttributeModifier> newMultimap = HashMultimap.create();
        AspectUtil.fillAttributeMultimaps(livingEntity, oldItemStack, newItemStack, equipmentSlot, oldMultimap, newMultimap);
        if(!(oldItemStack.getItem() instanceof AspectArmorItem) || equipmentSlot.getType() == EquipmentSlot.Type.ARMOR){
            livingEntity.getAttributes().removeAttributeModifiers(oldMultimap);
        }
        if(!(newItemStack.getItem() instanceof AspectArmorItem) || equipmentSlot.getType() == EquipmentSlot.Type.ARMOR){
            livingEntity.getAttributes().addTransientAttributeModifiers(newMultimap);
        }
    }
}
