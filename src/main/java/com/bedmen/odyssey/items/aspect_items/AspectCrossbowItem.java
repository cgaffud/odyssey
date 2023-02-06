package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.*;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.object.MultishotAspect;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.util.StringUtil;
import com.bedmen.odyssey.combat.*;
import com.google.common.collect.Lists;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AspectCrossbowItem extends CrossbowItem implements INeedsToRegisterItemModelProperty, OdysseyRangedAmmoWeapon, InnateAspectItem {
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private final float damageMultiplier;
    private final int baseMaxChargeTicks;
    private final InnateAspectHolder innateAspectHolder;

    public AspectCrossbowItem(Item.Properties propertiesIn, Tier tier, float damageMultiplier, int baseMaxChargeTicks, List<AspectInstance> abilityList, List<AspectInstance> innateAspectList) {
        super(propertiesIn.durability(tier.getUses()));
        this.damageMultiplier = damageMultiplier;
        this.baseMaxChargeTicks = baseMaxChargeTicks;
        this.innateAspectHolder = new InnateAspectHolder(abilityList, innateAspectList);
    }

    public InnateAspectHolder getInnateAspectHolder(){
        return this.innateAspectHolder;
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_OR_FIREWORK;
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack crossbow = player.getItemInHand(interactionHand);
        if (isCharged(crossbow)) {
            performShooting(level, player, interactionHand, crossbow, getShootingVelocity(crossbow), 1.0F);
            setCharged(crossbow, false);
            return InteractionResultHolder.consume(crossbow);
        } else if (WeaponUtil.hasAmmo(player,crossbow)) {
            if (!isCharged(crossbow)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                player.startUsingItem(interactionHand);
            }

            return InteractionResultHolder.consume(crossbow);
        } else {
            return InteractionResultHolder.fail(crossbow);
        }
    }

    public void releaseUsing(ItemStack crossbow, Level level, LivingEntity livingEntity, int count) {
        int useTicks = this.getUseDuration(crossbow) - count;
        if (useTicks >= WeaponUtil.getRangedMaxChargeTicks(crossbow) && !isCharged(crossbow) && tryLoadProjectiles(livingEntity, crossbow)) {
            setCharged(crossbow, true);
            SoundSource soundsource = livingEntity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundsource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    public static boolean tryLoadProjectiles(LivingEntity livingEntity, ItemStack crossbow) {
        int numberOfArrows = MultishotAspect.strengthToNumberOfTotalProjectiles(AspectUtil.getFloatAspectStrength(crossbow, Aspects.MULTISHOT));
        boolean isPlayer = livingEntity instanceof Player;
        boolean flag = isPlayer && ((Player)livingEntity).getAbilities().instabuild;
        WeaponUtil.AmmoStack ammoStack;
        ItemStack itemstack;
        if(isPlayer){
            ammoStack = WeaponUtil.getAmmo((Player)livingEntity, crossbow, false);
            itemstack = ammoStack.ammo;
            CompoundTag compoundTag = crossbow.getOrCreateTag();
            compoundTag.putBoolean("QuiverFreeAmmo", !ammoStack.canPickUp);
        } else {
            itemstack = livingEntity.getProjectile(crossbow);
            CompoundTag compoundTag = crossbow.getOrCreateTag();
            compoundTag.putBoolean("QuiverFreeAmmo", false);
        }

        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < numberOfArrows; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(Items.ARROW);
                itemstack1 = itemstack.copy();
            }

            if (!loadProjectile(livingEntity, crossbow, itemstack, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isCharged(ItemStack crossbow) {
        CompoundTag compoundtag = crossbow.getTag();
        return compoundtag != null && compoundtag.getBoolean("Charged");
    }

    public static void setCharged(ItemStack crossbow, boolean isCharged) {
        CompoundTag compoundtag = crossbow.getOrCreateTag();
        compoundtag.putBoolean("Charged", isCharged);
    }

    public static void addChargedProjectile(ItemStack crossbow, ItemStack ammoStack) {
        CompoundTag compoundtag = crossbow.getOrCreateTag();
        ListTag listtag;
        if (compoundtag.contains("ChargedProjectiles", Tag.TAG_LIST)) {
            listtag = compoundtag.getList("ChargedProjectiles", Tag.TAG_COMPOUND);
        } else {
            listtag = new ListTag();
        }

        CompoundTag compoundtag1 = new CompoundTag();
        ammoStack.save(compoundtag1);
        listtag.add(compoundtag1);
        compoundtag.put("ChargedProjectiles", listtag);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack crossbow) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = crossbow.getTag();
        if (compoundtag != null && compoundtag.contains("ChargedProjectiles", Tag.TAG_LIST)) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", Tag.TAG_COMPOUND);
            if (listtag != null) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    list.add(ItemStack.of(compoundtag1));
                }
            }
        }

        return list;
    }

    private static void clearChargedProjectiles(ItemStack crossbow) {
        CompoundTag compoundtag = crossbow.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 9);
            listtag.clear();
            compoundtag.put("ChargedProjectiles", listtag);
        }

    }

    public static boolean containsChargedProjectile(ItemStack crossbow, Item ammoItem) {
        return getChargedProjectiles(crossbow).stream().anyMatch((ammo) -> {
            return ammo.is(ammoItem);
        });
    }

    private static void shootProjectile(Level level, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack crossbow, ItemStack ammo, float pitch, boolean creativeModeFlag, float power, float inaccuracy, float angle) {
        if (!level.isClientSide) {
            boolean rocketFlag = ammo.is(Items.FIREWORK_ROCKET);
            boolean isMultishotArrow = angle != 0.0f;
            Projectile projectile;
            if (rocketFlag) {
                projectile = new FireworkRocketEntity(level, ammo, livingEntity, livingEntity.getX(), livingEntity.getEyeY() - (double)0.15F, livingEntity.getZ(), true);
            } else {
                projectile = getArrow(level, livingEntity, ammo);
                if (creativeModeFlag || isMultishotArrow || (crossbow.getOrCreateTag().contains("QuiverFreeAmmo") && crossbow.getOrCreateTag().getBoolean("QuiverFreeAmmo"))) {
                    ((AbstractArrow)projectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }
            if(projectile instanceof AbstractArrow abstractArrow && isMultishotArrow){
                abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() * MultishotAspect.strengthToDamagePenalty(AspectUtil.getFloatAspectStrength(crossbow, Aspects.MULTISHOT)));
            }

            if (livingEntity instanceof CrossbowAttackMob) {
                CrossbowAttackMob crossbowattackmob = (CrossbowAttackMob)livingEntity;
                crossbowattackmob.shootCrossbowProjectile(crossbowattackmob.getTarget(), crossbow, projectile, angle);
            } else {
                Vec3 vec31 = livingEntity.getUpVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3f(vec31), angle, true);
                Vec3 vec3 = livingEntity.getViewVector(1.0F);
                Vector3f vector3f = new Vector3f(vec3);
                vector3f.transform(quaternion);
                projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), power, inaccuracy);
            }

            crossbow.hurtAndBreak(rocketFlag ? 3 : 1, livingEntity, (livingEntity1) -> {
                livingEntity1.broadcastBreakEvent(interactionHand);
            });
            level.addFreshEntity(projectile);
            level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pitch);
        }
    }

    public static void performShooting(Level level, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack crossbow, float power, float inaccuracy) {
        List<ItemStack> list = getChargedProjectiles(crossbow);
        int size = list.size();
        List<Float> shotPitches = getShotPitches(livingEntity.getRandom(), size);

        for(int i = 0; i < size; ++i) {
            ItemStack itemstack = list.get(i);
            float angle = size > 1 ? i / ((float)(size - 1)) * 20.0f - 10.0f : 0.0f;
            boolean creativeModeFlag = livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild;
            if (!itemstack.isEmpty()) {
                shootProjectile(level, livingEntity, interactionHand, crossbow, itemstack, shotPitches.get(i), creativeModeFlag, power, inaccuracy, angle);
            }
        }

        onCrossbowShot(level, livingEntity, crossbow);
    }

    private static List<Float> getShotPitches(Random random, int size) {
        List<Float> list = new ArrayList<>();
        list.add(1.0f);
        boolean randomBool = random.nextBoolean();
        for(int i = 1; i < size; i++){
            randomBool = !randomBool;
            list.add(getRandomShotPitch(randomBool, random));
        }
        return list;
    }

    private static float getRandomShotPitch(boolean randomBool, Random random) {
        float f = randomBool ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static void onCrossbowShot(Level level, LivingEntity livingEntity, ItemStack crossbow) {
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer)livingEntity;
            if (!level.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, crossbow);
            }

            serverplayer.awardStat(Stats.ITEM_USED.get(crossbow.getItem()));
        }

        clearChargedProjectiles(crossbow);
    }

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack crossbow, int count) {
        if (!level.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, crossbow);
            SoundEvent soundevent = this.getStartSound(i);
            SoundEvent soundevent1 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = WeaponUtil.getCharge(livingEntity, crossbow);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundevent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundevent1 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundevent1, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
        if(WeaponUtil.getCharge(livingEntity, crossbow) >= 1.0f && AspectUtil.hasBooleanAspect(crossbow, Aspects.REPEAT)){
            livingEntity.stopUsingItem();
            this.releaseUsing(crossbow, level, livingEntity, count);
        }
    }

    public int getUseDuration(ItemStack crossbow) {
        return WeaponUtil.getRangedMaxChargeTicks(crossbow) + 3;
    }

    public UseAnim getUseAnimation(ItemStack crossbow) {
        return UseAnim.CROSSBOW;
    }

    private SoundEvent getStartSound(int quickChargeLevel) {
        switch(quickChargeLevel) {
            case 1:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.CROSSBOW_LOADING_START;
        }
    }

    public int getDefaultProjectileRange() {
        return 8;
    }

    public float getShootingVelocity(ItemStack crossbow) {
        float velocity = WeaponUtil.getMaxArrowVelocity(crossbow, true);
        velocity *= containsChargedProjectile(crossbow, Items.FIREWORK_ROCKET) ? 0.5f : 1.0f;
        return velocity;
    }

    public static boolean loadProjectile(LivingEntity livingEntity, ItemStack crossbow, ItemStack ammo, boolean multishotArrow, boolean inCreative) {
        if (ammo.isEmpty()) {
            return false;
        } else {
            boolean flag = inCreative && ammo.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !inCreative && !multishotArrow && !crossbow.getOrCreateTag().getBoolean("QuiverFreeAmmo")) {
                boolean quiverFlag = false;
                if(livingEntity instanceof Player){
                    quiverFlag = WeaponUtil.tryToConsumeFromQuiver((Player)livingEntity, ammo);
                }
                itemstack = ammo.split(1);
                if(!quiverFlag) {
                    if (ammo.isEmpty() && livingEntity instanceof Player) {
                        ((Player) livingEntity).getInventory().removeItem(ammo);
                    }
                }
            } else {
                itemstack = ammo.copy();
            }

            addChargedProjectile(crossbow, itemstack);
            return true;
        }
    }

    public static AbstractArrow getArrow(Level level, LivingEntity shooter, ItemStack ammo) {
        AspectArrowItem arrowItem = (AspectArrowItem)(ammo.getItem() instanceof AspectArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow abstractArrow = arrowItem.createArrow(level, ammo, shooter);
        abstractArrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractArrow.setShotFromCrossbow(true);
        return abstractArrow;
    }

    //Basically isCrossbow
    public boolean useOnRelease(ItemStack itemStack) {
        return true;
    }

    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int compartments, boolean selected) {
        ConditionalAmpUtil.setDamageTag(itemStack, entity);
        super.inventoryTick(itemStack, level, entity, compartments, selected);
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(itemStack) ? 0.0F : WeaponUtil.getCharge(livingEntity, itemStack);
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });
        ItemProperties.register(this, new ResourceLocation("charged"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });
        ItemProperties.register(this, new ResourceLocation("firework"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && CrossbowItem.isCharged(itemStack) && CrossbowItem.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }

    public void appendHoverText(ItemStack crossbow, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(crossbow, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.oddc.bow.damage_multiplier").append(StringUtil.multiplierFormat(WeaponUtil.getMaxDamageMultiplier(crossbow))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.ranged.charge_time").append(StringUtil.timeFormat(WeaponUtil.getRangedMaxChargeTicks(crossbow))).withStyle(ChatFormatting.BLUE));
        if (flagIn.isAdvanced()) {
            tooltip.add(new TranslatableComponent("item.oddc.ranged.velocity").append(StringUtil.floatFormat(WeaponUtil.getMaxArrowVelocity(crossbow, true))).withStyle(ChatFormatting.BLUE));
        }
    }

    public float getDamageMultiplier(ItemStack bow){
        return this.damageMultiplier + ConditionalAmpUtil.getDamageTag(bow);
    }

    public int getBaseMaxChargeTicks() {
        return this.baseMaxChargeTicks;
    }
}