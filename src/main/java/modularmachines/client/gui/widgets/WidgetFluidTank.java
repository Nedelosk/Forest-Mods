/*
 * For the fluid rendering: The MIT License (MIT) Copyright (c) 2014-2015 mezz
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: The above copyright
 * notice and this permission notice shall be included in all copies or
 * substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS",
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package modularmachines.client.gui.widgets;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.common.utils.RenderUtil;
import modularmachines.common.utils.Translator;

@SideOnly(Side.CLIENT)
public class WidgetFluidTank extends Widget {
	
	private static final int TEX_WIDTH = 14;
	private static final int TEX_HEIGHT = 14;
	private static final int MIN_FLUID_HEIGHT = 1;
	
	public IFluidTank tank;
	
	public WidgetFluidTank(int posX, int posY, IFluidTank tank) {
		super(posX, posY, 18, 60);
		
		this.tank = tank;
	}
	
	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		if (tank != null && tank.getFluid() != null) {
		}
	}
	
	@Override
	public void draw(int guiLeft, int guiTop) {
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.color(1, 1, 1, 1);
		RenderUtil.texture(widgetTexture);
		gui.drawTexturedModalRect(guiLeft + pos.x, guiTop + pos.y, 132, 127, pos.width, pos.height);
		drawFluid(guiLeft + pos.x + 1, guiTop + pos.y + 1, tank.getFluid());
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, 200);
		RenderUtil.texture(widgetTexture);
		gui.drawTexturedModalRect(guiLeft + pos.x, guiTop + pos.y, 150, 127, pos.width - 2, pos.height);
		GlStateManager.popMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
	}
	
	private void drawFluid(final int xPosition, final int yPosition, @Nullable FluidStack fluidStack) {
		if (fluidStack == null) {
			return;
		}
		Fluid fluid = fluidStack.getFluid();
		if (fluid == null) {
			return;
		}
		TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
		ResourceLocation fluidStill = fluid.getStill();
		TextureAtlasSprite fluidStillSprite = null;
		if (fluidStill != null) {
			fluidStillSprite = textureMapBlocks.getTextureExtry(fluidStill.toString());
		}
		if (fluidStillSprite == null) {
			fluidStillSprite = textureMapBlocks.getMissingSprite();
		}
		int fluidColor = fluid.getColor(fluidStack);
		int scaledAmount = (fluidStack.amount * 56) / tank.getCapacity();
		if (fluidStack.amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
			scaledAmount = MIN_FLUID_HEIGHT;
		}
		if (scaledAmount > 56) {
			scaledAmount = 56;
		}
		RenderUtil.bindBlockTexture();
		setGLColorFromInt(fluidColor);
		final int xTileCount = 14 / TEX_WIDTH;
		final int xRemainder = 14 - (xTileCount * TEX_WIDTH);
		final int yTileCount = scaledAmount / TEX_HEIGHT;
		final int yRemainder = scaledAmount - (yTileCount * TEX_HEIGHT);
		final int yStart = yPosition + 56;
		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
				int height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;
				int x = xPosition + (xTile * TEX_WIDTH);
				int y = yStart - ((yTile + 1) * TEX_HEIGHT);
				if (width > 0 && height > 0) {
					int maskTop = TEX_HEIGHT - height;
					int maskRight = TEX_WIDTH - width;
					drawFluidTexture(x, y, fluidStillSprite, maskTop, maskRight, 100);
				}
			}
		}
	}
	
	private static void setGLColorFromInt(int color) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GlStateManager.color(red, green, blue, 1.0F);
	}
	
	private static void drawFluidTexture(double xCoord, double yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, double zLevel) {
		double uMin = textureSprite.getMinU();
		double uMax = textureSprite.getMaxU();
		double vMin = textureSprite.getMinV();
		double vMax = textureSprite.getMaxV();
		uMax = uMax - (maskRight / 16.0 * (uMax - uMin));
		vMax = vMax - (maskTop / 16.0 * (vMax - vMin));
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(xCoord, yCoord + 16, zLevel).tex(uMin, vMax).endVertex();
		buffer.pos(xCoord + 16 - maskRight, yCoord + 16, zLevel).tex(uMax, vMax).endVertex();
		buffer.pos(xCoord + 16 - maskRight, yCoord + maskTop, zLevel).tex(uMax, vMin).endVertex();
		buffer.pos(xCoord, yCoord + maskTop, zLevel).tex(uMin, vMin).endVertex();
		tessellator.draw();
	}
	
	@Override
	public List<String> getTooltip() {
		ArrayList<String> description = new ArrayList<>();
		if (tank == null || tank.getFluidAmount() == 0) {
			description.add(Translator.translateToLocal("mm.tooltip.nonefluid"));
		} else {
			description.add(tank.getFluidAmount() + " " + Translator.translateToLocal(tank.getFluid().getLocalizedName()) + " mb / " + tank.getCapacity() + " " + Translator.translateToLocal(tank.getFluid().getLocalizedName()) + " mb");
		}
		return description;
	}
}