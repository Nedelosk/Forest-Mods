package de.nedelosk.modularmachines.common.core;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class FluidManager {

	public static Fluid STEAM;

	public static void registerFluids() {
		STEAM = Registry.registerFluid("steam", 500, Material.LAVA, false, true, 0);
	}
}
