package com.bedmen.odyssey.items;

import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public class OdysseyItemGroup extends ItemGroup {

    private Lazy<Item> iconLazy;
    public static final ItemGroup BUILDING_BLOCKS = new OdysseyItemGroup("oddc_building_blocks", Lazy.of(ItemRegistry.RUBY_ORE::get));
    public static final ItemGroup DECORATION_BLOCKS = new OdysseyItemGroup("oddc_decoration_blocks", Lazy.of(ItemRegistry.RESEARCH_TABLE::get));
    public static final ItemGroup MATERIALS = new OdysseyItemGroup("oddc_materials", Lazy.of(ItemRegistry.RUBY::get));
    public static final ItemGroup TOOLS = new OdysseyItemGroup("oddc_tools", Lazy.of(ItemRegistry.STERLING_SILVER_AXE::get));
    public static final ItemGroup COMBAT = new OdysseyItemGroup("oddc_combat", Lazy.of(ItemRegistry.STERLING_SILVER_SWORD::get));
    public static final ItemGroup SPAWN_EGGS = new OdysseyItemGroup("oddc_spawn_eggs", Lazy.of(ItemRegistry.ARCTIHORN_SPAWN_EGG::get));

    public OdysseyItemGroup(String label, Lazy<Item> itemLazy){
        super(label);
        this.iconLazy = itemLazy;
    }

    public ItemStack makeIcon(){
        return this.iconLazy.get().getDefaultInstance();
    }
}