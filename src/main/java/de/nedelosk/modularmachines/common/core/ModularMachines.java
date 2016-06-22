package de.nedelosk.modularmachines.common.core;

import java.io.File;

import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.IModuleLoader;
import de.nedelosk.modularmachines.api.modules.ModuleManager;
import de.nedelosk.modularmachines.common.recipse.RecipeManager;
import de.nedelosk.modularmachines.common.transport.TransportEventHandler;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES, guiFactory = "de.nedelosk.forestmods.common.config.ConfigFactory")
public class ModularMachines {

	public static File configFolder;
	public static File configFile;

	public static IForgeRegistry<IModule> iModuleRegistry;
	public static IForgeRegistry<IModuleContainer> iModuleContainerRegistry;
	public static IForgeRegistry<IModuleLoader> iModuleLoaderRegistry;
	
	@Instance(Constants.MODID)
	public static ModularMachines instance;
	
	@SidedProxy(clientSide = "de.nedelosk.modularmachines.client.core.ClientProxy", serverSide = "de.nedelosk.modularmachines.common.core.CommonProxy")
	
	public static CommonProxy proxy;
	public static ModularMachinesRegistry registry;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		iModuleRegistry = PersistentRegistryManager.createRegistry(new ResourceLocation("modularmachines:modules"), IModule.class, null, 0, 67108863, true, null, null, null);
		iModuleContainerRegistry = PersistentRegistryManager.createRegistry(new ResourceLocation("modularmachines:modulecontainers"), IModuleContainer.class, null, 0, 67108863, true, null, null, null);
		iModuleLoaderRegistry = PersistentRegistryManager.createRegistry(new ResourceLocation("modularmachines:moduleloaders"), IModuleLoader.class, null, 0, 67108863, true, null, null, null);
		
		registry = new ModularMachinesRegistry();
		MinecraftForge.EVENT_BUS.register(new TransportEventHandler());
		configFolder = new File(event.getModConfigurationDirectory(), "Forest-Mods");
		configFile = new File(configFolder, "Forest-Mods.cfg");
		registry.preInit(instance, event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		registry.init(instance, event);
		proxy.registerRenderers();
		proxy.registerTickHandlers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		registry.postInit(instance, event);
		RecipeManager.checkRecipes();
	}
}