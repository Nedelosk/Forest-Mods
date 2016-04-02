package de.nedelosk.forestmods.api.modular.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.nedelosk.forestmods.api.modular.IModular;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public interface IRenderState<M extends IModular> {

	ItemStack getItem();

	double getX();

	double getY();

	double getZ();

	M getModular();
}
