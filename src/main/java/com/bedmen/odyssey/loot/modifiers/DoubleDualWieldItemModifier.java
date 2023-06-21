package com.bedmen.odyssey.loot.modifiers;

import com.bedmen.odyssey.combat.WeaponUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DoubleDualWieldItemModifier extends LootModifier {

    public static final Codec<DoubleDualWieldItemModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, DoubleDualWieldItemModifier::new));
    public DoubleDualWieldItemModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> addList = new ArrayList<>();
        for(ItemStack itemStack : generatedLoot){
            if(WeaponUtil.isDualWieldItem(itemStack)){
                addList.add(itemStack.copy());
            }
        }
        generatedLoot.addAll(addList);
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}