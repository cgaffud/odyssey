package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.animal.OdysseyPolarBear;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tags.OdysseyEntityTags;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {

    @SubscribeEvent
    public static void onLivingUpdateEvent(final LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (!livingEntity.level.isClientSide && livingEntity.isAlive()) {
            int bleedLvl = EnchantmentUtil.getBleeding(livingEntity);
            int heavyLvl = EnchantmentUtil.getHeavy(livingEntity);
            int drowningLvl = EnchantmentUtil.getDrowning(livingEntity);

            if (bleedLvl > 0)
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.BLEEDING.get(), 2,
                        bleedLvl-1,false, false, false));
            if (heavyLvl > 0)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, heavyLvl-1
                ,false, false, false));
            if (drowningLvl > 0)
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.DROWNING.get(), 2, drowningLvl-1
                        ,false,false,false));
        }
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(final LivingHurtEvent event){
        float amount = event.getAmount();
        LivingEntity livingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();

        if (damageSource.getEntity() instanceof LivingEntity ) {
            LivingEntity attackingEntity = (LivingEntity) damageSource.getEntity();
            // downpour damage booster
            int downpourLevel = EnchantmentUtil.getDownpour(attackingEntity);
            if (downpourLevel > 0 && OdysseyEntityTags.HYDROPHOBIC.contains(livingEntity.getType()))
                amount += (float)downpourLevel * 3f;
        }

        if(amount >= 10.0f && livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemRegistry.HOLLOW_COCONUT.get() && damageSource != DamageSource.FALL){
            Consumer<LivingEntity> consumer = (p_233653_0_) -> {
                p_233653_0_.broadcastBreakEvent(EquipmentSlot.HEAD);
            };
            ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
            Item item = itemStack.getItem();
            item.damageItem(itemStack, 100, livingEntity, consumer);
            consumer.accept(livingEntity);
            itemStack.shrink(1);
            if(livingEntity instanceof Player player){
                player.awardStat(Stats.ITEM_BROKEN.get(item));
            }
        }
        event.setAmount(amount);
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

    @SubscribeEvent
    public static void onLivingSpawnEvent$CheckSpawn(final LivingSpawnEvent.CheckSpawn event){
        Entity entity = event.getEntity();

        if(entity instanceof Zombie zombie){
            if(inPrairieBiome(zombie)){
                zombie.setBaby(true);
            }
        }

        else if(entity instanceof Skeleton && entity.getType() == EntityType.SKELETON){
            EntityTypeRegistry.SKELETON.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
            event.setResult(Event.Result.DENY);
        }

        else if(entity instanceof Creeper creeper && entity.getType() == EntityType.CREEPER){
            Random random = creeper.getRandom();

            if(isCamo(random, entity.level.getDifficulty())){
                EntityTypeRegistry.CAMO_CREEPER.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
                event.setResult(Event.Result.DENY);
            }

            else if(isBaby(entity)){
                EntityTypeRegistry.BABY_CREEPER.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
                event.setResult(Event.Result.DENY);
            }
        }

        else if(entity instanceof PolarBear polarBear && entity.getType() == EntityType.POLAR_BEAR){
            OdysseyPolarBear odysseyPolarBear = (OdysseyPolarBear) EntityTypeRegistry.POLAR_BEAR.get().spawn((ServerLevel)entity.level, null, null, new BlockPos(entity.getPosition(1.0f)), event.getSpawnReason(), true, true);
            odysseyPolarBear.setAge(polarBear.getAge());
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onLootingLevelEvent(final LootingLevelEvent event){
        DamageSource damageSource = event.getDamageSource();
        if(damageSource != null){
            Entity directEntity = damageSource.getDirectEntity();
            if(directEntity instanceof OdysseyAbstractArrow){
                event.setLootingLevel(((OdysseyAbstractArrow) directEntity).getLootingLevel());
            }
        }
    }

    public static boolean isBaby(Entity entity){
        return inPrairieBiome(entity) || entity.level.random.nextFloat() <  ForgeConfig.SERVER.zombieBabyChance.get();
    }

    public static boolean inPrairieBiome(Entity entity){
        return entity.level.getBiome(entity.blockPosition()).getRegistryName().equals(BiomeRegistry.PRAIRIE.get().getRegistryName());
    }

    public static boolean isCamo(Random random, Difficulty difficulty){
        float chance = switch (difficulty) {
            case HARD -> 1f;
            case NORMAL -> 0.5f;
            default -> 0.25f;
        };
        return random.nextFloat() < chance;
    }
}
