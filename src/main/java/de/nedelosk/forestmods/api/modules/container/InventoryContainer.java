package de.nedelosk.forestmods.api.modules.container;

import de.nedelosk.forestmods.api.modular.basic.IModularDefault;
import de.nedelosk.forestmods.api.modules.IModule;
import de.nedelosk.forestmods.api.modules.IModuleDefault;
import de.nedelosk.forestmods.api.modules.IModuleSaver;
import de.nedelosk.forestmods.api.modules.inventory.IModuleInventory;
import de.nedelosk.forestmods.api.utils.ModuleStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryContainer<M extends IModule, S extends IModuleSaver> implements ISingleInventoryContainer<M, S> {

	private IModuleInventory<M, S> inventory;
	private String categoryUID;

	public InventoryContainer(IModuleInventory<M, S> inv, String categoryUID) {
		this.inventory = inv;
		this.categoryUID = categoryUID;
	}

	public InventoryContainer() {
	}

	@Override
	public IModuleInventory<M, S> getInventory() {
		return inventory;
	}

	@Override
	public void setInventory(IModuleInventory<M, S> inv) {
		this.inventory = inv;
	}

	@Override
	public String getCategoryUID() {
		return categoryUID;
	}

	@Override
	public void setCategoryUID(String categoryUID) {
		this.categoryUID = categoryUID;
	}

	/* NBT */
	@Override
	public void writeToNBT(NBTTagCompound nbt, IModularDefault modular) {
		nbt.setString("CategoryUID", categoryUID);
		if (inventory != null) {
			nbt.setString("Module", inventory.getModuleUID());
			ISingleModuleContainer moduleStack = modular.getModuleManager().getSingleModule(categoryUID);
			inventory.writeToNBT(nbt, modular, moduleStack.getStack());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, IModularDefault modular) {
		categoryUID = nbt.getString("CategoryUID");
		if (nbt.hasKey("Module")) {
			String m = nbt.getString("Module");
			ModuleStack moduleStack = modular.getModuleManager().getSingleModule(categoryUID).getStack();
			inventory = ((IModuleDefault) moduleStack.getModule()).createInventory(moduleStack);
			inventory.readFromNBT(nbt, modular, moduleStack);
		}
	}
}