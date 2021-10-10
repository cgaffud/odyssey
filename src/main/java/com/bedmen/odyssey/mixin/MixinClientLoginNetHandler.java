package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.network.play.OdysseyClientPlayNetHandler;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.login.ClientLoginNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ProtocolType;
import net.minecraft.network.login.server.SLoginSuccessPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(ClientLoginNetHandler.class)
public abstract class MixinClientLoginNetHandler {

    @Shadow
    private Consumer<ITextComponent> updateStatus;
    @Shadow
    private GameProfile localGameProfile;
    @Shadow
    private NetworkManager connection;
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private Screen parent;

    public void handleGameProfile(SLoginSuccessPacket p_147390_1_) {
        this.updateStatus.accept(new TranslationTextComponent("connect.joining"));
        this.localGameProfile = p_147390_1_.getGameProfile();
        this.connection.setProtocol(ProtocolType.PLAY);
        net.minecraftforge.fml.network.NetworkHooks.handleClientLoginSuccess(this.connection);
        this.connection.setListener(new OdysseyClientPlayNetHandler(this.minecraft, this.parent, this.connection, this.localGameProfile));
    }
}
