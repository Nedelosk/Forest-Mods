package modularmachines.common.core.managers;

import net.minecraft.item.Item;

import modularmachines.common.core.Registry;
import modularmachines.common.items.ItemCasing;
import modularmachines.common.items.ItemModuleMeta;

public class ModItems {
	
	public static Item itemModuleRack;
	public static Item itemEngineSteam;
	public static Item itemEngineElectric;
	public static Item itemTurbineSteam;
	public static Item itemCasings;
	
	public static void preInit() {
		itemCasings = register(new ItemCasing());
		itemModuleRack = register(new ItemModuleMeta("module_rack", new String[]{"brick", "bronze", "iron", "steel", "magmarium"}));
		itemEngineSteam = register(new ItemModuleMeta("engine_steam", new String[]{"bronze", "iron", "steel", "magmarium"}));
		itemEngineElectric = register(new ItemModuleMeta("engine_electric", new String[]{"bronze", "iron", "steel", "magmarium"}));
		itemTurbineSteam = register(new ItemModuleMeta("turbine_steam", new String[]{"bronze", "iron", "steel", "magmarium"}));
	}
	
	public static <I extends Item> I register(I item) {
		Registry.register(item, item.getUnlocalizedName().replace("forest.item.", "").replace("item.", ""));
		return item;
	}
}