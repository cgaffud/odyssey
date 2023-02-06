package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import com.bedmen.odyssey.combat.SmackPush;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.items.aspect_items.AspectItem;
import com.bedmen.odyssey.registry.ParticleTypeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
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

    /**
     * Sets player on fire unless they are nether immune
     * Increases max health when eating life fruit
     */
    @SubscribeEvent
    public static void onPlayerTickEvent(final TickEvent.PlayerTickEvent event){
        Player player =  event.player;
        //Beginning of Tick
        if(event.phase == TickEvent.Phase.START){
            //Both Sides
            if(player instanceof OdysseyPlayer odysseyPlayer) {
                odysseyPlayer.updateSniperScoping();
            }
            //Server Side
            if(event.side == LogicalSide.SERVER){
                if(!(player.isCreative() || player.isSpectator()) && player.level.dimensionType().ultraWarm()){
                    if(!AspectUtil.hasFireProtectionOrResistance(player))
                        player.setSecondsOnFire(1);
                }
                //TODO life fruits
//                if(player.hasEffect(EffectRegistry.LIFE_INCREASE.get())){
//                    IOdysseyPlayer playerPermanentBuffs = (IOdysseyPlayer)player;
//                    playerPermanentBuffs.incrementLifeFruits();
//                    AttributeInstance modifiableattributeinstance = player.getAttributes().getInstance(Attributes.MAX_HEALTH);
//                    if (modifiableattributeinstance != null) {
//                        modifiableattributeinstance.setBaseValue(20.0d + 2.0d * playerPermanentBuffs.getLifeFruits());
//                        player.setHealth(player.getHealth()+2.0f);
//                    }
//                }
            } else { //Client Side

            }
        } else { //End of Tick
            // Gliding
            if(player instanceof OdysseyLivingEntity odysseyLivingEntity){
                odysseyLivingEntity.setFlightLevels(AspectUtil.hasBooleanAspectOnArmor(player, Aspects.SLOW_FALL), AspectUtil.getIntegerAspectValueFromArmor(player, Aspects.GLIDE));
                if(player.isShiftKeyDown() && AspectUtil.hasBooleanAspectOnArmor(player, Aspects.SLOW_FALL) && odysseyLivingEntity.getFlightValue() > 0){
                    odysseyLivingEntity.decrementFlight();
                    if(!player.level.isClientSide){
                        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 2, 0, false, false, true));
                    }
                } else if(player.isOnGround()) {
                    odysseyLivingEntity.incrementFlight();
                }
            }

            // Turtle Mastery
            if (AspectUtil.hasBooleanAspectOnArmor(player, Aspects.TURTLE_MASTERY) && player.isShiftKeyDown() && !player.level.isClientSide) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 0, false, false, true));
            }
        }
    }

    /**
     * Sets player health to the appropriate level upon respawn based don life fruits eaten
     */
    @SubscribeEvent
    public static void onPlayerRespawnEvent(final PlayerEvent.PlayerRespawnEvent event){
        //Todo life fruits
//        Player player =  event.getPlayer();
//        if(player instanceof IOdysseyPlayer && !player.level.isClientSide){
//            player.setHealth(20.0f + 2.0f * ((IOdysseyPlayer) player).getLifeFruits());
//        }
    }

    @SubscribeEvent
    public static void onPlayerEvent$HarvestCheck(final PlayerEvent.HarvestCheck event){
        Block block = event.getTargetBlock().getBlock();
        Item item = event.getPlayer().getMainHandItem().getItem();
        if(block instanceof WebBlock && item instanceof SwordItem || item == Items.SHEARS){
            event.setCanHarvest(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntityEvent(final AttackEntityEvent event){
        Player player = event.getPlayer();
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
        boolean canSweep = isStandingStrike && AspectUtil.hasBooleanAspect(mainHandItemStack, Aspects.SWEEP);
        boolean canThrust = isStandingStrike && AspectUtil.hasBooleanAspect(mainHandItemStack, Aspects.THRUST);
        float sweepDamage = 1.0f + AspectUtil.getFloatAspectStrength(mainHandItemStack, Aspects.ADDITIONAL_SWEEP_DAMAGE);
        // Knockback
        if(hasExtraKnockbackFromSprinting && target instanceof OdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.pushKnockbackAspectQueue(AspectUtil.getFloatAspectStrength(mainHandItemStack, Aspects.KNOCKBACK));
        }
        // Sweep
        if(canSweep){
            // Unchanging variables are needed to use in the below lambda expressions
            player.level.getEntitiesOfClass(LivingEntity.class, mainHandItemStack.getSweepHitBox(player, target)).stream()
                    .filter(livingEntity ->
                            livingEntity != player
                                    && livingEntity != target
                                    && !player.isAlliedTo(livingEntity)
                                    && (!(livingEntity instanceof ArmorStand) || !((ArmorStand)livingEntity).isMarker())
                                    && player.distanceToSqr(livingEntity) < 9.0D)
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
        if(isFullyCharged && AspectUtil.hasBooleanAspect(mainHandItemStack, Aspects.SMACK) && target instanceof OdysseyLivingEntity odysseyLivingEntity){
            odysseyLivingEntity.setSmackPush(new SmackPush(attackStrengthScale, player, target));
        }
    }

    @SubscribeEvent
    public static void onItemTooltipEvent(final ItemTooltipEvent event){
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        TooltipFlag tooltipFlag = event.getFlags();
        Player player = event.getPlayer();
        Optional<Level> optionalLevel = player == null ? Optional.empty() : Optional.of(player.level);
        List<Component> componentList = new ArrayList<>();
        AspectTooltipContext aspectTooltipContext = new AspectTooltipContext(optionalLevel, Optional.of(itemStack));
        if(item instanceof AspectItem aspectItem){
            List<AspectHolder> aspectHolderList = aspectItem.getAspectHolderList();
            for(AspectHolder aspectHolder: aspectHolderList){
                aspectHolder.addTooltip(componentList, tooltipFlag, aspectTooltipContext);
            }
        }
        AspectUtil.addAddedModifierTooltip(itemStack, componentList, tooltipFlag, aspectTooltipContext);

        List<Component> tooltip = event.getToolTip();
        tooltip.addAll(1, componentList);
    }

    @SubscribeEvent
    public static void onPlayerEventClone(final PlayerEvent.Clone event){
        Player newPlayer = event.getPlayer();
        Player oldPlayer = event.getOriginal();
        if(newPlayer instanceof OdysseyPlayer newOdysseyPlayer && oldPlayer instanceof OdysseyPlayer oldOdysseyPlayer){
            newOdysseyPlayer.setPermabuffHolder(oldOdysseyPlayer.getPermabuffHolder());
        }
    }
}
