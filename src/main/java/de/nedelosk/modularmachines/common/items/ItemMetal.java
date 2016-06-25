package de.nedelosk.modularmachines.common.items;

import java.util.List;

import de.nedelosk.modularmachines.common.core.Registry;
import de.nedelosk.modularmachines.common.core.TabModularMachines;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMetal extends Item implements IItemModelRegister {

	private String[][] metals;
	private String uln;
	private String iconName;

	public ItemMetal(String uln, String iconName, String[][] metals) {
		setCreativeTab(TabModularMachines.tabForestMods);
		setUnlocalizedName(uln);
		setHasSubtypes(true);
		this.metals = metals;
		this.uln = uln;
		this.iconName = iconName;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		for(int m = 0; m < metals.length; m++) {
			String[] metal = metals[m];
			for(int i = 0; i < metal.length; ++i) {
				manager.registerItemModel(item, m * 10 + i, uln + "/" + iconName + metal[i]);
			}
		}
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int m = 0; m < metals.length; m++) {
			String[] metal = metals[m];
			for(int i = 0; i < metal.length; ++i) {
				list.add(new ItemStack(item, 1, m * 10 + i));
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return Registry.setUnlocalizedItemName(iconName + getName(itemstack.getItemDamage()));
	}

	private String getName(int id) {
		int i = 0;
		while (id > 9) {
			i++;
			id -= 10;
		}
		return metals[i][id];
	}
}
