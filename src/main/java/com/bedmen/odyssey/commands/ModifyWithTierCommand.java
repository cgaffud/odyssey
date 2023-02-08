package com.bedmen.odyssey.commands;

import com.bedmen.odyssey.aspect.AspectTierManager;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.PermabuffAspect;
import com.bedmen.odyssey.commands.arguments.ItemModifierArgument;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class ModifyWithTierCommand {

    private static final DynamicCommandExceptionType ERROR_TIER = new DynamicCommandExceptionType((object) -> {
        return new TranslatableComponent("commands.modifywithtier.failed.tier", object, AspectTierManager.HIGHEST_TIER);
    });

    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType((object) -> {
        return new TranslatableComponent("commands.enchant.failed.entity", object);
    });
    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType((object) -> {
        return new TranslatableComponent("commands.enchant.failed.itemless", object);
    });
    private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(new TranslatableComponent("commands.enchant.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("modifywithtier")
                .requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("tier", IntegerArgumentType.integer())
                                .executes((commandContext) -> modify(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "tier"), 1.0f))
                                .then(Commands.argument("chance", FloatArgumentType.floatArg(0.0f, 1.0f))
                                        .executes((commandContext) -> modify(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "tier"), FloatArgumentType.getFloat(commandContext, "chance")))))));
    }

    private static int modify(CommandSourceStack commandSourceStack, Collection<? extends Entity> entityCollection, int tier, float chance) throws CommandSyntaxException {
        if(tier > AspectTierManager.HIGHEST_TIER){
            throw ERROR_TIER.create(tier);
        }

        int numSuccess = 0;
        boolean isSingleEntity = entityCollection.size() == 1;

        for(Entity entity : entityCollection) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack itemstack = livingEntity.getMainHandItem();
                if (!itemstack.isEmpty()) {
                    AspectUtil.resetAddedModifiers(itemstack);
                    AspectTierManager.itemStackBuffedByTier(itemstack, livingEntity.getRandom(), tier, chance);
                    ++numSuccess;
                } else if (isSingleEntity) {
                    throw ERROR_NO_ITEM.create(livingEntity.getName().getString());
                }
            } else if (isSingleEntity) {
                throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
            }
        }

        if (numSuccess == 0) {
            throw ERROR_NOTHING_HAPPENED.create();
        } else {
            if (isSingleEntity) {
                commandSourceStack.sendSuccess(new TranslatableComponent("commands.modify.success.single", entityCollection.iterator().next().getDisplayName()), true);
            } else {
                commandSourceStack.sendSuccess(new TranslatableComponent("commands.modify.success.multiple", entityCollection.size()), true);
            }

            return numSuccess;
        }
    }

}
