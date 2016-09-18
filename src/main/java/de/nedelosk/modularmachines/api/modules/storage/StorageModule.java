package de.nedelosk.modularmachines.api.modules.storage;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.modularmachines.api.modules.EnumModuleSizes;
import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.Module;
import de.nedelosk.modularmachines.api.modules.items.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.position.EnumStoragePositions;
import de.nedelosk.modularmachines.api.modules.position.IStoragePosition;
import de.nedelosk.modularmachines.api.modules.storage.module.IModuleModuleStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public abstract class StorageModule extends Module implements IStorageModule {

	public StorageModule(String name) {
		super(name);
	}

	@Override
	public void addTooltip(List<String> tooltip, ItemStack stack, IModuleContainer container) {
		if(showPosition(container)){
			List<String> positions = new ArrayList<>();
			for(IStoragePosition position : getPositions(container)){
				if(position !=null){
					positions.add(position.getLocName());
				}
			}
			tooltip.add(I18n.translateToLocal("mm.module.tooltip.storage.position") + positions.toString().replace("[", "").replace("]", ""));
		}
		super.addTooltip(tooltip, stack, container);
	}

	protected IStoragePosition[] getPositions(IModuleContainer container){
		if(this instanceof IModuleModuleStorage){
			EnumModuleSizes size = getSize(container);
			if(size == EnumModuleSizes.LARGE) {
				return new IStoragePosition[] {EnumStoragePositions.LEFT, EnumStoragePositions.RIGHT};
			}else if(size == EnumModuleSizes.SMALL) {
				return new IStoragePosition[] {EnumStoragePositions.TOP, EnumStoragePositions.BACK};
			}
		}
		return new IStoragePosition[] {EnumStoragePositions.LEFT, EnumStoragePositions.RIGHT, EnumStoragePositions.TOP, EnumStoragePositions.BACK, EnumStoragePositions.CASING};
	}

	@Override
	protected boolean showPosition(IModuleContainer container) {
		return true;
	}

	@Override
	public boolean isValidForPosition(IStoragePosition position, IModuleContainer container) {
		IModuleProperties properties = container.getProperties();
		if(properties instanceof IStorageModuleProperties){
			return ((IStorageModuleProperties)properties).isValidForPosition(position, container);
		}
		return false;
	}
}
