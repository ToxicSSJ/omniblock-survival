package net.omniblock.survival.systems.commands.gui;

import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import org.bukkit.inventory.ItemStack;

public class ItemGUI {

	private ItemStack itemSatck;

	private InventoryBuilder.Action executor;

	public ItemGUI(ItemStack itemSatck, InventoryBuilder.Action executor){

		this.itemSatck = itemSatck;
		this.executor = executor;

	}

	public InventoryBuilder.Action getExecutor() {
		return executor;
	}

	public ItemStack getItemSatck() {
		return itemSatck;
	}
}
