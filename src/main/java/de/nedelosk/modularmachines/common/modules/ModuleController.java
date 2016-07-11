package de.nedelosk.modularmachines.common.modules;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.modularmachines.api.modules.IModelInitHandler;
import de.nedelosk.modularmachines.api.modules.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.IModuleController;
import de.nedelosk.modularmachines.api.modules.IModuleState;
import de.nedelosk.modularmachines.api.modules.models.IModelHandler;
import de.nedelosk.modularmachines.client.modules.ModelHandlerDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleController extends Module implements IModuleController {

	public final int allowedComplexity;
	public final int allowedModuleComplexity;
	public final int allowedToolComplexity;
	public final int allowedDriveComplexity;

	public ModuleController(int allowedComplexity, int allowedModuleComplexity, int allowedToolComplexity, int allowedDriveComplexity) {
		super("controller", 0);
		this.allowedComplexity = allowedComplexity;
		this.allowedModuleComplexity = allowedModuleComplexity;
		this.allowedToolComplexity = allowedToolComplexity;
		this.allowedDriveComplexity = allowedDriveComplexity;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IModelHandler createModelHandler(IModuleState state) {
		return new ModelHandlerDefault(new ResourceLocation("modularmachines:module/controllers/" + state.getContainer().getMaterial().getName()));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public List<IModelInitHandler> getInitModelHandlers(IModuleContainer container) {
		List<IModelInitHandler> handlers = new ArrayList<>();
		handlers.add(new ModelHandlerDefault(new ResourceLocation("modularmachines:module/controllers/" + container.getMaterial().getName())));
		return handlers;
	}

	@Override
	public int getAllowedComplexity(IModuleState state) {
		return allowedComplexity;
	}
}
