package potions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import util.RegistryHandler;

public class ModPotions {
	
	private static Method brewing_mixes;
	
	private static void addMix(Potion start, Item ingredient, Potion result) {
		if(brewing_mixes == null) {
			brewing_mixes = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addMix", Potion.class, Item.class, Potion.class);
			brewing_mixes.setAccessible(true);
		}
		
		try {
			brewing_mixes.invoke(null, start, ingredient, result);
		}
		catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
			e.printStackTrace();
		}
	}
	
	public static void addBrewingRecipes() {
		addMix(Potions.AWKWARD, Items.IRON_INGOT, RegistryHandler.DEFENSE.get());
		addMix(RegistryHandler.DEFENSE.get(), Items.REDSTONE, RegistryHandler.LONG_DEFENSE.get());
		
	}

}
