package nedelosk.modularmachines.common.modular.module.producer.tool;

import nedelosk.modularmachines.api.modular.machines.basic.IModular;
import nedelosk.modularmachines.api.modular.module.basic.basic.Module;
import nedelosk.modularmachines.api.modular.module.producer.tool.IModuleTool;
import nedelosk.modularmachines.api.modular.utils.ModuleStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ModuleTool extends Module implements IModuleTool {

	public ModuleTool(String modifier) {
		super(modifier);
	}
	
	public ModuleTool(NBTTagCompound nbt) {
		super(nbt);
	}
	
	public int tier;

	@Override
	public void update(IModular modular, ModuleStack stack) {
	}
	
	@Override
	public String getModuleName() {
		return "tool";
	}

}
