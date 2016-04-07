package de.nedelosk.forestcore.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import de.nedelosk.forestcore.inventory.IGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public abstract class Button<I extends IGuiHandler> extends GuiButton {

	protected RenderItem itemRender = RenderItem.getInstance();

	public Button(int ID, int xPosition, int yPosition, int width, int height, String displayString) {
		super(ID, xPosition, yPosition, width, height, displayString);
	}

	public boolean isMouseOver(int x, int y) {
		return x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
	}

	public List<String> getTooltip(IGuiBase<I> gui) {
		return null;
	}

	protected void drawItemStack(ItemStack stack, int x, int y) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		FontRenderer font = null;
		if (stack != null) {
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null) {
			font = Minecraft.getMinecraft().fontRenderer;
		}
		itemRender.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
		this.zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
	}

	public void onButtonClick(IGuiBase<I> gui) {
	}
}
