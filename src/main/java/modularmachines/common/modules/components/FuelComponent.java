package modularmachines.common.modules.components;

import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import net.minecraftforge.fluids.FluidStack;

import modularmachines.api.modules.INBTReadable;
import modularmachines.api.modules.INBTWritable;
import modularmachines.api.modules.Module;
import modularmachines.common.inventory.ItemHandlerModule;
import modularmachines.common.modules.ModuleComponent;
import modularmachines.common.tanks.FluidTankHandler;
import modularmachines.common.tanks.FluidTankModule;

public abstract class FuelComponent extends ModuleComponent implements INBTWritable, INBTReadable {
	protected int fuel;
	protected int fuelTotal;
	private final int fuelPerUse;
	
	public FuelComponent(Module module, int fuelPerUse) {
		super(module);
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
	
	public int getFuel() {
		return fuel;
	}
	
	public int getFuelTotal() {
		return fuelTotal;
	}
	
	public boolean hasFuel() {
		return fuel > 0;
	}
	
	public void removeFuel() {
		fuel -= fuelPerUse;
	}
	
	public abstract boolean updateFuel();
	
	public static class Items extends FuelComponent {
		private final Function<ItemStack, Integer> fuelGetter;
		private final int fuelSlot;
		
		public Items(Module module, int fuelPerUse, int fuelSlot) {
			this(module, fuelPerUse, TileEntityFurnace::getItemBurnTime, fuelSlot);
		}
		
		public Items(Module module, int fuelPerUse, Function<ItemStack, Integer> fuelGetter, int fuelSlot) {
			super(module, fuelPerUse);
			this.fuelGetter = fuelGetter;
			this.fuelSlot = fuelSlot;
		}
		
		@Override
		public boolean updateFuel() {
			ItemHandlerModule itemHandler = module.getComponentProvider().getComponent(ItemHandlerModule.class);
			if (itemHandler == null) {
				return false;
			}
			ItemStack input = itemHandler.getStackInSlot(fuelSlot);
			if (input.isEmpty() || itemHandler.extractItemInternal(fuelSlot, 1, false).isEmpty()) {
				return false;
			}
			fuel = fuelGetter.apply(input);
			fuelTotal = fuel;
			return true;
		}
		
		@Override
		public void removeFuel() {
		
		}
	}
	
	public static class Fluids extends FuelComponent {
		private final Function<FluidStack, Integer> fuelGetter;
		private final int neededAmount;
		private final int fuelSlot;
		
		public Fluids(Module module, int fuelPerUse, Function<FluidStack, Integer> fuelGetter, int fuelSlot, int neededAmount) {
			super(module, fuelPerUse);
			this.fuelGetter = fuelGetter;
			this.fuelSlot = fuelSlot;
			this.neededAmount = neededAmount;
		}
		
		@Override
		public boolean updateFuel() {
			FluidTankHandler fluidHandler = module.getComponentProvider().getComponent(FluidTankHandler.class);
			if (fluidHandler == null) {
				return false;
			}
			FluidTankModule tank = fluidHandler.getContainer(fuelSlot);
			if (tank == null) {
				return false;
			}
			FluidStack input = tank.getFluid();
			if (input == null) {
				return false;
			}
			input = tank.drainInternal(neededAmount, true);
			if (input == null) {
				return false;
			}
			fuel = fuelGetter.apply(input);
			fuelTotal = fuel;
			return true;
		}
	}
}
