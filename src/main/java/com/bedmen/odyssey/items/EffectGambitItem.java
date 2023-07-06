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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.world.item.Item.Properties;

public class EffectGambitItem extends MagicItem implements INeedsToRegisterItemModelProperty{

    private static final int NERF_DURATION = 6000; // 5 minutes

    private final Supplier<MobEffect> buff;
    private final Supplier<MobEffect> nerf;

    // If the Tag is not present, the item is not actively draining
    public static final String ACTIVATOR_UUID_TAG = Odyssey.MOD_ID + ":ActivatorUUID";

    public EffectGambitItem(Properties properties, ExperienceCost experienceCost, Supplier<MobEffect> buff, Supplier<MobEffect> nerf) {
        super(properties, experienceCost);
        this.buff = buff;
        this.nerf = nerf;
    }

    public boolean canBeUsed(ServerPlayer serverPlayer, ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        boolean isActivated = compoundTag.contains(ACTIVATOR_UUID_TAG);
        // Cannot activate two of the same gambit item
        if(serverPlayer.hasEffect(this.buff.get()) && !isActivated){
            return false;
        }
        if(isActivated){
            UUID uuid = compoundTag.getUUID(ACTIVATOR_UUID_TAG);
            // Only the player who activated the gambit item can deactivate it
            if(!serverPlayer.getUUID().equals(uuid)){
                return false;
            }
        }
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
                CompoundTag compoundTag = itemStack.getOrCreateTag();
                return compoundTag.contains(ACTIVATOR_UUID_TAG) ? 1.0f : 0.0f;
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
            CompoundTag compoundTag = itemStack.getOrCreateTag();
            if(compoundTag.contains(ACTIVATOR_UUID_TAG)){
                serverPlayer.removeEffect(this.buff.get());
                serverPlayer.addEffect(new MobEffectInstance(this.nerf.get(), NERF_DURATION));
                compoundTag.remove(ACTIVATOR_UUID_TAG);
            } else {
                serverPlayer.removeEffect(this.nerf.get());
                serverPlayer.addEffect(new MobEffectInstance(this.buff.get(), Integer.MAX_VALUE));
                compoundTag.putUUID(ACTIVATOR_UUID_TAG, serverPlayer.getUUID());
            }
        }
        return itemStack;
    }
}
