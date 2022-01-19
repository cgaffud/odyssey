package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.projectile.OdysseyAbstractArrow;
import com.bedmen.odyssey.items.OdysseyCrossbowItem;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.OdysseyRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class EquipmentCrossbowItem extends OdysseyCrossbowItem implements IEquipment {
    protected final Set<LevEnchSup> levEnchSupSet = new HashSet<>();
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    protected static final List<EquipmentCrossbowItem> UNFINISHED_EQUIPMENT = new ArrayList<>();

    public EquipmentCrossbowItem(Properties propertiesIn, float velocity, int chargeTime, LevEnchSup... levEnchSups) {
        super(propertiesIn.rarity(OdysseyRarity.EQUIPMENT), velocity, chargeTime);
        this.levEnchSupSet.add(UNENCHANTABLE);
        Collections.addAll(this.levEnchSupSet, levEnchSups);
        UNFINISHED_EQUIPMENT.add(this);
    }

    public static void initEquipment(){
        for(final EquipmentCrossbowItem equipmentCrossbowItem : UNFINISHED_EQUIPMENT){
            equipmentCrossbowItem.init();
        }
        UNFINISHED_EQUIPMENT.clear();
    }

    public void init(){
        for(LevEnchSup levEnchSup : this.levEnchSupSet){
            this.enchantmentMap.put(levEnchSup.enchantmentSupplier.get(), levEnchSup.level);
        }
    }

    public int getInnateEnchantmentLevel(Enchantment e) {
        Integer i = this.enchantmentMap.get(e);
        if(i == null)
            return 0;
        return i;
    }

    public Map<Enchantment, Integer> getInnateEnchantmentMap(){
        return this.enchantmentMap;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return this.getInnateEnchantmentLevel(enchantment) == 0 && enchantment.category.canEnchant(stack.getItem());
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        int j = this.getInnateEnchantmentLevel(EnchantmentRegistry.PUNCH_ARROWS.get());
        if (j > 0) {
            arrow.setKnockback(j);
        }
        j = this.getInnateEnchantmentLevel(EnchantmentRegistry.FLAMING_ARROWS.get());
        if (j > 0) {
            arrow.setSecondsOnFire(100*j);
        }
        j = this.getInnateEnchantmentLevel(EnchantmentRegistry.PIERCING.get());
        if (j > 0) {
            arrow.setPierceLevel((byte)j);
        }
        j = this.getInnateEnchantmentLevel(Enchantments.MOB_LOOTING);
        if(j > 0 && arrow instanceof OdysseyAbstractArrow){
            ((OdysseyAbstractArrow) arrow).setLootingLevel((byte)j);
        }
        return super.customArrow(arrow);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        for(Enchantment e : this.enchantmentMap.keySet()){
            if(EnchantmentRegistry.UNENCHANTABLE.get() == e && flagIn.isAdvanced())
                tooltip.add(1, EnchantmentUtil.getUnenchantableName());
            else if (EnchantmentRegistry.UNENCHANTABLE.get() != e )
                tooltip.add(e.getFullname(this.enchantmentMap.get(e)));
        }
        super.appendHoverText(stack, level, tooltip, flagIn);
    }
}
