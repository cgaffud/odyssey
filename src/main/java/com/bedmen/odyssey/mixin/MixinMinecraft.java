package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.util.EffectRegistry;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.apache.logging.log4j.Logger;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    public PlayerController playerController;
    @Shadow
    private int rightClickDelayTimer;
    @Shadow
    public ClientPlayerEntity player;
    @Shadow
    public RayTraceResult objectMouseOver;
    @Shadow
    private static Logger LOGGER;
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    public GameRenderer gameRenderer;
    @Shadow
    public ClientWorld world;

    //@Inject(method = "rightClickMouse", at = @At(value = "HEAD"))
    //private void startClient(CallbackInfo ci) {
    //    System.out.println("TemplateClient Working!");
    //}

    private void rightClickMouse() {
        if (!this.playerController.getIsHittingBlock()) {
            int rightClickDelayTimer = 4;
            if(player.isPotionActive(EffectRegistry.CONSTRUCTION.get())){
                int amplifier = player.getActivePotionEffect(EffectRegistry.CONSTRUCTION.get()).getAmplifier();
                if(amplifier == 0) rightClickDelayTimer = 2;
                else if(amplifier == 1) rightClickDelayTimer = 1;
            }
            this.rightClickDelayTimer = rightClickDelayTimer;
            if (!this.player.isRowingBoat()) {
                if (this.objectMouseOver == null) {
                    LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }

                for(Hand hand : Hand.values()) {
                    net.minecraftforge.client.event.InputEvent.ClickInputEvent inputEvent = net.minecraftforge.client.ForgeHooksClient.onClickInput(1, this.gameSettings.keyBindUseItem, hand);
                    if (inputEvent.isCanceled()) {
                        if (inputEvent.shouldSwingHand()) this.player.swingArm(hand);
                        return;
                    }
                    ItemStack itemstack = this.player.getHeldItem(hand);
                    if (this.objectMouseOver != null) {
                        switch(this.objectMouseOver.getType()) {
                            case ENTITY:
                                EntityRayTraceResult entityraytraceresult = (EntityRayTraceResult)this.objectMouseOver;
                                Entity entity = entityraytraceresult.getEntity();
                                ActionResultType actionresulttype = this.playerController.interactWithEntity(this.player, entity, entityraytraceresult, hand);
                                if (!actionresulttype.isSuccessOrConsume()) {
                                    actionresulttype = this.playerController.interactWithEntity(this.player, entity, hand);
                                }

                                if (actionresulttype.isSuccessOrConsume()) {
                                    if (actionresulttype.isSuccess()) {
                                        if (inputEvent.shouldSwingHand())
                                            this.player.swingArm(hand);
                                    }

                                    return;
                                }
                                break;
                            case BLOCK:
                                BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)this.objectMouseOver;
                                int i = itemstack.getCount();
                                ActionResultType actionresulttype1 = this.playerController.func_217292_a(this.player, this.world, hand, blockraytraceresult);
                                if (actionresulttype1.isSuccessOrConsume()) {
                                    if (actionresulttype1.isSuccess()) {
                                        if (inputEvent.shouldSwingHand())
                                            this.player.swingArm(hand);
                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || this.playerController.isInCreativeMode())) {
                                            this.gameRenderer.itemRenderer.resetEquippedProgress(hand);
                                        }
                                    }

                                    return;
                                }

                                if (actionresulttype1 == ActionResultType.FAIL) {
                                    return;
                                }
                        }
                    }

                    if (itemstack.isEmpty() && (this.objectMouseOver == null || this.objectMouseOver.getType() == RayTraceResult.Type.MISS))
                        net.minecraftforge.common.ForgeHooks.onEmptyClick(this.player, hand);

                    if (!itemstack.isEmpty()) {
                        ActionResultType actionresulttype2 = this.playerController.processRightClick(this.player, this.world, hand);
                        if (actionresulttype2.isSuccessOrConsume()) {
                            if (actionresulttype2.isSuccess()) {
                                this.player.swingArm(hand);
                            }

                            this.gameRenderer.itemRenderer.resetEquippedProgress(hand);
                            return;
                        }
                    }
                }

            }
        }
    }
}
