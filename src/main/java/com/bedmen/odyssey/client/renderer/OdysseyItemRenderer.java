package com.bedmen.odyssey.client.renderer;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;

public class OdysseyItemRenderer extends ItemRenderer {
    public OdysseyItemRenderer(TextureManager textureManager, ModelManager modelManager, ItemColors itemColors, BlockEntityWithoutLevelRenderer blockEntityWithoutLevelRenderer) {
        super(textureManager, modelManager, itemColors, blockEntityWithoutLevelRenderer);
    }
}
