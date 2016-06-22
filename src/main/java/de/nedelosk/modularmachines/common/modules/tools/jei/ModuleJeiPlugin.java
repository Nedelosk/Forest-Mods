package de.nedelosk.modularmachines.common.modules.tools.jei;

import java.util.List;

import com.google.common.collect.Lists;

import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.ModuleManager;
import de.nedelosk.modularmachines.api.modules.tool.IModuleTool;
import de.nedelosk.modularmachines.common.core.BlockManager;
import de.nedelosk.modularmachines.common.plugins.jei.ModularMachinesHandler;
import de.nedelosk.modularmachines.common.plugins.jei.ModuleRecipeHandler;
import de.nedelosk.modularmachines.common.plugins.jei.ShapedModuleRecipeHandler;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class ModuleJeiPlugin extends BlankModPlugin {

	public static boolean isAdded;
	private static List<String> producerHandlers = Lists.newArrayList();

	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(BlockManager.blockModular));
		registry.addRecipeCategories(new AlloySmelterRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new ModuleRecipeHandler(ModuleCategoryUIDs.ALLOYSMELTER));
	}

	/*@Override
	public void loadConfig() {
		registerHandler(new CharcoalKilnHandler());
		registerHandler(new CampfireHandler());
		registerHandler(new ShapedModuleRecipeHandler());
		registerHandler(new CraftingRecipeKilnHandler());
		isAdded = false;
		for(IModuleContainer container : ModuleManager.moduleRegistry.getModuleContainers()) {
			IModule module = ModuleManager.moduleRegistry.getFakeModule(container);
			if (module instanceof IModuleTool) {
				IModuleTool machine = (IModuleTool) module;
				if (!producerHandlers.contains(machine.getRecipeCategory())) {
					new ModularMachinesHandler(machine.getRecipeCategory(), machine.createNEIPage(machine));
					producerHandlers.add(machine.getRecipeCategory());
				}
			}
		}
		isAdded = true;
	}*/
}