package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public class OdysseyCreativeModeTab extends CreativeModeTab {

    private final Lazy<Item> iconLazy;
    public static final CreativeModeTab BUILDING_BLOCKS = new OdysseyCreativeModeTab("oddc_building_blocks", ItemRegistry.SILVER_ORE::get);
    public static final CreativeModeTab WOOD = new OdysseyCreativeModeTab("oddc_wood", ItemRegistry.PALM_LOG::get);
    public static final CreativeModeTab MATERIALS = new OdysseyCreativeModeTab("oddc_materials", ItemRegistry.RAW_SILVER::get);
    public static final CreativeModeTab TOOLS = new OdysseyCreativeModeTab("oddc_tools", ItemRegistry.STERLING_SILVER_AXE::get);
    public static final CreativeModeTab MELEE = new OdysseyCreativeModeTab("oddc_melee", ItemRegistry.COPPER_BATTLE_AXE::get);
    public static final CreativeModeTab RANGED = new OdysseyCreativeModeTab("oddc_ranged", ItemRegistry.BONE_LONG_BOW::get);
    public static final CreativeModeTab ARMOR = new OdysseyCreativeModeTab("oddc_armor", ItemRegistry.TURTLE_CHESTPLATE::get);
//    public static final CreativeModeTab MAGIC = new OdysseyCreativeModeTab("oddc_magic", ItemRegistry.PURGE_TABLET::get);
    public static final CreativeModeTab SPAWNING = new OdysseyCreativeModeTab("oddc_spawning", ItemRegistry.CAMO_CREEPER_SPAWN_EGG::get);

    public OdysseyCreativeModeTab(String label, Lazy<Item> itemLazy){
        super(label);
        this.iconLazy = itemLazy;
    }

    public ItemStack makeIcon(){
        return this.iconLazy.get().getDefaultInstance();
    }
}