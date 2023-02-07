package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.encapsulator.InnateAspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.SetBonusAspectHolder;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.OdysseyArmorMaterial;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.OdysseyTierItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class AspectArmorItem extends ArmorItem implements InnateAspectItem, OdysseyTierItem {

    private final InnateAspectHolder innateAspectHolder;

    public AspectArmorItem(Properties properties, OdysseyArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        super(armorMaterial, equipmentSlot, properties);
        this.innateAspectHolder = new InnateAspectHolder(abilityList, innateModifierList);
    }

    public OdysseyArmorMaterial getOdysseyArmorMaterial(){
        return (OdysseyArmorMaterial)this.material;
    }

    public SetBonusAspectHolder getSetBonusAbilityHolder() {
        return this.getOdysseyArmorMaterial().getSetBonusAbilityHolder();
    }

    public Tier getTier(){
        return this.getOdysseyArmorMaterial().getTier();
    }

    public InnateAspectHolder getInnateAspectHolder() {
        return this.innateAspectHolder;
    }

    public List<AspectHolder> getAspectHolderList(){
        return List.of(this.getSetBonusAbilityHolder(), this.getInnateAspectHolder());
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
    {
        return AspectUtil.hasBooleanAspect(stack, Aspects.PIGLIN_NEUTRAL);
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity livingEntity) {
        if(this.slot != EquipmentSlot.CHEST){
            return false;
        }
        if(!(AspectUtil.getIntegerAspectValueFromArmor(livingEntity, Aspects.GLIDE) > 0 || AspectUtil.getIntegerAspectStrength(stack, Aspects.GLIDE) > 0)){
            return false;
        }
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getFlightValue() > 0;
        }
        return false;
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity livingEntity, int flightTicks) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity && odysseyLivingEntity.getFlightValue() > 0){
            odysseyLivingEntity.decrementFlight();
            return true;
        }
        return false;
    }
}
