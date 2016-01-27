package nedelosk.forestcore.library.utils;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

public class FluidUtil {

	public static boolean fillContainers(IFluidHandler fluidHandler, IInventory inv, int inputSlot, int outputSlot, Fluid fluidToFill) {
		ItemStack input = inv.getStackInSlot(inputSlot);
		ItemStack output = inv.getStackInSlot(outputSlot);
		ItemStack filled = getFilledContainer(fluidToFill, input);
		if (filled != null && (output == null || (output.stackSize < output.getMaxStackSize() && ItemUtil.isItemEqual(filled, output)))) {
			FluidStack fluidInContainer = getFluidStackInContainer(filled);
			FluidStack drain = fluidHandler.drain(ForgeDirection.UNKNOWN, fluidInContainer, false);
			if (drain != null && drain.amount == fluidInContainer.amount) {
				fluidHandler.drain(ForgeDirection.UNKNOWN, fluidInContainer, true);
				if (output == null) {
					inv.setInventorySlotContents(outputSlot, filled);
				} else {
					output.stackSize++;
				}
				inv.decrStackSize(inputSlot, 1);
				return true;
			}
		}
		return false;
	}

	public static boolean drainContainers(IFluidHandler fluidHandler, IInventory inv, int inputSlot, int outputSlot) {
		ItemStack input = inv.getStackInSlot(inputSlot);
		ItemStack output = inv.getStackInSlot(outputSlot);
		if (input != null) {
			FluidStack fluidInContainer = getFluidStackInContainer(input);
			ItemStack emptyItem = input.getItem().getContainerItem(input);
			if (fluidInContainer != null
					&& (emptyItem == null || output == null || (output.stackSize < output.getMaxStackSize() && ItemUtil.isItemEqual(output, emptyItem)))) {
				int used = fluidHandler.fill(ForgeDirection.UNKNOWN, fluidInContainer, false);
				if (used >= fluidInContainer.amount) {
					fluidHandler.fill(ForgeDirection.UNKNOWN, fluidInContainer, true);
					if (emptyItem != null) {
						if (output == null) {
							inv.setInventorySlotContents(outputSlot, emptyItem);
						} else {
							output.stackSize++;
						}
					}
					inv.decrStackSize(inputSlot, 1);
					return true;
				}
			}
		}
		return false;
	}

	public static FluidStack getFluidStackInContainer(ItemStack stack) {
		return FluidContainerRegistry.getFluidForFilledItem(stack);
	}

	public static ItemStack getFilledContainer(Fluid fluid, ItemStack empty) {
		if (fluid == null || empty == null) {
			return null;
		}
		return FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, Integer.MAX_VALUE), empty);
	}

	public static FluidStack getFluidFromItem(ItemStack stack) {
		if (stack != null) {
			FluidStack fluidStack = null;
			if (stack.getItem() instanceof IFluidContainerItem) {
				fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
			}
			if (fluidStack == null) {
				fluidStack = FluidContainerRegistry.getFluidForFilledItem(stack);
			}
			if (fluidStack == null && Block.getBlockFromItem(stack.getItem()) instanceof IFluidBlock) {
				Fluid fluid = ((IFluidBlock) Block.getBlockFromItem(stack.getItem())).getFluid();
				if (fluid != null) {
					return new FluidStack(fluid, 1000);
				}
			}
			return fluidStack;
		}
		return null;
	}
}