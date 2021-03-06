package modularmachines.common.modules.components;

import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;

import modularmachines.api.modules.INBTReadable;
import modularmachines.api.modules.INBTWritable;
import modularmachines.api.modules.components.IFuelComponent;
import modularmachines.api.modules.components.handlers.IFluidHandlerComponent;
import modularmachines.api.modules.components.handlers.IItemHandlerComponent;

public abstract class FuelComponent extends ModuleComponent implements INBTWritable, INBTReadable, IFuelComponent {
	int fuel;
	int fuelTotal;
	private final int fuelPerUse;
	
	public FuelComponent(int fuelPerUse) {
		this.fuelPerUse = fuelPerUse;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		fuel = compound.getInteger("Fuel");
		fuelTotal = compound.getInteger("FuelTotal");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("Fuel", fuel);
		compound.setInteger("FuelTotal", fuelTotal);
		return compound;
	}
	
	@Override
	public int getFuel() {
		return fuel;
	}
	
	@Override
	public int getFuelTotal() {
		return fuelTotal;
	}
	
	@Override
	public boolean hasFuel() {
		return fuel > 0;
	}
	
	@Override
	public void removeFuel(double modifier) {
		fuel -= fuelPerUse * modifier;
	}
	
	public static class Items extends FuelComponent {
		private final Function<ItemStack, Integer> fuelGetter;
		private final int fuelSlot;
		
		public Items(int fuelPerUse, int fuelSlot) {
			this(fuelPerUse, TileEntityFurnace::getItemBurnTime, fuelSlot);
		}
		
		public Items(int fuelPerUse, Function<ItemStack, Integer> fuelGetter, int fuelSlot) {
			super(fuelPerUse);
			this.fuelGetter = fuelGetter;
			this.fuelSlot = fuelSlot;
		}
		
		@Override
		public void updateFuel() {
			IItemHandlerComponent itemHandler = provider.getComponent(IItemHandlerComponent.class);
			if (itemHandler == null) {
				return;
			}
			ItemStack input = itemHandler.getStackInSlot(fuelSlot);
			if (input.isEmpty() || itemHandler.extractItemInternal(fuelSlot, 1, false).isEmpty()) {
				return;
			}
			fuel = fuelTotal = fuelGetter.apply(input);
		}
	}
	
	public static class Fluids extends FuelComponent {
		private final Function<FluidStack, Integer> fuelGetter;
		private final int neededAmount;
		private final int fuelSlot;
		
		public Fluids(int fuelPerUse, Function<FluidStack, Integer> fuelGetter, int fuelSlot, int neededAmount) {
			super(fuelPerUse);
			this.fuelGetter = fuelGetter;
			this.fuelSlot = fuelSlot;
			this.neededAmount = neededAmount;
		}
		
		@Override
		public void updateFuel() {
			IFluidHandlerComponent fluidHandler = provider.getComponent(IFluidHandlerComponent.class);
			if (fluidHandler == null) {
				return;
			}
			IFluidHandlerComponent.ITank tank = fluidHandler.getTank(fuelSlot);
			if (tank == null) {
				return;
			}
			FluidStack input = tank.getFluid();
			if (input == null) {
				return;
			}
			input = tank.drainInternal(neededAmount, true);
			if (input == null) {
				return;
			}
			fuel = fuelTotal = fuelGetter.apply(input);
		}
	}
	
	public static class Energy extends FuelComponent {
		private final Function<Integer, Integer> fuelGetter;
		private final int energyPerUse;
		
		public Energy(int fuelPerUse, Function<Integer, Integer> fuelGetter, int energyPerUse) {
			super(fuelPerUse);
			this.fuelGetter = fuelGetter;
			this.energyPerUse = energyPerUse;
		}
		
		@Override
		public void updateFuel() {
			IEnergyStorage energyStorage = provider.getContainer().getComponent(IEnergyStorage.class);
			if (energyStorage == null) {
				return;
			}
			int extractedEnergy = energyStorage.extractEnergy(energyPerUse, true);
			if (extractedEnergy < energyPerUse) {
				return;
			}
			fuel = fuelTotal = fuelGetter.apply(extractedEnergy);
		}
	}
}
