package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.items.UpgradedArrowItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class UpgradedArrowEntity extends AbstractArrowEntity {
    private static final DataParameter<String> ARROW_TYPE = EntityDataManager.defineId(UpgradedArrowEntity.class, DataSerializers.STRING);

    public UpgradedArrowEntity(EntityType<? extends UpgradedArrowEntity> p_i50158_1_, World p_i50158_2_) {
        super(p_i50158_1_, p_i50158_2_);
    }

    public UpgradedArrowEntity(World p_i46768_1_, LivingEntity p_i46768_2_, UpgradedArrowItem.ArrowType arrowType) {
        super(EntityTypeRegistry.UPGRADED_ARROW.get(), p_i46768_2_, p_i46768_1_);
        this.setArrowType(arrowType);
        this.setBaseDamage(arrowType.getDamage());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ARROW_TYPE, "AMETHYST");
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(this.getArrowType().getItem());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("ArrowType")) {
            this.setArrowType(compoundNBT.getString("ArrowType"));
        }
        this.setBaseDamage(this.getArrowType().getDamage());
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("ArrowType", this.entityData.get(ARROW_TYPE));
    }

    public UpgradedArrowItem.ArrowType getArrowType(){
        return UpgradedArrowItem.ArrowType.valueOf(this.entityData.get(ARROW_TYPE));
    }

    public void setArrowType(UpgradedArrowItem.ArrowType arrowType){
        this.entityData.set(ARROW_TYPE, arrowType.name());
    }

    public void setArrowType(String s){
        this.entityData.set(ARROW_TYPE, s);
    }
}