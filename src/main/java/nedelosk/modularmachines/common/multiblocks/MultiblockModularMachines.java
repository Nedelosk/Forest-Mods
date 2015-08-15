package nedelosk.modularmachines.common.multiblocks;

import java.util.ArrayList;

import nedelosk.nedeloskcore.common.blocks.multiblocks.AbstractMultiblockFluid;
import nedelosk.nedeloskcore.common.blocks.multiblocks.MultiblockPattern;
import nedelosk.nedeloskcore.common.blocks.multiblocks.TileMultiblockBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public abstract class MultiblockModularMachines extends AbstractMultiblockFluid {

	@Override
	public String getMultiblockName() {
		return "multiblockModularMachines";
	}

	@Override
	public MultiblockPattern createPattern() {
		return null;
	}

	@Override
	public ArrayList<MultiblockPattern> createPatterns() {
		return null;
	}

	@Override
	public void updateMultiblock() {
		
	}

	@Override
	public boolean testBlock() {
		return false;
	}

	@Override
	public boolean isPatternBlockValid(int x, int y, int z, char pattern, TileMultiblockBase tile) {
		return false;
	}

	@Override
	public void onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side) {
		
	}

	@Override
	public Container getContainer(TileMultiblockBase base, InventoryPlayer inventory) {
		return null;
	}

	@Override
	public Object getGUIContainer(TileMultiblockBase base, InventoryPlayer inventory) {
		return null;
	}

	@Override
	public void updateClient(TileMultiblockBase base) {
		
	}

	@Override
	public void updateServer(TileMultiblockBase base) {
		
	}

}
