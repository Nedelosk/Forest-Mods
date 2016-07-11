package de.nedelosk.modularmachines.common.modules.engine;

import cofh.api.energy.IEnergyProvider;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModuleIndexStorage;
import de.nedelosk.modularmachines.api.modules.IModuleState;
import de.nedelosk.modularmachines.api.modules.storage.IModuleBattery;
import de.nedelosk.modularmachines.api.modules.tool.IModuleMachine;
import net.minecraftforge.items.IItemHandler;

public class ModuleEngineEnergy extends ModuleEngine {

	public ModuleEngineEnergy(int complexity, int burnTimeModifier, int materialPerTick) {
		super("engine", complexity, burnTimeModifier, materialPerTick);
	}

	@Override
	public boolean assembleModule(IItemHandler itemHandler, IModular modular, IModuleState state, IModuleIndexStorage storage) {
		if(!super.assembleModule(itemHandler, modular, state, storage)){
			return false;
		}
		if(modular.getModules(IModuleBattery.class).isEmpty()){
			return false;
		}
		return true;
	}

	@Override
	public boolean canWork(IModuleState state) {
		IModular modular = state.getModular();
		if(modular.getEnergyHandler() == null){
			return false;
		}
		if (modular.getEnergyHandler().getEnergyStored(null) > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeMaterial(IModuleState state, IModuleState<IModuleMachine> machineState) {
		IEnergyProvider energyHandler = state.getModular().getEnergyHandler();
		if(energyHandler == null){
			return false;
		}
		if (energyHandler.extractEnergy(null, materialPerTick, true) == materialPerTick) {
			return energyHandler.extractEnergy(null, materialPerTick, false) == materialPerTick;
		} else {
			return false;
		}
	}
}
