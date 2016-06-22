package de.nedelosk.modularmachines.common.items;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import de.nedelosk.modularmachines.common.core.TabModularMachines;
import de.nedelosk.modularmachines.common.utils.IColoredItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemComponent extends Item implements IColoredItem {

	public ArrayList<List> metas = Lists.newArrayList();
	public String componentName;

	public ItemComponent(String name) {
		this.setUnlocalizedName("component." + name);
		this.setCreativeTab(TabModularMachines.tabComponents);
		this.componentName = name;
	}

	public ItemComponent addMetaData(int color, String name, String... oreDict) {
		metas.add(Lists.newArrayList(color, name, oreDict));
		return this;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "component." + componentName + "." + stack.getItemDamage();
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < metas.size(); i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int renderpass) {
		if (metas.size() > stack.getItemDamage() && metas.get(stack.getItemDamage()) != null) {
			return (int) metas.get(stack.getItemDamage()).get(0);
		}
		return 16777215;
	}
}