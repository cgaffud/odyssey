package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.util.BowUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class OdysseyBowItem extends BowItem implements INeedsToRegisterItemModelProperty {
    private final float velocity;
    private final int chargeTime;

    public OdysseyBowItem(Item.Properties builder, float velocity, int chargeTime) {
        super(builder);
        this.velocity = velocity;
        this.chargeTime = chargeTime;
    }

    public void releaseUsing(ItemStack bowItemStack, Level level, LivingEntity livingEntity, int useTime) {
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowItemStack) > 0;
            ItemStack itemstack = player.getProjectile(bowItemStack);
            BowUtil.consumeQuiverAmmo(player, itemstack, livingEntity.getRandom());

            int i = this.getUseDuration(bowItemStack) - useTime;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bowItemStack, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float superCharge = EnchantmentUtil.getSuperChargeMultiplier(bowItemStack);
                Flag maxVelocityFlag = new Flag();
                float f = getPowerForTime(i, bowItemStack, superCharge, maxVelocityFlag);

                if (!((double)f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, bowItemStack, player));
                    if (!level.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrow abstractArrow = arrowitem.createArrow(level, itemstack, player);

                        float inaccuracy = EnchantmentUtil.getAccuracyMultiplier(livingEntity);
                        if(maxVelocityFlag.value && superCharge > 1.0f){
                            abstractArrow.setCritArrow(true);
                            inaccuracy /= superCharge;
                        }

                        abstractArrow = customArrow(abstractArrow);
                        abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * this.velocity * BowUtil.BASE_ARROW_VELOCITY, inaccuracy);

                        int j = EnchantmentUtil.getPower(bowItemStack);
                        if (j > 0) {
                            abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + (double)j * 0.5D + 0.5D);
                        }
                        j = EnchantmentUtil.getPunch(bowItemStack);
                        if (j > 0) {
                            abstractArrow.setKnockback(j);
                        }
                        j = EnchantmentUtil.getFlame(bowItemStack);
                        if (j > 0) {
                            abstractArrow.setSecondsOnFire(100*j);
                        }
                        j = EnchantmentUtil.getPiercing(bowItemStack);
                        if (j > 0) {
                            abstractArrow.setPierceLevel((byte)j);
                        }
                        j = EnchantmentUtil.getMobLooting(bowItemStack);
                        if(j > 0 && abstractArrow instanceof OdysseyAbstractArrow){
                            ((OdysseyAbstractArrow) abstractArrow).setLootingLevel((byte)j);
                        }

                        bowItemStack.hurtAndBreak(1, player, (p_40665_) -> {
                            p_40665_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(abstractArrow);
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public float getPowerForTime(int charge, ItemStack itemStack, float superCharge, Flag flag) {
        float f = (float)charge / (float) EnchantmentUtil.getQuickChargeTime(this.chargeTime, itemStack);
        if(f >= superCharge){
            f = superCharge;
            if(flag != null){
                flag.value = true;
            }
        }
        f = chargeCurve(f);
        return f;
    }

    public static float chargeCurve(float f){
        return (f * f + f * 2.0F) / 3.0F;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        boolean flag = !p_40673_.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, p_40672_, p_40673_, p_40674_, flag);
        if (ret != null) return ret;

        if (!p_40673_.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            p_40673_.startUsingItem(p_40674_);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public int getChargeTime(ItemStack itemStack){
        return Mth.floor(EnchantmentUtil.getQuickChargeTime(this.chargeTime, itemStack) * EnchantmentUtil.getSuperChargeMultiplier(itemStack));
    }

    public void registerItemModelProperties() {
        ItemProperties.register(this, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float)(this.getChargeTime(itemStack));
            }
        });
        ItemProperties.register(this, new ResourceLocation("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> {
            return p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.oddc.bow.velocity").append(StringUtil.floatFormat(this.velocity * chargeCurve(EnchantmentUtil.getSuperChargeMultiplier(stack)))).withStyle(ChatFormatting.BLUE));
        tooltip.add(new TranslatableComponent("item.oddc.bow.charge_time").append(StringUtil.floatFormat(this.getChargeTime(stack)/20f)).append("s").withStyle(ChatFormatting.BLUE));
    }

    public static class Flag {
        public boolean value;
    }
}
