package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.UpgradedArrowEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class UpgradedArrowItem extends ArrowItem {
    private final ArrowType arrowType;
    public UpgradedArrowItem(Item.Properties p_i48464_1_, ArrowType arrowType) {
        super(p_i48464_1_);
        this.arrowType = arrowType;
    }

    public AbstractArrowEntity createArrow(World world, ItemStack itemStack, LivingEntity livingEntity) {
        return new UpgradedArrowEntity(world, livingEntity, arrowType);
    }

    public enum ArrowType{
        AMETHYST(ItemRegistry.AMETHYST_ARROW, 2.5d),
        QUARTZ(ItemRegistry.QUARTZ_ARROW, 3.0d),
        RAZOR(ItemRegistry.RAZOR_ARROW, 3.5d);

        private Lazy<Item> itemSupplier;
        private double damage;

        ArrowType(Supplier<Item> itemSupplier, double damage){
            this.itemSupplier = Lazy.of(itemSupplier);
            this.damage = damage;
        }

        public Item getItem(){
            return this.itemSupplier.get();
        }

        public double getDamage(){
            return this.damage;
        }
    }
}