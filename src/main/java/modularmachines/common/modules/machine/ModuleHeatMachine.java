package modularmachines.common.modules.machine;

import net.minecraft.world.WorldServer;

import modularmachines.api.ILocatable;
import modularmachines.api.modules.energy.IHeatSource;
import modularmachines.api.recipes.IRecipeHeat;
import modularmachines.common.network.PacketHandler;
import modularmachines.common.network.packets.PacketSyncHeatBuffer;
import modularmachines.common.network.packets.PacketSyncModule;
import modularmachines.common.utils.ModuleUtil;

public abstract class ModuleHeatMachine<R extends IRecipeHeat> extends ModuleMachine<R> {

	protected double heatToRemove = 0;
	protected double heatRequired = 0;
	
	public ModuleHeatMachine(int workTimeModifier) {
		super(workTimeModifier);
	}
	
	@Override
	public void update() {
		super.update();
		boolean needUpdate = false;
		if (canWork()) {
			if (workTime >= workTimeTotal || recipe == null) {
				R validRecipe = getValidRecipe();
				if (recipe != null) {
					if (addOutputs()) {
						recipe = null;
						workTimeTotal = 0;
						workTime = 0;
						chance = rand.nextFloat();
						needUpdate = true;
					}
				} else if (validRecipe != null) {
					recipe = validRecipe;
					workTimeTotal = createWorkTimeTotal(validRecipe.getSpeed());
					chance = rand.nextFloat();
					if(validRecipe instanceof IRecipeHeat){
						IRecipeHeat heatRecipe = recipe;
						heatToRemove = heatRecipe.getHeatToRemove() / workTimeTotal;
						heatRequired = heatRecipe.getRequiredHeat();
					}
					needUpdate = true;
				}
			} else {
				int workTime = 0;
				IHeatSource heatBuffer = ModuleUtil.getHeat(container);
				if (heatBuffer.getHeatStored() >= heatRequired) {
					heatBuffer.extractHeat(heatToRemove, false);
					workTime = 1;
				}
				if (workTime > 0) {
					needUpdate = true;
					this.workTime+=workTime;
				}
			}
			if (needUpdate) {
				sendModuleUpdate();
			}
		}
	}
	
	@Override
	protected boolean isRecipeValid(R recipe) {
		if(!super.isRecipeValid(recipe)){
			return false;
		}
		IHeatSource heatBuffer = ModuleUtil.getHeat(container);
		if (recipe.getRequiredHeat() > heatBuffer.getHeatStored()) {
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean canWork() {
		IHeatSource heatBuffer = ModuleUtil.getHeat(container);
		return heatBuffer.getHeatStored() > 0;
	}
	
	@Override
	public void sendModuleUpdate() {
		ILocatable locatable = container.getLocatable();
		if (locatable != null) {
			PacketHandler.sendToNetwork(new PacketSyncModule(this), locatable.getCoordinates(), (WorldServer) locatable.getWorldObj());
			PacketHandler.sendToNetwork(new PacketSyncHeatBuffer(container), locatable.getCoordinates(), (WorldServer) locatable.getWorldObj());
		}
	}

}
