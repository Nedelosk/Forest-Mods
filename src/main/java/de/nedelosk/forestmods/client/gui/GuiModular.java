package de.nedelosk.forestmods.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.nedelosk.forestcore.library.gui.Button;
import de.nedelosk.forestcore.library.gui.GuiBase;
import de.nedelosk.forestcore.library.gui.Widget;
import de.nedelosk.forestcore.utils.RenderUtil;
import de.nedelosk.forestmods.api.modular.basic.IModularDefault;
import de.nedelosk.forestmods.api.modular.managers.IModularGuiManager;
import de.nedelosk.forestmods.api.modular.managers.IModularInventoryManager;
import de.nedelosk.forestmods.api.modular.tile.IModularTileEntity;
import de.nedelosk.forestmods.api.modules.gui.IModuleGui;
import de.nedelosk.forestmods.api.modules.inventory.IModuleInventory;
import de.nedelosk.forestmods.api.utils.ModularUtils;
import de.nedelosk.forestmods.api.utils.ModuleStack;
import de.nedelosk.forestmods.client.gui.buttons.ButtonGuiTab;
import de.nedelosk.forestmods.client.gui.widgets.WidgetManagerModular;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiModular<T extends TileEntity & IModularTileEntity<IModularDefault>> extends GuiBase<T> {

	private IModuleGui currentGui;

	public GuiModular(T tile, InventoryPlayer inventory, IModuleGui currentGui) {
		super(tile, inventory);
		widgetManager = new WidgetManagerModular(this);
		ModuleStack guiStack = ModularUtils.getModuleStackFromGui(tile.getModular(), currentGui);
		List<Widget> widgets = new ArrayList();
		currentGui.addWidgets(this, tile.getModular(), guiStack, widgets);
		widgetManager.add(widgets);
		ySize = currentGui.getGuiTop(tile.getModular(), guiStack);
		this.currentGui = currentGui;
	}

	@Override
	protected void renderStrings(FontRenderer fontRenderer, int x, int y) {
		ModuleStack guiStack = ModularUtils.getModuleStackFromGui(tile.getModular(), currentGui);
		currentGui.renderString(fontRenderer, guiLeft, guiTop, x, y, guiStack);
	}

	@Override
	protected void renderProgressBar() {
		if (currentGui == null) {
			return;
		}
		IModularInventoryManager invManager = tile.getModular().getInventoryManager();
		ModuleStack guiStack = ModularUtils.getModuleStackFromGui(tile.getModular(), currentGui);
		IModuleInventory inv = invManager.getInventory(guiStack);
		if (inv != null) {
			for ( int slotID = 36; slotID < inventorySlots.inventorySlots.size(); slotID++ ) {
				Slot slot = ((ArrayList<Slot>) inventorySlots.inventorySlots).get(slotID);
				if (slot.getSlotIndex() < inv.getSizeInventory(guiStack, tile.getModular())) {
					drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, 56, 238, 18, 18);
				}
			}
		}
	}

	@Override
	public void addButtons() {
		if (currentGui == null) {
			return;
		}
		IModularGuiManager guiManager = tile.getModular().getGuiManager();
		ModuleStack guiStack = ModularUtils.getModuleStackFromGui(tile.getModular(), currentGui);
		for ( int i = 0; i < guiManager.getAllGuis().size(); i++ ) {
			ModuleStack stack = ModularUtils.getModuleStackFromGui(tile.getModular(), guiManager.getAllGuis().get(i));
			buttonManager.add(new ButtonGuiTab(i, (i >= 7) ? guiLeft + 166 : guiLeft + -28, (i >= 7) ? guiTop + 8 + 22 * (i - 7) : guiTop + 8 + 22 * i, stack,
					guiManager.getAllGuis().get(i), i >= 7));
		}
		if (currentGui != null) {
			List<Button> buttons = new ArrayList();
			currentGui.addButtons(this, tile.getModular(), guiStack, buttons);
			buttonManager.add(buttons);
		}
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
		if (currentGui == null) {
			return;
		}
		ModuleStack guiStack = ModularUtils.getModuleStackFromGui(tile.getModular(), currentGui);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (currentGui.getCustomGui(tile.getModular(), guiStack) != null) {
			RenderUtil.bindTexture(currentGui.getCustomGui(tile.getModular(), guiStack));
		} else {
			RenderUtil.bindTexture(guiTexture);
		}
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		RenderUtil.bindTexture(new ResourceLocation(getModName(), "textures/gui/inventory_player.png"));
		drawTexturedModalRect(this.guiLeft + 7, this.guiTop + ySize - 83, 7, 83, 162, 76);
		RenderUtil.bindTexture(guiTexture);
		renderProgressBar();
		currentGui.updateGui(this, guiLeft, guiTop, tile.getModular(), guiStack);
		widgetManager.drawWidgets();
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (!widgetManager.keyTyped(p_73869_1_, p_73869_2_)) {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
	}
}