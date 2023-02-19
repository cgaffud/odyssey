package com.bedmen.odyssey.entity.item;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FallingHollowCoconut extends FallingBlockEntity {
    public FallingHollowCoconut(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FallingHollowCoconut(Level level, double x, double y, double z, BlockState blockState) {
        super(level, x, y, z, blockState);
    }

    public void tick() {
        List<Player> list = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox());
        for(Player player : list){
            ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
            if(head == ItemStack.EMPTY || head.getItem() == Items.AIR){
                player.setItemSlot(EquipmentSlot.HEAD, ItemRegistry.HOLLOW_COCONUT.get().getDefaultInstance());
                this.discard();
                return;
            }
        }
        super.tick();
    }
}
