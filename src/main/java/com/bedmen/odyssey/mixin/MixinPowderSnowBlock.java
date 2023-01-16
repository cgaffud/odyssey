package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.modifier.Modifiers;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class MixinPowderSnowBlock extends Block implements BucketPickup {
    public MixinPowderSnowBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void onCanEntityWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(entity instanceof LivingEntity livingEntity && ModifierUtil.hasBooleanModifierOnArmor(livingEntity, Modifiers.SNOWSHOE));
        cir.cancel();
    }
}
