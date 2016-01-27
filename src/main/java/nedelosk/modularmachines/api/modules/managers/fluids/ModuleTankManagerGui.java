package nedelosk.modularmachines.api.modules.managers.fluids;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nedelosk.forestcore.library.gui.IGuiBase;
import nedelosk.forestcore.library.gui.Widget;
import nedelosk.forestcore.library.gui.WidgetFluidTank;
import nedelosk.modularmachines.api.client.widget.WidgetDirection;
import nedelosk.modularmachines.api.client.widget.WidgetProducer;
import nedelosk.modularmachines.api.client.widget.WidgetTankMode;
import nedelosk.modularmachines.api.modular.IModular;
import nedelosk.modularmachines.api.modular.tile.IModularTileEntity;
import nedelosk.modularmachines.api.modules.fluids.TankData;
import nedelosk.modularmachines.api.modules.managers.ModuleManagerGui;
import nedelosk.modularmachines.api.modules.managers.fluids.IModuleTankManager.TankMode;
import nedelosk.modularmachines.api.utils.ModuleStack;

@SideOnly(Side.CLIENT)
public class ModuleTankManagerGui<P extends IModuleTankManager> extends ModuleManagerGui<P> {

	public ModuleTankManagerGui(String guiName) {
		super(guiName);
	}

	@Override
	protected void addWidgets(int tabID, IGuiBase<IModularTileEntity<IModular>> gui, ModuleStack<P> stack, List<Widget> widgets) {
		IModuleTankManagerSaver saver = (IModuleTankManagerSaver) stack.getSaver();
		if (!(saver.getDatas().length <= tabID)) {
			TankData data = saver.getData(tabID);
			WidgetFluidTank widget = new WidgetFluidTank(data == null ? null : data.getTank(), 36 + tabID * 51, 18, tabID);
			if (widget != null && widget instanceof WidgetFluidTank) {
				WidgetFluidTank tank = widget;
				widgets.add(tank);
				widgets.add(new WidgetTankMode(tank.posX - 22, tank.posY, data == null ? null : data.getMode(), tabID));
				if (gui.getTile().getModular().getFluidProducers() != null && !gui.getTile().getModular().getFluidProducers().isEmpty()) {
					widgets.add(new WidgetProducer(tank.posX - 22, tank.posY + 21, data == null ? -1 : data.getProducer(), tabID));
				}
				widgets.add(new WidgetDirection(tank.posX - 22, tank.posY + 42, data == null ? null : data.getDirection(), tabID));
			}
		}
	}

	@Override
	public void updateGui(IGuiBase base, int x, int y, IModular modular, ModuleStack<P> stack) {
		IModuleTankManagerSaver saver = (IModuleTankManagerSaver) stack.getSaver();
		List<Widget> widgets = base.getWidgetManager().getWidgets();
		for ( Widget widget : widgets ) {
			if (widget instanceof WidgetFluidTank) {
				int ID = ((WidgetFluidTank) widget).ID;
				if (saver.getData(ID) != null) {
					((WidgetFluidTank) widget).tank = saver.getData(ID).getTank();
				} else {
					((WidgetFluidTank) widget).tank = null;
				}
			} else if (widget instanceof WidgetDirection) {
				int ID = ((WidgetDirection) widget).ID;
				if (saver.getData(ID) != null) {
					((WidgetDirection) widget).direction = saver.getData(ID).getDirection();
				} else {
					((WidgetDirection) widget).direction = null;
				}
			} else if (widget instanceof WidgetProducer) {
				int ID = ((WidgetProducer) widget).ID;
				if (saver.getData(ID) != null && saver.getData(ID).getMode() != null && saver.getData(ID).getMode() != TankMode.NONE) {
					((WidgetProducer) widget).producer = saver.getData(ID).getProducer();
				} else {
					((WidgetProducer) widget).producer = -1;
				}
			} else if (widget instanceof WidgetTankMode) {
				int ID = ((WidgetTankMode) widget).ID;
				if (saver.getData(ID) != null) {
					((WidgetTankMode) widget).mode = saver.getData(ID).getMode();
				} else {
					((WidgetTankMode) widget).mode = null;
				}
			}
		}
	}
}