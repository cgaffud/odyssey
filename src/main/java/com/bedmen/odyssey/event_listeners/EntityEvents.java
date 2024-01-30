package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.block.entity.GraveBlockEntity;
import com.bedmen.odyssey.combat.SmackPush;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.combat.damagesource.OdysseyDamageSource;
import com.bedmen.odyssey.effect.AspectEffect;
import com.bedmen.odyssey.effect.FireEffect;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.effect.TemperatureSource;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.entity.monster.DodgesProjectileMob;
import com.bedmen.odyssey.entity.monster.SculkMob;
import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.food.OdysseyFoodData;
import com.bedmen.odyssey.items.EffectGambitItem;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.items.WarpTotemItem;
import com.bedmen.odyssey.items.aspect_items.AspectMeleeItem;
import com.bedmen.odyssey.items.aspect_items.ParryableWeaponItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.BlowbackAnimatePacket;
import com.bedmen.odyssey.network.packet.ColdSnapAnimatePacket;
import com.bedmen.odyssey.network.packet.FatalHitAnimatePacket;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.tier.OdysseyTiers;
import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.world.gen.biome.BiomeResourceKeys;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    private static final float BRUTE_CHANCE = 0.05f;

    @SubscribeEvent
    public static void onLivingUpdateEvent(final LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            // Set Temp Buffs
            List<AspectInstance> tempBuffList = new ArrayList<>();
            for(MobEffectInstance mobEffectInstance: livingEntity.getActiveEffects()){
                if(mobEffectInstance.getEffect() instanceof AspectEffect aspectEffect){
                    for(AspectInstance aspectInstance: aspectEffect.aspectInstanceList){
                        AspectUtil.addInstance(tempBuffList, aspectInstance.withMultipliedStrength(mobEffectInstance.getAmplifier()+1));
                    }
                }
            }
            odysseyLivingEntity.setTempBuffs(tempBuffList);
            // Adjust Shield Meter
            if(livingEntity.level.isClientSide){
                odysseyLivingEntity.updateShieldMeterO();
            }
            boolean isUsingParryable = WeaponUtil.isUsingParryable(livingEntity);
            int recoveryTime = 100;
            ItemStack itemStack = WeaponUtil.getHeldParryables(livingEntity);
            if(!itemStack.isEmpty()){
                recoveryTime = ((ParryableWeaponItem)itemStack.getItem()).getRecoveryTime(itemStack);
            }
            if(isUsingParryable){
                odysseyLivingEntity.adjustShieldMeter(-0.01f);
            } else {
                odysseyLivingEntity.adjustShieldMeter(1f/((float)recoveryTime));
            }
            if(odysseyLivingEntity.getShieldMeter() <= 0f && livingEntity instanceof Player player){
                player.stopUsingItem();
                for(Item item : ForgeRegistries.ITEMS.tags().getTag(OdysseyItemTags.PARRYABLES).stream().toList()){
                    player.getCooldowns().addCooldown(item, recoveryTime);
                }
            }
            // Perform Smack
            if(odysseyLivingEntity.getSmackPush().shouldPush){
                WeaponUtil.smackTarget(odysseyLivingEntity.getSmackPush());
                odysseyLivingEntity.setSmackPush(new SmackPush());
            }
            // Set true hurt time on client side
            if(livingEntity.level.isClientSide){
                Optional<Integer> trueHurtTime = odysseyLivingEntity.getTrueHurtTime();
                trueHurtTime.ifPresent(hurtTime -> {
                    livingEntity.hurtDuration = hurtTime;
                    livingEntity.hurtTime = livingEntity.hurtDuration-1;
                });
                odysseyLivingEntity.setTrueHurtTime(Optional.empty());
            }
        }

        if (!livingEntity.level.isClientSide && livingEntity.isAlive()) {
            int bloodLossStrength = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.BLOOD_LOSS);
            int weightStrength = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.WEIGHT);
            int oxygenDeprivationStrength = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.OXYGEN_DEPRIVATION);

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
        if(AspectUtil.getArmorAndEntityAspectStrength(livingEntity, Aspects.FROST_WALKER) && !blockPos.equals(livingEntity.lastPos)){
            FrostWalkerEnchantment.onEntityMoved(livingEntity, livingEntity.level, blockPos, 1);
        }

        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity && !livingEntity.level.isClientSide && !livingEntity.isDeadOrDying()){
            // Set Fire Type
            odysseyLivingEntity.setFireType(FireType.getStrongestFireEffectType(livingEntity));

            // Temperature Sources
            if(!(livingEntity instanceof Player player) || GeneralUtil.isSurvival(player)){
                if(livingEntity.isInPowderSnow){
                    TemperatureSource.POWDERED_SNOW.tick(livingEntity);
                }
                if(livingEntity.isInWaterRainOrBubble()){
                    TemperatureSource.WATER_OR_RAIN.tick(livingEntity);
                }
                if(odysseyLivingEntity.getFireType().isNotNone() || livingEntity.isOnFire()){
                    TemperatureSource.ON_FIRE.tick(livingEntity);
                }
            }

            // Move own temperature toward 0 if not undead
            if(!(livingEntity instanceof Mob mob) || mob.getMobType() != MobType.UNDEAD){
                TemperatureSource.stabilizeTemperatureNaturally(odysseyLivingEntity);
            }
            // Temperature Aspects
            float warmthStrength = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.WARMTH);
            if(warmthStrength > 0f){
                TemperatureSource.addHelpfulTemperature(odysseyLivingEntity, warmthStrength * TemperatureSource.ONE_PERCENT_PER_SECOND);
            }
            float coolingStrength = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.COOLING);
            if(coolingStrength > 0f){
                TemperatureSource.addHelpfulTemperature(odysseyLivingEntity, -coolingStrength * TemperatureSource.ONE_PERCENT_PER_SECOND);
            }
            float temperaturePerSecond = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.TEMPERATURE_PER_SECOND);
            if(temperaturePerSecond != 0f){
                TemperatureSource.temperaturePercentPerSecondSource(temperaturePerSecond).tick(livingEntity);
            }

            // Temperature Damage
            if(livingEntity.tickCount % 10 == 0){
                int damageCount = 0;
                while(Mth.abs(odysseyLivingEntity.getTemperature()) >= 1.0f + TemperatureSource.TEMPERATURE_PER_DAMAGE){
                    TemperatureSource.stabilizeTemperature(odysseyLivingEntity, TemperatureSource.TEMPERATURE_PER_DAMAGE);
                    damageCount++;
                }
                if(damageCount > 0){
                    livingEntity.hurt(TemperatureSource.damageSource(odysseyLivingEntity.isHot()), damageCount);
                }

            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(final LivingHurtEvent event){
        float amount = event.getAmount();
        LivingEntity hurtLivingEntity = event.getEntity();
        DamageSource damageSource = event.getSource();
        float invulnerabilityMultiplier = OdysseyDamageSource.getInvulnerabilityMultiplier(damageSource);
        Entity damageSourceEntity = damageSource.getDirectEntity();

        if (damageSourceEntity instanceof LivingEntity damageSourceLivingEntity) {
            ItemStack mainHandItemStack = damageSourceLivingEntity.getMainHandItem();
            // Smite, Bane of Arthropods, Hydro Damage
            amount += AspectUtil.getTargetConditionalAspectStrength(damageSourceLivingEntity, hurtLivingEntity);
            // Poison Damage
            int poisonStrength = AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.POISON_DAMAGE);
            if(poisonStrength > 0){
                AspectUtil.applyPoisonDamage(hurtLivingEntity, poisonStrength);
            }

            int hexflameStrength = AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.HEXFLAME_DAMAGE);
            if(hexflameStrength > 0) {
                if (hurtLivingEntity.hasEffect(EffectRegistry.HEXFLAME.get())) {
                    MobEffectInstance mobEffectInstance = hurtLivingEntity.getEffect(EffectRegistry.HEXFLAME.get());
                    int hexflameDuration = mobEffectInstance.getDuration() / 2 +  10 + (40 * hexflameStrength);
                    hurtLivingEntity.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), Math.min(hexflameDuration, (80 * hexflameStrength)), 0));
                }
                else
                    hurtLivingEntity.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), 10 + (40 * hexflameStrength), 0));
            }
            // Cobweb Chance
            float cobwebChance = AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.COBWEB_CHANCE);
            if(!(damageSourceLivingEntity instanceof OdysseyPlayer odysseyPlayer)
                    || odysseyPlayer.getAttackStrengthScaleO() > 0.9f) {
                Weaver.tryPlaceCobwebOnTarget(cobwebChance, hurtLivingEntity);
            }
            // Melee Larceny
            float larcenyChance = AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.LARCENY_CHANCE);
            WeaponUtil.tryLarceny(larcenyChance, damageSourceLivingEntity, hurtLivingEntity);

            // Melee Knockback
            if(hurtLivingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
                odysseyLivingEntity.pushKnockbackAspectQueue(
                        AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.KNOCKBACK)
                );
            }

            // Cold Snap
            if(AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.COLD_SNAP)){
                WeaponUtil.getSweepLivingEntities(damageSourceLivingEntity, hurtLivingEntity, true)
                                .forEach(livingEntity -> livingEntity.addEffect(new MobEffectInstance(EffectRegistry.STRAY_FREEZING.get(), 40, 4)));
                OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> hurtLivingEntity), new ColdSnapAnimatePacket(hurtLivingEntity));
            }

            // Thorns
            float thornsStrength = AspectUtil.getArmorAndEntityAspectStrength(hurtLivingEntity, Aspects.THORNS);
            if(thornsStrength > 0.0f && 0.25f >= hurtLivingEntity.getRandom().nextFloat()){
                damageSourceLivingEntity.hurt(DamageSource.thorns(hurtLivingEntity), thornsStrength);
            }

            // Absorbent Growth
            float absorbentGrowthCapacity = AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.ABSORBENT_GROWTH);
            if ((hurtLivingEntity instanceof Enemy) && absorbentGrowthCapacity > 0) {
                float progress = mainHandItemStack.getOrCreateTag().getFloat(AspectUtil.DAMAGE_GROWTH_TAG) + amount/400;
                mainHandItemStack.getOrCreateTag().putFloat(AspectUtil.DAMAGE_GROWTH_TAG, Math.min(progress, absorbentGrowthCapacity));
            }

            // Bludgeoning
            float bludgeoningStrength = AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.BLUDGEONING);
            if ((bludgeoningStrength > 0) && WeaponUtil.isBeingUsedTwoHanded(mainHandItemStack))
                hurtLivingEntity.addEffect(new MobEffectInstance(EffectRegistry.BLUDGEONING_SLOWNESS.get(), 50));

            // Vamp. Speed
            if (AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.VAMPIRIC_SPEED)) {
                MobEffectInstance vampSpeedInstance = damageSourceLivingEntity.getEffect(EffectRegistry.VAMPIRIC_SPEED.get());
                // Upper speed limit is set here (30%)
                int speedAmp = (vampSpeedInstance != null) ? (vampSpeedInstance.getAmplifier() == 5 ? 5 : vampSpeedInstance.getAmplifier()+1) : 0;
                damageSourceLivingEntity.addEffect(new MobEffectInstance(EffectRegistry.VAMPIRIC_SPEED.get(), 200, speedAmp));
            }

            // Dual Wield reduced invulnerability
            if(WeaponUtil.isDualWielding(damageSourceLivingEntity)){
                invulnerabilityMultiplier *= 0.5f;
            }
        } else if (damageSourceEntity instanceof OdysseyAbstractArrow odysseyAbstractArrow && hurtLivingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            // Ranged Knockback
            odysseyLivingEntity.pushKnockbackAspectQueue(odysseyAbstractArrow.getAspectStrength(Aspects.PROJECTILE_KNOCKBACK));
        }

        if (hurtLivingEntity.hasEffect(EffectRegistry.VULNERABLE.get()))
            amount *= 1f + (hurtLivingEntity.getEffect(EffectRegistry.VULNERABLE.get()).getAmplifier()+1)*0.2f;

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
        WeaponUtil.setInvulnerability(hurtLivingEntity, Integer.max(1, (int)(10.0f * invulnerabilityMultiplier)));

        if (hurtLivingEntity instanceof Player player) {
            if (player.isUsingItem() && (player.getUseItem().getItem() instanceof WarpTotemItem))
                player.stopUsingItem();
            player.getCooldowns().addCooldown(ItemRegistry.WARP_TOTEM.get(), 30);
        }
    }

    @SubscribeEvent
    public static void onLivingDamageEvent(final LivingDamageEvent event){
        float amount = event.getAmount();
        LivingEntity hurtLivingEntity = event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity damageSourceEntity = damageSource.getEntity();
        if (damageSourceEntity instanceof LivingEntity damageSourceLivingEntity) {
            float fatalDamage = AspectUtil.getOneHandedTotalAspectStrength(damageSourceLivingEntity, InteractionHand.MAIN_HAND, Aspects.FATAL_HIT);
            float currentHealth = hurtLivingEntity.getHealth();
            float newHealth = currentHealth - amount;
            if(newHealth > 0.0f && newHealth < fatalDamage){
                float deathChance = (fatalDamage - newHealth) / fatalDamage;
                if(deathChance > hurtLivingEntity.getRandom().nextFloat()) {
                    event.setAmount(currentHealth);
                    OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> hurtLivingEntity), new FatalHitAnimatePacket(hurtLivingEntity));
                }
            }
        }
    }

    private interface EntityReplacementFunction {
        Optional<EntityType<?>> call(Mob mob, RandomSource randomSource);
    }

    private static Map<EntityType<?>, EntityReplacementFunction> ENTITY_REPLACEMENT_MAP;

    public static void initEntityMap(){
        ENTITY_REPLACEMENT_MAP = new HashMap<>();
        ENTITY_REPLACEMENT_MAP.put(EntityType.SKELETON, EntityEvents::skeletonReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityType.STRAY, EntityEvents::vanillaStrayReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityTypeRegistry.STRAY.get(), EntityEvents::odysseyStrayReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityType.CREEPER, EntityEvents::creeperReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityType.ZOMBIE, EntityEvents::zombieReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityType.HUSK, EntityEvents::huskReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityType.SPIDER, EntityEvents::spiderReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityType.DROWNED, EntityEvents::zombieReplace);
        ENTITY_REPLACEMENT_MAP.put(EntityType.POLAR_BEAR, EntityEvents::polarBearReplace);
    }

    @SubscribeEvent
    public static void onLivingSpawnEvent$CheckSpawn(final LivingSpawnEvent.CheckSpawn event){
        Mob mob = event.getEntity();
        RandomSource randomSource = mob.level.random;
        EntityType<?> entityType = mob.getType();
        MobSpawnType mobSpawnType = event.getSpawnReason();

        if(mobSpawnType == MobSpawnType.SPAWNER || mobSpawnType == MobSpawnType.CHUNK_GENERATION){
            return;
        }

        if(ENTITY_REPLACEMENT_MAP.containsKey(entityType)) {
            EntityReplacementFunction entityReplacementFunction = ENTITY_REPLACEMENT_MAP.get(entityType);
            Optional<EntityType<?>> optionalEntityType = entityReplacementFunction.call(mob, randomSource);
            if (optionalEntityType.isPresent()) {
                EntityType<?> entityType1 = optionalEntityType.get();
                if (entityType1 != entityType) {
                    entityType1.spawn((ServerLevel) mob.level, null, null, new BlockPos(mob.getPosition(1.0f)), mobSpawnType, true, true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    private static Optional<EntityType<?>> skeletonReplace(Mob mob, RandomSource randomSource){
        if(inDripstoneBiome(mob)){
            return Optional.of(EntityTypeRegistry.ENCASED_SKELETON.get());
        }
        if(inLushCavesBiome(mob)) {
            return Optional.of(EntityTypeRegistry.OVERGROWN_SKELETON.get());
        }
        return Optional.of(EntityTypeRegistry.SKELETON.get());
    }

    private static Optional<EntityType<?>> vanillaStrayReplace(Mob mob, RandomSource randomSource){
        return Optional.of(EntityTypeRegistry.STRAY.get());
    }

    private static Optional<EntityType<?>> odysseyStrayReplace(Mob mob, RandomSource randomSource){
        if(randomSource.nextFloat() < BRUTE_CHANCE){
            return Optional.of(EntityTypeRegistry.STRAY_BRUTE.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> creeperReplace(Mob mob, RandomSource randomSource){
        if(randomSource.nextBoolean()){
            return Optional.of(EntityTypeRegistry.CAMO_CREEPER.get());
        }
        if(inDripstoneBiome(mob)){
            return Optional.of(EntityTypeRegistry.DRIPSTONE_CREEPER.get());
        }
        if(inLushCavesBiome(mob)) {
            return Optional.of(EntityTypeRegistry.OVERGROWN_CREEPER.get());
        }
        else if(isBaby(mob)){
            return Optional.of(EntityTypeRegistry.BABY_CREEPER.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> zombieReplace(Mob mob, RandomSource randomSource){
        if(inPrairieBiome(mob)){
            mob.setBaby(true);
            return Optional.empty();
        }
        if(inDripstoneBiome(mob)){
            return Optional.of(EntityTypeRegistry.ENCASED_ZOMBIE.get());
        }

        if(inLushCavesBiome(mob)){
            return Optional.of(EntityTypeRegistry.OVERGROWN_ZOMBIE.get());
        }

        float y = (float)mob.getY();
        // Guaranteed false for y>=8, linear gradient for -48 <= y <= 8, guaranteed true for y<-48
        if (randomSource.nextFloat() < (-(y-8)/56)) {
            return Optional.of(EntityTypeRegistry.FORGOTTEN.get());
        }
        if(randomSource.nextFloat() < BRUTE_CHANCE){
            return Optional.of(EntityTypeRegistry.ZOMBIE_BRUTE.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> huskReplace(Mob mob, RandomSource randomSource){
        return Optional.of(EntityTypeRegistry.HUSK.get());
    }

    private static Optional<EntityType<?>> spiderReplace(Mob mob, RandomSource randomSource){
        if(inPrairieBiome(mob)){
            return Optional.of(EntityTypeRegistry.BARN_SPIDER.get());
        }
        float y = (float)mob.getY();
        // Guaranteed false for y>=8, linear gradient for -48 <= y <= 8, guaranteed true for y<-48
        if (randomSource.nextFloat() < (-(y-8)/56)) {
            return Optional.of(EntityTypeRegistry.BLADE_SPIDER.get());
        }
        return Optional.empty();
    }

    private static Optional<EntityType<?>> polarBearReplace(Mob mob, RandomSource randomSource){
        return Optional.of(EntityTypeRegistry.POLAR_BEAR.get());
    }

    public static boolean isBaby(Entity entity){
        return inPrairieBiome(entity) || entity.level.random.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get();
    }

    public static boolean inPrairieBiome(Entity entity){
        return entity.level.getBiome(entity.blockPosition()).is(BiomeResourceKeys.PRAIRIE_RESOURCE_KEY);
    }

    public static boolean inDripstoneBiome(Entity entity){
        return entity.level.getBiome(entity.blockPosition()).is(Biomes.DRIPSTONE_CAVES);
    }

    public static boolean inLushCavesBiome(Entity entity){
        return entity.level.getBiome(entity.blockPosition()).is(Biomes.LUSH_CAVES);
    }

    @SubscribeEvent
    public static void onLivingSpawnEvent$SpecialSpawn(final LivingSpawnEvent.SpecialSpawn event){
        Entity entity = event.getEntity();
        RandomSource randomSource = entity.level.random;
        EntityType<?> entityType = entity.getType();
        if(entity instanceof Zombie zombie && entityType == EntityType.ZOMBIE && zombie.getMainHandItem().isEmpty() && randomSource.nextFloat() < 0.01){
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
                int looting = AspectUtil.getOneHandedTotalAspectStrength(livingEntity, InteractionHand.MAIN_HAND, Aspects.LOOTING_LUCK);
                if(looting > 0){
                    event.setLootingLevel(looting);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onShieldBlockEvent(final ShieldBlockEvent event){
        LivingEntity livingEntity = event.getEntity();
        ItemStack shield = livingEntity.getUseItem();
        if(shield.getItem() instanceof ParryableWeaponItem parryableWeaponItem){

            DamageSource damageSource = event.getDamageSource();
            float damageBlockMultiplier = 1.0f;
            if(AspectUtil.getItemStackAspectStrength(shield, Aspects.COLD_TO_THE_TOUCH)){
                WeaponUtil.getSweepLivingEntities(livingEntity, livingEntity, false)
                        .forEach(livingEntity1 -> livingEntity1.addEffect(new MobEffectInstance(EffectRegistry.STRAY_FREEZING.get(), 40, 4)));
                OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ColdSnapAnimatePacket(livingEntity));
            }
            // Parry boost
            if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
                if(odysseyLivingEntity.getShieldMeter() > 1.0f) {
                    damageBlockMultiplier *= (2 + AspectUtil.getItemStackAspectStrength(shield, Aspects.PRECISE_BLOCK)/2);
                    if (parryableWeaponItem instanceof AspectMeleeItem) {
                        livingEntity.addEffect(new MobEffectInstance(EffectRegistry.PARRY_STRENGTH.get(), 50, 0));
                    } else if ((AspectUtil.getItemStackAspectStrength(shield, Aspects.ASSISTED_STRIKE)) > 0) {
                        livingEntity.addEffect(new MobEffectInstance(EffectRegistry.ASSISTED_STRIKE_STRENGTH.get(), 50, 0));
                    }
                    float blowback = AspectUtil.getItemStackAspectStrength(shield, Aspects.BLOWBACK);
                    System.out.print(blowback);
                    if (blowback > 0 && (damageSource.getEntity() != null) && damageSource.getEntity() instanceof LivingEntity attacker) {
                        Vec3 awayVector = new Vec3(attacker.getX() - livingEntity.getX(), attacker.getY() - livingEntity.getY(), attacker.getZ()-livingEntity.getZ());
                        awayVector.normalize().multiply(blowback, blowback, blowback);
                        attacker.setDeltaMovement(awayVector);
                        OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> attacker), new BlowbackAnimatePacket(attacker));
                    }
                }
            }

            float blockedDamage = damageBlockMultiplier * parryableWeaponItem.getDamageBlock(shield, damageSource);

            if(livingEntity instanceof Player player){
                hurtCurrentlyUsedShield(player, blockedDamage);
                event.setShieldTakesDamage(false);
            }
            event.setBlockedDamage(blockedDamage);
        }
    }

    protected static void hurtCurrentlyUsedShield(Player player, float blockAmount) {
        if (player.getUseItem().canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
            ItemStack useItem = player.getUseItem();
            if (!player.level.isClientSide) {
                player.awardStat(Stats.ITEM_USED.get(useItem.getItem()));
            }

            int i = 1 + Mth.ceil(blockAmount);
            InteractionHand interactionhand = player.getUsedItemHand();
            useItem.hurtAndBreak(i, player, (p_219739_) -> {
                p_219739_.broadcastBreakEvent(interactionhand);
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, useItem, interactionhand);
            });
            if (useItem.isEmpty()) {
                if (interactionhand == InteractionHand.MAIN_HAND) {
                    player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }

                player.stopUsingItem();
                player.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + player.level.random.nextFloat() * 0.4F);
            }

        }
    }

    @SubscribeEvent
    public static void onLivingKnockBackEvent(final LivingKnockBackEvent event){
        LivingEntity target = event.getEntity();
        if(target instanceof OdysseyLivingEntity odysseyLivingEntity){
            event.setStrength(event.getOriginalStrength() * (1.0f + odysseyLivingEntity.popKnockbackAspectQueue()));
        }
    }

    @SubscribeEvent
    public static void onLivingEquipmentChangeEvent(final LivingEquipmentChangeEvent event){
        LivingEntity livingEntity = event.getEntity();
        ItemStack oldItemStack = event.getFrom();
        ItemStack newItemStack = event.getTo();
        EquipmentSlot equipmentSlot = event.getSlot();
        Multimap<Attribute, AttributeModifier> oldMultimap = HashMultimap.create();
        Multimap<Attribute, AttributeModifier> newMultimap = HashMultimap.create();
        AspectUtil.fillAttributeMultimaps(livingEntity, oldItemStack, newItemStack, equipmentSlot, oldMultimap, newMultimap);
        livingEntity.getAttributes().removeAttributeModifiers(oldMultimap);
        livingEntity.getAttributes().addTransientAttributeModifiers(newMultimap);
    }

    // The array represents the highest tier in each mob harvest level
    private static final Tier[] TIER_ARRAY = {OdysseyTiers.ULTRA_1, OdysseyTiers.ULTRA_2, OdysseyTiers.NETHERITE};
    @SubscribeEvent
    public static void onLivingDropsEvent(final LivingDropsEvent event){
        LivingEntity deadEntity = event.getEntity();
        if(deadEntity instanceof Mob) {
            DamageSource damageSource = event.getSource();
            Tier tier;
            BlockEntity grave = null;
            boolean isGraveMob = deadEntity instanceof SculkMob;
            if (isGraveMob) {
                deadEntity.level.setBlock(deadEntity.blockPosition(), BlockRegistry.GRAVE.get().defaultBlockState(), 2);
                grave = deadEntity.level.getBlockEntity(deadEntity.blockPosition());
            }

            if(damageSource != null){
                Entity entity = damageSource.getEntity();
                int mobHarvestLevel = entity instanceof Player player ? AspectUtil.getBuffAspectStrength(player, Aspects.ADDITIONAL_MOB_HARVEST_LEVEL) : 0;
                mobHarvestLevel = Integer.min(TIER_ARRAY.length-1, mobHarvestLevel);
                tier = TIER_ARRAY[mobHarvestLevel];
            } else {
                tier = TIER_ARRAY[0];
            }
            List<Tier> lowerTiers = TierSortingRegistry.getTiersLowerThan(tier);
            Collection<ItemEntity> itemEntityCollection = event.getDrops();
            Set<ItemEntity> itemEntityToRemove = new HashSet<>();
            int graveSlotIndex = 0;
            for(ItemEntity itemEntity: itemEntityCollection){
                if(itemEntity.getItem().getItem() instanceof OdysseyTierItem odysseyTierItem){
                    Tier itemTier = odysseyTierItem.getTier();
                    if(!lowerTiers.contains(itemTier) && tier != itemTier){
                        itemEntityToRemove.add(itemEntity);
                    }
                }

                if (isGraveMob && grave instanceof GraveBlockEntity graveBlockEntity) {
                    graveBlockEntity.setItem(graveSlotIndex, itemEntity.getItem());
                    graveSlotIndex++;
                    itemEntityToRemove.add(itemEntity);
                }
            }
            for(ItemEntity itemEntity: itemEntityToRemove){
                itemEntityCollection.remove(itemEntity);
            }
        }
        if(deadEntity instanceof Player){
            for(ItemEntity itemEntity: event.getDrops()){
                ItemStack itemStack = itemEntity.getItem();
                if(itemStack.getItem() instanceof EffectGambitItem){
                    EffectGambitItem.removeActivatorTag(itemStack);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(final EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player player){
            player.foodData = OdysseyFoodData.fromFoodData(player, player.foodData);
        }
    }

    @SubscribeEvent
    public static void onProjectileImpactEvent(final ProjectileImpactEvent event) {
        if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) event.getRayTraceResult();
            if (entityHitResult.getEntity() instanceof DodgesProjectileMob dodgesProjectileMob) {
                event.setCanceled(dodgesProjectileMob.tryDoDodge(event.getProjectile(), entityHitResult));
            }
        }
    }
}
