package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import com.bedmen.odyssey.combat.ShieldType;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class AspectShieldItem extends ShieldItem implements INeedsToRegisterItemModelProperty, InnateAspectItem, OdysseyTierItem, ParryableWeaponItem {
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

    public float getDamageBlock(ItemStack shield, DamageSource damageSource){
        return this.shieldType.damageBlock + AspectUtil.getShieldDamageBlockAspectStrength(shield, damageSource);
    }

    public int getRecoveryTime(ItemStack shield){
        float recoverySpeedMultiplier = 1.0f + AspectUtil.getItemStackAspectStrength(shield, Aspects.RECOVERY_SPEED);
        return Mth.ceil((float)this.shieldType.recoveryTime / recoverySpeedMultiplier);
    }

    public float getBlockingAngleWidth(ItemStack shield){
        float widthMultiplier = 1.0f + AspectUtil.getItemStackAspectStrength(shield, Aspects.WIDTH);
        return this.shieldType.blockingAngleWidth * widthMultiplier;
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
        AspectShieldItem aspectShieldItem = (AspectShieldItem)(shield.getItem());
        tooltip.add(Component.translatable("item.oddc.shield.damage_block").append(StringUtil.floatFormat(aspectShieldItem.shieldType.damageBlock)).withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.oddc.shield.recovery_time").append(StringUtil.timeFormat(this.getRecoveryTime(shield))).withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("item.oddc.shield.blocking_angle_width").append(StringUtil.angleFormat(this.getBlockingAngleWidth(shield))).withStyle(ChatFormatting.BLUE));
        BannerItem.appendHoverTextFromBannerBlockEntityTag(shield, tooltip);
    }
}
