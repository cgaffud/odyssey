package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.effect.TemperatureEffect;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class MixinCampfireBlockEntity extends BlockEntity implements Clearable {
    public MixinCampfireBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(method = "cookTick", at = @At(value = "HEAD"))
    private static void onCookTick(Level level, BlockPos blockPos, BlockState blockState, CampfireBlockEntity campfireBlockEntity, CallbackInfo ci) {
        AABB boundingBox = blockState.getShape(level, blockPos).bounds().move(blockPos).inflate(3.0d);
        level.getEntitiesOfClass(LivingEntity.class, boundingBox).forEach(livingEntity -> {
            MobEffectInstance mobEffectInstance = TemperatureEffect.getTemperatureEffectInstance(EffectRegistry.WARMING.get(), 30, 0, true);
            livingEntity.addEffect(mobEffectInstance);
        });
    }
}
