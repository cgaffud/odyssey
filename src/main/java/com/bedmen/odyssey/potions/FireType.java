package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public enum FireType {

    // List from strongest to weakest for sorting by enum ordinal

    SOUL("minecraft", "soul", true),
    HEX(Odyssey.MOD_ID, "hex", false);

    public Material material0;
    public Material material1;

    FireType(String modid, String id, boolean isSwitched){
        this.material0 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(modid,"block/"+id+"_fire_" + (isSwitched ? 1 : 0)));
        this.material1 = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(modid,"block/"+id+"_fire_" + (isSwitched ? 0 : 1)));
    }

}
