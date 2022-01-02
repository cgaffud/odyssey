package com.bedmen.odyssey;

import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import com.bedmen.odyssey.client.gui.AlloyFurnaceScreen;
import com.bedmen.odyssey.client.gui.OdysseyIngameGui;
import com.bedmen.odyssey.client.gui.screens.QuiverScreen;
import com.bedmen.odyssey.client.model.*;
import com.bedmen.odyssey.client.renderer.entity.WeaverRenderer;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseySignRenderer;
import com.bedmen.odyssey.client.renderer.entity.*;
import com.bedmen.odyssey.client.renderer.entity.MineralLeviathanBodyRenderer;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.entity.boss.MineralLeviathanBody;
import com.bedmen.odyssey.entity.boss.MineralLeviathanHead;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.network.OdysseyNetwork;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.tools.OdysseyTiers;
import com.bedmen.odyssey.util.CompostUtil;
import com.bedmen.odyssey.world.gen.FeatureGen;
import com.bedmen.odyssey.world.gen.OreGen;
import com.bedmen.odyssey.world.gen.StructureGen;
import com.bedmen.odyssey.world.gen.feature.WeaverColonyFeature;
import com.bedmen.odyssey.world.spawn.OdysseyBiomeEntitySpawn;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ready);
        MinecraftForge.EVENT_BUS.register(this);

        BlockRegistry.init();
        ItemRegistry.init();
//        AttributeRegistry.init();
//        BiomeRegistry.init();
        BlockEntityTypeRegistry.init();
        ContainerRegistry.init();
        DataSerializerRegistry.init();
        EffectRegistry.init();
        EnchantmentRegistry.init();
        EntityTypeRegistry.init();
        FeatureRegistry.init();
//        PotionRegistry.init();
        RecipeRegistry.init();
        SoundEventRegistry.init();
        StructureFeatureRegistry.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        OreGen.registerOres();
        FeatureGen.registerFeatures();
        StructureGen.registerStructures();
        StructureFeatureRegistry.setupStructures();
        OdysseyBiomeEntitySpawn.registerSpawners();
//        OdysseyStructureEntitySpawn.registerSpawners();
//        OdysseyPotions.addBrewingRecipes();
        CompostUtil.addCompostingRecipes();
//        OdysseyTrades.addTrades();
        OdysseyNetwork.init();
//        BiomeRegistry.register();
//
//        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.LUPINE.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LupineEntity::spawnPredicate);
//        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.ARCTIHORN.get(),EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ArctihornEntity::spawnPredicate);
        SpawnPlacements.register(EntityTypeRegistry.BABY_LEVIATHAN.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BabyLeviathan::spawnPredicate);
        SpawnPlacements.register(EntityTypeRegistry.WEAVER.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Weaver::spawnPredicate);
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        //For Coconut vision overlay
        OverlayRegistry.registerOverlayTop("OdysseyHelmet", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            if(gui instanceof OdysseyIngameGui odysseyIngameGui){
                odysseyIngameGui.renderOdysseyHelmet(partialTicks, mStack);
            }
        });

        //Override PlayerRenderer with OdysseyPlayerRenderer
        EntityRenderers.PLAYER_PROVIDERS = ImmutableMap.of("default", (context) -> {
            return new OdysseyPlayerRenderer(context, false);
        }, "slim", (context) -> {
            return new OdysseyPlayerRenderer(context, true);
        });

        //Block Render Types
       for(Block block : ForgeRegistries.BLOCKS.getValues()) {
           if (block instanceof INeedsToRegisterRenderType) {
               ItemBlockRenderTypes.setRenderLayer(block, ((INeedsToRegisterRenderType) block).getRenderType());
           }
       }

        //Item Model Properties
        for(Item item : ForgeRegistries.ITEMS.getValues()){
            if(item instanceof INeedsToRegisterItemModelProperty){
                ((INeedsToRegisterItemModelProperty) item).registerItemModelProperties();
            }
        }

        //Block Entity Renderings
        BlockEntityRenderers.register(BlockEntityTypeRegistry.SIGN.get(), OdysseySignRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.STERLING_SILVER_CHEST.get(), SterlingSilverChestTileEntityRenderer::new);

        //Screens
        MenuScreens.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
//        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
//        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
//        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
//        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
//        ScreenManager.register(ContainerRegistry.RECYCLE_FURNACE.get(), RecycleFurnaceScreen::new);
//        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
//        ScreenManager.register(ContainerRegistry.GRINDSTONE.get(), OdysseyGrindstoneScreen::new);
//        ScreenManager.register(ContainerRegistry.ANVIL.get(), OdysseyAnvilScreen::new);
        for(MenuType<QuiverMenu> containerType : ContainerRegistry.QUIVER_MAP.values()){
            MenuScreens.register(containerType, QuiverScreen::new);
        }
//
//        //Mob Renderings
//        EntityRenderers.register(EntityTypeRegistry.LUPINE.get(), LupineRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.BABY_SKELETON.get(), BabySkeletonRenderer::new);

        EntityRenderers.register(EntityTypeRegistry.BABY_CREEPER.get(), OdysseyCreeperRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeperRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.WEAVER.get(), WeaverRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.PASSIVE_WEAVER.get(), PassiveWeaverRenderer::new);

        EntityRenderers.register(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathanRenderer::new);
