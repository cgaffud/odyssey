package util;

import com.bedmen.odyssey.Odyssey;

import armor.ModArmorMaterial;
import blocks.AlloyFurnaceBlock;
import blocks.BlockItemBase;
import blocks.CarvedPumpkinBlock;
import blocks.FortunelessGoldOre;
import blocks.FortunelessIronOre;
import blocks.RubyBlock;
import blocks.RubyOre;
import container.AlloyFurnaceContainer;
import entities.RubyGolemEntity;
import items.ItemBase;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import recipes.AlloyRecipe;
import tileentity.AlloyFurnaceTileEntity;
import tools.ModItemTier;

public class RegistryHandler {
	
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Odyssey.MOD_ID);
	public static DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Odyssey.MOD_ID);
	public static DeferredRegister<Block> BLOCKS_VANILLA = DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraft");
	public static DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Odyssey.MOD_ID);
	public static DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Odyssey.MOD_ID);
	public static DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Odyssey.MOD_ID);
	public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES , Odyssey.MOD_ID);
	
	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCKS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
		RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	//Items
	public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", ItemBase::new);
	
	//Tools
	public static final RegistryObject<SwordItem> RUBY_SWORD = ITEMS.register("ruby_sword", () -> new SwordItem(ModItemTier.RUBY, 5, -2.4f, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<PickaxeItem> RUBY_PICKAXE = ITEMS.register("ruby_pickaxe", () -> new PickaxeItem(ModItemTier.RUBY, 4, -2.8f, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<AxeItem> RUBY_AXE = ITEMS.register("ruby_axe", () -> new AxeItem(ModItemTier.RUBY, 7.5f, -3.05f, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<ShovelItem> RUBY_SHOVEL = ITEMS.register("ruby_shovel", () -> new ShovelItem(ModItemTier.RUBY, 3.5f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)));
	public static final RegistryObject<HoeItem> RUBY_HOE = ITEMS.register("ruby_hoe", () -> new HoeItem(ModItemTier.RUBY, 0, -1.0f, new Item.Properties().group(ItemGroup.TOOLS)));
	
	//Armor
	public static final RegistryObject<ArmorItem> RUBY_HELMET = ITEMS.register("ruby_helmet", () -> new ArmorItem(ModArmorMaterial.RUBY, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<ArmorItem> RUBY_CHESTPLATE = ITEMS.register("ruby_chestplate", () -> new ArmorItem(ModArmorMaterial.RUBY, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<ArmorItem> RUBY_LEGGINGS = ITEMS.register("ruby_leggings", () -> new ArmorItem(ModArmorMaterial.RUBY, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<ArmorItem> RUBY_BOOTS = ITEMS.register("ruby_boots", () -> new ArmorItem(ModArmorMaterial.RUBY, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
	
	//Blocks
	public static final RegistryObject<Block> RUBY_BLOCK = BLOCKS.register("ruby_block", RubyBlock::new);
	public static final RegistryObject<Block> RUBY_ORE = BLOCKS.register("ruby_ore", RubyOre::new);
	public static final RegistryObject<Block> FORTUNELESS_IRON_ORE = BLOCKS.register("fortuneless_iron_ore", FortunelessIronOre::new);
	public static final RegistryObject<Block> FORTUNELESS_GOLD_ORE = BLOCKS.register("fortuneless_gold_ore", FortunelessGoldOre::new);
	public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", AlloyFurnaceBlock::new);
	public static final RegistryObject<Block> CARVED_PUMPKIN = BLOCKS_VANILLA.register("carved_pumpkin", CarvedPumpkinBlock::new);
	
	//Block Items
	public static final RegistryObject<Item> RUBY_BLOCK_ITEM = ITEMS.register("ruby_block", () -> new BlockItemBase(RUBY_BLOCK.get()));
	public static final RegistryObject<Item> RUBY_ORE_ITEM = ITEMS.register("ruby_ore", () -> new BlockItemBase(RUBY_ORE.get()));
	public static final RegistryObject<Item> FORTUNELESS_IRON_ORE_ITEM = ITEMS.register("fortuneless_iron_ore", () -> new BlockItemBase(FORTUNELESS_IRON_ORE.get()));
	public static final RegistryObject<Item> FORTUNELESS_GOLD_ORE_ITEM = ITEMS.register("fortuneless_gold_ore", () -> new BlockItemBase(FORTUNELESS_GOLD_ORE.get()));
	public static final RegistryObject<Item> ALLOY_FURNACE_ITEM = ITEMS.register("alloy_furnace", () -> new BlockItemBase(ALLOY_FURNACE.get()));
	public static final RegistryObject<Item> CARVED_PUMPKIN_ITEM = ITEMS_VANILLA.register("carved_pumpkin", () -> new BlockItemBase(CARVED_PUMPKIN.get()));
	
	//Tile Entities
	public static final RegistryObject<TileEntityType<AlloyFurnaceTileEntity>> ALLOY_FURNACE_TILE_ENTITY_TYPE = TILE_ENTITY_TYPES.register("alloy_furnace", () -> TileEntityType.Builder.create(AlloyFurnaceTileEntity::new, ALLOY_FURNACE.get()).build(null));
	
	//Recipe Type
	public static final RegistryObject<IRecipeSerializer<AlloyRecipe>> ALLOYING = RECIPES.register("alloying", () -> new AlloyRecipe.Serializer());
	
	//Container Type
	public static final RegistryObject<ContainerType<AlloyFurnaceContainer>> ALLOY_FURNACE_CONTAINER = CONTAINER_TYPES.register("alloy_furnace", () -> new ContainerType<AlloyFurnaceContainer>(AlloyFurnaceContainer::new));
	
	//Entity Type
	public static final RegistryObject<EntityType<RubyGolemEntity>> RUBY_GOLEM = ENTITY_TYPES.register("ruby_golem", () -> EntityType.Builder.<RubyGolemEntity>create(RubyGolemEntity::new, EntityClassification.MISC).size(1.4f,2.7f).trackingRange(10).build(new ResourceLocation(Odyssey.MOD_ID, "ruby_golem").toString()));
	
	
}