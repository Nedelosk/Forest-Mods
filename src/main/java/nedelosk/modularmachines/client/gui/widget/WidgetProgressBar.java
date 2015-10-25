package nedelosk.modularmachines.client.gui.widget;

import java.util.ArrayList;

import nedelosk.forestday.api.guis.IGuiBase;
import nedelosk.forestday.api.guis.Widget;
import nedelosk.forestday.utils.RenderUtils;

public class WidgetProgressBar extends Widget {

	public int burntime;
	public int burntimeTotal;
	
	public WidgetProgressBar(int posX, int posY, int burntime, int burntimeTotal) {
		super(posX, posY, 22, 15);
		widget = RenderUtils.getResourceLocation("modularmachines", "widgets", "gui");
		this.burntime = burntime;
		this.burntimeTotal = burntimeTotal;
	}
	
	@Override
	public ArrayList<String> getTooltip() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(burntime + " / " + burntimeTotal);
		return list;
	}
	
	@Override
	public void draw(IGuiBase gui) {
		RenderUtils.bindTexture(widget);
		int process = (burntimeTotal == 0) ? 0 : burntime * 22 / burntimeTotal;
	    int sx = gui.getGuiLeft();
	    int sy = gui.getGuiTop();
        this.drawTexturedModalRect(sx + posX, sy + posY, 54, 0, 22, 15);
        if (burntime > 0 && burntime <= burntimeTotal) {
        	this.drawTexturedModalRect(sx + posX, sy + posY, 76, 0, process, 15);
        }
	}

}
