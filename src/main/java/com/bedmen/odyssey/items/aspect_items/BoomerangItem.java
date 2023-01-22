package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.combat.BoomerangType;
import com.bedmen.odyssey.entity.projectile.Boomerang;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public class BoomerangItem extends ThrowableWeaponItem {

    public BoomerangItem(Item.Properties properties, BoomerangType boomerangType) {
        super(properties, boomerangType);
    }

    protected Boomerang getThrownWeaponEntity(Level level, LivingEntity owner, ItemStack boomerangStack, boolean isMultishot) {
        return new Boomerang(level, owner, boomerangStack, isMultishot);
    }

    public BoomerangType getBoomerangType(){
        return (BoomerangType) this.throwableType;
    }

    public int getBurnTime(ItemStack boomerangStack, @Nullable RecipeType<?> recipeType)
    {
        return this.getBoomerangType().burnTime;
    }

    protected Optional<SoundEvent> getThrowSound(){
        return Optional.empty();
    }
}
