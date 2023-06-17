package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.world.gen.biome.weather.OdysseyPrecipitation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.IExtensibleEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Biome.Precipitation.class)
public abstract class MixinPrecipitation implements IExtensibleEnum {
    private static Biome.Precipitation[] VALUES = null;
    private static Biome.Precipitation create(String name, String name2) {
        throw new IllegalStateException("Enum not extended");
    }

    /**
     * @author JemBren
     * @reason To add more weather types
     */
    @Overwrite
    public static Biome.Precipitation[] values(){
        if(VALUES == null){
            VALUES = new Biome.Precipitation[]{
                    Biome.Precipitation.NONE,
                    Biome.Precipitation.RAIN,
                    Biome.Precipitation.SNOW,
                    OdysseyPrecipitation.BLIZZARD};
        }
        return VALUES;
    }
}
