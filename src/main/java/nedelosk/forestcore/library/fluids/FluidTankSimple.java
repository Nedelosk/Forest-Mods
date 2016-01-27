package nedelosk.forestcore.library.fluids;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankSimple extends FluidTank {

	public FluidTankSimple(int capacity) {
		super(capacity);
	}

	public FluidTankSimple(FluidStack stack, int capacity) {
		super(stack, capacity);
	}

	public FluidTankSimple(Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
	}

	public float getFilledRatio() {
		return (float) getFluidAmount() / getCapacity();
	}

	public boolean isFull() {
		return getFluidAmount() >= getCapacity();
	}

	public boolean isEmpty() {
		return getFluidAmount() <= 0;
	}

	public boolean canDrainFluidType(FluidStack resource) {
		if (resource == null || resource.getFluid() == null || fluid == null) {
			return false;
		}
		return fluid.isFluidEqual(resource);
	}

	public boolean canDrainFluidType(Fluid fl) {
		if (fl == null || fluid == null) {
			return false;
		}
		return fl.getID() == fluid.getFluid().getID();
	}

	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (!canDrainFluidType(resource)) {
			return null;
		}
		return drain(resource.amount, doDrain);
	}
}