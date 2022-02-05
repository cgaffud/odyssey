package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.OdysseyChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraftforge.common.IExtensibleEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(ChatFormatting.class)
public class MixinChatFormatting implements IExtensibleEnum {
    private static ChatFormatting[] VALUES = null;
    private static ChatFormatting create(String name, String p_i49745_3_, char p_i49745_4_, int p_i49745_5_, @Nullable Integer p_i49745_6_) {
        throw new IllegalStateException("Enum not extended");
    }

    /**
     * @author JemBren
     * @reason To add more ChatFormatting colors
     */
    @Overwrite
    public static ChatFormatting[] values(){
        if(VALUES == null){
            VALUES = new ChatFormatting[]{
                    ChatFormatting.BLACK,
                    ChatFormatting.DARK_BLUE,
                    ChatFormatting.DARK_GREEN,
                    ChatFormatting.DARK_AQUA,
                    ChatFormatting.DARK_RED,
                    ChatFormatting.DARK_PURPLE,
                    ChatFormatting.GOLD,
                    ChatFormatting.GRAY,
                    ChatFormatting.DARK_GRAY,
                    ChatFormatting.BLUE,
                    ChatFormatting.GREEN,
                    ChatFormatting.AQUA,
                    ChatFormatting.RED,
                    ChatFormatting.LIGHT_PURPLE,
                    ChatFormatting.YELLOW,
                    ChatFormatting.WHITE,
                    ChatFormatting.OBFUSCATED,
                    ChatFormatting.BOLD,
                    ChatFormatting.STRIKETHROUGH,
                    ChatFormatting.UNDERLINE,
                    ChatFormatting.ITALIC,
                    ChatFormatting.RESET,
                    OdysseyChatFormatting.COPPER,
                    OdysseyChatFormatting.SILVER,
                    OdysseyChatFormatting.ODYSSEY_GOLD,
                    OdysseyChatFormatting.LAVENDER};
        }
        return VALUES;
    }
}
