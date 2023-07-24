package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.TriplePlantBlock;
import com.bedmen.odyssey.client.gui.OdysseyIngameGui;
import com.bedmen.odyssey.client.gui.screens.*;
import com.bedmen.odyssey.client.model.*;
import com.bedmen.odyssey.client.renderer.OdysseyItemInHandRenderer;
import com.bedmen.odyssey.client.renderer.StrayBruteRenderer;
import com.bedmen.odyssey.client.renderer.blockentity.*;
import com.bedmen.odyssey.client.renderer.entity.*;
import com.bedmen.odyssey.combat.ShieldType;
import com.bedmen.odyssey.combat.SpearType;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.entity.monster.*;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.bedmen.odyssey.particle.ThrustParticle;
import com.bedmen.odyssey.recipes.OdysseyRecipeBook;
import com.bedmen.odyssey.registry.*;
import com.bedmen.odyssey.util.ConditionalAmpUtil;
import com.bedmen.odyssey.world.gen.biome.weather.OdysseyOverworldEffects;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.HuskRenderer;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusClientEvents {

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



            //Override PlayerRenderer with OdysseyPlayerRenderer
            EntityRenderers.PLAYER_PROVIDERS = ImmutableMap.of("default", (context) -> {
                return new OdysseyPlayerRenderer(context, false);
            }, "slim", (context) -> {
                return new OdysseyPlayerRenderer(context, true);
            });

            //Item Model Properties
            for(Item item : ForgeRegistries.ITEMS.getValues()){
                if(item instanceof INeedsToRegisterItemModelProperty item1){
                    item1.registerItemModelProperties();
                }
            }

            //Block Entity Renderings
            BlockEntityRenderers.register(BlockEntityTypeRegistry.SIGN.get(), OdysseySignRenderer::new);
            BlockEntityRenderers.register(BlockEntityTypeRegistry.COVEN_HUT_DOOR.get(), CovenHutDoorRenderer::new);
            BlockEntityRenderers.register(BlockEntityTypeRegistry.INFUSION_PEDESTAL.get(), InfusionPedestalRenderer::new);
            BlockEntityRenderers.register(BlockEntityTypeRegistry.INFUSER.get(), InfuserRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.BEACON.get(), OdysseyBeaconTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(TileEntityTypeRegistry.ENCHANTING_TABLE.get(), OdysseyEnchantmentTableTileEntityRenderer::new);
            BlockEntityRenderers.register(BlockEntityTypeRegistry.TREASURE_CHEST.get(), (context) -> new TreasureChestRenderer<>(TreasureChestType.STERLING_SILVER, context));

            //Screens
            MenuScreens.register(ContainerRegistry.RECYCLING_FURNACE.get(), RecyclingFurnaceScreen::new);
            MenuScreens.register(ContainerRegistry.STITCHING_TABLE.get(), StitchingTableScreen::new);
            MenuScreens.register(ContainerRegistry.ALLOY_FURNACE.get(), AlloyFurnaceScreen::new);
            MenuScreens.register(ContainerRegistry.GRINDSTONE.get(), OdysseyGrindstoneScreen::new);
            MenuScreens.register(ContainerRegistry.ANVIL.get(), OdysseyAnvilScreen::new);
//        ScreenManager.register(ContainerRegistry.BEACON.get(), OdysseyBeaconScreen::new);
//        ScreenManager.register(ContainerRegistry.SMITHING_TABLE.get(), OdysseySmithingTableScreen::new);
//        ScreenManager.register(ContainerRegistry.ENCHANTMENT.get(), OdysseyEnchantmentScreen::new);
//        ScreenManager.register(ContainerRegistry.BOOKSHELF.get(), BookshelfScreen::new);
//        ScreenManager.register(ContainerRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
            for(MenuType<QuiverMenu> containerType : ContainerRegistry.QUIVER_MAP.values()){
                MenuScreens.register(containerType, QuiverScreen::new);
            }

            //Mob Renderings
//        EntityRenderers.register(EntityTypeRegistry.LUPINE.get(), LupineRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.ARCTIHORN.get(), ArctihornRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.HUSK.get(), HuskRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MOON_TOWER_ZOMBIE.get(), DungeonZombieRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.FORGOTTEN.get(), ForgottenRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ENCASED_ZOMBIE.get(), EncasedZombieRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ENCASED_SKELETON.get(), EncasedSkeletonRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.OVERGROWN_ZOMBIE.get(), OvergrownZombieRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.OVERGROWN_SKELETON.get(), OvergrownSkeletonRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.OVERGROWN_CREEPER.get(), OvergrownCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.SKELETON.get(), OdysseySkeletonRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.STRAY.get(), OdysseyStrayRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MOON_TOWER_SKELETON.get(), DungeonSkeletonRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BABY_CREEPER.get(), OdysseyCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.DRIPSTONE_CREEPER.get(), DripstoneCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WEAVER.get(), WeaverRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BLADE_SPIDER.get(), BladeSpiderRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PASSIVE_WEAVER.get(), PassiveWeaverRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathanRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.POLAR_BEAR.get(), PolarBearRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ZOMBIE_BRUTE.get(), ZombieBruteRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.STRAY_BRUTE.get(), StrayBruteRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BARN_SPIDER.get(), BarnSpiderRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WRAITH.get(), WraithRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WRAITH_STALKER.get(), WraithStalkerRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WRAITH_AMALGAM.get(), WraithAmalgamRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WRAITH_AMALGAM_PROJECTILE.get(), WraithAmalgamProjectileRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WRAITHLING.get(), WraithlingRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BANDIT.get(), BanditRenderer::new);

            //Boss Renderings
            EntityRenderers.register(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolemRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_MASTER.get(), MineralLeviathanMasterRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_HEAD.get(), MineralLeviathanHeadRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.COVEN_MASTER.get(), CovenMasterRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ENDER_WITCH.get(), EnderWitchRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.NETHER_WITCH.get(), NetherWitchRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.OVERWORLD_WITCH.get(), OverworldWitchRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.COVEN_ROOT_ENTITY.get(), CovenRootEntityRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PERMAFROST_MASTER.get(), PermafrostMasterRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PERMAFROST_ICICLE_ENTITY.get(), PermafrostIcicleRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PERMAFROST_CONDUIT.get(), PermafrostConduitRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PERMAFROST_BIG_ICICLE_ENTITY.get(), PermafrostBigIcicleRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PERMAFROST_SPAWNER_ICICLE.get(), PermafrostSpawnerIcicleRenderer::new);

            //Projectile Renderings
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.TRIDENT.get(), OdysseyTridentRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST_ICICLE.get(), PermafrostIcicleRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.ARROW.get(), OdysseyArrowRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BOOMERANG.get(), BoomerangRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.THROWN_SPEAR.get(), ThrownSpearRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.DRIPSTONE_SHARD.get(), DripstoneShardRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.SONIC_BOOM.get(), SonicBoomRenderer::new);

            //Boat Renderings
            EntityRenderers.register(EntityTypeRegistry.BOAT.get(), OdysseyBoatRenderer::new);

            // Other import render settings
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.gui = new OdysseyIngameGui(minecraft);
            OdysseyItemInHandRenderer odysseyItemInHandRenderer = new OdysseyItemInHandRenderer(minecraft, minecraft.getEntityRenderDispatcher(), minecraft.getItemRenderer());
            minecraft.getEntityRenderDispatcher().itemInHandRenderer = odysseyItemInHandRenderer;
            minecraft.gameRenderer.itemInHandRenderer = odysseyItemInHandRenderer;
        });
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlaysEvent(final RegisterGuiOverlaysEvent event){
        //Hollow coconut vision overlay
        event.registerAbove(VanillaGuiOverlay.HELMET.id(), "helmet", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            if(gui instanceof OdysseyIngameGui odysseyIngameGui){
                gui.setupOverlayRenderState(true, false);
                odysseyIngameGui.renderOdysseyHelmet(partialTicks, mStack);
            }
        });

        //Shield Meter overlay
        event.registerAbove(VanillaGuiOverlay.ARMOR_LEVEL.id(), "shield_meter", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            if(gui instanceof OdysseyIngameGui odysseyIngameGui && !odysseyIngameGui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()){
                if(!WeaponUtil.getHeldParryables(odysseyIngameGui.getMinecraft().player).isEmpty()){
                    gui.setupOverlayRenderState(true, false, OdysseyIngameGui.ODYSSEY_GUI_ICONS_LOCATION);
                    odysseyIngameGui.renderShieldMeter(screenWidth, screenHeight, mStack, partialTicks);
                }
            }
        });

        //Light bar for solar weapons (maybe later just the melee bar)
        event.registerAbove(VanillaGuiOverlay.AIR_LEVEL.id(), "light_bar", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            if(gui instanceof OdysseyIngameGui odysseyIngameGui && !odysseyIngameGui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()){
                gui.setupOverlayRenderState(true, false, OdysseyIngameGui.ODYSSEY_GUI_ICONS_LOCATION);
                odysseyIngameGui.renderLightBar(screenWidth, screenHeight, mStack);
            }
        });

        //Flight icon overlay
        event.registerAbove(VanillaGuiOverlay.AIR_LEVEL.id(), "flight", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            if(gui instanceof OdysseyIngameGui odysseyIngameGui && !odysseyIngameGui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()){
                gui.setupOverlayRenderState(true, false, OdysseyIngameGui.ODYSSEY_GUI_ICONS_LOCATION);
                odysseyIngameGui.renderFlight(screenWidth, screenHeight, mStack);
            }
        });

        //Frostbite overlay
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "frostbite", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            if(gui instanceof OdysseyIngameGui odysseyIngameGui){
                gui.setupOverlayRenderState(true, false);
                odysseyIngameGui.renderFrostbite();
            }
        });

        //Roasting heat overlay
        event.registerAbove(new ResourceLocation(Odyssey.MOD_ID, "frostbite"),"overheating", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            if(gui instanceof OdysseyIngameGui odysseyIngameGui){
                gui.setupOverlayRenderState(true, false);
                odysseyIngameGui.renderRoastingOverlay();
            }
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
        for(TreasureChestType treasureChestType : TreasureChestType.values()){
            event.addSprite(TreasureChestRenderer.getRenderMaterial(treasureChestType, false).texture());
            event.addSprite(TreasureChestRenderer.getRenderMaterial(treasureChestType, true).texture());
        }
        // Modded fire variants
        for(FireType fireType : FireType.values()){
            if(fireType.isNotNone()){
                event.addSprite(fireType.material0.texture());
                event.addSprite(fireType.material1.texture());
            }
        }
//        event.addSprite(OdysseyPlayerContainer.EMPTY_SLOT_TRINKET);

        event.addSprite(PermafrostConduitRenderer.SHELL_RESOURCE_LOCATION);
        event.addSprite(PermafrostConduitRenderer.ACTIVE_SHELL_RESOURCE_LOCATION);
        event.addSprite(PermafrostConduitRenderer.WIND_RESOURCE_LOCATION);
        event.addSprite(PermafrostConduitRenderer.VERTICAL_WIND_RESOURCE_LOCATION);
        event.addSprite(PermafrostConduitRenderer.OPEN_EYE_RESOURCE_LOCATION);

        /**
         *
         event.addSprite(PermafrostConduitRenderer.ACTIVE_SHELL_TEXTURE.texture());
         event.addSprite(PermafrostConduitRenderer.WIND_TEXTURE.texture());
         event.addSprite(PermafrostConduitRenderer.VERTICAL_WIND_TEXTURE.texture());
         event.addSprite(PermafrostConduitRenderer.OPEN_EYE_TEXTURE.texture());
         */
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
        event.registerLayerDefinition(WraithStalkerModel.LAYER_LOCATION, WraithStalkerModel::createBodyLayer);
        event.registerLayerDefinition(WraithAmalgamModel.LAYER_LOCATION, WraithAmalgamModel::createBodyLayer);
        event.registerLayerDefinition(WraithAmalgamProjectileModel.LAYER_LOCATION, WraithAmalgamProjectileModel::createBodyLayer);
        event.registerLayerDefinition(WraithlingModel.LAYER_LOCATION, WraithlingModel::createBodyLayer);
        event.registerLayerDefinition(MineralLeviathanHeadModel.LAYER_LOCATION, MineralLeviathanHeadModel::createBodyLayer);
        event.registerLayerDefinition(MineralLeviathanBodyModel.LAYER_LOCATION, MineralLeviathanBodyModel::createBodyLayer);
        event.registerLayerDefinition(ArmedCovenWitchModel.LAYER_LOCATION, ArmedCovenWitchModel::createBodyLayer);
        event.registerLayerDefinition(CovenRootModel.LAYER_LOCATION, CovenRootModel::createBodyLayer);
        event.registerLayerDefinition(SpearModel.LAYER_LOCATION, SpearModel::createBodyLayer);
        event.registerLayerDefinition(BladeSpiderModel.LAYER_LOCATION, BladeSpiderModel::createBodyLayer);
        event.registerLayerDefinition(ForgottenModel.LAYER_LOCATION, ForgottenModel::createBodyLayer);
        event.registerLayerDefinition(DripstoneCreeperModel.LAYER_LOCATION, DripstoneCreeperModel::createBodyLayer);
        event.registerLayerDefinition(PermafrostBigIcicleModel.LAYER_LOCATION, PermafrostBigIcicleModel::createBodyLayer);
        event.registerLayerDefinition(PermafrostSpawnerIcicleModel.LAYER_LOCATION, PermafrostSpawnerIcicleModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onColorHandlerEvent$Block(final RegisterColorHandlersEvent.Block event) {
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
    public static void onColorHandlerEvent$Item(final RegisterColorHandlersEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.register((itemStack, i) -> {
            BlockState blockstate = ((BlockItem)(itemStack).getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, i);
        }, BlockRegistry.PALM_LEAVES.get(), BlockRegistry.PALM_CORNER_LEAVES.get());
        itemColors.register((itemStack, i) ->
                GrassColor.get(0.5D, 1.0D),
                BlockRegistry.PRAIRIE_GRASS.get());
        itemColors.register((itemStack, i) -> i > 0 ? -1 : ((DyeableLeatherItem)itemStack.getItem()).getColor(itemStack), ItemRegistry.PARKA_HELMET.get(), ItemRegistry.PARKA_CHESTPLATE.get(), ItemRegistry.PARKA_LEGGINGS.get(), ItemRegistry.PARKA_BOOTS.get());
        itemColors.register((itemStack, i) -> i <= 0 ? -1 : ConditionalAmpUtil.getColorTag(itemStack), ItemRegistry.RAIN_SWORD.get(), ItemRegistry.ARID_MACE.get());
        itemColors.register((itemStack, i) -> ConditionalAmpUtil.getColorTag(itemStack), ItemRegistry.ICE_DAGGER.get());
        itemColors.register((itemStack, i) -> i == 0 ? -1 : MapItem.getColor(itemStack), ItemRegistry.FILLED_MAP.get());
    }

    @SubscribeEvent
    public static void onParticleFactoryRegisterEvent(RegisterParticleProvidersEvent event){
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.FATAL_HIT.get(), CritParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.THRUST.get(), ThrustParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onModelEvent$RegisterAdditional(final ModelEvent.RegisterAdditional event) {
        for(SpearType spearType: SpearType.NEED_MODEL_REGISTERED_SET){
            event.register(spearType.entityModelResourceLocation);
        }
    }

    @SubscribeEvent
    public static void onRegisterDimensionSpecialEffectsEvent(final RegisterDimensionSpecialEffectsEvent event){
        event.register(BuiltinDimensionTypes.OVERWORLD_EFFECTS, new OdysseyOverworldEffects(Minecraft.getInstance()));
    }
    
    @SubscribeEvent
    public static void onRegisterRecipeBookCategoriesEvent(final RegisterRecipeBookCategoriesEvent event){
        // Add aggregate categories
        event.registerAggregateCategory(OdysseyRecipeBook.ALLOYING_SEARCH, List.of(OdysseyRecipeBook.ALLOYING));
        event.registerAggregateCategory(OdysseyRecipeBook.RECYCLING_SEARCH, List.of(OdysseyRecipeBook.RECYCLING));
        event.registerAggregateCategory(OdysseyRecipeBook.STITCHING_SEARCH, List.of(OdysseyRecipeBook.STITCHING));
        // Add category finders
        event.registerRecipeCategoryFinder(RecipeTypeRegistry.ALLOYING.get(), recipe -> OdysseyRecipeBook.ALLOYING);
        event.registerRecipeCategoryFinder(RecipeTypeRegistry.RECYCLING.get(), recipe -> OdysseyRecipeBook.RECYCLING);
        event.registerRecipeCategoryFinder(RecipeTypeRegistry.WEAVING.get(), recipe -> OdysseyRecipeBook.WEAVING);
        event.registerRecipeCategoryFinder(RecipeTypeRegistry.STITCHING.get(), recipe -> OdysseyRecipeBook.STITCHING);
        event.registerRecipeCategoryFinder(RecipeTypeRegistry.INFUSER_CRAFTING.get(), recipe -> OdysseyRecipeBook.INFUSER_CRAFTING);
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegisterEvent(final SpawnPlacementRegisterEvent event) {
        event.register(EntityTypeRegistry.BABY_LEVIATHAN.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BabyLeviathan::spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypeRegistry.WEAVER.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Weaver::spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypeRegistry.WRAITH.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Wraith::spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypeRegistry.WRAITH_STALKER.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WraithStalker::spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypeRegistry.WRAITH_AMALGAM.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WraithAmalgam::spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypeRegistry.STRAY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OdysseyStray::spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypeRegistry.HUSK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OdysseyHusk::spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
    }



}
