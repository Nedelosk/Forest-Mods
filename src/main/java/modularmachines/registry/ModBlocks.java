package modularmachines.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import net.minecraftforge.fml.common.registry.GameRegistry;

import modularmachines.common.blocks.BlockModuleContainer;
import modularmachines.common.blocks.tile.TileEntityModuleContainer;
import modularmachines.common.core.Registry;

public class ModBlocks {
	
	public static BlockModuleContainer moduleStorage;
	
	public static void preInit() {
		moduleStorage = new BlockModuleContainer();
		register(moduleStorage);
		
		GameRegistry.registerTileEntity(TileEntityModuleContainer.class, "modularmachines.module");
	}
	
	public static <B extends Block> B register(B block) {
		String name = block.getTranslationKey().replace("tile.", "").replace("forest.tile.", "");
		Registry.register(block, name);
		return block;
	}
	
	public static <B extends Block> B register(B block, ItemBlock item) {
		String name = block.getTranslationKey().replace("tile.", "").replace("forest.tile.", "");
		Registry.register(block, name);
		Registry.register(item, name);
		return block;
	}
}
