package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.loot.OdysseyLootContextParams;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Explosion.class)
public abstract class MixinExplosion {

    @ModifyVariable(
            method = "finalizeExplosion",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Explosion;blockInteraction:Lnet/minecraft/world/level/Explosion$BlockInteraction;", ordinal = 1),
            index = 13
    )
    private LootContext.Builder onFinalizeExplosion(LootContext.Builder builder) {
        return builder.withParameter(OdysseyLootContextParams.IS_EXPLOSION, true);
    }
}
