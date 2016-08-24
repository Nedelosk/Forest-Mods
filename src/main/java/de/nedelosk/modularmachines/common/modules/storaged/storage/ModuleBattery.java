package de.nedelosk.modularmachines.common.modules.storaged.storage;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.modularmachines.api.energy.IEnergyType;
import de.nedelosk.modularmachines.api.gui.IContainerBase;
import de.nedelosk.modularmachines.api.gui.Widget;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.Module;
import de.nedelosk.modularmachines.api.modules.handlers.IModuleContentHandler;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import de.nedelosk.modularmachines.api.modules.handlers.energy.IModuleEnergyInterface;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.slots.SlotModule;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storaged.storage.IModuleBattery;
import de.nedelosk.modularmachines.api.modules.storaged.storage.IModuleBatteryProperties;
import de.nedelosk.modularmachines.client.gui.widgets.WidgetEnergyField;
import de.nedelosk.modularmachines.common.modules.handlers.ModulePage;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSyncModule;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ModuleBattery extends Module implements IModuleBattery {

	public ModuleBattery(String name) {
		super(name);
	}

	@Override
	public void sendModuleUpdate(IModuleState state){
		IModularHandler handler = state.getModular().getHandler();
		if(handler instanceof IModularHandlerTileEntity){
			PacketHandler.sendToNetwork(new PacketSyncModule(handler, state), ((IModularHandlerTileEntity)handler).getPos(), (WorldServer) handler.getWorld());
		}
	}

	@Override
	public int getCapacity(IModuleState state) {
		IModuleProperties properties = state.getContainer().getProperties();
		if(properties instanceof IModuleBatteryProperties){
			return ((IModuleBatteryProperties) properties).getCapacity(state);
		}
		return 0;
	}

	@Override
	public int getMaxReceive(IModuleState state) {
		IModuleProperties properties = state.getContainer().getProperties();
		if(properties instanceof IModuleBatteryProperties){
			return ((IModuleBatteryProperties) properties).getMaxReceive(state);
		}
		return 0;
	}

	@Override
	public int getMaxExtract(IModuleState state) {
		IModuleProperties properties = state.getContainer().getProperties();
		if(properties instanceof IModuleBatteryProperties){
			return ((IModuleBatteryProperties) properties).getMaxExtract(state);
		}
		return 0;
	}

	@Override
	public List<IModuleContentHandler> createHandlers(IModuleState state) {
		List<IModuleContentHandler> handlers = super.createHandlers(state);
		handlers.add(createEnergyInterface(state));
		return handlers;
	}

	@Override
	public IModuleState loadStateFromItem(IModuleState state, ItemStack stack) {
		IModuleContentHandler handler = state.getContentHandler(IModuleEnergyInterface.class);
		if(handler instanceof IModuleEnergyInterface){
			IModuleEnergyInterface energyInterface = (IModuleEnergyInterface)handler;
			energyInterface.setEnergyStored(getEnergyType(state), getStorageEnergy(state, stack));
		}
		return state;
	}

	@Override
	public ItemStack saveDataToItem(IModuleState state) {
		ItemStack stack = super.saveDataToItem(state);
		IModuleContentHandler handler = state.getContentHandler(IModuleEnergyInterface.class);
		if(handler instanceof IModuleEnergyInterface){
			IModuleEnergyInterface energyInterface = (IModuleEnergyInterface)handler;
			setStorageEnergy(state, energyInterface.getEnergyStored(getEnergyType(state)), stack);
		}
		return stack;
	}

	public abstract IModuleEnergyInterface createEnergyInterface(IModuleState state);

	public abstract IEnergyType getEnergyType(IModuleState state);

	@Override
	public List<IModulePage> createPages(IModuleState state) {
		List<IModulePage> pages = super.createPages(state);
		pages.add(new BatteryPage("Basic", state));
		return pages;
	}

	public class BatteryPage extends ModulePage<IModuleBattery> {

		public BatteryPage(String pageID, IModuleState<IModuleBattery> state) {
			super(pageID, "battery", state);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void updateGui() {
			super.updateGui();
			for(Widget widget : (ArrayList<Widget>) gui.getWidgetManager().getWidgets()) {
				if (widget instanceof WidgetEnergyField) {
					((WidgetEnergyField) widget).energyInterface = state.getContentHandler(IModuleEnergyInterface.class);
				}
			}
		}

		@Override
		public void createSlots(IContainerBase<IModularHandler> container, List<SlotModule> modularSlots) {
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void addWidgets() {
			gui.getWidgetManager().add(new WidgetEnergyField(state.getContentHandler(IModuleEnergyInterface.class), getEnergyType(state), 55, 15));
		}
	}
}