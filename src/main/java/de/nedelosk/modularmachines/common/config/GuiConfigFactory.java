package de.nedelosk.modularmachines.common.config;

import java.util.ArrayList;
import java.util.List;

import de.nedelosk.modularmachines.api.Translator;
import de.nedelosk.modularmachines.common.config.Config.ConfigGroup;
import de.nedelosk.modularmachines.common.core.Constants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({ "rawtypes" })
@SideOnly(Side.CLIENT)
public class GuiConfigFactory extends GuiConfig {

	public GuiConfigFactory(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), Constants.MODID, false, false, Translator.translateToLocal("mm.config.title"));
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parent) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		String prefix = "mm.config.";
		for(ConfigGroup group : Config.groups) {
			list.add(new ConfigElement(
					Config.config.getCategory(group.lc()).setLanguageKey(prefix + group.lang).setRequiresMcRestart(group.reloadMC)));
		}
		return list;
	}
}
