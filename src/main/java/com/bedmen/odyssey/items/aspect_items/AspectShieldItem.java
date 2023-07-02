package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import com.bedmen.odyssey.combat.ShieldType;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class AspectShieldItem extends ShieldItem implements INeedsToRegisterItemModelProperty, InnateAspectItem, OdysseyTierItem {
    public final ShieldType shieldType;

    public AspectShieldItem(Properties builder, ShieldType shieldType) {
        super(builder.durability(shieldType.tier.getUses() * 2));
        this.shieldType = shieldType;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public Tier getTier(){
        return this.shieldType.tier;
    }

    public InnateAspectHolder getInnateAspectHolder() {
        return this.shieldType.innateAspectHolder;
    }

    public boolean isValidRepairItem(ItemStack itemStack, ItemStack repairStack) {
        return this.shieldType.repairItemPredicate.test(repairStack.getItem());
    }

    public void registerItemModelProperties(){
        ItemProperties.register(this, new ResourceLocation("blocking"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    public float getDamageBlock(ItemStack shield, Difficulty difficulty, DamageSource damageSource){
        float damageBlock = this.shieldType.damageBlock;
        float additionalDamageBlock = AspectUtil.getDamageSourcePredicateAspectStrength(shield, damageSource);
        float totalUnadjustedDamageBlock = damageBlock + additionalDamageBlock;
        float difficultyAdjustedDamageBlock = getDifficultyAdjustedDamageBlock(totalUnadjustedDamageBlock, difficulty);
        return difficultyAdjustedDamageBlock;
    }

    public static float getDifficultyAdjustedDamageBlock(float damageBlock, Difficulty difficulty) {
        return damageBlock * (difficulty == Difficulty.HARD ? 1.5f : 1.0f);
    }

    public int getRecoveryTime(ItemStack shield){
        float recoverySpeedMultiplier = 1.0f + AspectUtil.getFloatAspectStrength(shield, Aspects.RECOVERY_SPEED);
        return Mth.ceil((float)this.shieldType.recoveryTime / recoverySpeedMultiplier);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return OdysseyBlockEntityWithoutLevelRenderer.getInstance();
            }
        });
    }

    public void appendHoverText(ItemStack shield, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        Difficulty difficulty = level == null ? null : level.getDifficulty();
        AspectShieldItem aspectShieldItem = (AspectShieldItem)(shield.getItem());
        float damageBlock = aspectShieldItem.shieldType.damageBlock;
        tooltip.add(Component.translatable("item.oddc.shield.damage_block").append(StringUtil.floatFormat(getDifficultyAdjustedDamageBlock(damageBlock, difficulty))).withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.oddc.shield.recovery_time").append(StringUtil.timeFormat(this.getRecoveryTime(shield))).withStyle(ChatFormatting.BLUE));
        BannerItem.appendHoverTextFromBannerBlockEntityTag(shield, tooltip);
    }
}
