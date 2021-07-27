package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.util.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public CooldownTracker getCooldowns() {return null;}
    @Shadow
    public int takeXpDelay;
    @Shadow
    private int sleepCounter;
    @Shadow
    public void stopSleepInBed(boolean p_225652_1_, boolean p_225652_2_) {}
    @Shadow
    protected boolean updateIsUnderwater() {return false;}
    @Shadow
    public Container containerMenu;
    @Shadow
    public void closeContainer() {}
    @Shadow
    public PlayerContainer inventoryMenu;
    @Shadow
    private void moveCloak() {}
    @Shadow
    protected FoodStats foodData;
    @Shadow
    public void awardStat(ResourceLocation p_195066_1_) {}
    @Shadow
    private ItemStack lastItemInMainHand;
    @Shadow
    public void resetAttackStrengthTicker() {}
    @Shadow
    private void turtleHelmetTick() {}
    @Shadow
    private CooldownTracker cooldowns;
    @Shadow
    protected void updatePlayerPose() {}


    protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    //Remade the amount of xp needed up to level 50 because oddc has lvl 50 enchants
    public int getXpNeededForNextLevel() {
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

    //Allows for ammo to be found from quivers
    public ItemStack getProjectile(ItemStack shootable) {
        if (!(shootable.getItem() instanceof ShootableItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                NonNullList<ItemStack> offhand = this.inventory.offhand;
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

                predicate = ((ShootableItem)shootable.getItem()).getAllSupportedProjectiles();

                for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                    ItemStack itemstack1 = this.inventory.getItem(i);
                    if (predicate.test(itemstack1)) {
                        return itemstack1;
                    }
                }

                return this.abilities.instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }

    //Disables both kinds of shields
    public void disableShield(boolean p_190777_1_) {
        float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (p_190777_1_) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            this.getCooldowns().addCooldown(ItemRegistry.SHIELD.get(), EnchantmentUtil.getRecovery(this));
            this.getCooldowns().addCooldown(ItemRegistry.SERPENT_SHIELD.get(), EnchantmentUtil.getRecovery(this));
            this.stopUsingItem();
            this.level.broadcastEntityEvent(this, (byte)30);
        }

    }


    /** I'm not sure if this is needed in a method anymore but it helps.
     * It also might be better to move this method to ItemStack or something */





    public void tick() {
        net.minecraftforge.fml.hooks.BasicEventHooks.onPlayerPreTick(toPlayerEntity(this));
        this.noPhysics = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }

        //Sets player on fire unless they have fire protection or an arctic chestplate
        if(!(this.abilities.instabuild || this.isSpectator()) && this.level.dimensionType().ultraWarm()){
            boolean fireFlag = true;
            for(ItemStack stack : this.getArmorSlots()){
                if(stack.getItem() == ItemRegistry.ARCTIC_CHESTPLATE.get()){
                    fireFlag = false;
                    break;
                }
               fireFlag &= !EnchantmentUtil.checkStackForEnch(stack,"minecraft:fire_protection");
            }
            if(fireFlag)
                this.setSecondsOnFire(1);
        }

        if (this.takeXpDelay > 0) {
            --this.takeXpDelay;
        }

        if (this.isSleeping()) {
            ++this.sleepCounter;
            if (this.sleepCounter > 100) {
                this.sleepCounter = 100;
            }

            if (!this.level.isClientSide && !net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck(toPlayerEntity(this), getSleepingPos())) {
                this.stopSleepInBed(false, true);
            }
        } else if (this.sleepCounter > 0) {
            ++this.sleepCounter;
            if (this.sleepCounter >= 110) {
                this.sleepCounter = 0;
            }
        }

        this.updateIsUnderwater();
        super.tick();
        if (!this.level.isClientSide && this.containerMenu != null && !this.containerMenu.stillValid(toPlayerEntity(this))) {
            this.closeContainer();
            this.containerMenu = this.inventoryMenu;
        }

        this.moveCloak();
        if (!this.level.isClientSide) {
            this.foodData.tick(toPlayerEntity(this));
            this.awardStat(Stats.PLAY_ONE_MINUTE);
            if (this.isAlive()) {
                this.awardStat(Stats.TIME_SINCE_DEATH);
            }

            if (this.isDiscrete()) {
                this.awardStat(Stats.CROUCH_TIME);
            }

            if (!this.isSleeping()) {
                this.awardStat(Stats.TIME_SINCE_REST);
            }
        }

        int i = 29999999;
        double d0 = MathHelper.clamp(this.getX(), -2.9999999E7D, 2.9999999E7D);
        double d1 = MathHelper.clamp(this.getZ(), -2.9999999E7D, 2.9999999E7D);
        if (d0 != this.getX() || d1 != this.getZ()) {
            this.setPos(d0, this.getY(), d1);
        }

        ++this.attackStrengthTicker;
        ItemStack itemstack = this.getMainHandItem();
        if (!ItemStack.matches(this.lastItemInMainHand, itemstack)) {
            if (!ItemStack.isSameIgnoreDurability(this.lastItemInMainHand, itemstack)) {
                this.resetAttackStrengthTicker();
            }

            this.lastItemInMainHand = itemstack.copy();
        }

        this.turtleHelmetTick();
        this.cooldowns.tick();
        this.updatePlayerPose();
        net.minecraftforge.fml.hooks.BasicEventHooks.onPlayerPostTick(toPlayerEntity(this));
    }


    private PlayerEntity toPlayerEntity(MixinPlayerEntity mixinPlayerEntity){
        return (PlayerEntity)(Object)mixinPlayerEntity;
    }
}
