package modularmachines.client.model.block;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.api.modules.Module;
import modularmachines.api.modules.ModuleRegistry;
import modularmachines.api.modules.logic.IModuleLogic;
import modularmachines.api.modules.storages.IMachineStoragePosition;
import modularmachines.api.modules.storages.IStorage;
import modularmachines.api.modules.storages.IStoragePosition;
import modularmachines.client.model.ModelManager;
import modularmachines.client.model.TRSRBakedModel;
import modularmachines.client.model.module.BakedMultiModel;
import modularmachines.client.model.module.ModelLoader;
import modularmachines.client.model.module.ModelLoader.DefaultTextureGetter;
import modularmachines.common.blocks.propertys.UnlistedBlockAccess;
import modularmachines.common.blocks.propertys.UnlistedBlockPos;
import modularmachines.common.blocks.tile.TileEntityMachine;
import modularmachines.common.utils.WorldUtil;

@SideOnly(Side.CLIENT)
public class ModelModular implements IBakedModel {

	public static final Set<TileEntityMachine> machines = new HashSet<>();
	private static final Cache<IModuleLogic, IBakedModel> inventoryCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build();
	private static final ItemOverrideList overrideList = new ItemOverrideListModular();
	private static IBakedModel missingModel;
	private static IBakedModel assemblerModel;

	@SubscribeEvent
	public static void onBakeModel(ModelBakeEvent event) {
		for (TileEntityMachine tileEntity : machines) {
			if (tileEntity != null) {
				if (tileEntity.isInvalid()) {
					machines.remove(tileEntity);
				} else {
					IModuleLogic logic = tileEntity.getLogic();
					for (Module module : logic.getModules()) {
						module.setModelNeedReload(true);
					}
				}
			}
		}
		inventoryCache.invalidateAll();
		assemblerModel = null;
		missingModel = null;
	}

	private static IBakedModel bakeModel(ICapabilityProvider provider, VertexFormat vertex, EnumFacing facing, boolean isAssembled) {
		IModuleLogic logic = getModuleLogic(provider);
		if (isAssembled) {
			IModelState modelState = ModelManager.getInstance().getDefaultBlockState();
			List<IBakedModel> models = new ArrayList<>();
			for (IStorage storage : logic.getStorages()) {
				Module module = storage.getModule();
				IBakedModel model = ModelLoader.getModel(module, storage, modelState, vertex);
				if (model != null) {
					// Rotate the storage module model
					IStoragePosition position = storage.getPosition();
					if(position instanceof IMachineStoragePosition){
						IMachineStoragePosition machinePosition = (IMachineStoragePosition) position;
						model = new TRSRBakedModel(model, 0F, 0F, 0F, 0F, machinePosition.getRotation(), 0F, 1F);
					}
					models.add(model);
				}
			}
			if (!models.isEmpty()) {
				float rotation = 0F;
				if (facing != null) {
					if (facing == EnumFacing.SOUTH) {
						rotation = (float) Math.PI;
					} else if (facing == EnumFacing.WEST) {
						rotation = (float) Math.PI / 2;
					} else if (facing == EnumFacing.EAST) {
						rotation = (float) -(Math.PI / 2);
					}
				}
				return new IPerspectiveAwareModel.MapWrapper(new TRSRBakedModel(new BakedMultiModel(models), 0F, 0F, 0F, 0F, rotation, 0F, 1F), modelState);
			}
		} else {
			if (assemblerModel == null) {
				return assemblerModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("modularmachines:block/modular")).bake(ModelManager.getInstance().getDefaultBlockState(), vertex, DefaultTextureGetter.INSTANCE);
			}
			return assemblerModel;
		}
		if (missingModel == null) {
			missingModel = ModelLoaderRegistry.getMissingModel().bake(ModelManager.getInstance().getDefaultBlockState(), vertex, DefaultTextureGetter.INSTANCE);
		}
		return missingModel;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState stateExtended = (IExtendedBlockState) state;
			IBlockAccess world = stateExtended.getValue(UnlistedBlockAccess.BLOCKACCESS);
			BlockPos pos = stateExtended.getValue(UnlistedBlockPos.POS);
			if (pos != null && world != null) {
				TileEntityMachine tileEntity = WorldUtil.getTile(world, pos, TileEntityMachine.class);
				if(tileEntity != null){
					boolean isAssembled = tileEntity.isAssembled();
					IBakedModel model = bakeModel(tileEntity, DefaultVertexFormats.BLOCK, tileEntity.getFacing(), isAssembled);
					if(isAssembled){
						machines.add(tileEntity);
					}
					if (model != null) {
						return model.getQuads(state, side, rand);
					}
				}
			}
		}
		return Collections.emptyList();
	}

	private static class ItemOverrideListModular extends ItemOverrideList {

		public ItemOverrideListModular() {
			super(Collections.emptyList());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			IModuleLogic logic = getModuleLogic(stack);
			IBakedModel bakedModel = inventoryCache.getIfPresent(logic);
			if (bakedModel == null) {
				if (stack.hasTagCompound()) {
					logic.readFromNBT(stack.getTagCompound());
				}
				if (logic != null) {
					bakedModel = bakeModel(stack, DefaultVertexFormats.ITEM, null, true);
					inventoryCache.put(logic, bakedModel);
				}
			}
			if (bakedModel != null) {
				return bakedModel;
			}
			return super.handleItemState(originalModel, stack, world, entity);
		}
	}

	public static IModuleLogic getModuleLogic(ICapabilityProvider provider) {
		if (provider == null) {
			return null;
		}
		if (provider.hasCapability(ModuleRegistry.MODULE_LOGIC, null)) {
			return provider.getCapability(ModuleRegistry.MODULE_LOGIC, null);
		}
		return null;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("modularmachines:blocks/modular_chassis");
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrideList;
	}
}
