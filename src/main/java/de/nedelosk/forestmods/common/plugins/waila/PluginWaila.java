package de.nedelosk.forestmods.common.plugins.waila;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;
import de.nedelosk.forestmods.common.blocks.tile.TileCampfire;
import de.nedelosk.forestmods.common.blocks.tile.TileCharcoalKiln;
import de.nedelosk.forestmods.common.blocks.tile.TileModular;
import de.nedelosk.forestmods.common.config.Config;
import de.nedelosk.forestmods.common.plugins.waila.provider.ProviderModular;
import de.nedelosk.forestmods.common.plugins.waila.provider.ProviderTileCampfire;
import de.nedelosk.forestmods.common.plugins.waila.provider.ProviderTileCharcoalKiln;
import de.nedelosk.forestmods.library.plugins.APlugin;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

@Optional.Interface(modid = "Waila", iface = "mcp.mobius.waila.api.IWailaRegistrar")
public class PluginWaila extends APlugin {

	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar registrar) {
		final IWailaDataProvider tileCampfire = new ProviderTileCampfire();
		registrar.registerBodyProvider(tileCampfire, TileCampfire.class);
		final IWailaDataProvider tileKiln = new ProviderTileCharcoalKiln();
		registrar.registerBodyProvider(tileKiln, TileCharcoalKiln.class);
		ProviderModular modular = new ProviderModular();
		registrar.registerBodyProvider(modular, TileModular.class);
		registrar.registerHeadProvider(modular, TileModular.class);
		registrar.registerTailProvider(modular, TileModular.class);
	}

	@Override
	public String getRequiredMod() {
		return "Waila";
	}

	@Override
	public void init() {
		FMLInterModComms.sendMessage("Waila", "register", PluginWaila.class.getName() + ".register");
	}

	@Override
	public boolean getConfigOption() {
		return Config.pluginWaila;
	}
}
