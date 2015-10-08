package nedelosk.modularmachines.api.modular.module.basic.gui;

import nedelosk.modularmachines.api.modular.machines.basic.IModular;
import nedelosk.modularmachines.api.modular.module.basic.basic.Module;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class ModuleGui extends Module implements IModuleGui {

	public ModuleGui() {
		super();
	}
	
	public ModuleGui(String modifier) {
		super(modifier);
	}
	
	public ModuleGui(NBTTagCompound nbt) {
		super(nbt);
	}
	
	@Override
	public int getGuiTop(IModular modular) {
		return 166;
	}

	@Override
	public ResourceLocation getCustomGui(IModular modular) {
		return null;
	}
	
}