package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.IPlayerPermanentBuffs;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.PermanentBuffsPacket;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity {

    public MixinServerPlayerEntity(World p_i241920_1_, BlockPos p_i241920_2_, float p_i241920_3_, GameProfile p_i241920_4_) {
        super(p_i241920_1_, p_i241920_2_, p_i241920_3_, p_i241920_4_);
    }

    @Inject(method = "restoreFrom", at = @At(value = "HEAD"))
    private void onRestoreFrom(ServerPlayerEntity serverPlayerEntity, boolean keepInventory, CallbackInfo ci) {
        boolean netherImmune = ((IPlayerPermanentBuffs)serverPlayerEntity).getNetherImmune();
        ((IPlayerPermanentBuffs) this).setNetherImmune(netherImmune);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    private void onReadAdditionalSaveData(CompoundNBT compoundNBT, CallbackInfo ci) {
        ((IPlayerPermanentBuffs) this).setNetherImmune(compoundNBT.getBoolean("netherImmune"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
    private void onAddAdditionalSaveData(CompoundNBT compoundNBT, CallbackInfo ci) {
        compoundNBT.putBoolean("netherImmune", ((IPlayerPermanentBuffs) this).getNetherImmune());
    }
}
