package modularmachines.client.model.module;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.model.IModelState;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.api.modules.Module;
import modularmachines.api.modules.model.ModelLocationBuilder;
import modularmachines.api.modules.storages.IStorage;

@SideOnly(Side.CLIENT)
public class ModelDataCasing extends ModelData {

	public static final String CASING = "casing";
	public static final String CASING_LEFT = "casingLeft";
	public static final String CASING_RIGHT = "casingRight";

	public static void initModelData(ModelLocationBuilder location){
		ModelDataCasing casing = new ModelDataCasing();
		casing.addLocation(CASING, new ModelLocationBuilder(location).addPreFix("casing"));
		casing.addLocation(CASING_LEFT, new ModelLocationBuilder(location).addPreFix("side_left"));
		casing.addLocation(CASING_RIGHT, new ModelLocationBuilder(location).addPreFix("side_right"));
		location.getData().addModel(TileEntity.class, casing);
	}
	
	@Override
	public IBakedModel getModel(Module module, IStorage storage, IModelState modelState, VertexFormat format, Function bakedTextureGetter) {
		List<IBakedModel> models = new ArrayList<>();
		/*IModuleLogic logic = module.getLogic();
		if (logic.getStorage(EnumStoragePosition.LEFT)	== null) {
			models.add(ModelLoader.getModel(get(CASING_LEFT), format));
		}
		if (logic.getStorage(EnumStoragePosition.RIGHT)	== null) {
			models.add(ModelLoader.getModel(get(CASING_RIGHT), format));
		}*/
		models.add(ModelLoader.getModel(get(CASING), format));
		for (Module otherModule : storage.getModules().getModules()) {
			if(otherModule != module){
				models.add(ModelLoader.getModel(otherModule, storage, modelState, format));
			}
		}
		return new BakedMultiModel(models);
	}
}