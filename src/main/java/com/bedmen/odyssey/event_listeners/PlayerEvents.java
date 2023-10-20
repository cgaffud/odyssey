package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.combat.SmackPush;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.combat.damagesource.OdysseyDamageSource;
import com.bedmen.odyssey.effect.TemperatureSource;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.items.aspect_items.AspectItem;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.registry.ParticleTypeRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.bedmen.odyssey.world.BiomeUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerTickEvent(final TickEvent.PlayerTickEvent event){
        Player player =  event.player;
        //Beginning of Tick
        if(event.phase == TickEvent.Phase.START){
            //Both Sides
            if(player instanceof OdysseyPlayer odysseyPlayer) {
                odysseyPlayer.updateSniperScoping();
                odysseyPlayer.updateBlizzardFogScaleO();
                if(BiomeUtil.isInBlizzard(player) && GeneralUtil.isSurvival(player)){
                    odysseyPlayer.decrementBlizzardFogScale();
                } else {
                    odysseyPlayer.incrementBlizzardFogScale();
                }
            }
            //Server Side
            if(event.side == LogicalSide.SERVER){
                // Temperature
                for(TemperatureSource temperatureSource: BiomeUtil.getTemperatureSourceList(player)){
                    temperatureSource.tick(player);
                }

                // Heat Exhaustion
                if(player instanceof OdysseyLivingEntity odysseyLivingEntity){
                    float temperature = Mth.clamp(odysseyLivingEntity.getTemperature(), 0.0f, 1.0f);
                    if(temperature > 0){
                        player.causeFoodExhaustion(temperature * 0.02f);
                    }
                }

                // Experience aspect
                float experienceGainPerTick = AspectUtil.getTotalAspectValue(player, Aspects.EXPERIENCE_PER_SECOND);
                if(experienceGainPerTick != 0f && player instanceof ServerPlayer serverPlayer){
                    ExperienceCost experienceCost = new ExperienceCost(-experienceGainPerTick);
                    if (experienceCost.canPay(serverPlayer)){
                        experienceCost.pay(serverPlayer);
                    } else {
                        serverPlayer.hurt(OdysseyDamageSource.MANALESS, Float.MAX_VALUE);
                    }
                }
            } else { //Client Side

            }
        } else { //End of Tick
            // Gliding
            if(player instanceof OdysseyLivingEntity odysseyLivingEntity){
                odysseyLivingEntity.setFlightLevels(AspectUtil.getArmorAspectValue(player, Aspects.SLOW_FALL), AspectUtil.getArmorAspectValue(player, Aspects.GLIDE));
                if(player.isShiftKeyDown() && AspectUtil.getArmorAspectValue(player, Aspects.SLOW_FALL) && odysseyLivingEntity.getFlightValue() > 0){
                    odysseyLivingEntity.decrementFlight();
                    if(!player.level.isClientSide){
                        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 2, 0, false, false, true));
                    }
                } else if(player.isOnGround()) {
                    odysseyLivingEntity.incrementFlight();
                }
            }

            // Turtle Mastery
            if (AspectUtil.getArmorAspectValue(player, Aspects.TURTLE_MASTERY) && player.isShiftKeyDown() && !player.level.isClientSide) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 0, false, false, true));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEvent$HarvestCheck(final PlayerEvent.HarvestCheck event){
        Block block = event.getTargetBlock().getBlock();
        Item item = event.getEntity().getMainHandItem().getItem();
        if(block instanceof WebBlock && item instanceof SwordItem || item == Items.SHEARS){
            event.setCanHarvest(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntityEvent(final AttackEntityEvent event){
        Player player = event.getEntity();
        ItemStack mainHandItemStack = player.getMainHandItem();
        Entity target = event.getTarget();
        float attackStrengthScale = player.getAttackStrengthScale(0.5F);
        boolean isFullyCharged = attackStrengthScale > 0.9F;
        boolean hasExtraKnockbackFromSprinting = player.isSprinting() && isFullyCharged;
        boolean isCrit = isFullyCharged
                && player.fallDistance > 0.0F
                && !player.isOnGround()
                && !player.onClimbable()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger()
                && target instanceof LivingEntity
                && !player.isSprinting();
        boolean isStandingStrike = isFullyCharged
                && !isCrit
                && !hasExtraKnockbackFromSprinting
                && player.isOnGround()
                && (player.walkDist - player.walkDistO) < (double)player.getSpeed();
        boolean canSweep = isStandingStrike && AspectUtil.getItemStackAspectValue(mainHandItemStack, Aspects.SWEEP);
        boolean canThrust = isStandingStrike && AspectUtil.getItemStackAspectValue(mainHandItemStack, Aspects.THRUST);
        float sweepDamage = 1.0f + AspectUtil.getOneHandedEntityTotalAspectValue(player, InteractionHand.MAIN_HAND, Aspects.ADDITIONAL_SWEEP_DAMAGE);
        // Knockback
        if(hasExtraKnockbackFromSprinting && target instanceof OdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.pushKnockbackAspectQueue(AspectUtil.getItemStackAspectValue(mainHandItemStack, Aspects.KNOCKBACK));
        }
        // Sweep
        if(canSweep){
            // Unchanging variables are needed to use in the below lambda expressions
            WeaponUtil.getSweepLivingEntities(player, target, false)
                    .forEach(livingEntity -> livingEntity.hurt(DamageSource.playerAttack(player), sweepDamage));

            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            player.sweepAttack();
        }
        // Thrust
        if(canThrust){
            for(LivingEntity livingEntity: WeaponUtil.getThrustAttackTargets(player, target)){
                livingEntity.hurt(DamageSource.playerAttack(player), 1.0f);
            }
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.7F);
            if(player.level instanceof ServerLevel serverLevel){
                Vec3 eyePosition = player.getEyePosition();
                Vec3 endOfThrustVector = WeaponUtil.getEndOfThrustVector(eyePosition, player.getViewVector(1.0f));
                Vec3 thrustVector = endOfThrustVector.subtract(eyePosition);
                for(float f = 0.1f; f <= 1.0f; f += 0.1f){
                    double d0 = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
                    double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
                    Vec3 point = eyePosition.add(thrustVector.scale(f));
                    serverLevel.sendParticles(ParticleTypeRegistry.THRUST.get(), point.x, point.y, point.z, 0, d0, 0.0D, d1, 0.0D);
                }
            }
        }
        // Smack
        if(isFullyCharged && AspectUtil.getItemStackAspectValue(mainHandItemStack, Aspects.SMACK) && target instanceof OdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.setSmackPush(new SmackPush(attackStrengthScale, player, target));
        }
    }

    @SubscribeEvent
    public static void onItemTooltipEvent(final ItemTooltipEvent event){
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        TooltipFlag tooltipFlag = event.getFlags();
        Player player = event.getEntity();
        List<Component> componentList = new ArrayList<>();
        
        // Aspect Tooltips
        AspectTooltipContext aspectTooltipContext = new AspectTooltipContext(Optional.of(itemStack));
        if(item instanceof AspectItem aspectItem){
            List<AspectHolder> aspectHolderList = aspectItem.getAspectHolderList();
            for(AspectHolder aspectHolder: aspectHolderList){
                aspectHolder.addTooltip(componentList, tooltipFlag, aspectTooltipContext);
            }
        }
        AspectUtil.addAddedModifierTooltip(itemStack, componentList, tooltipFlag, aspectTooltipContext);

        // Food Item Tooltips
        FoodProperties foodProperties = itemStack.getFoodProperties(player);
        if(foodProperties != null){
            List<Pair<MobEffectInstance, Float>> effectList = foodProperties.getEffects();
            if(!effectList.isEmpty()){
                for(Pair<MobEffectInstance, Float> pair: effectList){
                    MobEffectInstance mobEffectInstance = pair.getFirst();
                    float probability = pair.getSecond();
                    MutableComponent mutablecomponent = Component.translatable(mobEffectInstance.getDescriptionId());
                    MobEffect mobeffect = mobEffectInstance.getEffect();

                    if (mobEffectInstance.getAmplifier() > 0) {
                        mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobEffectInstance.getAmplifier()));
                    }

                    if (mobEffectInstance.getDuration() > 20) {
                        mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobEffectInstance, 1.0f));
                    }

                    if(probability < 1f){
                        Style style = mutablecomponent.getStyle();
                        mutablecomponent = mutablecomponent.append(Component.translatable("potion.withChance", StringUtil.percentFormat(probability)).withStyle(style));
                    }

                    componentList.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
                }
            }
        }

        List<Component> tooltip = event.getToolTip();
        tooltip.addAll(1, componentList);
    }

    @SubscribeEvent
    public static void onPlayerEventClone(final PlayerEvent.Clone event){
        Player newPlayer = event.getEntity();
        Player oldPlayer = event.getOriginal();
        if(newPlayer instanceof OdysseyLivingEntity newOdysseyLivingEntity && oldPlayer instanceof OdysseyLivingEntity oldOdysseyLivingEntity){
            newOdysseyLivingEntity.setPermaBuffHolder(oldOdysseyLivingEntity.getPermaBuffHolder());
        }
        // If this was a player death & keepInventory is off, transfer all soulbound items to new player instance.
        if (event.isWasDeath() && !newPlayer.getLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
            oldPlayer.getInventory().items.stream().forEach(itemStack -> newPlayer.getInventory().placeItemBackInInventory(itemStack)
            );
    }

    @SubscribeEvent
    public static void onPlayerEventBreakSpeed(final PlayerEvent.BreakSpeed event){
        Player player = event.getEntity();
        float speed = event.getOriginalSpeed();
        if (player.isEyeInFluid(FluidTags.WATER)
                && !EnchantmentHelper.hasAquaAffinity(player)
                && AspectUtil.getOneHandedEntityTotalAspectValue(player, InteractionHand.MAIN_HAND, Aspects.AQUA_AFFINITY)) {
            speed *= 5.0F;
        }
        speed *= 1.0f + AspectUtil.getOneHandedEntityTotalAspectValue(player, InteractionHand.MAIN_HAND, Aspects.EFFICIENCY);
        event.setNewSpeed(speed);
    }

    @SubscribeEvent
    public static void onPlayerEventCriticalHit(final CriticalHitEvent event) {
        float precisionStrikeStrength = AspectUtil.getOneHandedEntityTotalAspectValue(event.getEntity(), InteractionHand.MAIN_HAND , Aspects.PRECISION_STRIKE);
        if ((precisionStrikeStrength > 0) && (event.getOldDamageModifier() == 1.5f))
            event.setDamageModifier(2.0f);
    }
}
