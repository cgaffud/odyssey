package com.bedmen.odyssey.loot.modifiers;

import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DoubleHatchetModifier extends LootModifier {
    public DoubleHatchetModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> addList = new ArrayList<>();
        for(ItemStack itemStack : generatedLoot){
            if(itemStack.getItem() instanceof DualWieldItem){
                addList.add(itemStack.copy());
            }
        }
        generatedLoot.addAll(addList);
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<DoubleHatchetModifier> {

        @Override
        public DoubleHatchetModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            return new DoubleHatchetModifier(conditions);
        }

        @Override
        public JsonObject write(DoubleHatchetModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}