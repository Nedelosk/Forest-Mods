package de.nedelosk.forestmods.common.crafting;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.forestcore.utils.OreStack;
import de.nedelosk.forestmods.api.multiblocks.IBlastFurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class BlastFurnaceRecipeManager implements IBlastFurnaceRecipe {

	private static ArrayList<BlastFurnaceRecipe> recipes = new ArrayList();
	public static BlastFurnaceRecipeManager instance;

	public static void addRecipe(BlastFurnaceRecipe recipe) {
		recipes.add(recipe);
	}

	public static void removeRecipe(BlastFurnaceRecipe recipe) {
		recipes.remove(recipe);
	}

	public static void addAllRecipes(List<BlastFurnaceRecipe> recipe) {
		recipes.addAll(recipe);
	}

	@Override
	public void addRecipe(int burnTime, FluidStack[] output, Object[] input, int heat) {
		recipes.add(new BlastFurnaceRecipe(burnTime, output, input, heat));
	}

	public static boolean isItemInput(ItemStack stack) {
		for(BlastFurnaceRecipe sr : BlastFurnaceRecipeManager.recipes) {
			for(Object oj : sr.getInput()) {
				if (oj instanceof ItemStack) {
					ItemStack stackInput = (ItemStack) oj;
					if (stackInput.getItem() == stack.getItem() && stackInput.getItemDamage() == stack.getItemDamage()
							&& ItemStack.areItemStackTagsEqual(stack, stackInput)) {
						return true;
					}
				} else if (oj instanceof OreStack) {
					List<ItemStack> list = OreDictionary.getOres(((OreStack) oj).oreDict);
					for(ItemStack stackInput : list) {
						if (stackInput.getItem() == stack.getItem() && stackInput.getItemDamage() == stack.getItemDamage()
								&& ItemStack.areItemStackTagsEqual(stack, stackInput)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static BlastFurnaceRecipe getRecipe(ItemStack[] inputs) {
		for(BlastFurnaceRecipe sr : BlastFurnaceRecipeManager.recipes) {
			boolean isBreak = false;
			Inputs: for(int i = 0; i < sr.getInput().length; i++) {
				if (sr.getInput()[i] != null) {
					if (inputs[i] == null) {
						isBreak = true;
						break;
					}
					if (sr.getInput()[i] instanceof ItemStack) {
						if (((ItemStack) sr.getInput()[i]).getItem() == inputs[i].getItem() && ((ItemStack) sr.getInput()[i]).stackSize <= inputs[i].stackSize
								&& ((ItemStack) sr.getInput()[i]).getItemDamage() == inputs[i].getItemDamage()
								&& ItemStack.areItemStackTagsEqual(inputs[i], (ItemStack) sr.getInput()[i])) {
							continue;
						}
					}
					if (sr.getInput()[i] instanceof OreStack) {
						if (!(((OreStack) sr.getInput()[i]).stackSize <= inputs[i].stackSize)) {
							isBreak = true;
							break;
						}
						int ore = OreDictionary.getOreID(((OreStack) sr.getInput()[i]).getOreDict());
						for(int oreID : OreDictionary.getOreIDs(inputs[i])) {
							if (oreID == ore) {
								continue Inputs;
							}
						}
					}
					isBreak = true;
				}
			}
			if (!isBreak) {
				return sr;
			}
		}
		return null;
	}

	public static ArrayList<BlastFurnaceRecipe> getRecipes() {
		return recipes;
	}

	public static BlastFurnaceRecipeManager getInstance() {
		return instance;
	}
}