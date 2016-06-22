package de.nedelosk.modularmachines.common.core;

import de.nedelosk.modularmachines.common.transport.TransportServerTickHandler;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	public void registerRenderers() {
	}

	public void registerTickHandlers() {
		MinecraftForge.EVENT_BUS.register(new TransportServerTickHandler());
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		switch (ID) {
			case 0:
				if (tile != null && tile instanceof de.nedelosk.modularmachines.api.inventory.IGuiHandler) {
					return ((de.nedelosk.modularmachines.api.inventory.IGuiHandler) tile).getContainer(player.inventory);
				}
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if ((world instanceof WorldClient)) {
			switch (ID) {
				case 0:
					if (tile instanceof de.nedelosk.modularmachines.api.inventory.IGuiHandler) {
						return ((de.nedelosk.modularmachines.api.inventory.IGuiHandler) tile).getGUIContainer(player.inventory);
					}
				default:
					return null;
			}
		}
		return null;
	}
	
	public void registerBlock(Block block){
		
	}
	
	public void registerItem(Item item){
		
	}
}