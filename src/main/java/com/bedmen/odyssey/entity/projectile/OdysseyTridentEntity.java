package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.util.EntityTypeRegistry;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OdysseyTridentEntity extends AbstractTridentEntity {

    public OdysseyTridentEntity(EntityType<? extends OdysseyTridentEntity> type, World worldIn) {
        super(type, worldIn, new ItemStack(ItemRegistry.TRIDENT.get()));
    }

    public OdysseyTridentEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn, double damage) {
        super(worldIn, thrower, thrownStackIn, damage, EntityTypeRegistry.NEW_TRIDENT.get());
    }

    @OnlyIn(Dist.CLIENT)
    public OdysseyTridentEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z, EntityTypeRegistry.NEW_TRIDENT.get(), new ItemStack(ItemRegistry.TRIDENT.get()));
    }
}
