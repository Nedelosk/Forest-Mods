package de.nedelosk.modularmachines.client.gui;

import org.lwjgl.opengl.GL11;

import de.nedelosk.modularmachines.api.gui.IGuiHandler;
import de.nedelosk.modularmachines.api.gui.IPage;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiPage<H extends IGuiHandler> extends GuiBase<H> {

	protected IPage page;

	public GuiPage(H handler, InventoryPlayer inventory, IPage currentPage) {
		super(handler, inventory);
		this.page = currentPage;
		currentPage.setGui(this);
		currentPage.addWidgets();
		ySize = currentPage.getYSize();
		xSize = currentPage.getXSize();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		page.drawForeground(getFontRenderer(), mouseX, mouseY);
	}

	@Override
	public void addButtons() {
		page.addButtons();
	}

	@Override
	protected String getGuiTexture() {
		return null;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		page.drawBackground(mouseX, mouseY);
		widgetManager.drawWidgets();
		page.drawFrontBackground(mouseX, mouseY);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		page.updateGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float p_73863_3_) {
		super.drawScreen(mouseX, mouseY, p_73863_3_);
		page.drawTooltips(mouseX, mouseY);
	}

	@Override
	public Gui getGui() {
		return this;
	}
}