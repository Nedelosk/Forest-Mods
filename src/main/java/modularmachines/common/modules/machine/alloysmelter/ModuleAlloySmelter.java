package modularmachines.common.modules.machine.alloysmelter;

import modularmachines.api.modules.IModuleStorage;
import modularmachines.api.recipes.IRecipeConsumer;
import modularmachines.api.recipes.RecipeItem;
import modularmachines.common.inventory.ItemHandlerModule;
import modularmachines.common.modules.filters.FilterMachine;
import modularmachines.common.modules.filters.OutputFilter;
import modularmachines.common.modules.machine.MachineCategorys;
import modularmachines.common.modules.machine.ModuleHeatMachine;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleAlloySmelter extends ModuleHeatMachine {
	
	public final ItemHandlerModule itemHandler;
	
	public ModuleAlloySmelter(IModuleStorage storage, int workTimeModifier) {
		super(storage, workTimeModifier);
		itemHandler = new ItemHandlerModule(this);
		itemHandler.addSlot(true).addFilter(FilterMachine.INSTANCE);
		itemHandler.addSlot(true).addFilter(FilterMachine.INSTANCE);
		itemHandler.addSlot(false).addFilter(OutputFilter.INSTANCE);
		itemHandler.addSlot(false).addFilter(OutputFilter.INSTANCE);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		itemHandler.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		itemHandler.writeToNBT(compound);
		return compound;
	}
	
	@Override
	protected void initPages() {
		super.initPages();
		addPage(new PageAlloySmelter(this));
	}

	@Override
	protected IRecipeConsumer[] getConsumers() {
		return new IRecipeConsumer[]{itemHandler};
	}

	@Override
	public RecipeItem[] getInputs() {
		return itemHandler.getInputs();
	}

	@Override
	protected String getRecipeCategory() {
		return MachineCategorys.ALLOY_SMELTER;
	}
	
	public ItemHandlerModule getItemHandler() {
		return itemHandler;
	}

}