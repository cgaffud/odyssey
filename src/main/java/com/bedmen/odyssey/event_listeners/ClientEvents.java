package com.bedmen.odyssey.event_listeners;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import com.bedmen.odyssey.client.gui.OdysseyIngameGui;
import com.bedmen.odyssey.client.gui.screens.*;
import com.bedmen.odyssey.client.model.*;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseyBlockEntityWithoutLevelRenderer;
import com.bedmen.odyssey.client.renderer.blockentity.OdysseySignRenderer;
import com.bedmen.odyssey.client.renderer.blockentity.TreasureChestRenderer;
import com.bedmen.odyssey.client.renderer.entity.*;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import com.bedmen.odyssey.inventory.QuiverMenu;
import com.bedmen.odyssey.items.INeedsToRegisterItemModelProperty;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event)
    {
        System.out.println("beans");
        event.enqueueWork(() -> {
            //For hollow coconut vision overlay
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
            EntityRenderers.register(EntityTypeRegistry.BABY_SKELETON.get(), BabySkeletonRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BABY_CREEPER.get(), OdysseyCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.CAMO_CREEPER.get(), CamoCreeperRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.WEAVER.get(), WeaverRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.PASSIVE_WEAVER.get(), PassiveWeaverRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.BABY_LEVIATHAN.get(), BabyLeviathanRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.POLAR_BEAR.get(), PolarBearRenderer::new);

            //Boss Renderings
            EntityRenderers.register(EntityTypeRegistry.ABANDONED_IRON_GOLEM.get(), AbandonedIronGolemRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN.get(), MineralLeviathanHeadRenderer::new);
            EntityRenderers.register(EntityTypeRegistry.MINERAL_LEVIATHAN_BODY.get(), MineralLeviathanBodyRenderer::new);
//        EntityRenderers.registerEntityRenderingHandler(EntityTypeRegistry.PERMAFROST.get(), PermafrostRenderer::new);

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
//        minecraft.itemRenderer = new OdysseyItemRenderer(minecraft.getTextureManager(), minecraft.getModelManager(), minecraft.getItemColors());
        });
    }

    @SubscribeEvent
    public static void onTextureStitch(final TextureStitchEvent.Pre event){
        //Shield Textures
        for(OdysseyShieldItem.ShieldType shieldType : OdysseyShieldItem.ShieldType.values()){
            event.addSprite(OdysseyBlockEntityWithoutLevelRenderer.getShieldRenderMaterial(shieldType, false).texture());
            event.addSprite(OdysseyBlockEntityWithoutLevelRenderer.getShieldRenderMaterial(shieldType, true).texture());
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
        event.registerLayerDefinition(MineralLeviathanHeadModel.LAYER_LOCATION, MineralLeviathanHeadModel::createBodyLayer);
        event.registerLayerDefinition(MineralLeviathanBodyModel.LAYER_LOCATION, MineralLeviathanBodyModel::createBodyLayer);
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
