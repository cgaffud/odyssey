package com.bedmen.odyssey;


import com.bedmen.odyssey.client.gui.OdysseyIngameGui;
import com.bedmen.odyssey.client.renderer.entity.renderer.CamoCreeperRenderer;
import com.bedmen.odyssey.client.renderer.entity.renderer.OdysseyCreeperRenderer;
import com.bedmen.odyssey.entity.monster.BabyCreeper;
import com.bedmen.odyssey.entity.monster.CamoCreeper;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.tools.OdysseyTiers;
import com.bedmen.odyssey.world.gen.OreGen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
//        ContainerRegistry.init();
//        DataSerializerRegistry.init();
        EffectRegistry.init();
        EnchantmentRegistry.init();
        EntityTypeRegistry.init();
//        FeatureRegistry.init();
//        PotionRegistry.init();
//        RecipeRegistry.init();
//        SoundEventRegistry.init();
//        TileEntityTypeRegistry.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        OreGen.registerOres();
//        OdysseyFeatureGen.registerFeatures();
//        OdysseyBiomeEntitySpawn.registerSpawners();
//        OdysseyStructureEntitySpawn.registerSpawners();
//        OdysseyPotions.addBrewingRecipes();
//        CompostUtil.addCompostingRecipes();
//        OdysseyTrades.addTrades();
//        OdysseyNetwork.init();
//        BiomeRegistry.register();
//
//        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.LUPINE.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LupineEntity::spawnPredicate);
//        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.ARCTIHORN.get(),EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ArctihornEntity::spawnPredicate);
//        EntitySpawnPlacementRegistry.register(EntityTypeRegistry.BABY_LEVIATHAN.get(),EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BabyLeviathanEntity::spawnPredicate);
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
//        //Item Model Properties
//        for(Block block : ForgeRegistries.BLOCKS.getValues()){
//            if(block instanceof INeedsToRegisterRenderType){
//                RenderTypeLookup.setRenderLayer(block, ((INeedsToRegisterRenderType) block).getRenderType());
//            }
//        }
//
//        //Block Render Types
//        for(Item item : ForgeRegistries.ITEMS.getValues()){
//            if(item instanceof INeedsToRegisterItemModelProperty){
//                ((INeedsToRegisterItemModelProperty) item).registerItemModelProperties();
//            }
//        }
//
//        //Tile Entity Renderings
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.STERLING_SILVER_CHEST.get(), SterlingSilverChestTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.SIGN.get(), OdysseySignTileEntityRenderer::new);
//
//        //Container Screens
//        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
//        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
//        ScreenManager.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
//        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
//        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
//        ScreenManager.register(ContainerRegistry.RECYCLE_FURNACE.get(), RecycleFurnaceScreen::new);
//        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
//        ScreenManager.register(ContainerRegistry.GRINDSTONE.get(), OdysseyGrindstoneScreen::new);
//        ScreenManager.register(ContainerRegistry.ANVIL.get(), OdysseyAnvilScreen::new);
//        for(ContainerType<QuiverContainer> containerType : ContainerRegistry.QUIVER_MAP.values()){
//            ScreenManager.register(containerType, QuiverScreen::new);
//        }
//
//        //Mob Renderings
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.LUPINE.get(), LupineRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BABY_SKELETON.get(), BabySkeletonRenderer::new);

        EntityRenderers.register(EntityTypeRegistry.BABY_CREEPER.get(), OdysseyCreeperRenderer::new);
        EntityRenderers.register(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeperRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.WEAVER.get(), WeaverRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathanRenderer::new);
//
//        //Boss Renderings
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolemRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanHeadRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST.get(), PermafrostRenderer::new);
//
//        //Projectile Renderings
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.TRIDENT.get(), OdysseyTridentRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST_ICICLE.get(), PermafrostIcicleRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.ARROW.get(), OdysseyArrowRenderer::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BOOMERANG.get(), BoomerangRenderer::new);
//
//        //Boat Renderings
//        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegistry.BOAT.get(), OdysseyBoatRenderer::new);
//
//        //Wood Types
//        Atlases.addWoodType(OdysseyWoodType.PALM);
//
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gui = new OdysseyIngameGui(minecraft);
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
//        event.put(EntityTypeRegistry.BABY_SKELETON.get(), BabySkeletonEntity.createAttributes().build());
        event.put(EntityTypeRegistry.BABY_CREEPER.get(), BabyCreeper.createAttributes().build());
        event.put(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeper.createAttributes().build());
//        event.put(EntityTypeRegistry.WEAVER.get(), WeaverEntity.createAttributes().build());
//        event.put(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathanEntity.createAttributes().build());
//
//        //Bosses
//        event.put(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolemEntity.createAttributes().build());
//        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanHeadEntity.createAttributes().build());
//        event.put(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyEntity.createAttributes().build());
//        event.put(EntityTypeRegistry.PERMAFROST.get(), PermafrostEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Block event) {
//        event.getBlockColors().register((p_228061_0_, p_228061_1_, p_228061_2_, p_228061_3_) -> {
//            return p_228061_1_ != null && p_228061_2_ != null ? BiomeColors.getAverageFoliageColor(p_228061_1_, p_228061_2_) : FoliageColors.getDefaultColor();
//        }, BlockRegistry.PALM_LEAVES.get());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent(final ColorHandlerEvent.Item event) {
//        event.getItemColors().register((p_210235_1_, p_210235_2_) -> {
//            BlockState blockstate = ((BlockItem)(p_210235_1_).getItem()).getBlock().defaultBlockState();
//            return event.getBlockColors().getColor(blockstate, null, null, p_210235_2_);
//        }, BlockRegistry.PALM_LEAVES.get());
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(final ModelRegistryEvent event) {
//        ModelLoader.addSpecialModel(new ModelResourceLocation("oddc:boomerang_in_hand#inventory"));
    }
}