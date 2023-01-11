package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.innate_modifier.InnateModifierItem;
import com.bedmen.odyssey.modifier.Modifier;
import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.modifier.Modifiers;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.weapon.MeleeWeaponAbility;
import com.bedmen.odyssey.weapon.OdysseyAbilityWeapon;
import com.bedmen.odyssey.weapon.OdysseyMeleeWeapon;
import com.bedmen.odyssey.weapon.SmackPush;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WebBlock;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            if(player instanceof IOdysseyPlayer odysseyPlayer) {
                odysseyPlayer.updateSniperScoping();
            }
            //Server Side
            if(event.side == LogicalSide.SERVER){
                if(!(player.isCreative() || player.isSpectator()) && player.level.dimensionType().ultraWarm()){
                    if(!EnchantmentUtil.hasFireProtectionOrResistance(player))
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
            //Server Side
            if(event.side == LogicalSide.SERVER){
                if (EnchantmentUtil.hasTurtling(player) && player.isShiftKeyDown()) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 0, false, false, true));
                }
            } else { //Client Side

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
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();
        if(item instanceof OdysseyMeleeWeapon odysseyMeleeWeapon){
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
            boolean canSweep = isFullyCharged
                    && !isCrit
                    && !hasExtraKnockbackFromSprinting
                    && player.isOnGround()
                    && (player.walkDist - player.walkDistO) < (double)player.getSpeed()
                    && odysseyMeleeWeapon.getMeleeWeaponClass().hasAbility(MeleeWeaponAbility.SWEEP);
            float sweepDamage = ModifierUtil.getUnitModifierValue(itemStack, Modifiers.ADDITIONAL_SWEEP_DAMAGE);
            // Sweep
            if(canSweep){
                // Unchanging variables are needed to use in the below lambda expressions
                player.level.getEntitiesOfClass(LivingEntity.class, itemStack.getSweepHitBox(player, target)).stream()
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
            // Smack
            if(isFullyCharged && odysseyMeleeWeapon.getMeleeWeaponClass().hasAbility(MeleeWeaponAbility.SMACK) && target instanceof OdysseyLivingEntity odysseyLivingEntity){
                odysseyLivingEntity.setSmackPush(new SmackPush(attackStrengthScale, player, target));
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltipEvent(final ItemTooltipEvent event){
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();
        TooltipFlag tooltipFlag = event.getFlags();
        List<Component> componentList = new ArrayList<>();
        if(item instanceof OdysseyAbilityWeapon odysseyAbilityWeapon){
            odysseyAbilityWeapon.getAbilityHolder().addTooltip(componentList, tooltipFlag);
        }
        ModifierUtil.addModifierTooltip(itemStack, componentList, tooltipFlag);

        List<Component> tooltip = event.getToolTip();
        tooltip.addAll(1, componentList);
    }
}
