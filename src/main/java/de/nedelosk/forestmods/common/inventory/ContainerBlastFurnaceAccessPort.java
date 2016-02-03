package de.nedelosk.forestmods.common.inventory;

import de.nedelosk.forestcore.inventory.ContainerBase;
import de.nedelosk.forestmods.common.blocks.tile.TileBlastFurnaceAccessPort;
import de.nedelosk.forestmods.common.inventory.slots.SlotBlastFurnace;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBlastFurnaceAccessPort extends ContainerBase<TileBlastFurnaceAccessPort> {

	public ContainerBlastFurnaceAccessPort(TileBlastFurnaceAccessPort tile, InventoryPlayer inventory) {
		super(tile, inventory);
	}

	@Override
	protected void addSlots(InventoryPlayer inventory) {
		addSlotToContainer(new SlotBlastFurnace(inventoryBase, 0, 53, 35));
		addSlotToContainer(new SlotBlastFurnace(inventoryBase, 1, 71, 35));
		addSlotToContainer(new SlotBlastFurnace(inventoryBase, 2, 89, 35));
		addSlotToContainer(new SlotBlastFurnace(inventoryBase, 3, 107, 35));
	}
}