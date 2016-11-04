package modularmachines.api.modular.handlers;

import com.mojang.authlib.GameProfile;

import modularmachines.api.modular.IModular;
import modularmachines.api.modular.IModularAssembler;
import modularmachines.api.modular.ModularManager;
import modularmachines.api.modules.position.StoragePositions;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ModularHandler<K> implements IModularHandler<NBTTagCompound, K> {

	protected final StoragePositions<K> positions;
	protected IModular modular;
	protected IModularAssembler assembler;
	protected World world;
	protected GameProfile owner;

	public ModularHandler(World world, StoragePositions<K> positions) {
		this.world = world;
		this.positions = positions;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (modular != null) {
			nbt.setTag("Modular", modular.serializeNBT());
		}
		if (assembler != null) {
			nbt.setTag("Assembler", assembler.serializeNBT());
		}
		if (this.owner != null) {
			NBTTagCompound nbtTag = new NBTTagCompound();
			NBTUtil.writeGameProfile(nbtTag, owner);
			nbt.setTag("owner", nbtTag);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("Modular")) {
			modular = ModularManager.helper.createModular(this, nbt.getCompoundTag("Modular"));
		}
		if (nbt.hasKey("Assembler")) {
			assembler = ModularManager.helper.createModularAssembler(this, nbt.getCompoundTag("Assembler"));
		}
		if (nbt.hasKey("owner")) {
			owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("owner"));
		}
	}

	@Override
	public Container createContainer(InventoryPlayer inventory) {
		if (modular != null) {
			return modular.createContainer(this, inventory);
		} else if (assembler != null) {
			return assembler.createContainer(inventory);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiContainer createGui(InventoryPlayer inventory) {
		if (modular != null) {
			return modular.createGui(this, inventory);
		} else if (assembler != null) {
			return assembler.createGui(inventory);
		}
		return null;
	}

	@Override
	public void updateClient() {
		if (modular != null) {
			modular.update(false);
		}
	}

	@Override
	public void updateServer() {
		if (modular != null) {
			modular.update(true);
		}
	}

	@Override
	public void setModular(IModular modular) {
		if (modular != null) {
			modular.setHandler(this);
			setAssembler(null);
		}
		this.modular = modular;
		markDirty();
	}

	@Override
	public void setAssembler(IModularAssembler assembler) {
		if (assembler != null) {
			setModular(null);
		}
		this.assembler = assembler;
	}

	@Override
	public IModularAssembler getAssembler() {
		return assembler;
	}

	@Override
	public IModular getModular() {
		return modular;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean isAssembled() {
		return modular != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == ModularManager.MODULAR_HANDLER_CAPABILITY) {
			return ModularManager.MODULAR_HANDLER_CAPABILITY.cast(this);
		}
		if (modular != null) {
			if (modular.hasCapability(capability, facing)) {
				return modular.getCapability(capability, facing);
			}
		}
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == ModularManager.MODULAR_HANDLER_CAPABILITY) {
			return true;
		}
		if (modular != null) {
			if (modular.hasCapability(capability, facing)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public GameProfile getOwner() {
		return owner;
	}

	@Override
	public void setOwner(GameProfile owner) {
		this.owner = owner;
	}

	@Override
	public void setWorld(World world) {
		this.world = world;
	}

	@Override
	public StoragePositions<K> getPositions() {
		return positions;
	}

	protected EntityPlayer getPlayer() {
		return world.getPlayerEntityByName(getOwner().getName());
	}
}