package de.nedelosk.modularmachines.common.events;

import java.util.Random;

import de.nedelosk.modularmachines.api.modules.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.ModuleManager;
import de.nedelosk.modularmachines.common.utils.Translator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {

	@SubscribeEvent
	public void stickDrop(BlockEvent.HarvestDropsEvent event) {
		Random r = new Random();
		if (event.getState().getBlock().isLeaves(event.getState(), event.getWorld(), event.getPos())) {
			if (r.nextInt(16) == 0) {
				event.getDrops().add(new ItemStack(Items.STICK));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		IModuleContainer container = ModuleManager.getContainerFromItem(event.getItemStack());
		if (container != null) {
			event.getToolTip().add(Translator.translateToLocal("mm.module.tooltip.type") + ": " + container.getMaterial().getLocalizedName());
			event.getToolTip().add(Translator.translateToLocal("mm.module.tooltip.tier") + ": " + container.getMaterial().getTier());
			event.getToolTip().add(Translator.translateToLocal("mm.module.tooltip.name") + ": "
					+ Translator.translateToLocal(container.getUnlocalizedName()));
			container.addTooltip(event.getToolTip());
		}
	}
}
