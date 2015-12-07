package nedelosk.modularmachines.api.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import nedelosk.forestday.api.guis.GuiBase;
import nedelosk.forestday.api.tile.TileBaseInventory;
import nedelosk.forestday.api.utils.RenderUtils;
import nedelosk.modularmachines.api.modular.basic.managers.IModularGuiManager;
import nedelosk.modularmachines.api.modular.tile.IModularTileEntity;
import nedelosk.modularmachines.api.modules.IModule;
import nedelosk.modularmachines.api.producers.gui.IProducerGui;
import nedelosk.modularmachines.api.producers.gui.IProducerGuiWithButtons;
import nedelosk.modularmachines.api.producers.gui.IProducerGuiWithWidgets;
import nedelosk.modularmachines.api.producers.inventory.IProducerInventory;
import nedelosk.modularmachines.api.utils.ModuleStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiModularMachine<T extends TileBaseInventory & IModularTileEntity> extends GuiBase<T> {

	public GuiModularMachine(T tile, InventoryPlayer inventory) {
		super(tile, inventory);
		widgetManager = new WidgetManagerModular(this);

		ModuleStack<IModule, IProducerGui> gui = tile.getModular().getGuiManager().getModuleWithGui(Minecraft.getMinecraft().thePlayer, tile);

		if (gui != null && gui.getProducer() instanceof IProducerGuiWithWidgets)
			((IProducerGuiWithWidgets) gui.getProducer()).addWidgets(this, tile.getModular(), gui);
		ySize = gui.getProducer().getGuiTop(tile.getModular(), gui);
	}

	@Override
	protected void renderStrings(FontRenderer fontRenderer, int x, int y) {
		ModuleStack<IModule, IProducerGui> stack = tile.getModular().getGuiManager().getModuleWithGui(Minecraft.getMinecraft().thePlayer, tile);
		stack.getProducer().renderString(fontRenderer, guiLeft, guiTop, x, y, stack);
	}

	@Override
	protected void renderProgressBar() {

		IModularGuiManager guiManager = tile.getModular().getGuiManager();

		if (guiManager.getModuleWithGui(Minecraft.getMinecraft().thePlayer, tile).getProducer() instanceof IProducerInventory) {
			for (int slotID = 36;slotID < inventorySlots.inventorySlots.size();slotID++) {
				Slot slot = ((ArrayList<Slot>)inventorySlots.inventorySlots).get(slotID);
				ModuleStack gui = guiManager.getModuleWithGui(Minecraft.getMinecraft().thePlayer, tile);
				if (slot.getSlotIndex() < ((IProducerInventory) gui.getProducer()).getSizeInventory(gui)) {
					drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, 56, 238, 18, 18);
				}
			}
		}
	}
	
	@Override
	public void addButtons() {
		ModuleStack<IModule, IProducerGui> gui = tile.getModular().getGuiManager().getModuleWithGui(Minecraft.getMinecraft().thePlayer, tile);
		
		IModularGuiManager guiManager = tile.getModular().getGuiManager();
		
		for (int i = 0; i < guiManager.getModuleWithGuis().size(); i++) {
			buttonManager.add(new ButtonTabPage(i, (i >= 7) ? guiLeft + 166 : guiLeft + -28, (i >= 7) ? guiTop + 8 + 22 * (i - 7) : guiTop + 8 + 22 * i, tile.getModular().getGuiManager().getModuleWithGuis().get(i), i >= 7));
		}
		
		if (gui != null && gui.getProducer() instanceof IProducerGuiWithButtons)
			((IProducerGuiWithButtons) gui.getProducer()).addButtons(this, tile.getModular(), gui);
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	protected String getGuiName() {
		return "modular_machine";
	}

	@Override
	protected String getModName() {
		return "modularmachines";
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		ModuleStack<IModule, IProducerGui> gui = tile.getModular().getGuiManager().getModuleWithGui(Minecraft.getMinecraft().thePlayer, tile);
		IModularGuiManager guiManager = tile.getModular().getGuiManager();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (gui != null && gui.getProducer().getCustomGui(tile.getModular(), gui) != null)
			RenderUtils.bindTexture(gui.getProducer().getCustomGui(tile.getModular(), gui));
		else
			RenderUtils.bindTexture(guiTexture);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		RenderUtils.bindTexture(new ResourceLocation(getModName(), "textures/gui/inventory_player.png"));
		drawTexturedModalRect(this.guiLeft + 7, this.guiTop + ySize - 83, 7, 83, 162, 76);

		RenderUtils.bindTexture(guiTexture);
		renderProgressBar();
		gui.getProducer().updateGui(this, guiLeft, guiTop, tile.getModular(), gui);
		widgetManager.drawWidgets();

	}

}