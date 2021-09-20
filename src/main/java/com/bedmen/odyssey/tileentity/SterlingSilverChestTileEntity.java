package com.bedmen.odyssey.tileentity;

import com.bedmen.odyssey.registry.TileEntityTypeRegistry;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class SterlingSilverChestTileEntity extends ChestTileEntity {
    protected SterlingSilverChestTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public SterlingSilverChestTileEntity() {
        this(TileEntityTypeRegistry.STERLING_SILVER_CHEST.get());
    }
}
