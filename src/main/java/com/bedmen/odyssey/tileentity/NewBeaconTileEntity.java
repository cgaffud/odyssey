package com.bedmen.odyssey.tileentity;

import com.bedmen.odyssey.container.NewBeaconContainer;
import com.bedmen.odyssey.util.TileEntityTypeRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LockCode;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NewBeaconTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    /** A list of beam segments for this beacon */
    private List<NewBeaconTileEntity.BeamSegment> beamSegments = Lists.newArrayList();
    private List<NewBeaconTileEntity.BeamSegment> beamColorSegments = Lists.newArrayList();
    /** Level of this beacon's pyramid. */
    private int[] completion = new int[9];
    private int blocks;
    private int beaconSize = -1;
    /** Primary potion effect given by this beacon */
    @Nullable
    private Effect effect;
    private int amplifier = 0;
    /** The custom name for this beacon. This was unused until 1.14; see https://bugs.mojang.com/browse/MC-124395 */
    @Nullable
    private ITextComponent customName;
    private LockCode lockCode = LockCode.NO_LOCK;
    private final IIntArray beaconData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return NewBeaconTileEntity.this.completion[0];
                case 1:
                    return NewBeaconTileEntity.this.completion[1];
                case 2:
                    return NewBeaconTileEntity.this.completion[2];
                case 3:
                    return NewBeaconTileEntity.this.completion[3];
                case 4:
                    return NewBeaconTileEntity.this.completion[4];
                case 5:
                    return NewBeaconTileEntity.this.completion[5];
                case 6:
                    return NewBeaconTileEntity.this.completion[6];
                case 7:
                    return NewBeaconTileEntity.this.completion[7];
                case 8:
                    return NewBeaconTileEntity.this.completion[8];
                case 9:
                    return Effect.getId(NewBeaconTileEntity.this.effect);
                case 10:
                    return NewBeaconTileEntity.this.amplifier;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    NewBeaconTileEntity.this.completion[0] = value;
                    break;
                case 1:
                    NewBeaconTileEntity.this.completion[1] = value;
                    break;
                case 2:
                    NewBeaconTileEntity.this.completion[2] = value;
                    break;
                case 3:
                    NewBeaconTileEntity.this.completion[3] = value;
                    break;
                case 4:
                    NewBeaconTileEntity.this.completion[4] = value;
                    break;
                case 5:
                    NewBeaconTileEntity.this.completion[5] = value;
                    break;
                case 6:
                    NewBeaconTileEntity.this.completion[6] = value;
                    break;
                case 7:
                    NewBeaconTileEntity.this.completion[7] = value;
                    break;
                case 8:
                    NewBeaconTileEntity.this.completion[8] = value;
                    break;
                case 9:
                    if (!NewBeaconTileEntity.this.level.isClientSide && !NewBeaconTileEntity.this.beamSegments.isEmpty()) {
                        NewBeaconTileEntity.this.playSound(SoundEvents.BEACON_POWER_SELECT);
                    }
                    NewBeaconTileEntity.this.effect = Effect.byId(value);
                    break;
                case 10:
                    NewBeaconTileEntity.this.amplifier = value;
            }

        }

        public int getCount() {
            return 11;
        }
    };

    public NewBeaconTileEntity() {
        super(TileEntityTypeRegistry.BEACON.get());
    }

    public void tick() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        BlockPos blockpos;
        if (this.beaconSize < j) {
            blockpos = this.worldPosition;
            this.beamColorSegments = Lists.newArrayList();
            this.beaconSize = blockpos.getY() - 1;
        } else {
            blockpos = new BlockPos(i, this.beaconSize + 1, k);
        }

        NewBeaconTileEntity.BeamSegment beacontileentity$beamsegment = this.beamColorSegments.isEmpty() ? null : this.beamColorSegments.get(this.beamColorSegments.size() - 1);
        int l = this.level.getHeight(Heightmap.Type.WORLD_SURFACE, i, k);

        for(int i1 = 0; i1 < 10 && blockpos.getY() <= l; ++i1) {
            BlockState blockstate = this.level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            float[] afloat = blockstate.getBeaconColorMultiplier(this.level, blockpos, getBlockPos());
            if (afloat != null) {
                if (this.beamColorSegments.size() <= 1) {
                    beacontileentity$beamsegment = new NewBeaconTileEntity.BeamSegment(afloat);
                    this.beamColorSegments.add(beacontileentity$beamsegment);
                } else if (beacontileentity$beamsegment != null) {
                    if (Arrays.equals(afloat, beacontileentity$beamsegment.colors)) {
                        beacontileentity$beamsegment.incrementHeight();
                    } else {
                        beacontileentity$beamsegment = new NewBeaconTileEntity.BeamSegment(new float[]{(beacontileentity$beamsegment.colors[0] + afloat[0]) / 2.0F, (beacontileentity$beamsegment.colors[1] + afloat[1]) / 2.0F, (beacontileentity$beamsegment.colors[2] + afloat[2]) / 2.0F});
                        this.beamColorSegments.add(beacontileentity$beamsegment);
                    }
                }
            } else {
                if (beacontileentity$beamsegment == null || blockstate.getLightBlock(this.level, blockpos) >= 15 && block != Blocks.BEDROCK) {
                    this.beamColorSegments.clear();
                    this.beaconSize = l;
                    break;
                }

                beacontileentity$beamsegment.incrementHeight();
            }

            blockpos = blockpos.above();
            ++this.beaconSize;
        }

        int j1 = this.blocks;
        if (this.level.getGameTime() % 80L == 0L) {
            if (!this.beamSegments.isEmpty()) {
                this.checkBeaconLevel(i, j, k);
            }

            if (this.blocks > 0 && !this.beamSegments.isEmpty()) {
                this.addEffectsToPlayers();
                this.playSound(SoundEvents.BEACON_AMBIENT);
            }
        }

        if (this.beaconSize >= l) {
            this.beaconSize = -1;
            boolean flag = j1 > 0;
            this.beamSegments = this.beamColorSegments;
            if (!this.level.isClientSide) {
                boolean flag1 = this.blocks > 0;
                if (!flag && flag1) {
                    this.playSound(SoundEvents.BEACON_ACTIVATE);

                    for(ServerPlayerEntity serverplayerentity : this.level.getEntitiesOfClass(ServerPlayerEntity.class, (new AxisAlignedBB((double)i, (double)j, (double)k, (double)i, (double)(j - 4), (double)k)).inflate(10.0D, 5.0D, 10.0D))) {
                        //CriteriaTriggers.CONSTRUCT_BEACON.trigger(serverplayerentity, this);
                    }
                } else if (flag && !flag1) {
                    this.playSound(SoundEvents.BEACON_DEACTIVATE);
                }
            }
        }

    }

    private void checkBeaconLevel(int beaconXIn, int beaconYIn, int beaconZIn) {
        this.blocks = 0;
        for(int i = 0; i < 9; i++){this.completion[i] = 0;}
        int j = beaconYIn - 1;
        if(j < 0){return;}
        if(this.level.getBlockState(new BlockPos(beaconXIn, j, beaconZIn)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[4];}
        if(this.level.getBlockState(new BlockPos(beaconXIn-1, j, beaconZIn-1)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[0];}
        if(this.level.getBlockState(new BlockPos(beaconXIn-1, j, beaconZIn)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[3];}
        if(this.level.getBlockState(new BlockPos(beaconXIn-1, j, beaconZIn+1)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[6];}
        if(this.level.getBlockState(new BlockPos(beaconXIn, j, beaconZIn-1)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[1];}
        if(this.level.getBlockState(new BlockPos(beaconXIn, j, beaconZIn+1)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[7];}
        if(this.level.getBlockState(new BlockPos(beaconXIn+1, j, beaconZIn-1)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[2];}
        if(this.level.getBlockState(new BlockPos(beaconXIn+1, j, beaconZIn)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[5];}
        if(this.level.getBlockState(new BlockPos(beaconXIn+1, j, beaconZIn+1)).getBlock().equals(Blocks.DIAMOND_BLOCK)){++this.completion[8];}

        for(int i: this.completion){this.blocks += i;}
        if(this.completion[4] == 0)  this.blocks = 0;

    }

    /**
     * invalidates a tile entity
     */
    public void setRemoved() {
        this.playSound(SoundEvents.BEACON_DEACTIVATE);
        super.setRemoved();
    }

    private void addEffectsToPlayers() {
        if (!this.level.isClientSide && this.effect != null) {
            double d0 = (double)(this.blocks * 5 + 5);
            int i = this.amplifier;
            if (this.blocks < 9 && i > 0) {
                i = 0;
            }

            int j = (6 + this.blocks) * 20;
            AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.worldPosition)).inflate(d0).expandTowards(0.0D, (double)this.level.getMaxBuildHeight(), 0.0D);
            List<PlayerEntity> list = this.level.getEntitiesOfClass(PlayerEntity.class, axisalignedbb);

            for(PlayerEntity playerentity : list) {
                playerentity.addEffect(new EffectInstance(this.effect, j, i, true, true));
            }

        }
    }

    public void playSound(SoundEvent sound) {
        this.level.playSound((PlayerEntity)null, this.worldPosition, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public List<NewBeaconTileEntity.BeamSegment> getBeamSegments() {
        return (List<NewBeaconTileEntity.BeamSegment>)(this.blocks == 0 ? ImmutableList.of() : this.beamSegments);
    }

    public int[] getCompletion() {
        return this.completion;
    }

    public int getBlocks() {
        return this.blocks;
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 3, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link}
     */
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @OnlyIn(Dist.CLIENT)
    public double getViewDistance() {
        return 256.0D;
    }

    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.effect = Effect.byId(nbt.getInt("Primary"));
        this.amplifier = nbt.getInt("Secondary");
        if (nbt.contains("CustomName", 8)) {
            this.customName = ITextComponent.Serializer.fromJson(nbt.getString("CustomName"));
        }

        this.lockCode = LockCode.fromTag(nbt);
    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("Primary", Effect.getId(this.effect));
        compound.putInt("Secondary", this.amplifier);
        compound.putInt("Levels", this.blocks);
        if (this.customName != null) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        }

        this.lockCode.addToTag(compound);
        return compound;
    }

    /**
     * Sets the custom name for this beacon.
     */
    public void setCustomName(@Nullable ITextComponent aname) {
        this.customName = aname;
    }

    @Nullable
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return LockableTileEntity.canUnlock(p_createMenu_3_, this.lockCode, this.getDisplayName()) ? new NewBeaconContainer(p_createMenu_1_, p_createMenu_2_, this.beaconData, IWorldPosCallable.create(this.level, this.getBlockPos())) : null;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent)(this.customName != null ? this.customName : new TranslationTextComponent("container.beacon"));
    }

    public static class BeamSegment {
        /** RGB (0 to 1.0) colors of this beam segment */
        private final float[] colors;
        private int height;

        public BeamSegment(float[] colorsIn) {
            this.colors = colorsIn;
            this.height = 1;
        }

        protected void incrementHeight() {
            ++this.height;
        }

        /**
         * Returns RGB (0 to 1.0) colors of this beam segment
         */
        @OnlyIn(Dist.CLIENT)
        public float[] getColors() {
            return this.colors;
        }

        @OnlyIn(Dist.CLIENT)
        public int getHeight() {
            return this.height;
        }
    }
}
