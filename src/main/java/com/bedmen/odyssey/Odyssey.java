package com.bedmen.odyssey;

import com.bedmen.odyssey.blocks.INeedsToRegisterRenderType;
import com.bedmen.odyssey.blocks.OdysseyWoodType;
import com.bedmen.odyssey.client.gui.*;
import com.bedmen.odyssey.client.renderer.entity.renderer.*;
import com.bedmen.odyssey.client.renderer.tileentity.*;
import com.bedmen.odyssey.container.OdysseyPlayerContainer;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBodyEntity;
import com.bedmen.odyssey.entity.boss.MineralLeviathanEntity;
import com.bedmen.odyssey.entity.boss.PermafrostEntity;
import com.bedmen.odyssey.entity.monster.ArctihornEntity;
import com.bedmen.odyssey.entity.monster.BabySkeletonEntity;
import com.bedmen.odyssey.entity.monster.LupineEntity;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.OdysseySpawnEggItem;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.potions.OdysseyPotions;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.trades.OdysseyTrades;
import com.bedmen.odyssey.util.CompostUtil;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.world.gen.OdysseyFeatureGen;
import com.bedmen.odyssey.world.gen.OdysseyOreGen;
import com.bedmen.odyssey.world.spawn.OdysseyBiomeEntitySpawn;
import com.bedmen.odyssey.world.spawn.OdysseyStructureEntitySpawn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("oddc")
@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Odyssey
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "oddc";

    public Odyssey() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        BlockRegistry.init();
        ItemRegistry.init();
        AttributeRegistry.init();
        BiomeRegistry.init();
        ContainerRegistry.init();
        DataSerializerRegistry.init();
        EffectRegistry.init();
        EnchantmentRegistry.init();
        EntityTypeRegistry.init();
        FeatureRegistry.init();
        PotionRegistry.init();
        RecipeRegistry.init();
        SoundEventRegistry.init();
        TileEntityTypeRegistry.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        OdysseyOreGen.registerOres();
        OdysseyFeatureGen.registerFeatures();
        OdysseyBiomeEntitySpawn.registerSpawners();
        OdysseyStructureEntitySpawn.registerSpawners();
        OdysseyPotions.addBrewingRecipes();
        CompostUtil.addCompostingRecipes();
        OdysseyTrades.addTrades();
        OdysseyNetwork.init();
        BiomeRegistry.register();

        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.LUPINE.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LupineEntity::predicate);
        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.ARCTIHORN.get(),EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ArctihornEntity::predicate);
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        //Item Model Properties
        for(Block block : ForgeRegistries.BLOCKS.getValues()){
            if(block instanceof INeedsToRegisterRenderType){
                RenderTypeLookup.setRenderLayer(block, ((INeedsToRegisterRenderType) block).getRenderType());
            }
        }

        //Block Render Types
        for(Item item : ForgeRegistries.ITEMS.getValues()){
            if(item instanceof INeedsToRegisterItemModelProperty){
                ((INeedsToRegisterItemModelProperty) item).registerItemModelProperties();
            }
        }

        //Tile Entity Renderings
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.STERLING_SILVER_CHEST.get(), SterlingSilverChestTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.SIGN.get(), OdysseySignTileEntityRenderer::new);

        //Container Screens
        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
        ScreenManager.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
        ScreenManager.register(ContainerRegistry.RECYCLE_FURNACE.get(), RecycleFurnaceScreen::new);
        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        ScreenManager.register(ContainerRegistry.GRINDSTONE.get(), OdysseyGrindstoneScreen::new);
        ScreenManager.register(ContainerRegistry.ANVIL.get(), OdysseyAnvilScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER3.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER5.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER7.get(), QuiverScreen::new);
        ScreenManager.register(ContainerRegistry.QUIVER9.get(), QuiverScreen::new);

        //Mob Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.LUPINE.get(), LupineRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BABY_SKELETON.get(), BabySkeletonRenderer::new);

        //Boss Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST.get(), PermafrostRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanSegmentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanSegmentRenderer::new);

        //Projectile Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.TRIDENT.get(), OdysseyTridentRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST_ICICLE.get(), PermafrostIcicleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ARROW.get(), OdysseyArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BOOMERANG.get(), BoomerangRenderer::new);

        //Boat Renderings
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BOAT.get(), OdysseyBoatRenderer::new);

        //Wood Types
        Atlases.addWoodType(OdysseyWoodType.PALM);
    }

    //Fix Armor Max Value
    @SubscribeEvent
    public static void onRegisterAttributes(final RegistryEvent.Register<Attribute> event){
        ((RangedAttribute)Attributes.ARMOR).maxValue = 80.0d;
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
        OdysseySpawnEggItem.initSpawnEggs();
    }

    @SubscribeEvent
    public static void onRegisterEnchantments(final RegistryEvent.Register<Enchantment> event){
        EquipmentArmorItem.initEquipment();
        EquipmentMeleeItem.initEquipment();
        EquipmentItem.initEquipment();
        EquipmentPickaxeItem.initEquipment();
        EquipmentHoeItem.initEquipment();
        EquipmentShovelItem.initEquipment();
        EquipmentAxeItem.initEquipment();

        EnchantmentUtil.init();
    }

    @SubscribeEvent
    public static void onTextureStitch(final TextureStitchEvent.Pre event){
        event.addSprite(OdysseyItemStackTileEntityRenderer.LEVIATHAN_SHIELD_BASE_LOCATION);
        event.addSprite(OdysseyItemStackTileEntityRenderer.LEVIATHAN_SHIELD_BASE_NOPATTERN_LOCATION);
        event.addSprite(OdysseyPlayerContainer.EMPTY_SLOT_TRINKET);
        event.addSprite(SterlingSilverChestTileEntityRenderer.SINGLE_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.SINGLE_LOCKED_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.LEFT_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.LEFT_LOCKED_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.RIGHT_RESOURCE_LOCATION);
        event.addSprite(SterlingSilverChestTileEntityRenderer.RIGHT_LOCKED_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.ACTIVE_SHELL_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.WIND_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.VERTICAL_WIND_RESOURCE_LOCATION);
        event.addSprite(PermafrostRenderer.OPEN_EYE_RESOURCE_LOCATION);
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event){
        event.put(EntityTypeRegistry.LUPINE.get(), LupineEntity.createAttributes().build());
        event.put(EntityTypeRegistry.ARCTIHORN.get(), ArctihornEntity.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_SKELETON.get(), BabySkeletonEntity.createAttributes().build());
        event.put(EntityTypeRegistry.PERMAFROST.get(), PermafrostEntity.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanEntity.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Block event) {
        event.getBlockColors().register((p_228061_0_, p_228061_1_, p_228061_2_, p_228061_3_) -> {
            return p_228061_1_ != null && p_228061_2_ != null ? BiomeColors.getAverageFoliageColor(p_228061_1_, p_228061_2_) : FoliageColors.getDefaultColor();
        }, BlockRegistry.PALM_LEAVES.get());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Item event) {
        event.getItemColors().register((p_210235_1_, p_210235_2_) -> {
            BlockState blockstate = ((BlockItem)(p_210235_1_).getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, p_210235_2_);
        }, BlockRegistry.PALM_LEAVES.get());
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(final ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ModelResourceLocation("oddc:boomerang_in_hand#inventory"));
    }
}