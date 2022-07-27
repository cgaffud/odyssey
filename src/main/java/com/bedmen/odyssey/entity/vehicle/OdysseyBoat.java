package com.bedmen.odyssey.entity.vehicle;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.network.NetworkHooks;

import java.util.Locale;

public class OdysseyBoat extends Boat {

    public OdysseyBoat(EntityType<? extends Boat> p_i50129_1_, Level p_i50129_2_) {
        super(p_i50129_1_, p_i50129_2_);
    }

    public OdysseyBoat(Level p_i1705_1_, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
        this(EntityTypeRegistry.BOAT.get(), p_i1705_1_);
        this.setPos(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = p_i1705_2_;
        this.yo = p_i1705_4_;
        this.zo = p_i1705_6_;
    }

    protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
        p_213281_1_.putString("Type", this.getOdysseyBoatType().getName());
    }

    protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
        if (p_70037_1_.contains("Type", 8)) {
            this.setType(OdysseyBoat.Type.byName(p_70037_1_.getString("Type")));
        }
    }

    public void setType(OdysseyBoat.Type p_184458_1_) {
        this.entityData.set(DATA_ID_TYPE, p_184458_1_.ordinal());
    }

    public OdysseyBoat.Type getOdysseyBoatType() {
        return OdysseyBoat.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    public Item getDropItem() {
        return this.getOdysseyBoatType().getLazyItem();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void checkFallDamage(double p_38307_, boolean p_38308_, BlockState p_38309_, BlockPos p_38310_) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (p_38308_) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Boat.Status.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F, DamageSource.FALL);
                    if (!this.level.isClientSide && !this.isRemoved()) {
                        this.kill();
                        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for(int i = 0; i < 3; ++i) {
                                this.spawnAtLocation(this.getOdysseyBoatType().getLazyPlanks());
                            }

                            for(int j = 0; j < 2; ++j) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.fallDistance = 0.0F;
            } else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && p_38307_ < 0.0D) {
                this.fallDistance = (float)((double)this.fallDistance - p_38307_);
            }

        }
    }

    public enum Type {
        PALM(BlockRegistry.PALM_PLANKS::get, ItemRegistry.PALM_BOAT::get),
        GREATWOOD(BlockRegistry.GREATWOOD_PLANKS::get, ItemRegistry.GREATWOOD_BOAT::get);

        private final String name;
        private final Lazy<Block> lazyPlanks;
        private final Lazy<Item> lazyItem;
        private final ResourceLocation resourceLocation;

        Type(Lazy<Block> planks, Lazy<Item> lazyItem) {
            this.name = this.name().toLowerCase(Locale.ROOT);
            this.lazyPlanks = planks;
            this.lazyItem = lazyItem;
            this.resourceLocation = new ResourceLocation(Odyssey.MOD_ID,String.format("textures/entity/boat/%s.png", this.name));
        }

        public String getName() {
            return this.name;
        }

        public Block getLazyPlanks() {
            return this.lazyPlanks.get();
        }

        public Item getLazyItem() {
            return this.lazyItem.get();
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }

        public String toString() {
            return this.name;
        }

        public static OdysseyBoat.Type byId(int p_184979_0_) {
            OdysseyBoat.Type[] aboatentity$type = values();
            if (p_184979_0_ < 0 || p_184979_0_ >= aboatentity$type.length) {
                p_184979_0_ = 0;
            }

            return aboatentity$type[p_184979_0_];
        }

        public static OdysseyBoat.Type byName(String p_184981_0_) {
            OdysseyBoat.Type[] aboatentity$type = values();

            for(int i = 0; i < aboatentity$type.length; ++i) {
                if (aboatentity$type[i].getName().equals(p_184981_0_)) {
                    return aboatentity$type[i];
                }
            }

            return aboatentity$type[0];
        }
    }
}
