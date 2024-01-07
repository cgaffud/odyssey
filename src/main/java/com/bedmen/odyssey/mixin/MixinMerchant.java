package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.trades.OdysseyMerchantInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.OptionalInt;

/**
 * @author cgaffud
 * @reason Since merchant level is currently 1-5, we can use the remainder of allotted
 * space to pack w/ info about merchant lost within network transfer
 */
@Mixin(Merchant.class)
public interface MixinMerchant {

    @Shadow
    MerchantOffers getOffers();
    @Shadow
    int getVillagerXp();
    @Shadow
    boolean showProgressBar();
    @Shadow
    default boolean canRestock() {
        return false;
    }

    // p_45304_ is Merchant Level
    default void openTradingScreen(Player p_45302_, Component p_45303_, int p_45304_) {
        OptionalInt optionalint = p_45302_.openMenu(new SimpleMenuProvider((p_45298_, p_45299_, p_45300_) -> new MerchantMenu(p_45298_, p_45299_, (Merchant) this), p_45303_));
        if (optionalint.isPresent()) {
            MerchantOffers merchantoffers = this.getOffers();
            if (!merchantoffers.isEmpty()) {
                int packedLevel = p_45304_;
                if (this instanceof Villager villager) {
                    packedLevel += OdysseyMerchantInfo.packProfession(villager.getVillagerData().getProfession());
                }
                p_45302_.sendMerchantOffers(optionalint.getAsInt(), merchantoffers, packedLevel, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
            }
        }

    }

}
