package com.bedmen.odyssey.weapon;

import com.bedmen.odyssey.items.odyssey_versions.OdysseyBowItem;
import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class WeaponUtil {
    public static final int DEFAULT_RECOVERY_TIME = 100;
    public static final float BASE_ARROW_VELOCITY = 2.5f;
    public static final float BASE_ARROW_VELOCITY_ENEMIES = 1.6f;
    public static AmmoStack getAmmo(Player player, ItemStack bow, boolean consume){
        if (!(bow.getItem() instanceof ProjectileWeaponItem)) {
            return new AmmoStack(ItemStack.EMPTY, false);
        } else {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)bow.getItem()).getSupportedHeldProjectiles();
            ItemStack offhand = player.getOffhandItem();
            if(offhand.getItem() instanceof QuiverItem){
                QuiverItem quiverItem = (QuiverItem)offhand.getItem();
                CompoundTag compoundNBT = offhand.getOrCreateTag();
                if (compoundNBT.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(quiverItem.getQuiverType().getSize(), ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(compoundNBT, nonnulllist);
                    for(int j = 0; j < nonnulllist.size(); j++){
                        ItemStack ammoInQuiver = nonnulllist.get(j);
                        if (predicate.test(ammoInQuiver)) {
                            ItemStack ammo;
                            boolean canPickup = (quiverItem.getQuiverType().getFreeAmmoChance() < player.getRandom().nextFloat());
                            if(consume && canPickup){
                                ammo = ammoInQuiver.split(1);
                            } else {
                                ammo = ammoInQuiver.copy();
                                ammo.setCount(1);
                            }
                            ItemStack newQuiver = offhand.copy();
                            ContainerHelper.saveAllItems(compoundNBT, nonnulllist, true);
                            newQuiver.setTag(compoundNBT);
                            player.getInventory().offhand.set(0, newQuiver);
                            return new AmmoStack(ammo, canPickup);
                        }
                    }
                }
            }
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(player, predicate);
            if (!itemstack.isEmpty()) {
                return new AmmoStack(itemstack, true);
            } else {
                predicate = ((ProjectileWeaponItem)bow.getItem()).getAllSupportedProjectiles();

                for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                    ItemStack itemstack1 = player.getInventory().getItem(i);
                    if (predicate.test(itemstack1)) {
                        return new AmmoStack(itemstack1, true);
                    }
                }
                return player.getAbilities().instabuild ? new AmmoStack(new ItemStack(Items.ARROW), true) : new AmmoStack(ItemStack.EMPTY, false);
            }
        }
    }

    public static boolean hasAmmo(Player player, ItemStack bow){
        if (!(bow.getItem() instanceof ProjectileWeaponItem)) {
            return false;
        } else {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)bow.getItem()).getSupportedHeldProjectiles();
            ItemStack offhand = player.getOffhandItem();
            if(offhand.getItem() instanceof QuiverItem){
                QuiverItem quiverItem = (QuiverItem)offhand.getItem();
                CompoundTag compoundNBT = offhand.getOrCreateTag();
                if (compoundNBT.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(quiverItem.getQuiverType().getSize(), ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(compoundNBT, nonnulllist);
                    for(int j = 0; j < nonnulllist.size(); j++){
                        ItemStack ammoInQuiver = nonnulllist.get(j);
                        if (predicate.test(ammoInQuiver)) {
                            return true;
                        }
                    }
                }
            }
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(player, predicate);
            if (!itemstack.isEmpty()) {
                return true;
            } else {
                predicate = ((ProjectileWeaponItem)bow.getItem()).getAllSupportedProjectiles();

                for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                    ItemStack itemstack1 = player.getInventory().getItem(i);
                    if (predicate.test(itemstack1)) {
                        return true;
                    }
                }
                return player.getAbilities().instabuild;
            }
        }
    }

    public static boolean tryToConsumeFromQuiver(Player player, ItemStack ammo){
        ItemStack offhand = player.getOffhandItem();
        if(offhand.getItem() instanceof QuiverItem){
            QuiverItem quiverItem = (QuiverItem)offhand.getItem();
            CompoundTag compoundNBT = offhand.getOrCreateTag();
            if (compoundNBT.contains("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(quiverItem.getQuiverType().getSize(), ItemStack.EMPTY);
                ContainerHelper.loadAllItems(compoundNBT, nonnulllist);
                for(int j = 0; j < nonnulllist.size(); j++){
                    ItemStack ammoInQuiver = nonnulllist.get(j);
                    if (ammoInQuiver.is(ammo.getItem())) {
                        ammoInQuiver.shrink(1);
                        ItemStack newQuiver = offhand.copy();
                        ContainerHelper.saveAllItems(compoundNBT, nonnulllist, true);
                        newQuiver.setTag(compoundNBT);
                        player.getInventory().offhand.set(0, newQuiver);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static InteractionHand getHandHoldingBow(LivingEntity pLiving) {
        return pLiving.getMainHandItem().getItem() instanceof OdysseyBowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static int getRangedChargeTime(ItemStack itemStack, int baseMaxChargeTicks){
        return Mth.floor(EnchantmentUtil.getQuickChargeTime(baseMaxChargeTicks, itemStack) * EnchantmentUtil.getSuperChargeMultiplier(itemStack));
    }

    public static boolean isDualWielding(Player player){
        ItemStack mainHandItem = player.getMainHandItem();
        return isDualWieldItem(mainHandItem) && player.getOffhandItem().is(mainHandItem.getItem());
    }

    public static boolean isDualWieldItem(ItemStack itemStack){
        return itemStack.getItem() instanceof OdysseyMeleeWeapon odysseyMeleeWeapon && odysseyMeleeWeapon.getMeleeWeaponClass().hasAbility(MeleeWeaponAbility.DUAL_WIELD);
    }

    public static void smackTarget(SmackPush smackPush) {
        double knockback = 1.0d + smackPush.smacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double knockback_resistance = smackPush.target instanceof LivingEntity livingTarget ? livingTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0.0f;
        double netKnockback = knockback * (1.0d - knockback_resistance);
        if (netKnockback > 0.0D) {
            float strength = (float) (smackPush.attackStrengthScale * netKnockback);
            smackPush.target.push(0d, Math.sqrt(strength * 0.4d), 0d);
        }
    }

    public static class AmmoStack{
        public final ItemStack ammo;
        public final boolean canPickUp;

        AmmoStack(ItemStack ammo, boolean canPickUp){
            this.ammo = ammo;
            this.canPickUp = canPickUp;
        }
    }

    public static void tryStealItem(ItemStack itemStack, LivingEntity user, LivingEntity target, EquipmentSlot equipmentSlot, float chance) {
        if(target.getRandom().nextFloat() < chance) {
            ItemEntity itemEntity = target.spawnAtLocation(itemStack);
            if(itemEntity != null) {
                Vec3 vec3 = user.getPosition(1.0f).subtract(target.position()).normalize().add(0.0d, 0.5d, 0.0d).scale(0.25d);
                itemEntity.setDeltaMovement(vec3);
                if(equipmentSlot != null) {
                    target.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                }
            }
        }
    }

}
