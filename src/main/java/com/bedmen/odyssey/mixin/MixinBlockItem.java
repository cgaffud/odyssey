package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.EffectRegistry;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockItem.class)
public abstract class MixinBlockItem extends Item {

    public MixinBlockItem(Properties properties) {
        super(properties);
    }

    @Shadow
    public ActionResultType tryPlace(BlockItemUseContext context) {return null;}

    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getPlayer().isPotionActive(EffectRegistry.BUILDING_FATIGUE.get())) return ActionResultType.FAIL;
        ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
        return !actionresulttype.isSuccessOrConsume() && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
    }

}
