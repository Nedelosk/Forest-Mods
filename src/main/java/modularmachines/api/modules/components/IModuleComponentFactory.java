package modularmachines.api.modules.components;

import net.minecraft.util.math.AxisAlignedBB;

import modularmachines.api.modules.IModule;
import modularmachines.api.modules.components.block.IBoundingBoxComponent;
import modularmachines.api.modules.components.block.IInteractionComponent;
import modularmachines.api.modules.components.handlers.IEnergyHandlerComponent;
import modularmachines.api.modules.components.handlers.IFluidHandlerComponent;
import modularmachines.api.modules.components.handlers.IIOComponent;
import modularmachines.api.modules.components.handlers.IItemHandlerComponent;

/**
 * A factory to create module components.
 */
public interface IModuleComponentFactory {
	/**
	 * Creates and adds a bounding box component to the module.
	 *
	 * @param module      The module
	 * @param boundingBox The bounding box that should be added to the module.
	 * @return The created bounding box component.
	 */
	IBoundingBoxComponent addBoundingBox(IModule module, AxisAlignedBB boundingBox);
	
	/**
	 * Creates and adds a item handler and a io component to the module.
	 *
	 * @param module The module
	 * @return The created item handler component.
	 */
	IItemHandlerComponent addItemHandler(IModule module);
	
	/**
	 * Creates and adds a fluid handler and a io component to the module.
	 *
	 * @param module The module
	 * @return The created fluid handler component.
	 */
	IFluidHandlerComponent addFluidHandler(IModule module);
	
	/**
	 * Creates and adds a energy handler and a io component to the module.
	 *
	 * @param module   The module
	 * @param capacity The capacity of the energy handler
	 * @return The created energy handler component.
	 */
	IEnergyHandlerComponent addEnergyHandler(IModule module, int capacity);
	
	/**
	 * Creates and adds a energy handler and a io component to the module.
	 *
	 * @param module   The module
	 * @param capacity The capacity of the energy handler
	 * @return The created energy handler component.
	 */
	IEnergyHandlerComponent addEnergyHandler(IModule module, int capacity, int maxTransfer);
	
	/**
	 * Creates and adds a energy handler and a io component to the module.
	 *
	 * @param module   The module
	 * @param capacity The capacity of the energy handler
	 * @return The created energy handler component.
	 */
	IEnergyHandlerComponent addEnergyHandler(IModule module, int capacity, int maxReceive, int maxExtract);
	
	/**
	 * Creates and adds a io component to the module.
	 *
	 * @param module The module
	 * @return The created io component.
	 */
	IIOComponent addIO(IModule module);
	
	/**
	 * Creates and adds a gui component to the module.
	 *
	 * @param module The module
	 * @return The created gui component.
	 */
	IInteractionComponent addGui(IModule module, IGuiFactory guiFactory);
}
