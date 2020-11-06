package util;

import com.jeremybrennan.odyssey.Odyssey;
import com.jeremybrennan.odyssey.items.ItemBase;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
	
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Odyssey	.MOD_ID);
	
	public static void init()
	{
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	//Items
	public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", ItemBase::new);

}
