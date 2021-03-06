package modularmachines.api.modules;

import javax.annotation.Nullable;
import java.util.Collection;

import net.minecraft.util.math.RayTraceResult;

import modularmachines.api.modules.components.IModuleComponent;
import modularmachines.api.modules.container.IModuleContainer;
import modularmachines.api.modules.positions.IModulePosition;

/**
 * A interface that can be implemented by {@link IModuleComponent}s and {@link IModuleContainer}s to provide a
 * {@link IModuleHandler} and module positions.
 */
public interface IModuleProvider {
	
	/**
	 * @return A list that contains all modules of the {@link #getHandler()} and all modules that are contained by the
	 * modules.
	 */
	Collection<IModule> getModules();
	
	/**
	 * @return The module handler that this provider contains.
	 */
	IModuleHandler getHandler();
	
	/**
	 * @return The position that is at the given position of the {@link RayTraceResult}.
	 */
	@Nullable
	IModulePosition getPosition(RayTraceResult hit);
	
	/**
	 * @return The module container that implements this or contains the module that implements this interface.
	 */
	IModuleContainer getContainer();
	
	/**
	 * Checks if the given module can be placed at the given position at the {@link IModuleHandler} of this provider.
	 */
	default boolean isValidModule(IModulePosition position, IModuleType type) {
		return type.getData().isValidPosition(position);
	}
}
