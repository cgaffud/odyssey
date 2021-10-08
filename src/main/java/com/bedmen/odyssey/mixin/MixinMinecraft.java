package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.SneakingPacket;
import com.bedmen.odyssey.network.packet.SwungWithVolatilePacket;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    public PlayerController gameMode;
    @Shadow
    private int rightClickDelay;
    @Shadow
    public ClientPlayerEntity player;
    @Shadow
    public RayTraceResult hitResult;
    @Shadow
    private static Logger LOGGER;
    @Shadow
    public GameSettings options;
    @Shadow
    public GameRenderer gameRenderer;
    @Shadow
    public ClientWorld level;

    //@Inject(method = "rightClickMouse", at = @At(value = "HEAD"))
    //private void startClient(CallbackInfo ci) {
    //    System.out.println("TemplateClient Working!");
    //}

    //Added Construction effect
    private void startUseItem() {
        if (!this.gameMode.isDestroying()) {
            int rightClickDelay = 4;
            if(player.hasEffect(EffectRegistry.CONSTRUCTION.get())){
                rightClickDelay >>= (player.getEffect(EffectRegistry.CONSTRUCTION.get()).getAmplifier()+1);
            }
            this.rightClickDelay = rightClickDelay;
            if (!this.player.isHandsBusy()) {
                if (this.hitResult == null) {
                    LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }

                for(Hand hand : Hand.values()) {
                    net.minecraftforge.client.event.InputEvent.ClickInputEvent inputEvent = net.minecraftforge.client.ForgeHooksClient.onClickInput(1, this.options.keyUse, hand);
                    if (inputEvent.isCanceled()) {
                        if (inputEvent.shouldSwingHand()) this.player.swing(hand);
                        return;
                    }
                    ItemStack itemstack = this.player.getItemInHand(hand);
                    if (this.hitResult != null) {
                        switch(this.hitResult.getType()) {
                            case ENTITY:
                                EntityRayTraceResult entityraytraceresult = (EntityRayTraceResult)this.hitResult;
                                Entity entity = entityraytraceresult.getEntity();
                                ActionResultType actionresulttype = this.gameMode.interactAt(this.player, entity, entityraytraceresult, hand);
                                if (!actionresulttype.consumesAction()) {
                                    actionresulttype = this.gameMode.interact(this.player, entity, hand);
                                }

                                if (actionresulttype.consumesAction()) {
                                    if (actionresulttype.shouldSwing()) {
                                        if (inputEvent.shouldSwingHand())
                                            this.player.swing(hand);
                                    }

                                    return;
                                }
                                break;
                            case BLOCK:
                                BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)this.hitResult;
                                int i = itemstack.getCount();
                                ActionResultType actionresulttype1 = this.gameMode.useItemOn(this.player, this.level, hand, blockraytraceresult);
                                if (actionresulttype1.consumesAction()) {
                                    if (actionresulttype1.shouldSwing()) {
                                        if (inputEvent.shouldSwingHand())
                                            this.player.swing(hand);
                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || this.gameMode.hasInfiniteItems())) {
                                            this.gameRenderer.itemInHandRenderer.itemUsed(hand);
                                        }
                                    }

                                    return;
                                }

                                if (actionresulttype1 == ActionResultType.FAIL) {
                                    return;
                                }
                        }
                    }

                    if (itemstack.isEmpty() && (this.hitResult == null || this.hitResult.getType() == RayTraceResult.Type.MISS))
                        net.minecraftforge.common.ForgeHooks.onEmptyClick(this.player, hand);

                    if (!itemstack.isEmpty()) {
                        ActionResultType actionresulttype2 = this.gameMode.useItem(this.player, this.level, hand);
                        if (actionresulttype2.consumesAction()) {
                            if (actionresulttype2.shouldSwing()) {
                                this.player.swing(hand);
                            }

                            this.gameRenderer.itemInHandRenderer.itemUsed(hand);
                            return;
                        }
                    }
                }

            }
        }
    }

    @Inject(method = "startAttack", at = @At(value = "HEAD"))
    private void startStartAttack(CallbackInfo ci) {
        PlayerEntity playerEntity =  this.player;
        if(EnchantmentUtil.hasVolatile(playerEntity.getItemInHand(Hand.MAIN_HAND))){
            OdysseyNetwork.CHANNEL.sendToServer(new SwungWithVolatilePacket());
        }
    }
}
