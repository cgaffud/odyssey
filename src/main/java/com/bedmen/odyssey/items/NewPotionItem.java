package com.bedmen.odyssey.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NewPotionItem extends Item {
    public NewPotionItem(Item.Properties builder) {
        super(builder);
    }

    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;
        if (playerentity instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)playerentity, stack);
        }

        if (!worldIn.isClientSide) {
            for(EffectInstance effectinstance : PotionUtils.getMobEffects(stack)) {
                if (effectinstance.getEffect().isInstantenous()) {
                    effectinstance.getEffect().applyInstantenousEffect(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
                } else {
                    entityLiving.addEffect(new EffectInstance(effectinstance));
                }
            }
        }

        if (playerentity != null) {
            playerentity.awardStat(Stats.ITEM_USED.get(this));
            if (!playerentity.abilities.instabuild) {
                stack.shrink(1);
            }
        }

        if (playerentity == null || !playerentity.abilities.instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerentity != null) {
                playerentity.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return DrinkHelper.useDrink(worldIn, playerIn, handIn);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getDescriptionId(ItemStack stack) {
        return PotionUtils.getPotion(stack).getName(this.getDescriptionId() + ".effect.");
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    /**
     * Returns true if this item has an enchantment glint. By default, this returns <code>stack.isItemEnchanted()</code>,
     * but other items can override it (for instance, written books always return true).
     *
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            for(Potion potion : Registry.POTION) {
                if (potion != Potions.EMPTY) {
                    items.add(PotionUtils.setPotion(new ItemStack(this), potion));
                }
            }
        }

    }

    public static void RegisterBaseProperties(Item item){
        ItemModelsProperties.register(item, new ResourceLocation("type"),  (itemStack, world, entity) -> {
            CompoundNBT compoundnbt = itemStack.getTag();
            if(compoundnbt != null && compoundnbt.contains("Potion")){
                String s = compoundnbt.get("Potion").getAsString();
                if(s.contains("awkward")) return 3;
                else if(s.contains("long")) return 1;
                else if(s.contains("strong")) return 2;
            }
            return 0;
        });
    }

}
