package nedelosk.modularmachines.api.basic.machine;

import cofh.api.energy.IEnergyHandler;
import nedelosk.modularmachines.api.basic.machine.modular.IModular;
import nedelosk.nedeloskcore.api.INBTTagable;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public interface IModularTileEntity<M extends IModular> extends ISidedInventory, INBTTagable, IFluidHandler, IEnergyHandler {

	World getWorldObj();
	
	int getXCoord();
	
	int getYCoord();
	
	int getZCoord();
	
	M getModular();
	
}
