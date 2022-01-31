package com.bedmen.odyssey.mixin;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public abstract class MixinAbstractArrow extends Projectile {

    @Shadow
    public IntOpenHashSet piercingIgnoreEntityIds;

    protected MixinAbstractArrow(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Inject(method = "canHitEntity", at = @At(value = "RETURN"))
    protected void onCanHitEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        String s1 = "ClientSide?:" + this.level.isClientSide;
        String s2 = " Null?:" + (this.piercingIgnoreEntityIds == null);
        String s3 = "";
        if(this.piercingIgnoreEntityIds != null){
            s3 = " InSet?:" + (this.piercingIgnoreEntityIds.contains(entity.getId()));
        }
        //System.out.println(s1+s2+s3);
    }
}
