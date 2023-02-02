package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {

    @Shadow public ItemStack getItem() {return null;}

    public MixinItemEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public boolean fireImmune() {
        return this.getItem().getItem().isFireResistant() || super.fireImmune() || AspectUtil.hasBooleanAspect(this.getItem(), Aspects.BURN_PROOF);
    }
}
