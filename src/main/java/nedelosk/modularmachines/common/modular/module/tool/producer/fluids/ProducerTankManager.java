package nedelosk.modularmachines.common.modular.module.tool.producer.fluids;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.SideOnly;
import nedelosk.forestday.api.guis.IContainerBase;
import nedelosk.forestday.api.guis.IGuiBase;
import nedelosk.forestday.client.gui.widget.WidgetFluidTank;
import nedelosk.forestday.common.fluids.FluidTankNedelosk;
import nedelosk.modularmachines.api.modular.machines.basic.IModular;
import nedelosk.modularmachines.api.modular.module.tool.producer.basic.ProducerManager;
import nedelosk.modularmachines.api.modular.module.tool.producer.fluids.IProducerFluidManager;
import nedelosk.modularmachines.api.modular.utils.ModuleStack;
import nedelosk.modularmachines.common.modular.machines.modular.handlers.FluidHandler;
import nedelosk.modularmachines.common.modular.module.tool.producer.fluids.manager.TankManager;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class ProducerTankManager extends ProducerManager implements IProducerFluidManager {

	public TankManager manager;
	public int tankSlots;
	
	public ProducerTankManager(int tankSlots) {
		super("TankManager");
		this.tankSlots = tankSlots;
	}
	
	public ProducerTankManager(NBTTagCompound nbt, IModular modular, ModuleStack stack) {
		super(nbt, modular, stack);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt, IModular modular, ModuleStack stack) throws Exception{
		super.writeToNBT(nbt, modular, stack);
		if(manager != null)
			manager.writeToNBT(nbt);
		nbt.setInteger("TankSlots", tankSlots);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt, IModular modular, ModuleStack stack) throws Exception{
		super.readFromNBT(nbt, modular, stack);
		if(modular.getManager().getFluidHandler() != null)
		{
			manager = new TankManager();
			manager.readFromNBT(nbt);
		}
		tankSlots = nbt.getInteger("TankSlots");
	}

	@Override
	@SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
	public void addWidgets(IGuiBase gui, IModular modular, ModuleStack stack) {
		if(modular.getManager().getFluidHandler() != null)
		{
			for(int i = 0;i < ((FluidHandler)modular.getManager().getFluidHandler()).tanks.length;i++)
			{
				FluidTankNedelosk tank = ((FluidHandler)modular.getManager().getFluidHandler()).tanks[i];
				WidgetFluidTank widget = new WidgetFluidTank(tank, 36 + i * 51, 10, i);
				manager.addWidgets(widget, gui);
			}
		}
	}
	
	@Override
	public boolean hasCustomInventoryName(ModuleStack stack) {
		return true;
	}

	@Override
	public ArrayList<Slot> addSlots(IContainerBase container, IModular modular, ModuleStack stack) {
		return new ArrayList<Slot>();
	}

	@Override
	public int getSizeInventory(ModuleStack stack) {
		return 3;
	}
	
	@Override
	public boolean onBuildModular(IModular modular, ModuleStack stack) {
		modular.getManager().setFluidHandler(new FluidHandler(modular));
		return super.onBuildModular(modular, stack);
	}

}
