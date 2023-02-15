package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.InfusionPedestalBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfusionPedestalRenderer extends AbstractInfusionPedestalRenderer<InfusionPedestalBlockEntity> {

    private static final ResourceLocation EXPERIENCE_ORB_LOCATION = new ResourceLocation("textures/entity/experience_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(EXPERIENCE_ORB_LOCATION);

    public InfusionPedestalRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected float getItemScale(InfusionPedestalBlockEntity blockEntity) {
        float completionInSeconds = blockEntity.getCompletionInSeconds();
        if(completionInSeconds < 1.0f){
            return 1.0f;
        } else if (completionInSeconds < 2.0f){
            return 2.0f - completionInSeconds;
        } else {
            return 0.0f;
        }
    }

}