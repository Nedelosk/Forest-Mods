package de.nedelosk.modularmachines.common.core;

import de.nedelosk.modularmachines.api.material.EnumMaterials;
import de.nedelosk.modularmachines.common.items.ItemModule;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TabModularMachines extends CreativeTabs {

	public static CreativeTabs tabForestMods = new TabModularMachines(0, "forestmods");
	public static CreativeTabs tabComponents = new TabModularMachines(1, "forestmods.components");
	public static CreativeTabs tabModules = new TabModularMachines(2, "forestmods.modules");
	private final int tabIcon;

	private TabModularMachines(int tabIcon, String label) {
		super(label);
		this.tabIcon = tabIcon;
	}

	@Override
	public ItemStack getIconItemStack() {
		Item iconItem;
		int iconMeta;
		ItemStack stack = null;
		switch (tabIcon) {
			case 0:
				stack = new ItemStack(BlockManager.blockCampfire, 1, 2);
				break;
			case 1:
				stack = new ItemStack(ItemManager.itemCompGears, 1, 5);
				break;
			case 2:
				stack = ItemModule.getItem(new ModuleUID(ModuleUIDs.PRODUCER + ":" + ModuleUIDs.MACHINE_ALLOY_SMELTER), EnumMaterials.BRONZE);
				break;
		}
		return stack;
	}

	@Override
	public Item getTabIconItem() {
		return null;
	}
}