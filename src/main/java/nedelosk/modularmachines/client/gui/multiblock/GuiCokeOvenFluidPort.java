package nedelosk.modularmachines.client.gui.multiblock;

import nedelosk.forestcore.library.gui.GuiBase;
import nedelosk.forestcore.library.gui.WidgetFluidTank;
import nedelosk.modularmachines.common.multiblock.cokeoven.TileCokeOvenFluidPort;
import nedelosk.modularmachines.common.multiblock.cokeoven.TileCokeOvenFluidPort.PortType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiCokeOvenFluidPort extends GuiBase<TileCokeOvenFluidPort> {

	public GuiCokeOvenFluidPort(TileCokeOvenFluidPort tile, InventoryPlayer inventory) {
		super(tile, inventory);
		if (tile.getPortType() == PortType.FUEL) {
			widgetManager.add(new WidgetFluidTank(tile.getController().getTankManager().getTank(0), 79, 12));
		} else if (tile.getPortType() == PortType.OUTPUT) {
			widgetManager.add(new WidgetFluidTank(tile.getController().getTankManager().getTank(1), 79, 12));
		}
	}

	@Override
	protected void renderStrings(FontRenderer fontRenderer, int x, int y) {
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		if (widgetManager != null && widgetManager.getWidgets().size() > 0 && widgetManager.getWidgets().get(0) instanceof WidgetFluidTank) {
			if (tile.getPortType() == PortType.FUEL) {
				((WidgetFluidTank) widgetManager.getWidgets().get(0)).tank = tile.getController().getTankManager().getTank(0);
			} else if (tile.getPortType() == PortType.OUTPUT) {
				((WidgetFluidTank) widgetManager.getWidgets().get(0)).tank = tile.getController().getTankManager().getTank(1);
			}
		}
	}

	@Override
	protected void renderProgressBar() {
	}

	@Override
	protected String getGuiName() {
		return "gui_multiblock_fluid";
	}

	@Override
	protected String getModName() {
		return "modularmachines";
	}
}