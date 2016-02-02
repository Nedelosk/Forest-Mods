package nedelosk.modularmachines.api.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Maps;

import nedelosk.modularmachines.api.modular.IModular;
import nedelosk.modularmachines.api.modular.basic.IModularDefault;
import nedelosk.modularmachines.api.modules.IModule;
import nedelosk.modularmachines.api.modules.IModuleSaver;
import nedelosk.modularmachines.api.modules.container.module.IModuleContainer;
import nedelosk.modularmachines.api.modules.container.module.IMultiModuleContainer;
import nedelosk.modularmachines.api.modules.container.module.ISingleModuleContainer;
import nedelosk.modularmachines.api.modules.fluids.IModuleWithFluid;
import nedelosk.modularmachines.api.modules.gui.IModuleGui;
import nedelosk.modularmachines.api.modules.inventory.IModuleInventory;
import nedelosk.modularmachines.api.modules.managers.fluids.IModuleTankManager;
import nedelosk.modularmachines.api.modules.managers.fluids.IModuleTankManagerSaver;

public class ModularUtils {

	public static HashMap<String, ModuleStack<IModuleWithFluid, IModuleSaver>> getFluidProducers(IModular modular) {
		HashMap<String, ModuleStack<IModuleWithFluid, IModuleSaver>> stacks = Maps.newHashMap();
		for ( ModuleStack stack : (List<ModuleStack>) modular.getModuleManager().getModuleStacks() ) {
			if (stack != null && stack.getModule() != null && stack.getModule() instanceof IModuleWithFluid) {
				if (((IModuleWithFluid) stack.getModule()).useFluids(stack)) {
					stacks.put(stack.getModule().getUID(), stack);
				}
			}
		}
		return stacks;
	}

	/* MODULES FROM MODULAR */
	public static ISingleModuleContainer getCasing(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.CASING);
	}

	public static ModuleStack<IModuleTankManager, IModuleTankManagerSaver> getTankManager(IModular modular) {
		return getModuleStack(modular, ModuleCategoryUIDs.MANAGERS, ModuleCategoryUIDs.MANAGER_TANK);
	}

	public static IMultiModuleContainer getManagers(IModular modular) {
		return getMultiContainer(modular, ModuleCategoryUIDs.MANAGERS);
	}

	public static ISingleModuleContainer getBattery(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.BATTERY);
	}

	public static ISingleModuleContainer getEngine(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.ENGINE);
	}

	public static ISingleModuleContainer getMachine(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.MACHINE);
	}

	public static ISingleModuleContainer getCapacitor(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.CAPACITOR);
	}

	public static IMultiModuleContainer<IModule, IModuleSaver, Collection<ModuleStack<IModule, IModuleSaver>>> getMultiContainer(IModular modular,
			String categoryUID) {
		IModuleContainer container = getModuleStack(modular, categoryUID);
		if (container == null || !(container instanceof IMultiModuleContainer)) {
			return null;
		}
		return (IMultiModuleContainer) container;
	}

	public static ModuleStack getModuleStack(IModular modular, String categoryUID, String moduleUID) {
		return modular.getModuleManager().getModuleFromUID(categoryUID + ":" + moduleUID);
	}

	public static ISingleModuleContainer getSingleContainer(IModular modular, String categoryUID) {
		IModuleContainer container = getModuleStack(modular, categoryUID);
		if (container == null || !(container instanceof ISingleModuleContainer)) {
			return null;
		}
		return (ISingleModuleContainer) container;
	}

	public static IModuleContainer getModuleStack(IModular modular, String categoryUID) {
		if (modular == null) {
			return null;
		}
		return modular.getModuleManager().getModule(categoryUID);
	}

	public static <M extends IModule, S extends IModuleSaver> ModuleStack<M, S> getModuleStackFromGui(IModularDefault modular, IModuleGui<M, S> gui) {
		if (gui == null) {
			return null;
		}
		return modular.getModuleManager().getModuleFromUID(gui.getUID());
	}

	public static <M extends IModule, S extends IModuleSaver> ModuleStack<M, S> getModuleStackFromInventory(IModularDefault modular,
			IModuleInventory<M, S> inv) {
		if (inv == null) {
			return null;
		}
		return modular.getModuleManager().getModuleFromUID(inv.getCategoryUID() + ":" + inv.getModuleUID());
	}
}