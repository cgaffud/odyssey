package com.bedmen.odyssey.entity.item;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class OdysseyBoatEntity extends BoatEntity {

    public OdysseyBoatEntity(EntityType<? extends BoatEntity> p_i50129_1_, World p_i50129_2_) {
        super(p_i50129_1_, p_i50129_2_);
    }

    public OdysseyBoatEntity(World p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
        this(EntityTypeRegistry.BOAT.get(), p_i1705_1_);
        this.setPos(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = p_i1705_2_;
        this.yo = p_i1705_4_;
        this.zo = p_i1705_6_;
    }

    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        p_213281_1_.putString("Type", this.getOdysseyBoatType().getName());
    }

    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        if (p_70037_1_.contains("Type", 8)) {
            this.setType(OdysseyBoatEntity.Type.byName(p_70037_1_.getString("Type")));
        }
    }

    public void setType(OdysseyBoatEntity.Type p_184458_1_) {
        this.entityData.set(DATA_ID_TYPE, p_184458_1_.ordinal());
    }

    public OdysseyBoatEntity.Type getOdysseyBoatType() {
        return OdysseyBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public Item getDropItem() {
        switch(this.getOdysseyBoatType()) {
            default:
                return ItemRegistry.PALM_BOAT.get();
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (p_184231_3_) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != BoatEntity.Status.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F);
                    if (!this.level.isClientSide && !this.removed) {
                        this.remove();
                        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for(int i = 0; i < 3; ++i) {
                                this.spawnAtLocation(this.getOdysseyBoatType().getPlanks());
                            }

                            for(int j = 0; j < 2; ++j) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.fallDistance = 0.0F;
            } else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && p_184231_1_ < 0.0D) {
                this.fallDistance = (float)((double)this.fallDistance - p_184231_1_);
            }
        }
    }

    public enum Type {
        PALM(BlockRegistry.PALM_PLANKS.get(), "palm", new ResourceLocation(Odyssey.MOD_ID, "textures/entity/boat/palm.png"));

        private final String name;
        private final Block planks;
        private final ResourceLocation resourceLocation;

        Type(Block p_i48146_3_, String p_i48146_4_, ResourceLocation resourceLocation) {
            this.name = p_i48146_4_;
            this.planks = p_i48146_3_;
            this.resourceLocation = resourceLocation;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }

        public String toString() {
            return this.name;
        }

        public static OdysseyBoatEntity.Type byId(int p_184979_0_) {
            OdysseyBoatEntity.Type[] aboatentity$type = values();
            if (p_184979_0_ < 0 || p_184979_0_ >= aboatentity$type.length) {
                p_184979_0_ = 0;
            }

            return aboatentity$type[p_184979_0_];
        }

        public static OdysseyBoatEntity.Type byName(String p_184981_0_) {
            OdysseyBoatEntity.Type[] aboatentity$type = values();

            for(int i = 0; i < aboatentity$type.length; ++i) {
                if (aboatentity$type[i].getName().equals(p_184981_0_)) {
                    return aboatentity$type[i];
                }
            }

            return aboatentity$type[0];
        }
    }
}
