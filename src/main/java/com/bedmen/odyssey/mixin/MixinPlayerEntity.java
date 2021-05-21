package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

    @Shadow
    public int experienceLevel;
    @Shadow
    public PlayerInventory inventory;
    @Shadow
    public PlayerAbilities abilities;
    @Shadow
    public CooldownTracker getCooldownTracker() {return null;}
    @Shadow
    public int xpCooldown;
    @Shadow
    private int sleepTimer;
    @Shadow
    public Container openContainer;
    @Shadow
    protected boolean updateEyesInWaterPlayer() {return false;}
    @Shadow
    private void updateCape() {}
    @Shadow
    public void addStat(ResourceLocation stat) {}
    @Shadow
    public void closeScreen() {}
    @Shadow
    private CooldownTracker cooldownTracker;
    @Shadow
    public void stopSleepInBed(boolean p_225652_1_, boolean p_225652_2_) {}
    @Shadow
    private void updateTurtleHelmet() {}
    @Shadow
    private ItemStack itemStackMainHand;
    @Shadow
    protected void updatePose() {}
    @Shadow
    public void resetCooldown() {}
    @Shadow
    protected FoodStats foodStats;
    @Shadow
    public PlayerContainer container;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public int xpBarCap() {
        if (this.experienceLevel >= 50) {
            return 304 + (this.experienceLevel - 49) * 12;
        } else if (this.experienceLevel >= 40) {
            return 204 + (this.experienceLevel - 39) * 10;
        } else if (this.experienceLevel >= 30) {
            return 124 + (this.experienceLevel - 29) * 8;
        } else if (this.experienceLevel >= 20) {
            return 64 + (this.experienceLevel - 19) * 6;
        } else if (this.experienceLevel >= 10) {
            return 24 + (this.experienceLevel - 9) * 4;
        } else {
            return 4 + (this.experienceLevel+1) * 2;
        }
    }

    public ItemStack findAmmo(ItemStack shootable) {
        if (!(shootable.getItem() instanceof ShootableItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getAmmoPredicate();
            ItemStack itemstack = ShootableItem.getHeldAmmo(this, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                NonNullList<ItemStack> offhand = this.inventory.offHandInventory;
                for (ItemStack itemstack1 : offhand) {
                    Item item = itemstack1.getItem();
                    if (item instanceof QuiverItem) {
                        CompoundNBT compoundNBT = itemstack1.getOrCreateTag();
                        if (compoundNBT.contains("Items", 9)) {
                            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(((QuiverItem) item).getSize(), ItemStack.EMPTY);
                            ItemStackHelper.loadAllItems(compoundNBT, nonnulllist);
                            for (int j = 0; j < nonnulllist.size(); j++) {
                                ItemStack itemstack2 = nonnulllist.get(j);
                                if (predicate.test(itemstack2)) {
                                    return itemstack2;
                                }
                            }
                        }
                    }
                }

                predicate = ((ShootableItem)shootable.getItem()).getInventoryAmmoPredicate();

                for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                    ItemStack itemstack1 = this.inventory.getStackInSlot(i);
                    if (predicate.test(itemstack1)) {
                        return itemstack1;
                    }
                }

                return this.abilities.isCreativeMode ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }

    public void disableShield(boolean p_190777_1_) {
        float f = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
        if (p_190777_1_) {
            f += 0.75F;
        }

        if (this.rand.nextFloat() < f) {
            this.getCooldownTracker().setCooldown(ItemRegistry.SHIELD.get(), EnchantmentUtil.getRecovery(this));
            this.getCooldownTracker().setCooldown(ItemRegistry.SERPENT_SHIELD.get(), EnchantmentUtil.getRecovery(this));
            this.resetActiveHand();
            this.world.setEntityState(this, (byte)30);
        }

    }

    public void tick() {
        net.minecraftforge.fml.hooks.BasicEventHooks.onPlayerPreTick(toPlayerEntity(this));
        this.noClip = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }

        if(!(this.abilities.isCreativeMode || this.isSpectator()) && this.world.getDimensionType().isUltrawarm()){
            boolean fireFlag = true;
            for(ItemStack stack : this.getArmorInventoryList()){
                if(stack.getItem() == ItemRegistry.FROZEN_CHESTPLATE.get()){
                    fireFlag = false;
                    break;
                }
                if (!stack.isEmpty()) {
                    ListNBT listnbt = stack.getEnchantmentTagList();
                    for(int i = 0; i < listnbt.size(); ++i) {
                        String s = listnbt.getCompound(i).getString("id");
                        System.out.println(s);
                        if(s.equals("minecraft:fire_protection")){
                            fireFlag = false;
                            break;
                        }
                    }

                }
            }
            if(fireFlag)
                this.setFire(1);
        }

        if (this.xpCooldown > 0) {
            --this.xpCooldown;
        }

        if (this.isSleeping()) {
            ++this.sleepTimer;
            if (this.sleepTimer > 100) {
                this.sleepTimer = 100;
            }

            if (!this.world.isRemote && !net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck(toPlayerEntity(this), getBedPosition())) {
                this.stopSleepInBed(false, true);
            }
        } else if (this.sleepTimer > 0) {
            ++this.sleepTimer;
            if (this.sleepTimer >= 110) {
                this.sleepTimer = 0;
            }
        }

        this.updateEyesInWaterPlayer();
        super.tick();
        if (!this.world.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(toPlayerEntity(this))) {
            this.closeScreen();
            this.openContainer = this.container;
        }

        this.updateCape();
        if (!this.world.isRemote) {
            this.foodStats.tick(toPlayerEntity(this));
            this.addStat(Stats.PLAY_ONE_MINUTE);
            if (this.isAlive()) {
                this.addStat(Stats.TIME_SINCE_DEATH);
            }

            if (this.isDiscrete()) {
                this.addStat(Stats.SNEAK_TIME);
            }

            if (!this.isSleeping()) {
                this.addStat(Stats.TIME_SINCE_REST);
            }
        }

        int i = 29999999;
        double d0 = MathHelper.clamp(this.getPosX(), -2.9999999E7D, 2.9999999E7D);
        double d1 = MathHelper.clamp(this.getPosZ(), -2.9999999E7D, 2.9999999E7D);
        if (d0 != this.getPosX() || d1 != this.getPosZ()) {
            this.setPosition(d0, this.getPosY(), d1);
        }

        ++this.ticksSinceLastSwing;
        ItemStack itemstack = this.getHeldItemMainhand();
        if (!ItemStack.areItemStacksEqual(this.itemStackMainHand, itemstack)) {
            if (!ItemStack.areItemsEqualIgnoreDurability(this.itemStackMainHand, itemstack)) {
                this.resetCooldown();
            }

            this.itemStackMainHand = itemstack.copy();
        }

        this.updateTurtleHelmet();
        this.cooldownTracker.tick();
        this.updatePose();
        net.minecraftforge.fml.hooks.BasicEventHooks.onPlayerPostTick(toPlayerEntity(this));
    }

    private PlayerEntity toPlayerEntity(MixinPlayerEntity mixinPlayerEntity){
        return (PlayerEntity)(Object)mixinPlayerEntity;
    }
}
