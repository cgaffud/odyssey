package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.bedmen.odyssey.network.OdysseyNetwork;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.apache.logging.log4j.Logger;

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
    @Shadow
    protected int missTime;
    private boolean alternateHands = false;

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

    private void startAttack() {
        PlayerEntity playerEntity =  this.player;
        if(EnchantmentUtil.hasVolatile(playerEntity.getItemInHand(Hand.MAIN_HAND))){
            OdysseyNetwork.CHANNEL.sendToServer(new SwungWithVolatilePacket());
        }
        if (this.missTime <= 0) {
            if (this.hitResult == null) {
                LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
                if (this.gameMode.hasMissTime()) {
                    this.missTime = 10;
                }

            } else if (!this.player.isHandsBusy()) {
                net.minecraftforge.client.event.InputEvent.ClickInputEvent inputEvent = net.minecraftforge.client.ForgeHooksClient.onClickInput(0, this.options.keyAttack, Hand.MAIN_HAND);
                if (!inputEvent.isCanceled())
                    switch(this.hitResult.getType()) {
                        case ENTITY:
                            this.gameMode.attack(this.player, ((EntityRayTraceResult)this.hitResult).getEntity());
                            break;
                        case BLOCK:
                            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)this.hitResult;
                            BlockPos blockpos = blockraytraceresult.getBlockPos();
                            if (!this.level.isEmptyBlock(blockpos)) {
                                this.gameMode.startDestroyBlock(blockpos, blockraytraceresult.getDirection());
                                break;
                            }
                        case MISS:
                            if (this.gameMode.hasMissTime()) {
                                this.missTime = 10;
                            }

                            this.player.resetAttackStrengthTicker();
                            net.minecraftforge.common.ForgeHooks.onEmptyLeftClick(this.player);
                    }

                if (inputEvent.shouldSwingHand()) {
                    if (DualWieldItem.isDuelWieldingHatchets(this.player)) {
                        this.player.swing(this.alternateHands ? Hand.OFF_HAND : Hand.MAIN_HAND);
                        this.alternateHands ^= true;
                    } else {
                        this.player.swing(Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
}
