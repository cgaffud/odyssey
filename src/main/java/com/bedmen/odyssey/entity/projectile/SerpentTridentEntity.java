package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.util.EntityTypeRegistry;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SerpentTridentEntity extends AbstractTridentEntity {

    public SerpentTridentEntity(EntityType<? extends SerpentTridentEntity> type, World worldIn) {
        super(type, worldIn, new ItemStack(ItemRegistry.SERPENT_TRIDENT.get()));
    }

    public SerpentTridentEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn, double damage) {
        super(worldIn, thrower, thrownStackIn, damage, EntityTypeRegistry.SERPENT_TRIDENT.get());
    }

    @OnlyIn(Dist.CLIENT)
    public SerpentTridentEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z, EntityTypeRegistry.SERPENT_TRIDENT.get(),new ItemStack(ItemRegistry.SERPENT_TRIDENT.get()));
    }
}
