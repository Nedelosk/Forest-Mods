package modularmachines.client.model.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.api.modules.Module;
import modularmachines.api.modules.model.IModelData;
import modularmachines.api.modules.model.ModelLocationBuilder;
import modularmachines.client.model.TRSRBakedModel;

@SideOnly(Side.CLIENT)
public abstract class ModelData<M extends Module> implements IModelData<IBakedModel> {

	protected final Map<String, ResourceLocation> locations;
	
	public ModelData() {
		locations = new HashMap<>();
	}
	
	public void addLocation(String key, ModelLocationBuilder location){
		locations.put(key, location.build());
	}
	
	protected ResourceLocation get(String key){
		return locations.get(key);
	}
	
	@Override
	public Collection<ResourceLocation> getValidLocations() {
		return locations.values();
	}
	
	protected IBakedModel createModel(IBakedModel model, float y){
		return new TRSRBakedModel(model, 0F, y, 0F);
	}
}