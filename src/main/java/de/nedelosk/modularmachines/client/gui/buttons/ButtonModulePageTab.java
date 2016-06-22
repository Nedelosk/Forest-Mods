package de.nedelosk.modularmachines.client.gui.buttons;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.nedelosk.modularmachines.api.gui.IGuiBase;
import de.nedelosk.modularmachines.api.modular.IModularTileEntity;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.client.gui.Button;
import de.nedelosk.modularmachines.client.gui.GuiModular;
import de.nedelosk.modularmachines.common.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ButtonModulePageTab extends Button<IModularTileEntity> {

	protected ResourceLocation guiTextureOverlay = RenderUtil.getResourceLocation("forestmods", "modular_machine", "gui");
	public IModuleState state;
	public boolean isDown;
	public int pageID;

	public ButtonModulePageTab(int buttonID, int xPos, int yPos, IModuleState state, boolean isDown, int tabID) {
		super(buttonID, xPos, yPos, 28, 21, null);
		this.state = state;
		this.isDown = isDown;
		this.pageID = tabID;
	}

	@Override
	public void drawButton(Minecraft mc, int mx, int my) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GuiModular machine = (GuiModular) mc.currentScreen;
		RenderUtil.bindTexture(guiTextureOverlay);
		machine.drawTexturedModalRect(xPosition, yPosition, state.getModular().getCurrentPage().getPageID() == pageID ? 74 : 103,
				isDown ? 237 : 218, 29, 19);
		RenderUtil.bindTexture(RenderUtil.getResourceLocation("forestmods", "widgets", "gui"));
		machine.drawTexturedModalRect(xPosition + 6, yPosition, 0, 18 + pageID * 18, 18, 18);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	@Override
	public void onButtonClick(IGuiBase<IModularTileEntity> gui) {
		IModularTileEntity tile = gui.getHandler();
		if (pageID != state.getModular().getCurrentPage().getPageID()) {
			tile.getModular().setCurrentPage(pageID);
		}
	}

	@Override
	public List<String> getTooltip(IGuiBase<IModularTileEntity> gui) {
		return Arrays.asList(Integer.toString(pageID + 1));
	}
}