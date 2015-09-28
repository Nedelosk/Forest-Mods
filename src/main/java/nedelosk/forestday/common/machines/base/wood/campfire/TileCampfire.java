package nedelosk.forestday.common.machines.base.wood.campfire;

import nedelosk.forestday.client.machines.base.gui.GuiCampfire;
import nedelosk.forestday.common.config.ForestdayConfig;
import nedelosk.forestday.common.items.materials.ItemCampfire;
import nedelosk.forestday.common.registrys.FItems;
import nedelosk.nedeloskcore.common.blocks.tile.TileMachineBase;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileCampfire extends TileMachineBase {
	
	public int fuelStorage;
	public ItemStack output;
	
	public TileCampfire() {
		super(7);
		timerMax = 25;
	}

	@Override
	public String getMachineName() {
		return "campfire";
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		nbt.setInteger("Fuel", fuelStorage);
		
		if(output != null)
		{
			NBTTagCompound nbtTag = new NBTTagCompound();
			output.writeToNBT(nbtTag);
		
			nbt.setTag("Output", nbtTag);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		this.fuelStorage = nbt.getInteger("Fuel");
		
		if(nbt.hasKey("Output"))
		{
			NBTTagCompound Output = nbt.getCompoundTag("Output");
			output = ItemStack.loadItemStackFromNBT(Output);
		}
		
	}

	@Override
	public Container getContainer(InventoryPlayer inventory) {
		return new ContainerCampfire(this, inventory);
	}

	@Override
	public Object getGUIContainer(InventoryPlayer inventory) {
		return new GuiCampfire(this, inventory);
	}

	@Override
	public void updateClient() {
		
	}

	@Override
	public void updateServer() {
		
		if(burnTime >= burnTimeTotal || burnTimeTotal == 0)
		{
			if(output != null)
			{
				if(addToOutput(output, 3, 4))
				{
					output = null;
					isWorking = false;
				}
			}
			else
			{
				ItemStack input = getStackInSlot(0);
				ItemStack input2 = getStackInSlot(1);
				if(input != null)
				{
					CampfireRecipe recipe = CampfireRecipeManager.getRecipe(input, input2, (getStackInSlot(6) != null) ? getStackInSlot(6).getItemDamage() + 1 : 0);
					if(recipe != null)
					{
						if(input.stackSize >= recipe.input.stackSize && (recipe.input2 == null || input2 != null && input2.stackSize >= recipe.input2.stackSize))
						{
							decrStackSize(0, recipe.input.stackSize);
							if(recipe.input2 != null)
							{
								decrStackSize(1, recipe.input2.stackSize);
							}
							output = recipe.output.copy();
							isWorking = true;
							burnTimeTotal = recipe.burnTime;
							burnTime = 0;
						}
					}
				} if(output == null){
					if(input2 != null)
					{
						CampfireRecipe recipe = CampfireRecipeManager.getRecipe(input2, input, (getStackInSlot(6) != null) ? getStackInSlot(6).getItemDamage() + 1 : 0);
						if(recipe != null)
						{
							if(input2.stackSize >= recipe.input.stackSize && (recipe.input2 == null || input != null && input.stackSize >= recipe.input2.stackSize))
							{
								decrStackSize(1, recipe.input.stackSize);
								if(recipe.input2 != null)
								{
									decrStackSize(0, recipe.input2.stackSize);
								}
								output = recipe.output.copy();
								isWorking = true;
								burnTimeTotal = recipe.burnTime;
								burnTime = 0;
							}
						}
					}
				}
			}
		}
		
		if(getStackInSlot(6) != null)
		{
			if(getStackInSlot(5) == null)
			{
				EntityItem entityItem = new EntityItem(worldObj, xCoord, yCoord, zCoord, getStackInSlot(6));
				worldObj.spawnEntityInWorld(entityItem);
				setInventorySlotContents(6, null);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
		
		if(getCurbTier() == -1)
		{
			burnTime = 0;
			return;
		}
		
		ItemStack fuel = getStackInSlot(2);
		if(fuel != null)
		{
			
			if(TileEntityFurnace.getItemBurnTime(fuel) > 0)
			{
				if(fuelStorage < ForestdayConfig.campfireFuelStorageMax[getCurbTier()] && !(TileEntityFurnace.getItemBurnTime(fuel) + fuelStorage > ForestdayConfig.campfireFuelStorageMax[getCurbTier()]))
				{
					fuelStorage = fuelStorage + TileEntityFurnace.getItemBurnTime(fuel);
					decrStackSize(2, 1);
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
				else if(fuelStorage > ForestdayConfig.campfireFuelStorageMax[getCurbTier()])
				{
					fuelStorage = ForestdayConfig.campfireFuelStorageMax[getCurbTier()];
				}
			}
		}
		
		if(worldObj.isRaining()){
			if(yCoord == 175)
				return;
			boolean foundBlock = false;
			for(int i = 1;i < 11;i++){
				Block block = worldObj.getBlock(xCoord, yCoord + i, zCoord);
				if(!worldObj.getBlock(xCoord, yCoord + i, zCoord).isAir(worldObj, xCoord, yCoord + i, zCoord)){
				 	foundBlock = true;
				 	continue;
				}
			}
			if(!foundBlock){
				if(isWorking){
					isWorking = false;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
				return;
			}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
		if(fuelStorage > 0)
		{
			if(timer >= timerMax)
			{
				if(isWorking)
				{
					if(fuelStorage == 0)
					{
						isWorking = false;
					}
					if(output == null)
					{
						isWorking = false;
					}
					fuelStorage-= 5;
				}
				if(burnTime != burnTimeTotal)
					burnTime++;
				if(output != null)
					isWorking = true;
				if(fuelStorage > ForestdayConfig.campfireFuelStorageMax[getCurbTier()])
				{
					fuelStorage = ForestdayConfig.campfireFuelStorageMax[getCurbTier()];
				}
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				timer = 0;
			}
			else
			{
				timer++;
			}
		}
	}
	
	public int getCurbTier()
	{
		ItemStack curb  = getStackInSlot(4);
		if(curb != null)
		{
			if(curb.getItem() instanceof ItemCampfire && ((ItemCampfire)curb.getItem()).itemName == "curb")
			{
				return curb.getItemDamage();
			}
		}
		return -1;
	}
	
	public ItemStack setCampfireItem(ItemStack stack){
		int ID = 0;
		if(stack.getItem() == FItems.curb.item())
			ID = 0;
		if(stack.getItem() == FItems.pot_holder.item())
			ID = 1;
		if(stack.getItem() == FItems.pot.item())
			ID = 2;
		ItemStack stackOld = getStackInSlot(4 + ID);
		setInventorySlotContents(ID + 4, stack);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		return stackOld;
	}
}
