/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.bedmen.odyssey.tier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord") // can't make it a record because the method names will be obfuscated
public class OdysseyTier implements Tier
{
    private final int uses;
    private final float speed;
    private final TagKey<Block> tag;
    @Nonnull
    public final Supplier<Ingredient> repairIngredient;

    public OdysseyTier(@Nonnull Supplier<Ingredient> repairIngredient) {
        this(1, repairIngredient);
    }

    public OdysseyTier(int uses, @Nonnull Supplier<Ingredient> repairIngredient) {
        this(uses, 2.0f, repairIngredient);
    }

    public OdysseyTier(int uses, float speed, @Nonnull Supplier<Ingredient> repairIngredient) {
        this(uses, speed, null, repairIngredient);
    }

    public OdysseyTier(int uses,
                       float speed,
                       TagKey<Block> tag,
                       @Nonnull Supplier<Ingredient> repairIngredient)
    {
        this.uses = uses;
        this.speed = speed;
        this.tag = tag;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses()
    {
        return this.uses;
    }

    @Override
    public float getSpeed()
    {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return -1.0f;
    }

    @Override
    public int getLevel()
    {
        return 0;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 0;
    }

    public TagKey<Block> getTag()
    {
        return this.tag;
    }

    @Nonnull
    @Override
    public Ingredient getRepairIngredient()
    {
        return this.repairIngredient.get();
    }
}
