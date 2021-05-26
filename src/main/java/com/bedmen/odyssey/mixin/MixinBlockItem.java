package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.EffectRegistry;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.item.Item.Properties;

@Mixin(BlockItem.class)
public abstract class MixinBlockItem extends Item {

    public MixinBlockItem(Properties properties) {
        super(properties);
    }

    @Shadow
    public ActionResultType place(BlockItemUseContext context) {return null;}

    public ActionResultType useOn(ItemUseContext context) {
        if(context.getPlayer().hasEffect(EffectRegistry.BUILDING_FATIGUE.get())) return ActionResultType.FAIL;
        ActionResultType actionresulttype = this.place(new BlockItemUseContext(context));
        return !actionresulttype.consumesAction() && this.isEdible() ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : actionresulttype;
    }

}
