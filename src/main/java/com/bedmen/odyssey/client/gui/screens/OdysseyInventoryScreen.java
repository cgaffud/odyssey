package com.bedmen.odyssey.client.gui.screens;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class OdysseyInventoryScreen extends InventoryScreen {

    private static final int PLAYER_MID_X = 51;
    private static final int PLAYER_BOT_Y = 75;
    private static final int PLAYER_WIDTH = 32;
    private static final int PLAYER_LEFT_X = PLAYER_MID_X - PLAYER_WIDTH/2;
    private static final int PLAYER_RIGHT_X = PLAYER_MID_X + PLAYER_WIDTH/2;
    private static final int PLAYER_HEIGHT = 64;
    private static final int PLAYER_TOP_Y = PLAYER_BOT_Y - PLAYER_HEIGHT;

    public OdysseyInventoryScreen(Player player) {
        super(player);
    }

    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        int adjustedMouseX = mouseX - this.leftPos;
        int adjustedMouseY = mouseY - this.topPos;
        if (this.menu.getCarried().isEmpty()
                && adjustedMouseX > PLAYER_LEFT_X
                && adjustedMouseX < PLAYER_RIGHT_X
                && adjustedMouseY > PLAYER_TOP_Y
                && adjustedMouseY < PLAYER_BOT_Y) {
            this.renderTooltip(poseStack, AspectUtil.getBuffTooltip(Minecraft.getInstance().player), Optional.empty(), mouseX, mouseY);
        } else {
            super.renderTooltip(poseStack, mouseX, mouseY);
        }
    }
}
