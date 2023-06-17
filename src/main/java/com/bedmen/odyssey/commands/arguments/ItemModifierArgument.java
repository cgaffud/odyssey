package com.bedmen.odyssey.commands.arguments;

import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ItemModifierArgument implements ArgumentType<Aspect> {
    private static final Collection<String> EXAMPLES = Arrays.asList("knockback", "smite_damage");
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_MODIFIER = new DynamicCommandExceptionType((object) -> new TranslatableComponent("modifier.unknown", object));

    public static ItemModifierArgument modifier() {
        return new ItemModifierArgument();
    }

    public static Aspect getModifier(CommandContext<CommandSourceStack> commandContext, String argument) {
        return commandContext.getArgument(argument, Aspect.class);
    }

    public Aspect parse(StringReader stringReader) throws CommandSyntaxException {
        ResourceLocation resourcelocation = ResourceLocation.read(stringReader);
        String aspectName = resourcelocation.getPath();
        if(Aspects.ASPECT_REGISTER.containsKey(aspectName)){
            return Aspects.ASPECT_REGISTER.get(aspectName);
        }
        throw ERROR_UNKNOWN_MODIFIER.create(aspectName);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest(Aspects.ASPECT_REGISTER.keySet(), suggestionsBuilder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}