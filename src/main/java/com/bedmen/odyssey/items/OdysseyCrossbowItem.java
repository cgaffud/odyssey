package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.WeaponUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OdysseyCrossbowItem extends CrossbowItem implements INeedsToRegisterItemModelProperty {
    private static final int MAX_CHARGE_DURATION = 25;
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private final float velocity;
    private final int chargeTime;

    public OdysseyCrossbowItem(Item.Properties propertiesIn, float velocity, int chargeTime) {
        super(propertiesIn);
        this.velocity = velocity;
        this.chargeTime = chargeTime;
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_OR_FIREWORK;
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (isCharged(itemstack)) {
            performShooting(level, player, interactionHand, itemstack, getShootingPower(itemstack), 1.0F);
            setCharged(itemstack, false);
            return InteractionResultHolder.consume(itemstack);
        } else if (WeaponUtil.hasAmmo(player,itemstack)) {
            if (!isCharged(itemstack)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                player.startUsingItem(interactionHand);
            }

            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public void releaseUsing(ItemStack p_40875_, Level p_40876_, LivingEntity p_40877_, int p_40878_) {
        int i = this.getUseDuration(p_40875_) - p_40878_;
        float f = getPowerForTime(i, p_40875_);
        if (f >= 1.0F && !isCharged(p_40875_) && tryLoadProjectiles(p_40877_, p_40875_)) {
            setCharged(p_40875_, true);
            SoundSource soundsource = p_40877_ instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            p_40876_.playSound((Player)null, p_40877_.getX(), p_40877_.getY(), p_40877_.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundsource, 1.0F, 1.0F / (p_40876_.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    public static boolean tryLoadProjectiles(LivingEntity livingEntity, ItemStack crossbow) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.MULTISHOT.get(), crossbow);
        int j = i == 0 ? 1 : 3;
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

        for(int k = 0; k < j; ++k) {
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

    public static boolean isCharged(ItemStack p_40933_) {
        CompoundTag compoundtag = p_40933_.getTag();
        return compoundtag != null && compoundtag.getBoolean("Charged");
    }

    public static void setCharged(ItemStack p_40885_, boolean p_40886_) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        compoundtag.putBoolean("Charged", p_40886_);
    }

    public static void addChargedProjectile(ItemStack p_40929_, ItemStack p_40930_) {
        CompoundTag compoundtag = p_40929_.getOrCreateTag();
        ListTag listtag;
        if (compoundtag.contains("ChargedProjectiles", 9)) {
            listtag = compoundtag.getList("ChargedProjectiles", 10);
        } else {
            listtag = new ListTag();
        }

        CompoundTag compoundtag1 = new CompoundTag();
        p_40930_.save(compoundtag1);
        listtag.add(compoundtag1);
        compoundtag.put("ChargedProjectiles", listtag);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack p_40942_) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = p_40942_.getTag();
        if (compoundtag != null && compoundtag.contains("ChargedProjectiles", 9)) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 10);
            if (listtag != null) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    list.add(ItemStack.of(compoundtag1));
                }
            }
        }

        return list;
    }

    private static void clearChargedProjectiles(ItemStack p_40944_) {
        CompoundTag compoundtag = p_40944_.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 9);
            listtag.clear();
            compoundtag.put("ChargedProjectiles", listtag);
        }

    }

    public static boolean containsChargedProjectile(ItemStack p_40872_, Item p_40873_) {
        return getChargedProjectiles(p_40872_).stream().anyMatch((p_40870_) -> {
            return p_40870_.is(p_40873_);
        });
    }

    private static void shootProjectile(Level level, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack crossbow, ItemStack ammo, float pitch, boolean multishotFlag, float power, float inaccuracy, float angle) {
        if (!level.isClientSide) {
            boolean flag = ammo.is(Items.FIREWORK_ROCKET);
            Projectile projectile;
            if (flag) {
                projectile = new FireworkRocketEntity(level, ammo, livingEntity, livingEntity.getX(), livingEntity.getEyeY() - (double)0.15F, livingEntity.getZ(), true);
            } else {
                projectile = getArrow(level, livingEntity, crossbow, ammo);
                if (multishotFlag || angle != 0.0F || (crossbow.getOrCreateTag().contains("QuiverFreeAmmo") && crossbow.getOrCreateTag().getBoolean("QuiverFreeAmmo"))) {
                    ((AbstractArrow)projectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
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
                projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), power, inaccuracy);
            }

            crossbow.hurtAndBreak(flag ? 3 : 1, livingEntity, (p_40858_) -> {
                p_40858_.broadcastBreakEvent(interactionHand);
            });
            level.addFreshEntity(projectile);
            level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pitch);
        }
    }

    public static void performShooting(Level p_40888_, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack crossbow, float power, float inaccuracy) {
        List<ItemStack> list = getChargedProjectiles(crossbow);
        float[] afloat = getShotPitches(livingEntity.getRandom());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild;
            if (!itemstack.isEmpty()) {
                if (i == 0) {
                    shootProjectile(p_40888_, livingEntity, interactionHand, crossbow, itemstack, afloat[i], flag, power, inaccuracy, 0.0F);
                } else if (i == 1) {
                    shootProjectile(p_40888_, livingEntity, interactionHand, crossbow, itemstack, afloat[i], flag, power, inaccuracy, -10.0F);
                } else if (i == 2) {
                    shootProjectile(p_40888_, livingEntity, interactionHand, crossbow, itemstack, afloat[i], flag, power, inaccuracy, 10.0F);
                }
            }
        }

        onCrossbowShot(p_40888_, livingEntity, crossbow);
    }

    private static float[] getShotPitches(Random p_40924_) {
        boolean flag = p_40924_.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, p_40924_), getRandomShotPitch(!flag, p_40924_)};
    }

    private static float getRandomShotPitch(boolean p_150798_, Random p_150799_) {
        float f = p_150798_ ? 0.63F : 0.43F;
        return 1.0F / (p_150799_.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static void onCrossbowShot(Level p_40906_, LivingEntity p_40907_, ItemStack p_40908_) {
        if (p_40907_ instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer)p_40907_;
            if (!p_40906_.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, p_40908_);
            }

            serverplayer.awardStat(Stats.ITEM_USED.get(p_40908_.getItem()));
        }

        clearChargedProjectiles(p_40908_);
    }

    public void onUseTick(Level p_40910_, LivingEntity p_40911_, ItemStack p_40912_, int p_40913_) {
        if (!p_40910_.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, p_40912_);
            SoundEvent soundevent = this.getStartSound(i);
            SoundEvent soundevent1 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(p_40912_.getUseDuration() - p_40913_) / (float)getChargeDuration(p_40912_);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                p_40910_.playSound((Player)null, p_40911_.getX(), p_40911_.getY(), p_40911_.getZ(), soundevent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundevent1 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                p_40910_.playSound((Player)null, p_40911_.getX(), p_40911_.getY(), p_40911_.getZ(), soundevent1, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    public int getUseDuration(ItemStack p_40938_) {
        return getChargeDuration(p_40938_) + 3;
    }

    public UseAnim getUseAnimation(ItemStack p_40935_) {
        return UseAnim.CROSSBOW;
    }

    private SoundEvent getStartSound(int p_40852_) {
        switch(p_40852_) {
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

    public float getShootingPower(ItemStack itemStack) {
        float f = WeaponUtil.BASE_ARROW_VELOCITY * this.velocity;
        f *= containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 0.5f : 1.0f;
        return f;
    }

    public float getVelocity(){
        return this.velocity;
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

    public static AbstractArrow getArrow(Level level, LivingEntity shooter, ItemStack crossbow, ItemStack ammo) {
        ArrowItem arrowItem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow abstractArrow = arrowItem.createArrow(level, ammo, shooter);
        if(crossbow.getItem() instanceof OdysseyCrossbowItem odysseyCrossbowItem){
            return odysseyCrossbowItem.customArrow(abstractArrow);
        }
        abstractArrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractArrow.setShotFromCrossbow(true);
        int k = EnchantmentUtil.getPower(crossbow);
        if (k > 0) {
            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + (double)k * 0.5D + 0.5D);
        }
        k = EnchantmentUtil.getPunch(crossbow);
        if (k > 0) {
            abstractArrow.setKnockback(k);
        }
        k = EnchantmentUtil.getFlame(crossbow);
        if (k > 0) {
            abstractArrow.setRemainingFireTicks(100*k);
        }
        k = EnchantmentUtil.getPiercing(crossbow);
        if (k > 0) {
            abstractArrow.setPierceLevel((byte)k);
        }
        k = EnchantmentUtil.getMobLooting(crossbow);
        if(k > 0 && abstractArrow instanceof OdysseyAbstractArrow){
            ((OdysseyAbstractArrow) abstractArrow).setLootingLevel((byte)k);
        }
        return abstractArrow;
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }

    public static int getChargeDuration(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if(item instanceof OdysseyCrossbowItem){
            return EnchantmentUtil.getQuickChargeTime(((OdysseyCrossbowItem)item).chargeTime, itemStack);
        }
        return EnchantmentUtil.getQuickChargeTime(MAX_CHARGE_DURATION, itemStack);
    }

    public static float getPowerForTime(int p_40854_, ItemStack p_40855_) {
        float f = (float)p_40854_ / (float)getChargeDuration(p_40855_);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.oddc.bow.velocity").append(StringUtil.floatFormat(this.velocity)).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.bow.charge_time").append(StringUtil.floatFormat(getChargeDuration(stack)/20f)).append("s").withStyle(ChatFormatting.BLUE));
        if(stack.getOrCreateTag().contains("QuiverFreeAmmo")){
            tooltip.add(new TranslatableComponent(Boolean.valueOf(stack.getOrCreateTag().getBoolean("QuiverFreeAmmo")).toString()).withStyle(ChatFormatting.BLUE));
        }
        super.appendHoverText(stack, level, tooltip, flagIn);
    }

    //Basically isCrossbow
    public boolean useOnRelease(ItemStack itemStack) {
        return true;
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("pull"), (p_174620_, p_174621_, p_174622_, p_174623_) -> {
            if (p_174622_ == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(p_174620_) ? 0.0F : (float)(p_174620_.getUseDuration() - p_174622_.getUseItemRemainingTicks()) / (float)getChargeDuration(p_174620_);
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), (p_174615_, p_174616_, p_174617_, p_174618_) -> {
            return p_174617_ != null && p_174617_.isUsingItem() && p_174617_.getUseItem() == p_174615_ && !CrossbowItem.isCharged(p_174615_) ? 1.0F : 0.0F;
        });
        ItemProperties.register(this, new ResourceLocation("charged"), (p_174610_, p_174611_, p_174612_, p_174613_) -> {
            return p_174612_ != null && CrossbowItem.isCharged(p_174610_) ? 1.0F : 0.0F;
        });
        ItemProperties.register(this, new ResourceLocation("firework"), (p_174605_, p_174606_, p_174607_, p_174608_) -> {
            return p_174607_ != null && CrossbowItem.isCharged(p_174605_) && CrossbowItem.containsChargedProjectile(p_174605_, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }
}