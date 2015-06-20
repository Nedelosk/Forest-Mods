package nedelosk.forestday.client.machines.multiblock.charcoalkiln;

import nedelosk.forestday.client.machines.base.renderer.tile.TileKilnRenderer;
import nedelosk.forestday.client.proxy.ClientProxy;
import nedelosk.forestday.common.machines.base.wood.kiln.TileKiln;
import nedelosk.forestday.common.machines.mutiblock.charcoalkiln.TileCharcoalKiln;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemCharcoalKilnRenderer implements IItemRenderer {

	public TileCharcoalKilnRenderer kiln;
	
	public ItemCharcoalKilnRenderer() {
		kiln = (TileCharcoalKilnRenderer)ClientProxy.getRenderer(TileCharcoalKiln.class);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		kiln.renderItem(item);
	}

}
