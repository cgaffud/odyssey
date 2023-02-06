package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

public enum ArrowType{
    FLINT(ItemRegistry.ARROW::get, 5.0d, new ResourceLocation("textures/entity/projectiles/arrow.png")),
    SPIDER_FANG("spider_fang", ItemRegistry.SPIDER_FANG_ARROW::get, 5d, List.of(new AspectInstance(Aspects.PROJECTILE_POISON_DAMAGE, 2))),
    WEAVER_FANG("weaver_fang", ItemRegistry.WEAVER_FANG_ARROW::get, 5.5, List.of(new AspectInstance(Aspects.PROJECTILE_COBWEB_CHANCE, 0.2f))),
    CLOVER_STONE("clover_stone", ItemRegistry.CLOVER_STONE_ARROW::get, 6d, List.of(new AspectInstance(Aspects.PROJECTILE_LOOTING_LUCK, 1))),
    AMETHYST("amethyst", ItemRegistry.AMETHYST_ARROW::get, 6.0d, List.of());

    private final Lazy<Item> lazyItem;
    public final double damage;
    public final InnateAspectHolder innateAspectHolder;
    public final ResourceLocation resourceLocation;

    ArrowType(Lazy<Item> lazyItem, double damage, ResourceLocation resourceLocation){
        this.lazyItem = lazyItem;
        this.damage = damage;
        this.innateAspectHolder = new InnateAspectHolder(List.of(), List.of());
        this.resourceLocation = resourceLocation;
    }

    ArrowType(String id, Lazy<Item> lazyItem, double damage, List<AspectInstance> innateAspectList){
        this.lazyItem = lazyItem;
        this.damage = damage;
        this.innateAspectHolder = new InnateAspectHolder(List.of(), innateAspectList);
        this.resourceLocation = new ResourceLocation(Odyssey.MOD_ID, String.format("textures/entity/projectiles/%s_arrow.png", id));
    }

    public Item getItem(){
        return this.lazyItem.get();
    }
}