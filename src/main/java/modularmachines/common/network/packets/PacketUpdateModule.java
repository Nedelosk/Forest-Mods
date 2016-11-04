package modularmachines.common.network.packets;

import java.io.IOException;

import modularmachines.api.modular.handlers.IModularHandler;
import modularmachines.api.modules.IModule;
import modularmachines.api.modules.IModulePage;
import modularmachines.api.modules.network.DataInputStreamMM;
import modularmachines.api.modules.network.DataOutputStreamMM;
import modularmachines.api.modules.network.IStreamable;
import modularmachines.api.modules.state.IModuleState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateModule extends PacketModule implements IPacketClient {

	/** Server Only */
	private IModularHandler modularHandler;

	public PacketUpdateModule() {
	}

	public PacketUpdateModule(IModuleState state) {
		super(state);
		this.modularHandler = state.getModular().getHandler();
	}

	public PacketUpdateModule(IModularHandler handler, int index, String pageId) {
		super(handler, index, pageId);
		this.modularHandler = handler;
	}

	@Override
	protected void writeData(DataOutputStreamMM data) throws IOException {
		super.writeData(data);
		if (index > 0) {
			if (modularHandler.isAssembled()) {
				IModuleState state = modularHandler.getModular().getModule(index);
				if (pageId != null) {
					IModulePage page = state.getPage(pageId);
					if (page instanceof IStreamable) {
						((IStreamable) page).writeData(data);
					}
				} else {
					IModule module = state.getModule();
					if (module instanceof IStreamable) {
						((IStreamable) module).writeData(data);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onPacketData(DataInputStreamMM data, EntityPlayer player) throws IOException {
		if (index > 0) {
			IModularHandler modularHandler = getModularHandler(player);
			if (modularHandler.isAssembled()) {
				IModuleState state = getModule(modularHandler);
				if (pageId != null) {
					IModulePage page = state.getPage(pageId);
					if (page instanceof IStreamable) {
						((IStreamable) page).readData(data);
					}
				} else {
					IModule module = state.getModule();
					if (module instanceof IStreamable) {
						((IStreamable) module).readData(data);
					}
				}
			}
		}
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.UPDATE_MODULE;
	}
}