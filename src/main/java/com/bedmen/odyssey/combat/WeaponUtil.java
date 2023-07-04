package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.aspect_items.AspectBowItem;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.ReduceInvulnerabilityPacket;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WeaponUtil {
    public static final int DEFAULT_RECOVERY_TIME = 100;
    public static final float BASE_ARROW_VELOCITY = 2.5f;
    public static final float BASE_ARROW_VELOCITY_ENEMIES = 1.6f;

    public static final float SHIELD_METER_MAX = 1.05f;
    public static AmmoStack getAmmo(Player player, ItemStack bow, boolean consume){
        if (!(bow.getItem() instanceof ProjectileWeaponItem)) {
            return new AmmoStack(ItemStack.EMPTY, false);
        } else {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)bow.getItem()).getSupportedHeldProjectiles();
            Optional<ItemStack> optionalQuiver = getQuiver(player);
            if(optionalQuiver.isPresent()){
                ItemStack quiver = optionalQuiver.get();
                QuiverItem quiverItem = (QuiverItem)quiver.getItem();
                CompoundTag compoundNBT = quiver.getOrCreateTag();
                if (compoundNBT.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(quiverItem.quiverType.size, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(compoundNBT, nonnulllist);
                    for(int j = 0; j < nonnulllist.size(); j++){
                        ItemStack ammoInQuiver = nonnulllist.get(j);
                        if (predicate.test(ammoInQuiver)) {
                            ItemStack ammo;
                            boolean canPickup = (quiverItem.quiverType.freeAmmoChance < player.getRandom().nextFloat());
                            if(consume && canPickup){
                                ammo = ammoInQuiver.split(1);
                            } else {
                                ammo = ammoInQuiver.copy();
                                ammo.setCount(1);
                            }
                            ItemStack newQuiver = quiver.copy();
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
            Optional<ItemStack> optionalQuiver = WeaponUtil.getQuiver(player);
            if (optionalQuiver.isPresent()) {
                ItemStack quiver = optionalQuiver.get();
                QuiverItem quiverItem = (QuiverItem)quiver.getItem();
                CompoundTag compoundNBT = quiver.getOrCreateTag();
                if (compoundNBT.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(quiverItem.quiverType.size, ItemStack.EMPTY);
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
        Optional<ItemStack> optionalQuiver = WeaponUtil.getQuiver(player);
        if (optionalQuiver.isPresent()) {
            ItemStack quiver = optionalQuiver.get();
            QuiverItem quiverItem = (QuiverItem)quiver.getItem();
            CompoundTag compoundNBT = quiver.getOrCreateTag();
            if (compoundNBT.contains("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(quiverItem.quiverType.size, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(compoundNBT, nonnulllist);
                for(int j = 0; j < nonnulllist.size(); j++){
                    ItemStack ammoInQuiver = nonnulllist.get(j);
                    if (ammoInQuiver.is(ammo.getItem())) {
                        ammoInQuiver.shrink(1);
                        ItemStack newQuiver = quiver.copy();
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
        return pLiving.getMainHandItem().getItem() instanceof AspectBowItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static ItemStack getItemInOtherHand(LivingEntity livingEntity,InteractionHand interactionHand) {
        if (interactionHand == InteractionHand.MAIN_HAND) {
            return livingEntity.getItemBySlot(EquipmentSlot.OFFHAND);
        } else if (interactionHand == InteractionHand.OFF_HAND) {
            return livingEntity.getItemBySlot(EquipmentSlot.MAINHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand " + interactionHand);
        }
    }

    public static boolean isDualWielding(LivingEntity livingEntity){
        ItemStack mainHandItem = livingEntity.getMainHandItem();
        return isDualWieldItem(mainHandItem) && livingEntity.getOffhandItem().is(mainHandItem.getItem());
    }

    public static boolean isDualWieldItem(ItemStack itemStack){
        return AspectUtil.hasBooleanAspect(itemStack, Aspects.DUAL_WIELD);
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

    public static void tryLarceny(float larcenyChance, Entity user, LivingEntity target){
        if(larcenyChance > 0.0f && !target.level.isClientSide){
            boolean mainHandFull = !target.getMainHandItem().isEmpty();
            boolean offHandFull = !target.getOffhandItem().isEmpty();
            EquipmentSlot equipmentSlot = null;
            if(mainHandFull && offHandFull){
                equipmentSlot = target.getRandom().nextBoolean() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            } else if(mainHandFull || offHandFull) {
                equipmentSlot = mainHandFull ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            }
            if(equipmentSlot != null) {
                ItemStack itemStack = target.getItemBySlot(equipmentSlot);
                WeaponUtil.tryStealItem(itemStack, user, target, equipmentSlot, larcenyChance);
            }
            if(target instanceof AbstractIllager || target instanceof AbstractVillager) {
                WeaponUtil.tryStealItem(Items.EMERALD.getDefaultInstance(), user, target, null, larcenyChance);
            }
        }
    }

    public static void tryStealItem(ItemStack itemStack, Entity user, LivingEntity target, EquipmentSlot equipmentSlot, float chance) {
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

    public static float getMaxDamageMultiplier(ItemStack bow){
        return getDamageMultiplierForUseTicks(getRangedMaxChargeTicks(bow), bow);
    }

    public static float getDamageMultiplierForUseTicks(int useTicks, ItemStack bow){
        float velocityFactor = getVelocityFactorForTime(useTicks, bow);
        float damageMultiplier = getRangedDamageMultiplier(bow);
        return velocityFactor * velocityFactor * damageMultiplier;
    }

    public static float getMaxArrowVelocity(ItemStack bow, boolean isPlayerUsing){
        return getArrowVelocityForUseTicks(getRangedMaxChargeTicks(bow), bow, isPlayerUsing);
    }

    public static float getArrowVelocityForUseTicks(int useTicks, ItemStack bow, boolean isPlayerUsing){
        float baseVelocity = isPlayerUsing ? BASE_ARROW_VELOCITY : BASE_ARROW_VELOCITY_ENEMIES;
        float velocityFactor = getVelocityFactorForTime(useTicks, bow);
        float damageMultiplier = getRangedDamageMultiplier(bow);
        return baseVelocity * velocityFactor * Mth.sqrt(damageMultiplier);
    }

    private static float getVelocityFactorForTime(int useTicks, ItemStack bow) {
        return getVelocityFactorFromCharge(getCharge(useTicks, bow));
    }

    public static float getCharge(LivingEntity livingEntity, ItemStack bow){
        return getCharge(livingEntity.getTicksUsingItem(), bow);
    }

    public static float getCharge(int useTicks, ItemStack bow){
        return Mth.clamp((float)useTicks / (float)getRangedMaxChargeTicks(bow), 0.0f, 1.0f);
    }

    /**
     * @return The number of ticks needed to fully charge the item
     */
    public static int getRangedMaxChargeTicks(ItemStack itemStack){
        if(itemStack.getItem() instanceof OdysseyRangedWeapon odysseyRangedWeapon){
            return odysseyRangedWeapon.getBaseMaxChargeTicks();
        }
        return 20;
    }

    public static float getRangedDamageMultiplier(ItemStack itemStack){
        Item item = itemStack.getItem();
        if(item instanceof OdysseyRangedAmmoWeapon odysseyRangedAmmoWeapon){
            return odysseyRangedAmmoWeapon.getDamageMultiplier(itemStack);
        }
        return 1.0f;
    }

    private static float getVelocityFactorFromCharge(float charge){
        return Mth.sqrt((charge * charge + charge * 2.0F) / 3.0F);
    }

    public static final double THRUST_RANGE = 5.0d;
    public static final double THRUST_RADIUS = 0.25d;
    public static Collection<LivingEntity> getThrustAttackTargets(Entity attacker, Entity target){
        Vec3 viewVector = attacker.getViewVector(1.0f);
        Vec3 eyePosition = attacker.getEyePosition();
        Vec3 endOfThrustVector = getEndOfThrustVector(eyePosition, viewVector);
        Predicate<LivingEntity> thrustPredicate = livingEntity -> !livingEntity.isSpectator() && livingEntity.isPickable() && livingEntity != attacker && livingEntity != target;
        AABB aabb = attacker.getBoundingBox().expandTowards(viewVector.scale(THRUST_RANGE)).inflate(1.0d + THRUST_RADIUS);
        List<LivingEntity> livingEntityList = attacker.level.getEntitiesOfClass(LivingEntity.class, aabb, thrustPredicate);
        return livingEntityList.stream().
                filter(livingEntity -> livingEntity.getBoundingBox().inflate(livingEntity.getPickRadius() + THRUST_RADIUS).clip(eyePosition, endOfThrustVector).isPresent())
                .collect(Collectors.toList());
    }

    public static Vec3 getEndOfThrustVector(Vec3 eyePosition, Vec3 viewVector){
        return eyePosition.add(viewVector.scale(THRUST_RANGE));
    }

    public static Optional<ItemStack> getQuiver(LivingEntity livingEntity){
        ItemStack offhand = livingEntity.getOffhandItem();
        if(offhand.getItem() instanceof QuiverItem){
            return Optional.of(offhand);
        }
        return Optional.empty();
    }

    public static Optional<ItemStack> getProjectileWeapon(LivingEntity livingEntity){
        ItemStack mainHandItemStack = livingEntity.getMainHandItem();
        if(mainHandItemStack.getItem() instanceof ProjectileWeaponItem){
            return Optional.of(mainHandItemStack);
        }
        ItemStack offHandItemStack = livingEntity.getOffhandItem();
        if(offHandItemStack.getItem() instanceof ProjectileWeaponItem){
            return Optional.of(offHandItemStack);
        }
        return Optional.empty();
    }

    public static void setInvulnerability(LivingEntity livingEntity, int invulnerabilityTicks){
        if(livingEntity.level.isClientSide){
            if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
                odysseyLivingEntity.setTrueHurtTime(Optional.of(invulnerabilityTicks));
            }
        } else {
            livingEntity.invulnerableTime = 10 + invulnerabilityTicks;
            livingEntity.hurtDuration = invulnerabilityTicks;
            livingEntity.hurtTime = livingEntity.hurtDuration;
            OdysseyNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new ReduceInvulnerabilityPacket(livingEntity, invulnerabilityTicks));
        }
    }

    private static AABB getSweepHitBox(@Nonnull Entity target)
    {
        return target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D);
    }

    public static Set<LivingEntity> getSweepLivingEntities(LivingEntity attacker, Entity target, boolean includeTarget){
        return attacker.level.getEntitiesOfClass(LivingEntity.class, getSweepHitBox(target)).stream()
                .filter(livingEntity ->
                        livingEntity != attacker
                                && (livingEntity != target || includeTarget)
                                && !attacker.isAlliedTo(livingEntity)
                                && (!(livingEntity instanceof ArmorStand) || !((ArmorStand)livingEntity).isMarker())
                                && attacker.distanceToSqr(livingEntity) < 9.0D)
                .collect(Collectors.toSet());
    }

    public static ItemStack getHeldParryables(LivingEntity livingEntity){
        ItemStack itemStack = livingEntity.getMainHandItem();
        if(itemStack.is(OdysseyItemTags.PARRYABLES)){
            return itemStack;
        }
        itemStack = livingEntity.getOffhandItem();
        if(itemStack.is(OdysseyItemTags.PARRYABLES)){
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    public static boolean isUsingParryable(LivingEntity livingEntity){
        return livingEntity.getUseItem().is(OdysseyItemTags.PARRYABLES);
    }
}


