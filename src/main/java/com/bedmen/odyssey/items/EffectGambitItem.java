package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class EffectGambitItem extends MagicItem implements INeedsToRegisterItemModelProperty{

    private final Supplier<MobEffect> buff;
    private final Supplier<MobEffect> nerf;
    public static final String IS_DRAINING_TAG = Odyssey.MOD_ID + ":Draining";
    public static final String ACTIVATOR_UUID_TAG = Odyssey.MOD_ID + ":ActivatorUUID";

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

    public static ItemPropertyFunction getEffectGambitPropertyFunction() {
        return (itemStack, clientLevel, livingEntity, i) -> {
            if (itemStack.getItem() instanceof EffectGambitItem) {
                CompoundTag status = itemStack.getOrCreateTag();
                float active = 0.0f;
                if (status.contains(IS_DRAINING_TAG))
                    active = status.getBoolean(IS_DRAINING_TAG) ? 1.0f : 0.0f;
                return active;
            }
            return 0.0f;
        };
    }

    // Todo: when you die the model doesn't seem to update? we may have to also force the activaiton to stop on the
    // item when the effect kills you
    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("active"), EffectGambitItem.getEffectGambitPropertyFunction());
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
                    // Just for safety I'm having this hit you really hard instead of just setting health to 1.0f
                    activator.hurt(DamageSource.MAGIC, activator.getHealth()-1.0f);
                    activator.removeEffect(EffectRegistry.GAMBIT_DRAIN.get());
                    activator.removeEffect(this.buff.get());
                    activator.addEffect(new MobEffectInstance(this.nerf.get(), 999999));
                }
            } else {
                serverPlayer.addEffect(new MobEffectInstance(this.buff.get(), 999999));
                serverPlayer.addEffect(new MobEffectInstance(EffectRegistry.GAMBIT_DRAIN.get(), 6000));
                status.putBoolean(IS_DRAINING_TAG, true);
                status.putUUID(ACTIVATOR_UUID_TAG, serverPlayer.getUUID());
            }
        }
        return itemStack;
    }
}
