package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.client.renderer.entity.model.AbandonedIronGolemModel;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolemEntity;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AbandonedIronGolemCracksLayer extends LayerRenderer<AbandonedIronGolemEntity, AbandonedIronGolemModel<AbandonedIronGolemEntity>> {
    private static final Map<IronGolemEntity.Cracks, ResourceLocation> resourceLocations = ImmutableMap.of(IronGolemEntity.Cracks.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"), IronGolemEntity.Cracks.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), IronGolemEntity.Cracks.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));

    public AbandonedIronGolemCracksLayer(IEntityRenderer<AbandonedIronGolemEntity, AbandonedIronGolemModel<AbandonedIronGolemEntity>> p_i226040_1_) {
        super(p_i226040_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, AbandonedIronGolemEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!pLivingEntity.isInvisible()) {
            IronGolemEntity.Cracks irongolementity$cracks = pLivingEntity.getCrackiness();
            if (irongolementity$cracks != IronGolemEntity.Cracks.NONE) {
                ResourceLocation resourcelocation = resourceLocations.get(irongolementity$cracks);
                renderColoredCutoutModel(this.getParentModel(), resourcelocation, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}