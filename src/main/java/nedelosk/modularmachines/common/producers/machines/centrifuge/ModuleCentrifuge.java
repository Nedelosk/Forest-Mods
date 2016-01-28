package nedelosk.modularmachines.common.producers.machines.centrifuge;

import java.util.ArrayList;
import java.util.List;

import nedelosk.forestcore.library.gui.IGuiBase;
import nedelosk.modularmachines.api.modular.IModular;
import nedelosk.modularmachines.api.modules.IModuleGui;
import nedelosk.modularmachines.api.modules.inventory.IModuleInventory;
import nedelosk.modularmachines.api.modules.machines.recipe.ModuleMachineRecipe;
import nedelosk.modularmachines.api.recipes.IRecipe;
import nedelosk.modularmachines.api.recipes.NeiStack;
import nedelosk.modularmachines.api.recipes.RecipeItem;
import nedelosk.modularmachines.api.utils.ModuleCategoryUIDs;
import nedelosk.modularmachines.api.utils.ModuleStack;

public class ModuleCentrifuge extends ModuleMachineRecipe {

	public ModuleCentrifuge() {
		this(60);
	}

	public ModuleCentrifuge(int speedModifier) {
		super(ModuleCategoryUIDs.MACHINE_CENTRIFUGE, 2, 2, speedModifier);
	}

	@Override
	public List<NeiStack> addNEIStacks(ModuleStack stack, IRecipe recipe) {
		ArrayList<NeiStack> list = new ArrayList<NeiStack>();
		list.add(new NeiStack(56, 24, true));
		list.add(new NeiStack(74, 24, true));
		list.add(new NeiStack(116, 24, false));
		list.add(new NeiStack(134, 24, false));
		return list;
	}

	@Override
	public RecipeItem[] getInputs(IModular modular, ModuleStack stack) {
		return getInputItems(modular, stack);
	}

	@Override
	public String getRecipeName(ModuleStack stack) {
		return "Centrifuge";
	}

	@Override
	public int getColor() {
		return 0xABA8A8;
	}

	@Override
	public IModuleInventory getInventory(ModuleStack stack) {
		return new ModuleCentrifugeInventory(getCategoryUID(), getModuleUID(), itemInputs + itemOutputs);
	}

	@Override
	public IModuleGui getGui(ModuleStack stack) {
		return new ModuleCentrifugeGui(getCategoryUID(), getModuleUID());
	}

	@Override
	public List addNEIWidgets(IGuiBase gui, ModuleStack stack, IRecipe recipe) {
		return null;
	}
}
