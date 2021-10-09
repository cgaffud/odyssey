package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.equipment.DualWieldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FirstPersonRenderer.class)
public abstract class MixinFirstPersonRenderer {

    @Shadow
    private float mainHandHeight;
    @Shadow
    private float oMainHandHeight;
    @Shadow
    private float offHandHeight;
    @Shadow
    private float oOffHandHeight;
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private ItemStack mainHandItem;
    @Shadow
    private ItemStack offHandItem;

    //Makes it so that when dual wielding hatchets the main hand doesn't move when offhand is swung
    public void tick() {
        this.oMainHandHeight = this.mainHandHeight;
        this.oOffHandHeight = this.offHandHeight;
        ClientPlayerEntity clientplayerentity = this.minecraft.player;
        ItemStack itemstack = clientplayerentity.getMainHandItem();
        ItemStack itemstack1 = clientplayerentity.getOffhandItem();
        if (ItemStack.matches(this.mainHandItem, itemstack)) {
            this.mainHandItem = itemstack;
        }

        if (ItemStack.matches(this.offHandItem, itemstack1)) {
            this.offHandItem = itemstack1;
        }

        if (clientplayerentity.isHandsBusy()) {
            this.mainHandHeight = MathHelper.clamp(this.mainHandHeight - 0.4F, 0.0F, 1.0F);
            this.offHandHeight = MathHelper.clamp(this.offHandHeight - 0.4F, 0.0F, 1.0F);
        } else {
            float f = DualWieldItem.isDuelWieldingHatchets(this.minecraft.player) ? 1.0f : clientplayerentity.getAttackStrengthScale(1.0F);
            System.out.println(f);
            boolean requipM = net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(this.mainHandItem, itemstack, clientplayerentity.inventory.selected);
            boolean requipO = net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(this.offHandItem, itemstack1, -1);

            if (!requipM && this.mainHandItem != itemstack)
                this.mainHandItem = itemstack;
            if (!requipO && this.offHandItem != itemstack1)
                this.offHandItem = itemstack1;

            this.mainHandHeight += MathHelper.clamp((!requipM ? f * f * f : 0.0F) - this.mainHandHeight, -0.4F, 0.4F);
            this.offHandHeight += MathHelper.clamp((float)(!requipO ? 1 : 0) - this.offHandHeight, -0.4F, 0.4F);
        }

        if (this.mainHandHeight < 0.1F) {
            this.mainHandItem = itemstack;
        }

        if (this.offHandHeight < 0.1F) {
            this.offHandItem = itemstack1;
        }

    }
}