//
//        //Boss Renderings
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolemRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanHeadRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST.get(), PermafrostRenderer::new);
//
//        //Projectile Renderings
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.TRIDENT.get(), OdysseyTridentRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST_ICICLE.get(), PermafrostIcicleRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.ARROW.get(), OdysseyArrowRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.BOOMERANG.get(), BoomerangRenderer::new);

        //Boat Renderings
        EntityRenderers.register(EntityTypeRegistry.BOAT.get(), OdysseyBoatRenderer::new);

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gui = new OdysseyIngameGui(minecraft);
//        minecraft.itemRenderer = new OdysseyItemRenderer(minecraft.getTextureManager(), minecraft.getModelManager(), minecraft.getItemColors());
    }

    @SubscribeEvent
    public void ready(FMLLoadCompleteEvent event){
        OdysseyTiers.init();
    }

    @SubscribeEvent
    public static void onTextureStitch(final TextureStitchEvent.Pre event){
//        event.addSprite(OdysseyItemStackTileEntityRenderer.LEVIATHAN_SHIELD_BASE_LOCATION);
//        event.addSprite(OdysseyItemStackTileEntityRenderer.LEVIATHAN_SHIELD_BASE_NOPATTERN_LOCATION);
//        event.addSprite(OdysseyPlayerContainer.EMPTY_SLOT_TRINKET);
//        event.addSprite(SterlingSilverChestTileEntityRenderer.SINGLE_RESOURCE_LOCATION);
//        event.addSprite(SterlingSilverChestTileEntityRenderer.SINGLE_LOCKED_RESOURCE_LOCATION);
//        event.addSprite(SterlingSilverChestTileEntityRenderer.LEFT_RESOURCE_LOCATION);
//        event.addSprite(SterlingSilverChestTileEntityRenderer.LEFT_LOCKED_RESOURCE_LOCATION);
//        event.addSprite(SterlingSilverChestTileEntityRenderer.RIGHT_RESOURCE_LOCATION);
//        event.addSprite(SterlingSilverChestTileEntityRenderer.RIGHT_LOCKED_RESOURCE_LOCATION);
//        event.addSprite(PermafrostRenderer.ACTIVE_SHELL_RESOURCE_LOCATION);
//        event.addSprite(PermafrostRenderer.WIND_RESOURCE_LOCATION);
//        event.addSprite(PermafrostRenderer.VERTICAL_WIND_RESOURCE_LOCATION);
//        event.addSprite(PermafrostRenderer.OPEN_EYE_RESOURCE_LOCATION);
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event){
//        event.put(EntityTypeRegistry.LUPINE.get(), LupineEntity.createAttributes().build());
//        event.put(EntityTypeRegistry.ARCTIHORN.get(), ArctihornEntity.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_SKELETON.get(), BabySkeleton.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_CREEPER.get(), BabyCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.WEAVER.get(), Weaver.createAttributes().build());
        event.put(EntityTypeRegistry.PASSIVE_WEAVER.get(), PassiveWeaver.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathan.createAttributes().build());
//
//        //Bosses
//        event.put(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolemEntity.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanHead.createAttributes().build());
        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBody.createAttributes().build());
//        event.put(EntityTypeRegistry.PERMAFROST.get(), PermafrostEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onEntityRenderersEvent$RegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BabyLeviathanModel.LAYER_LOCATION, BabyLeviathanModel::createBodyLayer);
        for(OdysseyBoat.Type type : OdysseyBoat.Type.values()){
            event.registerLayerDefinition(OdysseyBoatModel.LAYER_LOCATION.get(type), OdysseyBoatModel::createBodyModel);
        }
        event.registerLayerDefinition(QuiverModel.LAYER_LOCATION, QuiverModel::createBodyLayer);
        event.registerLayerDefinition(BoomerangModel.LAYER_LOCATION, BoomerangModel::createBodyLayer);
        event.registerLayerDefinition(MineralLeviathanHeadModel.LAYER_LOCATION, MineralLeviathanHeadModel::createBodyLayer);
        event.registerLayerDefinition(MineralLeviathanBodyModel.LAYER_LOCATION, MineralLeviathanBodyModel::createBodyLayer);
        event.registerLayerDefinition(WeaverModel.LAYER_LOCATION, WeaverModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Block event) {
        event.getBlockColors().register((p_228061_0_, p_228061_1_, p_228061_2_, p_228061_3_) -> {
            return p_228061_1_ != null && p_228061_2_ != null ? BiomeColors.getAverageFoliageColor(p_228061_1_, p_228061_2_) : FoliageColor.getDefaultColor();
        }, BlockRegistry.PALM_LEAVES.get(), BlockRegistry.PALM_CORNER_LEAVES.get());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Item event) {
        event.getItemColors().register((p_210235_1_, p_210235_2_) -> {
            BlockState blockstate = ((BlockItem)(p_210235_1_).getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, p_210235_2_);
        }, BlockRegistry.PALM_LEAVES.get(), BlockRegistry.PALM_CORNER_LEAVES.get());
    }
}