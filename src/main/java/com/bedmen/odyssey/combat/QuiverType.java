package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.tier.OdysseyTier;
import com.bedmen.odyssey.tier.OdysseyTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;

import java.util.List;

public enum QuiverType{
    RABBIT_HIDE_QUIVER("rabbit_hide_quiver", OdysseyTiers.LEATHER, 3, 0.1f, false, List.of(), List.of()),
    RABBIT_HIDE_ROCKET_BAG("rabbit_hide_rocket_bag", OdysseyTiers.LEATHER, 2, 0.3f, true, List.of(), List.of());

    public final Tier tier;
    public final int size;
    public final float freeAmmoChance;
    public final boolean isRocketBag;
    public final ResourceLocation textureResourceLocation;
    public final InnateAspectHolder innateAspectHolder;

    QuiverType(String id, OdysseyTier odysseyTier, int size, float freeAmmoChance, boolean isRocketBag, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList){
        this.tier = odysseyTier;
        this.size = size;
        this.freeAmmoChance = freeAmmoChance;
        this.isRocketBag = isRocketBag;
        this.textureResourceLocation = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quivers/"+id+".png");
        this.innateAspectHolder = new InnateAspectHolder(abilityList, innateModifierList);
    }
}
