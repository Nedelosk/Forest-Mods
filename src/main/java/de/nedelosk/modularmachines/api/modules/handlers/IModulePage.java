package de.nedelosk.modularmachines.api.modules.handlers;

import java.util.List;

import de.nedelosk.modularmachines.api.inventory.IContainerBase;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularTileEntity;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventoryBuilder;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.slots.SlotModule;
import de.nedelosk.modularmachines.api.modules.handlers.tank.IModuleTankBuilder;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;

public interface IModulePage extends IPage {

	void createTank(IModuleTankBuilder tankBuilder);
	
	void createInventory(IModuleInventoryBuilder invBuilder);

	void createSlots(IContainerBase<IModularTileEntity> container, List<SlotModule> modularSlots);

	int getPageID();

	IModular getModular();

	IModuleState getModuleState();
}
