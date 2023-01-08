package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import com.bedmen.odyssey.block.TriplePlantBlock;
import com.bedmen.odyssey.client.gui.OdysseyIngameGui;
import com.bedmen.odyssey.client.gui.screens.AlloyFurnaceScreen;
import com.bedmen.odyssey.client.gui.screens.QuiverScreen;
import com.bedmen.odyssey.client.gui.screens.RecyclingFurnaceScreen;
import com.bedmen.odyssey.client.gui.screens.StitchingTableScreen;
import com.bedmen.odyssey.client.model.*;
import com.bedmen.odyssey.client.renderer.OdysseyItemInHandRenderer;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseySignRenderer;
import com.bedmen.odyssey.client.renderer.blockentity.TreasureChestRenderer;
import com.bedmen.odyssey.client.renderer.entity.*;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.ShieldType;
import com.bedmen.odyssey.items.TomeItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            //For Build Testing
//            File file = new File("C:\\Users\\18029\\Documents\\odyssey-1.18.1\\sample.txt");
//            PrintStream stream = null;
//            try {
//                stream = new PrintStream(file);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            System.setOut(stream);
//            System.out.println("From now on "+file.getAbsolutePath()+" will be your console");

            //For hollow coconut vision overlay
            OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HELMET_ELEMENT,"Odyssey Helmet", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
                if(gui instanceof OdysseyIngameGui odysseyIngameGui){
                    gui.setupOverlayRenderState(true, false);
                    odysseyIngameGui.renderOdysseyHelmet(partialTicks, mStack);
                }
            });

            //For hollow coconut vision overlay
            OverlayRegistry.registerOverlayAbove(ForgeIngameGui.AIR_LEVEL_ELEMENT,"Odyssey Flight", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
                if(gui instanceof OdysseyIngameGui odysseyIngameGui && !odysseyIngameGui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()){
                    gui.setupOverlayRenderState(true, false, OdysseyIngameGui.ODYSSEY_GUI_ICONS_LOCATION);
                    odysseyIngameGui.renderFlight(screenWidth, screenHeight, mStack);
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
                if (block instanceof INeedsToRegisterRenderType block1) {
                    ItemBlockRenderTypes.setRenderLayer(block, block1.getRenderType());
                }
            }

            //Item Model Properties
            for(Item item : ForgeRegistries.ITEMS.getValues()){
                if(item instanceof INeedsToRegisterItemModelProperty item1){
                    item1.registerItemModelProperties();
                }
            }

            //Block Entity Renderings
            BlockEntityRenderers.register(BlockEntityTypeRegistry.SIGN.get(), OdysseySignRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);
            BlockEntityRenderers.register(BlockEntityTypeRegistry.TREASURE_CHEST.get(), (context) -> new TreasureChestRenderer<>(TreasureChestMaterial.STERLING_SILVER, context));

            //Screens
            MenuScreens.register(ContainerRegistry.RECYCLING_FURNACE.get(), RecyclingFurnaceScreen::new);
            MenuScreens.register(ContainerRegistry.STITCHING_TABLE.get(), StitchingTableScreen::new);
            MenuScreens.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
//        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
//        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
//        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
//        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
//        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
//        ScreenManager.register(ContainerRegistry.GRINDSTONE.get(), OdysseyGrindstoneScreen::new);
//        ScreenManager.register(ContainerRegistry.ANVIL.get(), OdysseyAnvilScreen::new);
            for(MenuType<QuiverMenu> containerType : ContainerRegistry.QUIVER_MAP.values()){
                MenuScreens.register(containerType, QuiverScreen::new);
            }

            //Mob Renderings
//        EntityRenderers.register(EntityTypeRegistry.LUPINE.get(), LupineRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MOON_TOWER_ZOMBIE.get(), DungeonZombieRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.SKELETON.get(), OdysseySkeletonRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MOON_TOWER_SKELETON.get(), DungeonSkeletonRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BABY_CREEPER.get(), OdysseyCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WEAVER.get(), WeaverRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PASSIVE_WEAVER.get(), PassiveWeaverRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathanRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.POLAR_BEAR.get(), PolarBearRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ZOMBIE_BRUTE.get(), ZombieBruteRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BARN_SPIDER.get(), BarnSpiderRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WRAITH.get(), WraithRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BANDIT.get(), BanditRenderer::new);

            //Boss Renderings
            EntityRenderers.register(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolemRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_MASTER.get(), MineralLeviathanMasterRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_HEAD.get(), MineralLeviathanHeadRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST.get(), PermafrostRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.COVEN_MASTER.get(), CovenMasterRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ENDER_WITCH.get(), EnderWitchRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.NETHER_WITCH.get(), NetherWitchRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.OVERWORLD_WITCH.get(), OverworldWitchRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.COVEN_ROOT_ENTITY.get(), CovenRootEntityRenderer::new);

            //Projectile Renderings
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.TRIDENT.get(), OdysseyTridentRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST_ICICLE.get(), PermafrostIcicleRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ARROW.get(), OdysseyArrowRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BOOMERANG.get(), BoomerangRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.SONIC_BOOM.get(), SonicBoomRenderer::new);

            //Boat Renderings
            EntityRenderers.register(EntityTypeRegistry.BOAT.get(), OdysseyBoatRenderer::new);

            Minecraft minecraft = Minecraft.getInstance();
            minecraft.gui = new OdysseyIngameGui(minecraft);
            OdysseyItemInHandRenderer odysseyItemInHandRenderer = new OdysseyItemInHandRenderer(minecraft);
            minecraft.itemInHandRenderer = odysseyItemInHandRenderer;
            minecraft.gameRenderer.itemInHandRenderer = odysseyItemInHandRenderer;
