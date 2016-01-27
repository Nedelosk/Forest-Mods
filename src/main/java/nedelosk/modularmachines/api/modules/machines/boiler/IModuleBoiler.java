package nedelosk.modularmachines.api.modules.machines.boiler;

import nedelosk.modularmachines.api.modules.machines.recipe.IModuleMachineRecipe;

public interface IModuleBoiler extends IModuleMachineRecipe {

	int getHeat();

	int getHeatTotal();
}