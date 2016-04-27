package de.nedelosk.forestmods.client.render.tile;

import org.lwjgl.opengl.GL11;

import de.nedelosk.forestmods.client.render.models.ModelTransport;
import de.nedelosk.forestmods.common.transport.TileEntityTransport;
import de.nedelosk.forestmods.library.transport.ITransportPart;
import de.nedelosk.forestmods.library.utils.RenderUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTransportRenderer extends TileEntitySpecialRenderer {

	public static final ResourceLocation textureModel = new ResourceLocation("forestmods", "textures/models/transport_base.png");
	private ModelTransport model;

	public TileTransportRenderer() {
		this.model = new ModelTransport();
	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		TileEntityTransport tPort = (TileEntityTransport) entity;
		ITransportPart part = tPort.getPart();
		GL11.glPushMatrix();
		GL11.glTranslated((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotated(180, 0F, 0F, 1F);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glPushMatrix();
		RenderUtil.bindTexture(textureModel);
		model.render();
		if (part.getSide(ForgeDirection.SOUTH).isActive()) {
			model.renderLaser();
		}
		if (part.getSide(ForgeDirection.WEST).isActive()) {
			GL11.glRotated(90, 0F, 1F, 0F);
			model.renderLaser();
			GL11.glRotated(-90, 0F, 1F, 0F);
		}
		if (part.getSide(ForgeDirection.NORTH).isActive()) {
			GL11.glRotated(180, 0F, 1F, 0F);
			model.renderLaser();
			GL11.glRotated(-180, 0F, 1F, 0F);
		}
		if (part.getSide(ForgeDirection.EAST).isActive()) {
			GL11.glRotated(270, 0F, 1F, 0F);
			model.renderLaser();
			GL11.glRotated(-270, 0F, 1F, 0F);
		}
		if (part.getSide(ForgeDirection.UP).isActive()) {
			model.renderLaserTop();
		}
		if (part.getSide(ForgeDirection.DOWN).isActive()) {
			GL11.glRotated(180, 0F, 0F, 1F);
			GL11.glTranslated(0, -2F, 0);
			model.renderLaserTop();
			GL11.glRotated(-180, 0F, 0F, 1F);
			GL11.glTranslated(0, 2F, 0);
		}
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	public void renderItem() {
		GL11.glPushMatrix();
		GL11.glTranslated(0.5F, 2F, 0.5F);
		GL11.glRotated(180, 0F, 0F, 1F);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glScaled(1.5F, 1.5F, 1.5F);
		GL11.glPushMatrix();
		RenderUtil.bindTexture(textureModel);
		model.render();
		model.renderLaser();
		GL11.glRotated(90, 0F, 1F, 0F);
		model.renderLaser();
		GL11.glRotated(90, 0F, 1F, 0F);
		model.renderLaser();
		GL11.glRotated(90, 0F, 1F, 0F);
		model.renderLaser();
		GL11.glRotated(90, 0F, 1F, 0F);
		model.renderLaserTop();
		GL11.glRotated(180, 0F, 0F, 1F);
		GL11.glTranslated(0, -2F, 0);
		model.renderLaserTop();
		GL11.glRotated(-180, 0F, 0F, 1F);
		GL11.glTranslated(0, 2F, 0);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
