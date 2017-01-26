package modularmachines.common.containers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import modularmachines.api.ILocatable;
import modularmachines.api.modules.assemblers.IStoragePage;
import modularmachines.api.modules.assemblers.SlotAssemblerStorage;
import modularmachines.api.modules.storages.IStoragePosition;
import modularmachines.common.modules.assembler.Assembler;
import modularmachines.common.utils.ContainerUtil;

public class ContainerAssembler extends BaseContainer<Assembler> {

	private final IStoragePage page;
	private boolean afterPage = false;
	private boolean transferStack = false;
	private boolean hasStorageChange = false;

	public ContainerAssembler(Assembler assembler, InventoryPlayer inventory) {
		super(assembler, inventory);
		IStoragePosition position = assembler.getSelectedPosition();
		assembler.updatePages();
		this.page = assembler.getPage(position);
		// Add slots to container
		if (page.isEmpty()) {
			addSlotToContainer(new SlotAssemblerStorage(assembler, this, 44, 35, page));
		} else {
			List<Slot> slots = new ArrayList<>();
			page.createSlots(this, source, slots);
			for (Slot slot : slots) {
				addSlotToContainer(slot);
			}
		}
		if (page != null) {
			page.setContainer(this);
			page.onSlotChanged(this, source);
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
		ILocatable locatable = source.getLocatable();
		if(locatable != null){
			ContainerUtil.openGuiSave(source);
			hasStorageChange = false;
		}
		return stack;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if (page != null) {
			page.setContainer(null);
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (page != null) {
			page.detectAndSendChanges();
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		return super.transferStackInSlot(player, slotIndex);
	}

	@Override
	protected void addSlots(InventoryPlayer inventory) {
	}

	public boolean transferStack() {
		return transferStack;
	}

	public boolean hasStorageChange() {
		return hasStorageChange;
	}

	public void setHasStorageChange(boolean hasChange) {
		this.hasStorageChange = hasChange;
	}
}