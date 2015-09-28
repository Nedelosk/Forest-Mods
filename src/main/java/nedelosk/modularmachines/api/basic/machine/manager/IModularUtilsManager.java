package nedelosk.modularmachines.api.basic.machine.manager;

import cofh.api.energy.IEnergyHandler;
import net.minecraftforge.fluids.IFluidHandler;

public interface IModularUtilsManager{

	IFluidHandler getFluidHandler();
	
	IEnergyHandler getEnergyHandler();
	
	void setFluidHandler(IFluidHandler fluidHandler);
	
	void setEnergyHandler(IEnergyHandler energyHandler);
	
}
