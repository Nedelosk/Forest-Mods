package modularmachines.common.modules.transfer.items;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import net.minecraftforge.items.IItemHandler;

import modularmachines.common.modules.transfer.ITransferHandlerWrapper;
import modularmachines.common.modules.transfer.TransferCycle;
import modularmachines.common.utils.ItemUtil;

public class ItemTransferCycle extends TransferCycle<IItemHandler> {

	protected final int[] slots;
	protected final int[] insertSlots;
	protected Predicate<ItemStack> matcher;
	
	public ItemTransferCycle(ModuleTransferItem moduleTransfer, ITransferHandlerWrapper<IItemHandler> startHandler, int[] slots, ITransferHandlerWrapper<IItemHandler> endHandler, int[] insertSlots, int time, int priority, int amount, NonNullList<ItemStack> filters, boolean metaMode, boolean oredictMode, boolean blacklist, boolean nbtMode) {
		super(moduleTransfer, startHandler, endHandler, time, priority, amount);
		if (slots == null || slots.length == 0) {
			slots = generateDefaultSlots(moduleTransfer.getHandler(startHandler));
		}
		this.slots = slots;
		if (insertSlots == null || insertSlots.length == 0) {
			insertSlots = generateDefaultSlots(moduleTransfer.getHandler(endHandler));
		}
		this.insertSlots = insertSlots;
		NonNullList<ItemStack> filterList = NonNullList.create();
		for (ItemStack stack : filters) {
			if (!stack.isEmpty()) {
				filterList.add(stack);
			}
		}
		if (filterList.isEmpty()) {
			matcher = itemStack -> true;
		} else {
			ItemFilterCache filterCache = new ItemFilterCache(metaMode, oredictMode, blacklist, nbtMode, filterList);
			matcher = filterCache::match;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setIntArray("Slots", slots);
		compound.setIntArray("InsertSlots", insertSlots);
		return compound;
	}
	
	public ItemTransferCycle(ModuleTransferItem moduleTransfer, NBTTagCompound compound) {
		super(moduleTransfer, compound);
		slots = compound.getIntArray("Slots");
		insertSlots = compound.getIntArray("InsertSlots");
	}

	@Override
	public void work(int ticks) {
		if (ticks % time == 0) {
			ItemUtil.transferStacks(moduleTransfer, this);
		}
	}

	@Override
	public boolean canWork() {
		return true;
	}

	protected int[] generateDefaultSlots(IItemHandler handler) {
		return ItemUtil.getSlots(handler);
	}
	
	public int[] getSlots() {
		return slots;
	}

	public int[] getInsertSlots() {
		return insertSlots;
	}

	@Override
	public int getComplexity() {
		return 0;
	}

	@Override
	public Predicate<ItemStack> getFilter() {
		return matcher;
	}
}