package de.nedelosk.modularmachines.common.modules.storaged.drives.engine;

import java.util.List;

import de.nedelosk.modularmachines.api.gui.IContainerBase;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventory;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventoryBuilder;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.slots.SlotModule;
import de.nedelosk.modularmachines.api.modules.handlers.tank.IModuleTank;
import de.nedelosk.modularmachines.api.modules.handlers.tank.IModuleTankBuilder;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storaged.drives.IModuleEngine;
import de.nedelosk.modularmachines.client.gui.widgets.WidgetFluidTank;
import de.nedelosk.modularmachines.common.core.FluidManager;
import de.nedelosk.modularmachines.common.modules.handlers.FluidFilter;
import de.nedelosk.modularmachines.common.modules.handlers.ItemFluidFilter;
import de.nedelosk.modularmachines.common.modules.handlers.ModulePage;
import de.nedelosk.modularmachines.common.modules.handlers.OutputAllFilter;
import de.nedelosk.modularmachines.common.utils.ModuleUtil;
import net.minecraftforge.fluids.FluidStack;

public class ModuleEngineSteam extends ModuleEngine {

	public ModuleEngineSteam() {
		super("engine.steam");
	}

	@Override
	public boolean removeMaterial(IModuleState state) {
		IModuleTank tank = (IModuleTank) state.getContentHandler(IModuleTank.class);
		if(tank == null){
			return false;
		}
		FluidStack drained = tank.drainInternal(new FluidStack(FluidManager.Steam, getMaterialPerWork(state)), false);
		if (drained != null && drained.amount >= getMaterialPerWork(state)) {
			return tank.drainInternal(new FluidStack(FluidManager.Steam, getMaterialPerWork(state)), true).amount >= getMaterialPerWork(state);
		} else {
			return false;
		}
	}

	@Override
	public void updateServer(IModuleState state, int tickCount) {
		super.updateServer(state, tickCount);

		if(state.getModular().updateOnInterval(20)){
			IModuleInventory inventory = (IModuleInventory) state.getContentHandler(IModuleInventory.class);
			IModuleTank tank = (IModuleTank) state.getContentHandler(IModuleTank.class);

			ModuleUtil.tryEmptyContainer(0, 1, inventory, tank.getTank(0));
		}
	}

	@Override
	public boolean canWork(IModuleState state) {
		IModuleTank tank = (IModuleTank) state.getContentHandler(IModuleTank.class);
		if(tank == null){
			return false;
		}
		if (tank.getTank(0).getFluid() == null) {
			return false;
		} 
		if (tank.getTank(0).getFluid().amount > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<IModulePage> createPages(IModuleState state) {
		List<IModulePage> pages = super.createPages(state);
		pages.add(new EnginePage("Basic", state));
		return pages;
	}

	public class EnginePage extends ModulePage<IModuleEngine>{

		public EnginePage(String pageID, IModuleState<IModuleEngine> module) {
			super(pageID, "engine.steam", module);
		}

		@Override
		public void createInventory(IModuleInventoryBuilder invBuilder) {
			invBuilder.addInventorySlot(true, 15, 28, new ItemFluidFilter(true));
			invBuilder.addInventorySlot(false, 15, 48, new OutputAllFilter());
		}

		@Override
		public void createSlots(IContainerBase<IModularHandler> container, List<SlotModule> modularSlots) {
			modularSlots.add(new SlotModule(state, 0).setBackgroundTexture("liquid"));
			modularSlots.add(new SlotModule(state, 1).setBackgroundTexture("container"));
		}

		@Override
		public void createTank(IModuleTankBuilder tankBuilder) {
			tankBuilder.addFluidTank(16000, true, 80, 18, new FluidFilter(FluidManager.Steam));
		}

		@Override
		public void addWidgets() {
			add(new WidgetFluidTank(state.getContentHandler(IModuleTank.class).getTank(0)));
		}

	}
}