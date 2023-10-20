package com.bedmen.odyssey.commands;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.commands.arguments.ItemModifierArgument;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class ModifyCommand {

    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType((object) -> {
        return Component.translatable("commands.enchant.failed.entity", object);
    });
    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType((object) -> {
        return Component.translatable("commands.enchant.failed.itemless", object);
    });
    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType((p_137020_) -> {
        return Component.translatable("commands.modify.failed.incompatible", p_137020_);
    });
    private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.enchant.failed"));
    private static final DynamicCommandExceptionType ERROR_NOT_BUFF = new DynamicCommandExceptionType((object) -> {
        return Component.translatable("commands.modify.failed.buff", object);
    });


    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("modify")
                .requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("itemStack")
                    .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("modifier", ItemModifierArgument.modifier())
                                .executes((commandContext) -> modifyItemStack(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ItemModifierArgument.getModifier(commandContext, "modifier"), 1, false, false))
                                .then(Commands.argument("strength", FloatArgumentType.floatArg(0.0f))
                                        .executes((commandContext) -> modifyItemStack(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ItemModifierArgument.getModifier(commandContext, "modifier"), FloatArgumentType.getFloat(commandContext, "strength"), false, false))
                                        .then(Commands.argument("obfuscated", BoolArgumentType.bool())
                                                .executes((commandContext) -> modifyItemStack(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ItemModifierArgument.getModifier(commandContext, "modifier"), FloatArgumentType.getFloat(commandContext, "strength"), BoolArgumentType.getBool(commandContext, "obfuscated"), false))
                                                .then(Commands.argument("bypass_checks", BoolArgumentType.bool())
                                                        .executes((commandContext) -> modifyItemStack(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ItemModifierArgument.getModifier(commandContext, "modifier"), FloatArgumentType.getFloat(commandContext, "strength"), BoolArgumentType.getBool(commandContext, "obfuscated"), BoolArgumentType.getBool(commandContext, "bypass_checks")))))))))
                .then(Commands.literal("entity")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("modifier", ItemModifierArgument.modifier())
                                        .executes((commandContext) -> modifyEntity(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ItemModifierArgument.getModifier(commandContext, "modifier"), 1))
                                        .then(Commands.argument("strength", FloatArgumentType.floatArg(0.0f))
                                                .executes((commandContext) -> modifyEntity(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ItemModifierArgument.getModifier(commandContext, "modifier"), FloatArgumentType.getFloat(commandContext, "strength"))))))));
    }

    private static int modifyItemStack(CommandSourceStack commandSourceStack, Collection<? extends Entity> entityCollection, Aspect<?> aspect, float strength, boolean obfuscated, boolean bypassChecks) throws CommandSyntaxException {
        int numSuccess = 0;
        boolean isSingleEntity = entityCollection.size() == 1;

        for(Entity entity : entityCollection) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack itemstack = livingEntity.getMainHandItem();
                if (!itemstack.isEmpty()) {
                    AspectInstance<?> aspectInstance = new AspectInstance<>(aspect, aspect.floatToValue(strength), aspect.getValueClass());
                    if(bypassChecks || AspectUtil.canAddModifier(itemstack, aspectInstance)){
                        aspectInstance = obfuscated ? aspectInstance.withObfuscation() : aspectInstance;
                        AspectUtil.replaceModifier(itemstack, aspectInstance);
                        ++numSuccess;
                    } else if (isSingleEntity) {
                        throw ERROR_INCOMPATIBLE.create(itemstack.getItem().getName(itemstack).getString());
                    }
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
                commandSourceStack.sendSuccess(Component.translatable("commands.modify.itemstack.success.single", aspect.getComponent(), entityCollection.iterator().next().getDisplayName()), true);
            } else {
                commandSourceStack.sendSuccess(Component.translatable("commands.modify.itemstack.success.multiple", aspect.getComponent(), entityCollection.size()), true);
            }

            return numSuccess;
        }
    }

    private static int modifyEntity(CommandSourceStack commandSourceStack, Collection<? extends Entity> entityCollection, Aspect<?> aspect, float strength) throws CommandSyntaxException {
        if(!aspect.isBuff){
            throw ERROR_NOT_BUFF.create(aspect.getComponent());
        }

        int numSuccess = 0;
        boolean isSingleEntity = entityCollection.size() == 1;

        for(Entity entity : entityCollection) {
            if (entity instanceof OdysseyLivingEntity odysseyLivingEntity) {
                AspectInstance aspectInstance = new AspectInstance(aspect, strength);
                odysseyLivingEntity.setPermaBuff(aspectInstance);
                ++numSuccess;
            } else if (isSingleEntity) {
                throw ERROR_NOT_LIVING_ENTITY.create(entity.getName());
            }
        }

        if (numSuccess == 0) {
            throw ERROR_NOTHING_HAPPENED.create();
        } else {
            if (isSingleEntity) {
                commandSourceStack.sendSuccess(Component.translatable("commands.modify.player.success.single", aspect.getComponent(), entityCollection.iterator().next().getDisplayName()), true);
            } else {
                commandSourceStack.sendSuccess(Component.translatable("commands.modify.player.success.multiple", aspect.getComponent(), entityCollection.size()), true);
            }

            return numSuccess;
        }
    }

}
