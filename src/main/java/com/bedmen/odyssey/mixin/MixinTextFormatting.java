package com.bedmen.odyssey.mixin;
import com.bedmen.odyssey.util.OdysseyTextFormatting;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.IExtensibleEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(TextFormatting.class)
public class MixinTextFormatting implements IExtensibleEnum {
    private static TextFormatting create(String name, String p_i49745_3_, char p_i49745_4_, int p_i49745_5_, @Nullable Integer p_i49745_6_) {
        throw new IllegalStateException("Enum not extended");
    }

    @Overwrite
    public static TextFormatting[] values(){
        return new TextFormatting[]{
                TextFormatting.BLACK,
                TextFormatting.DARK_BLUE,
                TextFormatting.DARK_GREEN,
                TextFormatting.DARK_AQUA,
                TextFormatting.DARK_RED,
                TextFormatting.DARK_PURPLE,
                TextFormatting.GOLD,
                TextFormatting.GRAY,
                TextFormatting.DARK_GRAY,
                TextFormatting.BLUE,
                TextFormatting.GREEN,
                TextFormatting.AQUA,
                TextFormatting.RED,
                TextFormatting.LIGHT_PURPLE,
                TextFormatting.YELLOW,
                TextFormatting.WHITE,
                TextFormatting.OBFUSCATED,
                TextFormatting.BOLD,
                TextFormatting.STRIKETHROUGH,
                TextFormatting.UNDERLINE,
                TextFormatting.ITALIC,
                TextFormatting.RESET,
                OdysseyTextFormatting.ORANGE,
                OdysseyTextFormatting.COPPER};
    }
}
