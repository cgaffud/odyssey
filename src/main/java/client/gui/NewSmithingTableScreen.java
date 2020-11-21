package client.gui;

import container.NewSmithingTableContainer;

import com.bedmen.odyssey.Odyssey;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NewSmithingTableScreen extends ContainerScreen<NewSmithingTableContainer> {
   private ResourceLocation guiTexture;

   public NewSmithingTableScreen(NewSmithingTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
      super(screenContainer, inv, titleIn);
      this.guiTexture = new ResourceLocation(Odyssey.MOD_ID, "textures/gui/containers/new_smith_table.png");
      
   }

   public void init() {
      super.init();
      this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
	   this.renderBackground(matrixStack);
	   super.render(matrixStack, mouseX, mouseY, partialTicks);
	   this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
   }

   protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.minecraft.getTextureManager().bindTexture(this.guiTexture);
      int i = this.guiLeft;
      int j = (this.height - this.ySize) / 2;
      this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
   }
}