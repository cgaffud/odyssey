package com.bedmen.odyssey.world.gen.layer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IBishopTransformer;

public enum AddSpecialIslandLayer implements IBishopTransformer {
    INSTANCE;

    public int apply(INoiseRandom p_202792_1_, int p_202792_2_, int p_202792_3_, int p_202792_4_, int p_202792_5_, int p_202792_6_) {
        int i = p_202792_1_.nextRandom(100);
        if (isShallowOcean(p_202792_6_) && isShallowOcean(p_202792_5_) && isShallowOcean(p_202792_2_) && isShallowOcean(p_202792_4_) && isShallowOcean(p_202792_3_)) {
            if(i == 0){
                return 14;
            } else if(1 <= i && i <= 3) {
                return 52;
            }
        }
        return p_202792_6_;
    }

    protected static boolean isShallowOcean(int p_203631_0_) {
        return p_203631_0_ == 44 || p_203631_0_ == 45 || p_203631_0_ == 0 || p_203631_0_ == 46 || p_203631_0_ == 10;
    }

    public static boolean isWarmShallowOcean(int p_203631_0_) {
        return p_203631_0_ == 44;
    }
}