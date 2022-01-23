package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.tags.OdysseyItemTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PowderSnowBlock.class)
public abstract class MixinPowderSnowBlock extends Block implements BucketPickup {
    public MixinPowderSnowBlock(Properties properties) {
        super(properties);
    }

    /**
     * @author JemBren
     * @reason To swap on Leather Boots with item tag powder_snow_walkables
     */
    @Overwrite
    public static boolean canEntityWalkOnPowderSnow(Entity entity) {
        if (entity.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        } else {
            return entity instanceof LivingEntity && ((LivingEntity) entity).getItemBySlot(EquipmentSlot.FEET).is(OdysseyItemTags.POWDER_SNOW_WALKABLES);
        }
    }
}
