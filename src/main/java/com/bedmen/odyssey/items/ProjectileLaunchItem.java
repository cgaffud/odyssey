package com.bedmen.odyssey.items;

import com.bedmen.odyssey.entity.projectile.SwungProjectile;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

public class ProjectileLaunchItem extends Item {
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers;
    public final SwungProjectileFactory swungProjectileFactory;
    public ProjectileLaunchItem(Properties properties, float attackRate, SwungProjectileFactory swungProjectileFactory) {
        super(properties);
        Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
        attributeModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)attackRate - 4.0d, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = attributeModifiers;
        this.swungProjectileFactory = swungProjectileFactory;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public interface SwungProjectileFactory {
        SwungProjectile get(ServerLevel serverLevel, ServerPlayer serverPlayer);
    }
}
