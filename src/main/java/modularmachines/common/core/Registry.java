package modularmachines.common.core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import modularmachines.common.ModularMachines;
import modularmachines.common.blocks.FluidBlock;

public class Registry {
	
	private Registry() {
	}
	
	public static Fluid registerFluid(String fluidName, int temperature, Material material, boolean createBlock, boolean isGas, int density) {
		Fluid fluid = FluidRegistry.getFluid(fluidName);
		if (fluid == null) {
			ResourceLocation stillLocation = new ResourceLocation("modularmachines:blocks/fluids/" + fluidName + "_still");
			fluid = new Fluid(fluidName, stillLocation, stillLocation).setTemperature(temperature);
			fluid.setUnlocalizedName(fluidName);
			if (isGas) {
				fluid.setGaseous(isGas);
			}
			fluid.setDensity(density);
			FluidRegistry.registerFluid(fluid);
		}
		if (createBlock) {
			if (!fluid.canBePlacedInWorld()) {
				Block fluidBlock = new FluidBlock(fluid, material, fluidName);
				fluidBlock.setRegistryName("fluid_" + fluidName);
				ForgeRegistries.BLOCKS.register(fluidBlock);
				ItemBlock itemBlock = new ItemBlock(fluidBlock);
				itemBlock.setRegistryName("fluid_" + fluidName);
				ForgeRegistries.ITEMS.register(itemBlock);
				ModularMachines.proxy.registerFluidStateMapper(fluidBlock, fluid);
			}
			if (!FluidRegistry.getBucketFluids().contains(fluid)) {
				FluidRegistry.addBucketForFluid(fluid);
			}
		}
		return FluidRegistry.getFluid(fluidName);
	}
	
	public static <V extends IForgeRegistryEntry<V>> IForgeRegistryEntry<V> register(V entry, String name) {
		if (entry.getRegistryName() == null) {
			entry.setRegistryName(new ResourceLocation(Loader.instance().activeModContainer().getModId(), name));
		}
		IForgeRegistry registry;
		if (entry instanceof Block) {
			ModularMachines.proxy.registerBlock((Block) entry);
			registry = ForgeRegistries.BLOCKS;
		} else if (entry instanceof Item) {
			ModularMachines.proxy.registerItem((Item) entry);
			registry = ForgeRegistries.ITEMS;
		} else {
			throw new IllegalArgumentException();
		}
		registry.register(entry);
		return entry;
	}
	
	public static Item register(Item entry) {
		entry.setRegistryName(new ResourceLocation(Loader.instance().activeModContainer().getModId(), entry.getTranslationKey()));
		ForgeRegistries.ITEMS.register(entry);
		ModularMachines.proxy.registerItem(entry);
		return entry;
	}
	
	public static void registerTile(Class<? extends TileEntity> tile, String name, String modName) {
		GameRegistry.registerTileEntity(tile, "modularmachines." + modName + "." + name);
	}
	
	public static String setUnlocalizedBlockName(String name) {
		return "modularmachines.block." + name;
	}
	
	public static String getItemName(String name) {
		return "modularmachines.item." + name;
	}
}
