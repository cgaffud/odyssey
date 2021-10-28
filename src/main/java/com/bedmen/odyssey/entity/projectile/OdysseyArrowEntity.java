package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.items.OdysseyArrowItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class OdysseyArrowEntity extends OdysseyAbstractArrowEntity implements IEntityAdditionalSpawnData {
    private OdysseyArrowItem.ArrowType arrowType = OdysseyArrowItem.ArrowType.FLINT;

    public OdysseyArrowEntity(EntityType<? extends OdysseyArrowEntity> p_i50158_1_, World p_i50158_2_) {
        super(p_i50158_1_, p_i50158_2_);
    }

    public OdysseyArrowEntity(World p_i46768_1_, LivingEntity p_i46768_2_, OdysseyArrowItem.ArrowType arrowType) {
        super(EntityTypeRegistry.ARROW.get(), p_i46768_2_, p_i46768_1_);
        this.arrowType = arrowType;
        this.setBaseDamage(arrowType.getDamage());
    }

    public OdysseyArrowEntity(World p_i46769_1_, double p_i46769_2_, double p_i46769_4_, double p_i46769_6_) {
        super(EntityTypeRegistry.ARROW.get(), p_i46769_2_, p_i46769_4_, p_i46769_6_, p_i46769_1_);
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
            this.arrowType = OdysseyArrowItem.ArrowType.valueOf(compoundNBT.getString("ArrowType"));
        }
        this.setBaseDamage(this.getArrowType().getDamage());
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("ArrowType", this.arrowType.name());
    }

    public OdysseyArrowItem.ArrowType getArrowType(){
        return this.arrowType;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(this.arrowType.ordinal());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.arrowType = OdysseyArrowItem.ArrowType.values()[additionalData.readInt()];
    }
}