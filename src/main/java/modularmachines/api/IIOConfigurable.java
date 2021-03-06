package modularmachines.api;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;

public interface IIOConfigurable {
	
	/**
	 * Checks if the io mode of the given facing supports the given io mode.
	 */
	boolean supportsMode(IOMode ioMode, @Nullable EnumFacing facing);
	
	/**
	 * The current io mode of the given facing.
	 * If the facing is null, is returns the default mode.
	 * The default mode is always used if the io mode of the facing is {@link IOMode#NONE}.
	 *
	 * @return The current io mode of the given facing.
	 */
	IOMode getMode(@Nullable EnumFacing facing);
	
	void doPull(EnumFacing facing);
	
	void doPush(EnumFacing facing);
}
