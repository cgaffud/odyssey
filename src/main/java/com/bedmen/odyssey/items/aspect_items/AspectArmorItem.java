package com.bedmen.odyssey.items.aspect_items;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.OdysseyArmorMaterial;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.OdysseyTierItem;
import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

import java.util.List;

public class AspectArmorItem extends ArmorItem implements InnateAspectItem, OdysseyTierItem {

    private final AspectHolder innateAspectHolder;
    private final AspectHolder abilityHolder;

    public AspectArmorItem(Properties properties, OdysseyArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<AspectInstance<?>> abilityList, List<AspectInstance<?>> innateModifierList) {
        super(armorMaterial, equipmentSlot, properties);
        this.innateAspectHolder = new AspectHolder(innateModifierList, AspectHolderType.INNATE_ASPECT);
        this.abilityHolder = new AspectHolder(abilityList, AspectHolderType.ABILITY);
    }

    public OdysseyArmorMaterial getOdysseyArmorMaterial(){
        return (OdysseyArmorMaterial)this.material;
    }

    public AspectHolder getSetBonusAbilityHolder() {
        return this.getOdysseyArmorMaterial().getSetBonusAbilityHolder();
    }

    public Tier getTier(){
        return this.getOdysseyArmorMaterial().getTier();
    }

    public AspectHolder getInnateAspectHolder() {
        return this.innateAspectHolder;
    }

    public AspectHolder getAbilityHolder() {
        return this.abilityHolder;
    }

    public List<AspectHolder> getAspectHolderList(){
        return List.of(this.getSetBonusAbilityHolder(), this.innateAspectHolder, this.abilityHolder);
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
    {
        return AspectUtil.getItemStackAspectValue(stack, Aspects.PIGLIN_NEUTRAL);
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity livingEntity) {
        if(this.slot != EquipmentSlot.CHEST){
            return false;
        }
        if(!(AspectUtil.getArmorAspectValue(livingEntity, Aspects.GLIDE) > 0 || AspectUtil.getItemStackAspectValue(stack, Aspects.GLIDE) > 0)){
            return false;
        }
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getFlightValue() > 0;
        }
        return false;
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity livingEntity, int flightTicks) {
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity && odysseyLivingEntity.getFlightValue() > 0){
            if(!(livingEntity instanceof Player player) || GeneralUtil.isSurvival(player)){
                odysseyLivingEntity.decrementFlight();
            }
            return true;
        }
        return false;
    }
}
