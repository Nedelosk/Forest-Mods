package nedelosk.modularmachines.api.modular.machines.basic;

import nedelosk.modularmachines.api.modular.utils.ModuleStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotModular extends Slot {

	public ModuleStack stack;
	
	public SlotModular(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ModuleStack stack) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		this.stack = stack;
	}
	
	@Override
	public ItemStack getStack() {
		return ((IModularTileEntity<IModularInventory>)inventory).getModular().getInventoryManager().getStackInSlot(stack.getModule().getName(stack), this.getSlotIndex());
	}
	
	@Override
    public void putStack(ItemStack p_75215_1_)
    {
		((IModularTileEntity<IModularInventory>)inventory).getModular().getInventoryManager().setInventorySlotContents(stack.getModule().getName(stack), getSlotIndex(), p_75215_1_);
        this.onSlotChanged();
    }
	
	@Override
    public ItemStack decrStackSize(int p_75209_1_)
    {
        return ((IModularTileEntity<IModularInventory>)inventory).getModular().getInventoryManager().decrStackSize(stack.getModule().getName(stack), getSlotIndex(), p_75209_1_);
    }

}