//        minecraft.itemRenderer = new OdysseyItemRenderer(minecraft.getTextureManager(), minecraft.getModelManager(), minecraft.getItemColors());
        });
    }

    @SubscribeEvent
    public static void onTextureStitchEvent$Pre(final TextureStitchEvent.Pre event){
        //Shield Textures
        for(ShieldType shieldType : ShieldType.values()){
            event.addSprite(shieldType.getRenderMaterial(false).texture());
            event.addSprite(shieldType.getRenderMaterial(true).texture());
        }
        //Treasure Chest Textures
        for(TreasureChestMaterial treasureChestMaterial : TreasureChestMaterial.values()){
            event.addSprite(TreasureChestRenderer.getRenderMaterial(treasureChestMaterial, false).texture());
            event.addSprite(TreasureChestRenderer.getRenderMaterial(treasureChestMaterial, true).texture());
        }
//        event.addSprite(OdysseyPlayerContainer.EMPTY_SLOT_TRINKET);
//        event.addSprite(PermafrostRenderer.ACTIVE_SHELL_RESOURCE_LOCATION);
//        event.addSprite(PermafrostRenderer.WIND_RESOURCE_LOCATION);
//        event.addSprite(PermafrostRenderer.VERTICAL_WIND_RESOURCE_LOCATION);
//        event.addSprite(PermafrostRenderer.OPEN_EYE_RESOURCE_LOCATION);
    }

    @SubscribeEvent
    public static void onEntityRenderersEvent$RegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(QuiverModel.LAYER_LOCATION, QuiverModel::createBodyLayer);
        for(OdysseyBoat.Type type : OdysseyBoat.Type.values()){
            event.registerLayerDefinition(OdysseyBoatModel.LAYER_LOCATION.get(type), OdysseyBoatModel::createBodyModel);
        }
        event.registerLayerDefinition(WeaverModel.LAYER_LOCATION, WeaverModel::createBodyLayer);
        event.registerLayerDefinition(AbandonedIronGolemModel.LAYER_LOCATION, AbandonedIronGolemModel::createBodyLayer);
        event.registerLayerDefinition(BabyLeviathanModel.LAYER_LOCATION, BabyLeviathanModel::createBodyLayer);
        event.registerLayerDefinition(WraithModel.LAYER_LOCATION, WraithModel::createBodyLayer);
        event.registerLayerDefinition(MineralLeviathanHeadModel.LAYER_LOCATION, MineralLeviathanHeadModel::createBodyLayer);
        event.registerLayerDefinition(MineralLeviathanBodyModel.LAYER_LOCATION, MineralLeviathanBodyModel::createBodyLayer);
        event.registerLayerDefinition(ArmedCovenWitchModel.LAYER_LOCATION, ArmedCovenWitchModel::createBodyLayer);
        event.registerLayerDefinition(CovenRootModel.LAYER_LOCATION, CovenRootModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onColorHandlerEvent$Block(final ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();
        blockColors.register(
                (blockState, blockAndTintGetter, blockPos, i) ->
                        blockAndTintGetter != null && blockPos != null ? BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos) : FoliageColor.getDefaultColor(),
                BlockRegistry.PALM_LEAVES.get(),
                BlockRegistry.PALM_CORNER_LEAVES.get());
        blockColors.register((blockState, blockAndTintGetter, blockPos, i) ->
                blockAndTintGetter != null && blockPos != null ? BiomeColors.getAverageGrassColor(blockAndTintGetter,
                        blockState.getValue(TriplePlantBlock.THIRD) == TriplePlantBlock.TripleBlockThird.UPPER ? blockPos.below(2) : (blockState.getValue(TriplePlantBlock.THIRD) == TriplePlantBlock.TripleBlockThird.MIDDLE ? blockPos.below() : blockPos)) : -1,
                BlockRegistry.PRAIRIE_GRASS.get());
        blockColors.addColoringState(TriplePlantBlock.THIRD, BlockRegistry.PRAIRIE_GRASS.get());
    }

    @SubscribeEvent
    public static void onColorHandlerEvent$Item(final ColorHandlerEvent.Item event) {
        event.getItemColors().register((itemStack, i) -> {
            BlockState blockstate = ((BlockItem)(itemStack).getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, i);
        }, BlockRegistry.PALM_LEAVES.get(), BlockRegistry.PALM_CORNER_LEAVES.get());
        event.getItemColors().register((itemStack, i) ->
                GrassColor.get(0.5D, 1.0D),
                BlockRegistry.PRAIRIE_GRASS.get());
        event.getItemColors().register((itemStack, i) -> {
            return i > 0 ? -1 : ((DyeableLeatherItem)itemStack.getItem()).getColor(itemStack);
        }, ItemRegistry.PARKA_HELMET.get(), ItemRegistry.PARKA_CHESTPLATE.get(), ItemRegistry.PARKA_LEGGINGS.get(), ItemRegistry.PARKA_BOOTS.get());
        event.getItemColors().register((itemStack, i) -> {
            return i <= 0 ? -1 : ConditionalAmpUtil.getColorTag(itemStack);
        }, ItemRegistry.RAIN_SWORD.get(), ItemRegistry.ARID_MACE.get());
        event.getItemColors().register((itemStack, i) -> {
            return ConditionalAmpUtil.getColorTag(itemStack);
        }, ItemRegistry.ICE_DAGGER.get());
        for(TomeItem tomeItem : TomeItem.TOMES){
            event.getItemColors().register((itemStack, i) -> {
                return i < 1 ? -1 : ((TomeItem)itemStack.getItem()).getColor();
            }, tomeItem);
        }
    }

    @SubscribeEvent
    public static void onParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event){
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.FATAL_HIT.get(), CritParticle.Provider::new);
    }
}
