package modularmachines.common.network.packets;

/*public class PacketSyncToolMode extends PacketModule implements IPacketClient, IPacketServer {

	private int mode;

	public PacketSyncToolMode() {
	}

	public PacketSyncToolMode(IModularHandler modularHandler, IModuleState<IModuleModeMachine> moduleState) {
		super(moduleState);
		this.mode = moduleState.getModule().getCurrentMode(moduleState).ordinal();
	}

	@Override
	protected void writeData(DataOutputStreamMM data) throws IOException {
		super.writeData(data);
		data.writeInt(mode);
	}

	@Override
	public void readData(DataInputStreamMM data) throws IOException {
		super.readData(data);
		mode = data.readInt();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onPacketData(DataInputStreamMM data, EntityPlayer player) throws IOException {
		IModularHandler modularHandler = getModularHandler(player);
		if (modularHandler.getModular() != null && modularHandler.isAssembled()) {
			IModuleState<IModuleModeMachine> machine = getModule(modularHandler);
			if (machine != null) {
				machine.getModule().setCurrentMode(machine, machine.getModule().getMode(mode));
			}
		}
	}

	@Override
	public void onPacketData(DataInputStreamMM data, EntityPlayerMP player) throws IOException {
		IModularHandler modularHandler = getModularHandler(player);
		BlockPos pos = getPos(modularHandler);
		if (modularHandler.getModular() != null && modularHandler.isAssembled()) {
			IModuleState<IModuleModeMachine> machine = modularHandler.getModular().getModule(index);
			if (machine != null) {
				machine.getModule().setCurrentMode(machine, machine.getModule().getMode(mode));
			}
		}
		PacketHandler.sendToNetwork(this, pos, player.getServerWorld());
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.SYNC_TOOL_MODE;
	}
}*/
