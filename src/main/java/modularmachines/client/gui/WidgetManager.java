package modularmachines.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import modularmachines.api.IGuiProvider;
import modularmachines.api.ILocatableSource;
import modularmachines.client.gui.widgets.Widget;
import modularmachines.common.utils.RenderUtil;

public class WidgetManager<P extends IGuiProvider, S extends ILocatableSource> {

	protected final List<Widget> widgets = new ArrayList<>();
	public final GuiBase<P, S> gui;

	public WidgetManager(GuiBase<P, S> gui) {
		this.gui = gui;
	}

	public void add(Widget widget) {
		if (!widgets.contains(widget)) {
			this.widgets.add(widget);
		}
	}

	public void addAll(Collection<Widget> widgets) {
		if (widgets == null) {
			return;
		}
		for (Widget widget : widgets) {
			if (!widgets.contains(widget)) {
				widgets.add(widget);
			}
		}
	}

	public void remove(Widget widget) {
		this.widgets.remove(widget);
	}

	public void clear() {
		this.widgets.clear();
	}

	public Widget getWidgetAtMouse(int mouseX, int mouseY) {
		for (Widget widget : widgets) {
			if (widget.isMouseOver(mouseX, mouseY)) {
				return widget;
			}
		}
		return null;
	}

	public void drawWidgets(int guiLeft, int guiTop) {
		for (Widget slot : widgets) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			slot.draw(guiLeft, guiTop);
		}
		for (Widget slot : widgets) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			slot.drawStrings();
		}
	}

	public boolean keyTyped(char keyChar, int keyCode) {
		/*
		 * for(Widget widget : widgets) { if (widget.keyTyped(keyChar, keyCode,
		 * gui)) { return true; } } return false;
		 */
		Widget focused = null;
		for (Widget widget : widgets) {
			if (widget.isFocused()) {
				focused = widget;
			}
		}
		// If esc is pressed
		if (keyCode == 1) {
			// If there is a focused text field unfocus it
			if (focused != null && keyCode == 1) {
				focused.setFocused(false);
				focused = null;
				return true;
			}
		}
		// If the user pressed tab, switch to the next text field, or unfocus if
		// there are none
		if (keyChar == '\t') {
			for (int i = 0; i < widgets.size(); i++) {
				Widget widget = widgets.get(i);
				if (widget.isFocused()) {
					widgets.get((i + 1) % widgets.size()).setFocused(true);
					widget.setFocused(false);
					return true;
				}
			}
		}
		// If there is a focused text field, attempt to type into it
		if (focused != null) {
			String old = focused.getText();
			if (focused.keyTyped(keyChar, keyCode)) {
				gui.onTextFieldChanged(focused, old);
				return true;
			}
		}
		// More JEI behavior, f key focuses first text field
		if (keyChar == 'f' && focused == null && !widgets.isEmpty()) {
			focused = widgets.get(0);
			focused.setFocused(true);
		}
		return false;
	}

	public void drawTooltip(int mouseX, int mouseY) {
		int posX = mouseX - gui.getGuiLeft();
		int posY = mouseY - gui.getGuiTop();
		Widget widget = getWidgetAtMouse(posX, posY);
		if (widget != null) {
			RenderUtil.renderTooltip(mouseX, mouseY, widget.getTooltip());
		}
	}

	public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
		int posX = mouseX - gui.getGuiLeft();
		int posY = mouseY - gui.getGuiTop();
		Widget widget = getWidgetAtMouse(posX, posY);
		if (widget != null) {
			widget.handleMouseClick(mouseX, mouseY, mouseButton);
		}
	}

	public List<Widget> getWidgets() {
		return widgets;
	}
	
	public GuiBase<P, S> getGui() {
		return gui;
	}
}
