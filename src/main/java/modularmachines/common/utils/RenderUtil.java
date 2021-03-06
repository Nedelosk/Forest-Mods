package modularmachines.common.utils;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;

import net.minecraftforge.fml.client.config.GuiUtils;

public class RenderUtil {
	
	public static void renderTooltip(int x, int y, List<String> tooltipData) {
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
		GuiUtils.drawHoveringText(tooltipData, x, y, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), -1, Minecraft.getMinecraft().fontRenderer);
	}
	
	public static TextureManager engine() {
		return Minecraft.getMinecraft().renderEngine;
	}
	
	public static TextureMap map() {
		return Minecraft.getMinecraft().getTextureMapBlocks();
	}
	
	public static void texture(ResourceLocation tex) {
		engine().bindTexture(tex);
	}
	
	public static void bindBlockTexture() {
		texture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
	
	public static TextureAtlasSprite getSprite(FluidStack fluidStack) {
		return getSprite(fluidStack.getFluid().getStill());
	}
	
	public static TextureAtlasSprite getSprite(ResourceLocation resourceLocation) {
		return map().getAtlasSprite(resourceLocation.toString());
	}
	
	public static TextureAtlasSprite getMissingSprite() {
		return map().getMissingSprite();
	}
	
}
