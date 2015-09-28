package nedelosk.modularmachines.client.renderers;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nedelosk.modularmachines.api.basic.machine.part.IMachinePart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ItemPartRenderer {

	public static final HashMap<Item, IItemRenderer> partRenderer = Maps.newHashMap();
	
	public static void preInit(){
		for(Object item : Item.itemRegistry){
			if(item instanceof IMachinePart)
				if(((IMachinePart) item).getPartRenderer() != null)
					partRenderer.put((Item)item, ((IMachinePart) item).getPartRenderer());
		}
		for(Entry<Item, IItemRenderer> entry : partRenderer.entrySet())
			MinecraftForgeClient.registerItemRenderer(entry.getKey(), entry.getValue());
	}
	
}
