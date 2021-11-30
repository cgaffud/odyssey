package com.bedmen.odyssey.items;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class OdysseySpawnEggItem extends SpawnEggItem {
    protected static final List<OdysseySpawnEggItem> UNADDED_EGGS = new ArrayList<OdysseySpawnEggItem>();
    private final Lazy<? extends EntityType<? extends Mob>> entityTypeSupplier;

    public OdysseySpawnEggItem(final RegistryObject<? extends EntityType<? extends Mob>> entityTypeSupplier, final int primaryColor, final int secondaryColor, final Item.Properties properties) {
        super(null, primaryColor, secondaryColor, properties);
        this.entityTypeSupplier = Lazy.of(entityTypeSupplier::get);
        UNADDED_EGGS.add(this);
    }

    public static void initSpawnEggs(){
        DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior(){
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack){
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                type.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };

        for(final OdysseySpawnEggItem spawnEgg : UNADDED_EGGS){
            SpawnEggItem.BY_ID.put(spawnEgg.getType(null), spawnEgg);
            DispenserBlock.registerBehavior(spawnEgg, dispenseBehavior);
        }
        UNADDED_EGGS.clear();
    }

    @Override
    public EntityType<? extends Mob> getType(CompoundTag nbt){
        return this.entityTypeSupplier.get();
    }
}