package modularmachines.common.utils;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import modularmachines.api.ILocatable;

public class WorldUtil {
	
	/**
	 * Returns the tile of the specified class, returns null if it is the wrong type or does not exist.
	 * Avoids creating new tile entities when using a ChunkCache (off the main thread).
	 * see {@link BlockFlowerPot#getActualState(IBlockState, IBlockAccess, BlockPos)}
	 */
	@Nullable
	public static <T extends TileEntity> T getTile(IBlockAccess world, BlockPos pos, Class<T> tileClass) {
		TileEntity tileEntity;
		
		if (world instanceof ChunkCache) {
			ChunkCache chunkCache = (ChunkCache) world;
			tileEntity = chunkCache.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
		} else {
			tileEntity = world.getTileEntity(pos);
		}
		
		if (tileClass.isInstance(tileEntity)) {
			return tileClass.cast(tileEntity);
		} else {
			return null;
		}
	}
	
	@Nullable
	public static TileEntity getTile(ILocatable locatable) {
		return getTile(locatable, TileEntity.class);
	}
	
	@Nullable
	public static TileEntity getTile(ILocatable locatable, EnumFacing facing) {
		return getTile(locatable, facing, TileEntity.class);
	}
	
	@Nullable
	public static <T extends TileEntity> T getTile(ILocatable locatable, EnumFacing facing, Class<T> tileClass) {
		return getTile(locatable.getWorldObj(), locatable.getCoordinates().offset(facing), tileClass);
	}
	
	@Nullable
	public static <T extends TileEntity> T getTile(ILocatable locatable, Class<T> tileClass) {
		return getTile(locatable.getWorldObj(), locatable.getCoordinates(), tileClass);
	}
	
	public static boolean isUsableByPlayer(EntityPlayer player, TileEntity tile) {
		if (player.isSneaking()) {
			return false;
		}
		BlockPos pos = tile.getPos();
		World world = tile.getWorld();
		
		return !tile.isInvalid() && world.getTileEntity(pos) == tile && player.getDistanceSqToCenter(pos) <= 64.0D;
	}
	
	public static void dropItems(World world, BlockPos pos, List<ItemStack> drops) {
		if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
			for (ItemStack stack : drops) {
				if (stack.isEmpty()) {
					continue;
				}
				double xOffset = (double) (world.rand.nextFloat() * 0.5F) + 0.25D;
				double yOffset = (double) (world.rand.nextFloat() * 0.5F) + 0.25D;
				double zOffset = (double) (world.rand.nextFloat() * 0.5F) + 0.25D;
				EntityItem entityitem = new EntityItem(world, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, stack);
				entityitem.setDefaultPickupDelay();
				world.spawnEntity(entityitem);
			}
		}
	}
	
}
