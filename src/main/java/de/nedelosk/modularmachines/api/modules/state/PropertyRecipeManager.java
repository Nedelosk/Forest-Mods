package de.nedelosk.modularmachines.api.modules.state;

import de.nedelosk.modularmachines.api.modules.IRecipeManager;
import de.nedelosk.modularmachines.common.modules.tools.RecipeManager;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyRecipeManager extends PropertyBase<IRecipeManager, NBTTagCompound> {

	public PropertyRecipeManager(String name) {
		super(name, IRecipeManager.class);
	}

	@Override
	public NBTTagCompound writeToNBT(IModuleState state, IRecipeManager value) {
		NBTTagCompound nbtTag = new NBTTagCompound();
		value.writeToNBT(nbtTag, state);
		return nbtTag;
	}

	@Override
	public IRecipeManager readFromNBT(NBTTagCompound nbt, IModuleState state) {
		IRecipeManager recipeManager = new RecipeManager();
		recipeManager.readFromNBT(nbt, state);
		return recipeManager;
	}

}