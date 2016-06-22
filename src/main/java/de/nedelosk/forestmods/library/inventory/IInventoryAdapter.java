package de.nedelosk.forestmods.library.inventory;

import de.nedelosk.modularmachines.api.recipes.IRecipeInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;

public interface IInventoryAdapter extends ISidedInventory, IRecipeInventory {

	void readFromNBT(NBTTagCompound nbttagcompound);

	void writeToNBT(NBTTagCompound nbttagcompound);
}