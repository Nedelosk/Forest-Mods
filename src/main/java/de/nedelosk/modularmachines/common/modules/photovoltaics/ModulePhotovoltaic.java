package de.nedelosk.modularmachines.common.modules.photovoltaics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.nedelosk.modularmachines.api.energy.IEnergyBuffer;
import de.nedelosk.modularmachines.api.modular.AssemblerException;
import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularAssembler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandler;
import de.nedelosk.modularmachines.api.modular.handlers.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modules.EnumWallType;
import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.IModulePage;
import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.ITickable;
import de.nedelosk.modularmachines.api.modules.containers.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.containers.IModuleItemContainer;
import de.nedelosk.modularmachines.api.modules.controller.ModuleControlled;
import de.nedelosk.modularmachines.api.modules.models.IModelHandler;
import de.nedelosk.modularmachines.api.modules.models.ModelHandlerDefault;
import de.nedelosk.modularmachines.api.modules.models.ModuleModelLoader;
import de.nedelosk.modularmachines.api.modules.photovoltaics.IModulePhotovoltaicProperties;
import de.nedelosk.modularmachines.api.modules.position.EnumModulePositions;
import de.nedelosk.modularmachines.api.modules.position.IModulePositioned;
import de.nedelosk.modularmachines.api.modules.position.IModulePostion;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.api.modules.storage.IStorage;
import de.nedelosk.modularmachines.api.modules.storage.energy.IModuleBattery;
import de.nedelosk.modularmachines.common.modules.pages.ControllerPage;
import de.nedelosk.modularmachines.common.network.PacketHandler;
import de.nedelosk.modularmachines.common.network.packets.PacketSyncModule;
import de.nedelosk.modularmachines.common.utils.Translator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModulePhotovoltaic extends ModuleControlled implements ITickable, IModulePhotovoltaicProperties, IModulePositioned {

	public ModulePhotovoltaic() {
		super("photovoltaic");
	}

	@Override
	public void assembleModule(IModularAssembler assembler, IModular modular, IStorage storage, IModuleState state) throws AssemblerException {
		if (modular.getModules(IModuleBattery.class).isEmpty()) {
			throw new AssemblerException(Translator.translateToLocal("modular.assembler.error.no.battery"));
		}
	}

	@Override
	public void sendModuleUpdate(IModuleState state) {
		IModularHandler handler = state.getModular().getHandler();
		if (handler instanceof IModularHandlerTileEntity) {
			PacketHandler.sendToNetwork(new PacketSyncModule(state), ((IModularHandlerTileEntity) handler).getPos(), (WorldServer) handler.getWorld());
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IModelHandler createModelHandler(IModuleState state) {
		IModuleItemContainer container = state.getContainer().getItemContainer();
		ResourceLocation location = ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), name, container.getSize());
		return new ModelHandlerDefault(location);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Map<ResourceLocation, ResourceLocation> getModelLocations(IModuleItemContainer container) {
		Map<ResourceLocation, ResourceLocation> location = new HashMap<>();
		location.put(ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), container.getMaterial().getName(), name, container.getSize()),
				ModuleModelLoader.getModelLocation(getRegistryName().getResourceDomain(), "default", name, container.getSize()));
		return location;
	}

	@Override
	public void updateServer(IModuleState<IModule> state, int tickCount) {
		if (tickCount % 2 == 0) {
			if (state.getModular().getHandler() instanceof IModularHandlerTileEntity) {
				IModularHandlerTileEntity handler = (IModularHandlerTileEntity) state.getModular().getHandler();
				IEnergyBuffer energyBuffer = state.getModular().getEnergyBuffer();
				if (energyBuffer.getCapacity() > energyBuffer.getEnergyStored()) {
					World world = handler.getWorld();
					BlockPos pos = handler.getPos();
					float lightRatio = calculateLightModifier(world);
					if (world.canSeeSky(pos.up())) {
						energyBuffer.receiveEnergy(state, null, Float.valueOf(getEnergyModifier(state) * lightRatio).longValue(), false);
					}
				}
			}
		}
	}

	@Override
	public int getEnergyModifier(IModuleState state) {
		IModuleProperties properties = state.getContainer().getProperties();
		if (properties instanceof IModulePhotovoltaicProperties) {
			return ((IModulePhotovoltaicProperties) properties).getEnergyModifier(state);
		}
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateClient(IModuleState<IModule> state, int tickCount) {
	}

	private float calculateLightModifier(World world) {
		int lightValue = EnumSkyBlock.SKY.defaultLightValue - world.getSkylightSubtracted();
		float sun = world.getCelestialAngleRadians(1.0F);
		if (sun < (float) Math.PI) {
			sun += (0.0F - sun) * 0.2F;
		} else {
			sun += (((float) Math.PI * 2F) - sun) * 0.2F;
		}
		lightValue = Math.round(lightValue * MathHelper.cos(sun));
		lightValue = MathHelper.clamp_int(lightValue, 0, 15);
		return lightValue / 15f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWallType getWallType(IModuleState state) {
		return EnumWallType.NONE;
	}

	@Override
	public IModulePostion[] getValidPositions(IModuleContainer container) {
		return new IModulePostion[] { EnumModulePositions.TOP };
	}

	@Override
	public List<IModuleState> getUsedModules(IModuleState state) {
		return new ArrayList(state.getModular().getModules(IModuleBattery.class));
	}

	@Override
	protected IModulePage getControllerPage(IModuleState state) {
		return new ControllerPage(state);
	}
}
