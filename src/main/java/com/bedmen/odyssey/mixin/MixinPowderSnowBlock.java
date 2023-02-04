package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import net.minecraft.world.entity.Entity;
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
        cir.setReturnValue(entity instanceof LivingEntity livingEntity && AspectUtil.hasBooleanAspectOnArmor(livingEntity, Aspects.SNOWSHOE));
        cir.cancel();
    }
}
