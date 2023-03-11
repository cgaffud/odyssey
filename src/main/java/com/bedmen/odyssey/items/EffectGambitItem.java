package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.CallbackI;

import java.util.Optional;
import java.util.function.Supplier;

public class EffectGambitItem extends MagicItem{

    private final Supplier<MobEffect> buff;
    private final Supplier<MobEffect> nerf;
    private final String IS_DRAINING_TAG = Odyssey.MOD_ID + ":Draining";
    private final String ACTIVATOR_UUID_TAG = Odyssey.MOD_ID + ":ActivatorUUID";

    public EffectGambitItem(Properties properties, ExperienceCost experienceCost, Supplier<MobEffect> buff, Supplier<MobEffect> nerf) {
        super(properties, experienceCost);
        this.buff = buff;
        this.nerf = nerf;
    }

    public boolean canBeUsed(ServerPlayer serverPlayer, ItemStack itemStack){
        return super.canBeUsed(serverPlayer, itemStack);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if(this.markedAsCanBeUsed(itemStack)){
            level.playSound(null, player.blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1.0f, 1.0f);
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public int getUseDuration(ItemStack itemStack) {
        return 40;
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if(this.markedAsCanBeUsed(itemStack) && livingEntity instanceof ServerPlayer serverPlayer){
            CompoundTag status = itemStack.getOrCreateTag();
            if (!status.contains(IS_DRAINING_TAG))
                status.putBoolean(IS_DRAINING_TAG, false);

            if (status.getBoolean(IS_DRAINING_TAG)) {
                status.putBoolean(IS_DRAINING_TAG, false);
                if (status.contains(ACTIVATOR_UUID_TAG)) {
                    ServerPlayer activator = (ServerPlayer) level.getPlayerByUUID(status.getUUID(ACTIVATOR_UUID_TAG));
                    activator.setHealth(1.0f);
                    activator.removeEffect(this.buff.get());
                    activator.addEffect(new MobEffectInstance(this.nerf.get(), 999999));
                }
            } else {
                serverPlayer.addEffect(new MobEffectInstance(this.buff.get(), 999999));
                status.putBoolean(IS_DRAINING_TAG, true);
                status.putUUID(ACTIVATOR_UUID_TAG, serverPlayer.getUUID());
            }

        }
        return itemStack;
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        super.inventoryTick(itemStack, level, entity, compartments, selected);
        CompoundTag status = itemStack.getOrCreateTag();

        if (level instanceof ServerLevel serverLevel && status.contains(IS_DRAINING_TAG) && status.getBoolean(IS_DRAINING_TAG)) {
            if (status.contains(ACTIVATOR_UUID_TAG)) {
                ServerPlayer activator = (ServerPlayer) serverLevel.getPlayerByUUID(status.getUUID(ACTIVATOR_UUID_TAG));
                if (activator.isAlive() && GeneralUtil.isHashTick(this, level, 20)) {
                    if (this.experienceCost.canPay(activator))
                        this.experienceCost.pay(activator);
                    else
                        activator.hurt(DamageSource.MAGIC, Float.MAX_VALUE);
                }

                if (!activator.isAlive())
                    status.remove(ACTIVATOR_UUID_TAG);
            } else {
                status.putBoolean(IS_DRAINING_TAG, false);
            }
        }
    }
}
