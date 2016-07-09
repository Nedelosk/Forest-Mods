package de.nedelosk.modularmachines.common.items.blocks;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import de.nedelosk.modularmachines.api.modular.IModular;
import de.nedelosk.modularmachines.api.modular.IModularHandler;
import de.nedelosk.modularmachines.api.modular.IModularHandlerItem;
import de.nedelosk.modularmachines.api.modular.IModularHandlerTileEntity;
import de.nedelosk.modularmachines.api.modular.ModularManager;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;
import de.nedelosk.modularmachines.common.blocks.tile.TileModular;
import de.nedelosk.modularmachines.common.modular.handlers.ModularHandlerItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemBlockModularMachine extends ItemBlock {

	public ItemBlockModularMachine(Block block) {
		super(block);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ModularHandlerItem(stack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if(GuiScreen.isShiftKeyDown()){
			IModularHandler<IModular, NBTTagCompound> handler = stack.getCapability(ModularManager.MODULAR_HANDLER_CAPABILITY, null);
			if(handler != null){
				handler.deserializeNBT(stack.getTagCompound());
				IModular modular = handler.getModular();
				for(IModuleState state : modular.getModuleStates()){
					if(state != null){
						tooltip.add(ChatFormatting.GRAY + state.getModule().getUnlocalizedName());
					}
				}
			}
		}
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState){
		if (!world.setBlockState(pos, newState, 3)) {
			return false;
		}

		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == this.block){
			TileEntity tile = world.getTileEntity(pos);
			if (!(tile instanceof TileModular)) {
				world.setBlockToAir(pos);
				return false;
			}
			IModularHandlerItem itemHandler = (IModularHandlerItem) stack.getCapability(ModularManager.MODULAR_HANDLER_CAPABILITY, null);
			if(itemHandler != null){
				itemHandler.deserializeNBT(stack.getTagCompound());
			}
			if(itemHandler != null && itemHandler.getModular() != null){
				TileModular machine = (TileModular) tile;
				IModularHandlerTileEntity  tileHandler = (IModularHandlerTileEntity) machine.getCapability(ModularManager.MODULAR_HANDLER_CAPABILITY, null);
				int heading = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

				tileHandler.setModular(itemHandler.getModular().copy(tileHandler));
				tileHandler.setOwner(player.getGameProfile());
				tileHandler.setFacing(getFacingForHeading(heading));
			}

			setTileEntityNBT(world, player, pos, stack);
		}
		this.block.onBlockPlacedBy(world, pos, state, player, stack);

		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	private EnumFacing getFacingForHeading(int heading) {
		switch (heading) {
			case 0:
				return EnumFacing.NORTH;
			case 1:
				return EnumFacing.EAST;
			case 2:
				return EnumFacing.SOUTH;
			case 3:
			default:
				return EnumFacing.WEST;
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack).replace("tile.", "") + "." + stack.getItemDamage() + ".name";
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}
}
