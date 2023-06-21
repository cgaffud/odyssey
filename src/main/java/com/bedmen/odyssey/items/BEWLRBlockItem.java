package com.bedmen.odyssey.items;

import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

import net.minecraft.world.item.Item.Properties;

public class BEWLRBlockItem extends BlockItem {
    public BEWLRBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return OdysseyBlockEntityWithoutLevelRenderer.getInstance();
            }
        });
    }
}
