package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.world.gen.layer.AddSpecialIslandLayer;
import com.bedmen.odyssey.world.gen.layer.OdysseyBiomeLayer;
import com.bedmen.odyssey.world.gen.layer.OdysseyShoreLayer;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.LongFunction;

@Mixin(LayerUtil.class)
public abstract class MixinLayerUtil {

    @Shadow
    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> zoom(long p_202829_0_, IAreaTransformer1 p_202829_2_, IAreaFactory<T> p_202829_3_, int p_202829_4_, LongFunction<C> p_202829_5_) {return null;}

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getDefaultLayer(boolean p_237216_0_, int p_237216_1_, int p_237216_2_, LongFunction<C> p_237216_3_) {
        IAreaFactory<T> iareafactory = IslandLayer.INSTANCE.run(p_237216_3_.apply(1L));
        iareafactory = ZoomLayer.FUZZY.run(p_237216_3_.apply(2000L), iareafactory);
        iareafactory = AddIslandLayer.INSTANCE.run(p_237216_3_.apply(1L), iareafactory);
        iareafactory = ZoomLayer.NORMAL.run(p_237216_3_.apply(2001L), iareafactory);
        iareafactory = AddIslandLayer.INSTANCE.run(p_237216_3_.apply(2L), iareafactory);
        iareafactory = AddIslandLayer.INSTANCE.run(p_237216_3_.apply(50L), iareafactory);
        iareafactory = AddIslandLayer.INSTANCE.run(p_237216_3_.apply(70L), iareafactory);
        iareafactory = RemoveTooMuchOceanLayer.INSTANCE.run(p_237216_3_.apply(2L), iareafactory);
        IAreaFactory<T> iareafactory1 = OceanLayer.INSTANCE.run(p_237216_3_.apply(2L));
        iareafactory1 = zoom(2001L, ZoomLayer.NORMAL, iareafactory1, 6, p_237216_3_);
        iareafactory = AddSnowLayer.INSTANCE.run(p_237216_3_.apply(2L), iareafactory);
        iareafactory = AddIslandLayer.INSTANCE.run(p_237216_3_.apply(3L), iareafactory);
        iareafactory = EdgeLayer.CoolWarm.INSTANCE.run(p_237216_3_.apply(2L), iareafactory);
        iareafactory = EdgeLayer.HeatIce.INSTANCE.run(p_237216_3_.apply(2L), iareafactory);
        iareafactory = EdgeLayer.Special.INSTANCE.run(p_237216_3_.apply(3L), iareafactory);
        iareafactory = ZoomLayer.NORMAL.run(p_237216_3_.apply(2002L), iareafactory);
        iareafactory = ZoomLayer.NORMAL.run(p_237216_3_.apply(2003L), iareafactory);
        iareafactory = AddIslandLayer.INSTANCE.run(p_237216_3_.apply(4L), iareafactory);
        iareafactory = AddSpecialIslandLayer.INSTANCE.run(p_237216_3_.apply(5L), iareafactory);
        iareafactory = DeepOceanLayer.INSTANCE.run(p_237216_3_.apply(4L), iareafactory);
        iareafactory = zoom(1000L, ZoomLayer.NORMAL, iareafactory, 0, p_237216_3_);
        IAreaFactory<T> lvt_6_1_ = zoom(1000L, ZoomLayer.NORMAL, iareafactory, 0, p_237216_3_);
        lvt_6_1_ = StartRiverLayer.INSTANCE.run(p_237216_3_.apply(100L), lvt_6_1_);
        IAreaFactory<T> lvt_7_1_ = (new OdysseyBiomeLayer(p_237216_0_)).run(p_237216_3_.apply(200L), iareafactory);
        lvt_7_1_ = AddBambooForestLayer.INSTANCE.run(p_237216_3_.apply(1001L), lvt_7_1_);
        lvt_7_1_ = zoom(1000L, ZoomLayer.NORMAL, lvt_7_1_, 2, p_237216_3_);
        lvt_7_1_ = EdgeBiomeLayer.INSTANCE.run(p_237216_3_.apply(1000L), lvt_7_1_);
        IAreaFactory<T> lvt_8_1_ = zoom(1000L, ZoomLayer.NORMAL, lvt_6_1_, 2, p_237216_3_);
        lvt_7_1_ = HillsLayer.INSTANCE.run(p_237216_3_.apply(1000L), lvt_7_1_, lvt_8_1_);
        lvt_6_1_ = zoom(1000L, ZoomLayer.NORMAL, lvt_6_1_, 2, p_237216_3_);
        lvt_6_1_ = zoom(1000L, ZoomLayer.NORMAL, lvt_6_1_, p_237216_2_, p_237216_3_);
        lvt_6_1_ = RiverLayer.INSTANCE.run(p_237216_3_.apply(1L), lvt_6_1_);
        lvt_6_1_ = SmoothLayer.INSTANCE.run(p_237216_3_.apply(1000L), lvt_6_1_);
        lvt_7_1_ = RareBiomeLayer.INSTANCE.run(p_237216_3_.apply(1001L), lvt_7_1_);

        for(int i = 0; i < p_237216_1_; ++i) {
            lvt_7_1_ = ZoomLayer.NORMAL.run(p_237216_3_.apply((long)(1000 + i)), lvt_7_1_);
            if (i == 0) {
                lvt_7_1_ = AddIslandLayer.INSTANCE.run(p_237216_3_.apply(3L), lvt_7_1_);
            }

            if (i == 1 || p_237216_1_ == 1) {
                lvt_7_1_ = OdysseyShoreLayer.INSTANCE.run(p_237216_3_.apply(1000L), lvt_7_1_);
            }
        }

        lvt_7_1_ = SmoothLayer.INSTANCE.run(p_237216_3_.apply(1000L), lvt_7_1_);
        lvt_7_1_ = MixRiverLayer.INSTANCE.run(p_237216_3_.apply(100L), lvt_7_1_, lvt_6_1_);
        return MixOceansLayer.INSTANCE.run(p_237216_3_.apply(100L), lvt_7_1_, iareafactory1);
    }
}
