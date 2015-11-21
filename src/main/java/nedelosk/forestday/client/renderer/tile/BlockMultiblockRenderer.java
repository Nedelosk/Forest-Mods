package nedelosk.forestday.client.renderer.tile;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import nedelosk.forestday.common.blocks.BlockMultiblock;
import nedelosk.forestday.common.multiblocks.TileMultiblockBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

public class BlockMultiblockRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderblocks) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (world.getTileEntity(x, y, z) != null
				&& (((TileMultiblockBase) world.getTileEntity(x, y, z)).master != null
						&& ((TileMultiblockBase) world.getTileEntity(x, y, z)).master.getMultiblock() != null)
				|| ((TileMultiblockBase) world.getTileEntity(x, y, z)).isMaster
						&& ((TileMultiblockBase) world.getTileEntity(x, y, z)).isMultiblock)
			if (((TileMultiblockBase) world.getTileEntity(x, y, z)).multiblock != null
					&& ((TileMultiblockBase) world.getTileEntity(x, y, z)).isMultiblock
					&& TileMultiblockRenderer.getRenderer(
							((TileMultiblockBase) world.getTileEntity(x, y, z)).multiblock.getClass()) != null
					|| ((TileMultiblockBase) world.getTileEntity(x, y, z)).master != null
							&& ((TileMultiblockBase) world.getTileEntity(x, y, z)).master.isMultiblock()
							&& TileMultiblockRenderer
									.getRenderer(((TileMultiblockBase) world.getTileEntity(x, y, z)).master
											.getMultiblock().getClass()) != null) {
				return true;
			}
		BlockMultiblock.renderPass = 0;
		renderblocks.setRenderBoundsFromBlock(block);
		renderblocks.renderStandardBlock(block, x, y, z);
		if (tile != null && tile instanceof TileMultiblockBase && ((TileMultiblockBase) tile).getIcon(4) != null) {
			BlockMultiblock.renderPass = 1;
			BlockMultiblock.tileBase = (TileMultiblockBase) tile;
			renderblocks.setRenderBoundsFromBlock(block);
			renderblocks.renderStandardBlock(block, x, y, z);
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
	}

	@Override
	public int getRenderId() {
		return 0;
	}

	/*
	 * @Override public int getRenderId() { return
	 * NCBlockManager.Multiblock.block().getRenderType(); }
	 * 
	 * private void renderItem(RenderBlocks renderBlocks, int meta, IIcon
	 * texture) { if (texture == null) return;
	 * 
	 * Block block = NCBlockManager.Multiblock.block();
	 * block.setBlockBoundsForItemRender();
	 * renderBlocks.setRenderBoundsFromBlock(block);
	 * 
	 * if (renderBlocks.useInventoryTint) { int color =
	 * block.getRenderColor(meta);
	 * 
	 * float r = (color >> 16 & 255) / 255.0F; float g = (color >> 8 & 255) /
	 * 255.0F; float b = (color & 255) / 255.0F; GL11.glColor4f(r, g, b, 1.0F);
	 * }
	 * 
	 * GL11.glTranslatef(-0.5F, -0.5F, -0.5F); Tessellator tess =
	 * Tessellator.instance; tess.startDrawingQuads(); tess.setNormal(0.0F,
	 * -1.0F, 0.0F); renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,
	 * texture); tess.draw(); tess.startDrawingQuads(); tess.setNormal(0.0F,
	 * 1.0F, 0.0F); renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,
	 * texture); tess.draw(); tess.startDrawingQuads(); tess.setNormal(0.0F,
	 * 0.0F, -1.0F); renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D,
	 * texture); tess.draw(); tess.startDrawingQuads(); tess.setNormal(0.0F,
	 * 0.0F, 1.0F); renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,
	 * texture); tess.draw(); tess.startDrawingQuads(); tess.setNormal(-1.0F,
	 * 0.0F, 0.0F); renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D,
	 * texture); tess.draw(); tess.startDrawingQuads(); tess.setNormal(1.0F,
	 * 0.0F, 0.0F); renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D,
	 * texture); tess.draw(); GL11.glTranslatef(0.5F, 0.5F, 0.5F); }
	 * 
	 * @Override public boolean handleRenderType(ItemStack item, ItemRenderType
	 * type) { return true; }
	 * 
	 * @Override public boolean shouldUseRenderHelper(ItemRenderType type,
	 * ItemStack item, ItemRendererHelper helper) { switch (helper) { case
	 * EQUIPPED_BLOCK: case BLOCK_3D: case ENTITY_BOBBING: case ENTITY_ROTATION:
	 * case INVENTORY_BLOCK: return true; default: return false; } }
	 * 
	 * @Override public void renderItem(ItemRenderType type, ItemStack item,
	 * Object... data) { switch (type) { case EQUIPPED: case
	 * EQUIPPED_FIRST_PERSON: GL11.glTranslatef(0.5f, 0.5f, 0.5f);
	 * renderItem((RenderBlocks) data[0], item, type); break; case INVENTORY:
	 * renderItem((RenderBlocks) data[0], item, type); break; case ENTITY:
	 * renderEntity((RenderBlocks) data[0], (EntityItem) data[1]); break; case
	 * FIRST_PERSON_MAP: break; default: break; } }
	 * 
	 * public void renderItem(RenderBlocks renderer, ItemStack stack,
	 * ItemRenderType type) { GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
	 * GL11.glEnable(GL11.GL_DEPTH_TEST); GL11.glEnable(GL11.GL_BLEND);
	 * GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	 * 
	 * BlockMultiblock.renderPass = 0; renderItem(renderer,
	 * stack.getItemDamage(), NCBlockManager.Multiblock.block().getIcon(0,
	 * stack.getItemDamage())); //renderItem(renderer, stack.getItemDamage(),
	 * NCBlocks.Multiblock.block().getIcon(0, stack.getItemDamage()));
	 * 
	 * GL11.glPopAttrib(); }
	 * 
	 * protected void renderEntity(RenderBlocks render, EntityItem item) { byte
	 * num = 1;
	 * 
	 * ItemStack stack = item.getEntityItem();
	 * 
	 * if (stack.stackSize > 1) num = 2;
	 * 
	 * if (stack.stackSize > 5) num = 3;
	 * 
	 * if (stack.stackSize > 20) num = 4;
	 * 
	 * if (render.useInventoryTint) { int color =
	 * stack.getItem().getColorFromItemStack(stack, 0); float r = (color >> 16 &
	 * 255) / 255.0F; float g = (color >> 8 & 255) / 255.0F; float b = (color &
	 * 255) / 255.0F; GL11.glColor4f(r, g, b, 1.0F); }
	 * 
	 * BlockMultiblock.renderPass = 0; renderItem(render, stack,
	 * ItemRenderType.ENTITY); }
	 */

}