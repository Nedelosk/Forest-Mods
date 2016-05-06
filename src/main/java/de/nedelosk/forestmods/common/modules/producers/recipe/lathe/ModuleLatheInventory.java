package de.nedelosk.forestmods.common.modules.producers.recipe.lathe;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.forestmods.library.inventory.IContainerBase;
import de.nedelosk.forestmods.library.modules.ModuleUID;
import net.minecraft.inventory.Slot;

public class ModuleLatheInventory extends ModuleProducerRecipeInventory<ModuleLathe, IModuleProducerRecipeModeSaver> {

	public ModuleLatheInventory(ModuleUID UID, int slots) {
		super(UID, slots);
	}

	@Override
	public List<Slot> addSlots(IContainerBase container, IModularDefault modular, ModuleStack<ModuleLathe, IModuleProducerRecipeModeSaver> stack) {
		ArrayList<Slot> list = new ArrayList<Slot>();
		list.addAll(new SlotModularInput(modular.getHandler(), 0, 54, 35, stack));
		list.addAll(new SlotModularOutput(modular.getHandler(), 1, 116, 35, stack));
		list.addAll(new SlotModularOutput(modular.getHandler(), 2, 134, 35, stack));
		return list;
	}
}
