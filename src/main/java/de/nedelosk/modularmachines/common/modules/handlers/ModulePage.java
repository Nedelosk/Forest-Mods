package de.nedelosk.modularmachines.common.modules.handlers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import de.nedelosk.modularmachines.api.gui.IGuiBase;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.handlers.IModulePage;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventory;
import de.nedelosk.modularmachines.api.modules.handlers.inventory.IModuleInventoryBuilder;
import de.nedelosk.modularmachines.api.modules.handlers.tank.IModuleTankBuilder;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.tool.IModuleTool;
import de.nedelosk.modularmachines.client.gui.Widget;
import de.nedelosk.modularmachines.client.gui.buttons.ButtonModulePageTab;
import de.nedelosk.modularmachines.client.gui.buttons.ButtonModuleTab;
import de.nedelosk.modularmachines.client.gui.widgets.WidgetProgressBar;
import de.nedelosk.modularmachines.common.utils.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ModulePage<M extends IModule> implements IModulePage {

	protected int pageID;
	protected IModular modular;
	protected IModuleState<M> state;
	@SideOnly(Side.CLIENT)
	protected IGuiBase gui;

	public ModulePage(int pageID, IModuleState<M> module) {
		this.pageID = pageID;
		this.modular = module.getModular();
		this.state = module;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateGui(int x, int y) {
		List<Widget> widgets = gui.getWidgetManager().getWidgets();
		for(Widget widget : widgets) {
			if (widget instanceof WidgetProgressBar) {
				if(state instanceof IModuleTool){
					((WidgetProgressBar) widget).burntime = ((IModuleTool) state.getModule()).getWorkTime(state);
					((WidgetProgressBar) widget).burntimeTotal = ((IModuleTool) state.getModule()).getWorkTimeTotal(state);
				}
			}
		}
	}

	@Override
	public void createTank(IModuleTankBuilder tankBuilder) {
	}

	@Override
	public void createInventory(IModuleInventoryBuilder invBuilder) {
	}

	@SideOnly(Side.CLIENT)
	protected boolean renderInventoryName() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawForeground(FontRenderer fontRenderer, int mouseX, int mouseY) {
		IModuleInventory inventory = (IModuleInventory)state.getContentHandler(ItemStack.class);
		if (renderInventoryName()&& inventory != null && inventory.hasCustomInventoryName()) {
			fontRenderer.drawString(inventory.getInventoryName(), 90 - (fontRenderer.getStringWidth(inventory.getInventoryName()) / 2),
					6, 4210752);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawBackground(int mouseX, int mouseY) {
		RenderUtil.bindTexture(getGuiTexture());
		gui.getGui().drawTexturedModalRect(gui.getGuiLeft(), gui.getGuiTop(), 0, 0, getXSize(), getYSize());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawFrontBackground(int mouseX, int mouseY) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawSlots() {
		IModuleInventory inventory = (IModuleInventory) state.getContentHandler(ItemStack.class);
		if (inventory != null && gui.getGui() instanceof GuiContainer) {
			Container container = ((GuiContainer) gui).inventorySlots;
			for(int slotID = 36; slotID < container.inventorySlots.size(); slotID++) {
				Slot slot = ((ArrayList<Slot>) container.inventorySlots).get(slotID);
				if (slot.getSlotIndex() < inventory.getSlots()) {
					drawSlot(slot);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	protected void drawSlot(Slot slot) {
		RenderUtil.bindTexture(getGuiTexture());
		gui.getGui().drawTexturedModalRect(gui.getGuiLeft() + slot.xDisplayPosition - 1, gui.getGuiTop() + slot.yDisplayPosition - 1, 56, 238, 18, 18);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawPlayerInventory() {
		RenderUtil.bindTexture(getInventoryTexture());
		int invPosition = getPlayerInvPosition();
		gui.getGui().drawTexturedModalRect(gui.getGuiLeft() + 7, gui.getGuiTop() + invPosition, 7, invPosition, 162, 76);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getXSize() {
		return 176;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getYSize() {
		return 166;
	}

	@Override
	public int getPlayerInvPosition() {
		return 83;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getGuiTexture() {
		return new ResourceLocation("forestmods:textures/gui/modular_machine.png");
	}

	@SideOnly(Side.CLIENT)
	protected ResourceLocation getInventoryTexture() {
		return new ResourceLocation("forestmods:textures/gui/inventory_player.png");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addButtons(List buttons) {
		List<IModuleState> modelsWithPages = getModulesWithPages(modular);

		for(int i = 0; i < modelsWithPages.size(); i++) {
			IModuleState module = modelsWithPages.get(i);
			buttons.add(new ButtonModuleTab(i, (i >= 7) ? gui.getGuiLeft() + getXSize() : gui.getGuiLeft() - 28,
					(i >= 7) ? gui.getGuiTop() + 8 + 22 * (i - 7) : gui.getGuiTop() + 8 + 22 * i, module, modular.getTile(), i >= 7));
		}

		for(int pageID = 0; pageID < state.getPages().length; pageID++) {
			IModulePage page = state.getPages()[pageID];
			buttons.add(new ButtonModulePageTab(gui.getButtonManager().getButtons().size(),
					pageID > 4 ? 12 + gui.getGuiLeft() + (pageID - 5) * 30 : 12 + gui.getGuiLeft() + pageID * 30,
							pageID > 4 ? gui.getGuiTop() + getYSize() : gui.getGuiTop() - 19, state, pageID > 4 ? true : false, pageID));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addWidgets(List widgets) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawTooltips(int mouseX, int mouseY) {
	}

	@Override
	public int getPageID() {
		return pageID;
	}

	@Override
	public IModular getModular() {
		return modular;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IGuiBase getGui() {
		return gui;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setGui(IGuiBase gui) {
		this.gui = gui;
	}

	@Override
	public IModuleState getModuleState() {
		return state;
	}

	public static List<IModuleState> getModulesWithPages(IModular modular){
		List<IModuleState> modulesWithPages = Lists.newArrayList();
		for(IModuleState moduleState : modular.getModuleStates()) {
			if (moduleState != null && moduleState.getPages() != null) {
				modulesWithPages.add(moduleState);
			}
		}
		return modulesWithPages;
	}
}
