package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.odyssey_versions.OdysseyMapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraftforge.common.IExtensibleEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MapDecoration.Type.class)
public abstract class MixinMapDecorationType implements IExtensibleEnum {
    private static MapDecoration.Type[] VALUES = null;
    private static MapDecoration.Type create(String name, boolean renderedOnFrame, int mapColor, boolean trackCount) {
        throw new IllegalStateException("Enum not extended");
    }

    /**
     * @author JemBren
     * @reason To add more map icons
     */
    @Overwrite
    public static MapDecoration.Type[] values(){
        if(VALUES == null){
            VALUES = new MapDecoration.Type[]{
                    MapDecoration.Type.PLAYER,
                    MapDecoration.Type.FRAME,
                    MapDecoration.Type.RED_MARKER,
                    MapDecoration.Type.BLUE_MARKER,
                    MapDecoration.Type.TARGET_X,
                    MapDecoration.Type.TARGET_POINT,
                    MapDecoration.Type.PLAYER_OFF_MAP,
                    MapDecoration.Type.PLAYER_OFF_LIMITS,
                    MapDecoration.Type.MANSION,
                    MapDecoration.Type.MONUMENT,
                    MapDecoration.Type.BANNER_WHITE,
                    MapDecoration.Type.BANNER_ORANGE,
                    MapDecoration.Type.BANNER_MAGENTA,
                    MapDecoration.Type.BANNER_LIGHT_BLUE,
                    MapDecoration.Type.BANNER_YELLOW,
                    MapDecoration.Type.BANNER_LIME,
                    MapDecoration.Type.BANNER_PINK,
                    MapDecoration.Type.BANNER_GRAY,
                    MapDecoration.Type.BANNER_LIGHT_GRAY,
                    MapDecoration.Type.BANNER_CYAN,
                    MapDecoration.Type.BANNER_PURPLE,
                    MapDecoration.Type.BANNER_BLUE,
                    MapDecoration.Type.BANNER_BROWN,
                    MapDecoration.Type.BANNER_GREEN,
                    MapDecoration.Type.BANNER_RED,
                    MapDecoration.Type.BANNER_BLACK,
                    MapDecoration.Type.RED_X,
                    OdysseyMapItem.DecorationType.COVEN_HUT};
        }
        return VALUES;
    }
}
