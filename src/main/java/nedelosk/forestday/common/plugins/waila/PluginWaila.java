package nedelosk.forestday.common.plugins.waila;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import nedelosk.forestday.common.machines.base.wood.kiln.TileKiln;
import nedelosk.forestday.common.plugins.waila.provider.machines.ProviderTileKiln;
import nedelosk.nedeloskcore.plugins.Plugin;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;

public class PluginWaila extends Plugin {

	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar registrar){
		final IWailaDataProvider tileResin = new ProviderTileKiln();
		
		registrar.registerBodyProvider( tileResin, TileKiln.class);

	}
	
	public void init(){
		if(Loader.isModLoaded("Waila"))
			FMLInterModComms.sendMessage( "Waila", "register", PluginWaila.class.getName() + ".register" );
	}
	
}
