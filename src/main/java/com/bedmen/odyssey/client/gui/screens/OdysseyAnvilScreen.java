package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.inventory.OdysseyAnvilMenu;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.network.packet.AnvilRenamePacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyAnvilScreen extends ItemCombinerScreen<OdysseyAnvilMenu> {
    private static final ResourceLocation ANVIL_LOCATION = new ResourceLocation("textures/gui/container/anvil.png");
    private static final Component TOO_EXPENSIVE_TEXT = new TranslatableComponent("container.repair.expensive");
    private EditBox name;
    private final Player player;

    public OdysseyAnvilScreen(OdysseyAnvilMenu odysseyAnvilMenu, Inventory inventory, Component component) {
        super(odysseyAnvilMenu, inventory, component, ANVIL_LOCATION);
        this.player = inventory.player;
        this.titleLabelX = 60;
    }

    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    protected void subInit() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 62, j + 24, 103, 12, new TranslatableComponent("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        this.name.setEditable(false);
    }

    public void resize(Minecraft minecraft, int mouseX, int mouseY) {
        String s = this.name.getValue();
        this.init(minecraft, mouseX, mouseY);
        this.name.setValue(s);
    }

    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.name.keyPressed(keyCode, scanCode, modifiers) || this.name.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onNameChanged(String name) {
        if (!name.isEmpty()) {
            String newName = name;
            Slot slot = this.menu.getSlot(0);
            if (slot.hasItem() && !slot.getItem().hasCustomHoverName() && name.equals(slot.getItem().getHoverName().getString())) {
                newName = "";
            }

            this.menu.setItemName(newName);
            OdysseyNetwork.CHANNEL.sendToServer(new AnvilRenamePacket(newName));
        }
    }

    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.renderLabels(poseStack, mouseX, mouseY);
        int i = this.menu.getCost();
        if (i > 0) {
            int j = 8453920;
            Component component;
            if (i >= 40 && !this.minecraft.player.getAbilities().instabuild) {
                component = TOO_EXPENSIVE_TEXT;
                j = 16736352;
            } else if (!this.menu.getSlot(2).hasItem()) {
                component = null;
            } else {
                component = new TranslatableComponent("container.repair.cost", i);
                if (!this.menu.getSlot(2).mayPickup(this.player)) {
                    j = 16736352;
                }
            }

            if (component != null) {
                int k = this.imageWidth - 8 - this.font.width(component) - 2;
                int l = 69;
                fill(poseStack, k - 2, 67, this.imageWidth - 8, 79, 1325400064);
                this.font.drawShadow(poseStack, component, (float)k, 69.0F, j);
            }
        }

    }

    public void renderFg(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.name.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public void slotChanged(AbstractContainerMenu abstractContainerMenu, int slotId, ItemStack itemStack) {
        if (slotId == 0) {
            this.name.setValue(itemStack.isEmpty() ? "" : itemStack.getHoverName().getString());
            this.name.setEditable(!itemStack.isEmpty());
            this.setFocused(this.name);
        }

    }
}