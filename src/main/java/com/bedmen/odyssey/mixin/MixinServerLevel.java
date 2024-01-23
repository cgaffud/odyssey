package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.custom_spawners.BanditEncounterSpawner;
import com.google.common.collect.ImmutableList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel {

    private List<CustomSpawner> odysseyCustomSpawners;
    @Shadow
    @Final
    private List<CustomSpawner> customSpawners;

    // customSpawners is annoyingly a immutable final list. We sidestep by populating our own copy.
    @Inject(method = "<init>", at = @At("RETURN"))
    public void serverLevelTail(CallbackInfo callbackInfo) {
        if (!(this.customSpawners == null) && !this.customSpawners.isEmpty())
        {
            ImmutableList.Builder<CustomSpawner> builder = ImmutableList.builder();
            builder.addAll(this.customSpawners);
            // Add Custom Spawners Here!
            builder.add(new BanditEncounterSpawner());
            odysseyCustomSpawners = builder.build();
        } else {
            odysseyCustomSpawners = ImmutableList.of();
        }
    }

    // Then, when tickCustomSpawners tries to call for customSpawners, we give it odysseyCustomSpawners
    @Redirect(
            method = "tickCustomSpawners",
            at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerLevel;customSpawners:Ljava/util/List;")
    )
    private List<CustomSpawner> tickCustomSpawners(ServerLevel instance) {
        return odysseyCustomSpawners;
    }
}
