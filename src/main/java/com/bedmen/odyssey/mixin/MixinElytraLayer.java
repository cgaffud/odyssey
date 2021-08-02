package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ElytraLayer.class)
public abstract class MixinElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {

    private static final ResourceLocation ELYTRA_WINGS_LOCATION = new ResourceLocation("textures/entity/elytra.png");
    private static final ResourceLocation ZEPHYR_WINGS_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/zephyr_wings.png");

    public MixinElytraLayer(IEntityRenderer<T, M> p_i50926_1_) {
        super(p_i50926_1_);
    }

    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.getItem() == Items.ELYTRA || EnchantmentUtil.hasGliding(entity);

    }

    public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
        if(stack.getItem() == ItemRegistry.ZEPHYR_CHESTPLATE.get())
            return ZEPHYR_WINGS_LOCATION;
        return ELYTRA_WINGS_LOCATION;
    }

}
