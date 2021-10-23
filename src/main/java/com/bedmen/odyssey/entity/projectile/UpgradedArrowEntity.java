package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.items.UpgradedArrowItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class UpgradedArrowEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData {
    private UpgradedArrowItem.ArrowType arrowType = UpgradedArrowItem.ArrowType.FLINT;

    public UpgradedArrowEntity(EntityType<? extends UpgradedArrowEntity> p_i50158_1_, World p_i50158_2_) {
        super(p_i50158_1_, p_i50158_2_);
    }

    public UpgradedArrowEntity(World p_i46768_1_, LivingEntity p_i46768_2_, UpgradedArrowItem.ArrowType arrowType) {
        super(EntityTypeRegistry.UPGRADED_ARROW.get(), p_i46768_2_, p_i46768_1_);
        this.arrowType = arrowType;
        this.setBaseDamage(arrowType.getDamage());
    }

    public UpgradedArrowEntity(World p_i46769_1_, double p_i46769_2_, double p_i46769_4_, double p_i46769_6_) {
        super(EntityTypeRegistry.UPGRADED_ARROW.get(), p_i46769_2_, p_i46769_4_, p_i46769_6_, p_i46769_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(this.getArrowType().getItem());
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("ArrowType")) {
            this.arrowType = UpgradedArrowItem.ArrowType.valueOf(compoundNBT.getString("ArrowType"));
        }
        this.setBaseDamage(this.getArrowType().getDamage());
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("ArrowType", this.arrowType.name());
    }

    public UpgradedArrowItem.ArrowType getArrowType(){
        return this.arrowType;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(this.arrowType.ordinal());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.arrowType = UpgradedArrowItem.ArrowType.values()[additionalData.readInt()];
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}