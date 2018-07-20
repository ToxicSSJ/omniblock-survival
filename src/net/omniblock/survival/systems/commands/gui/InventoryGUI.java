package net.omniblock.survival.systems.commands.gui;

import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import net.omniblock.network.library.helpers.inventory.paginator.InventorySlotter;
import net.omniblock.network.library.helpers.inventory.paginator.PaginatorStyle;
import net.omniblock.network.library.utils.TextUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InventoryGUI {

	public static List<InventoryGUI> createdGUI = new ArrayList<>();

	private Paginator paginator;
	private InventorySlotter slotter;

	public InventoryGUI(){

		if(RegisterGUI.getGuis().isEmpty())
			return;

		this.paginator = new Paginator(PaginatorStyle.COLOURED_ARROWS);
		this.slotter = new InventorySlotter(InventorySlotter.SlotLocatorType.LINE);

		InventoryBuilder cacheBuilder = new InventoryBuilder(TextUtil.format("&4&lComandos"), 1 * 9, false);

		for(GuiExecutor cache : RegisterGUI.getGuis())
			for(ItemGUI item : cache.onCreate()){

				if(!slotter.hasNext()){

					paginator.addPage(cacheBuilder);
					slotter.reset();

					cacheBuilder = new InventoryBuilder(TextUtil.format("&a&lComandos"), 1 * 9, false);
				}

				if(item.getExecutor() == null)
					cacheBuilder.addItem(item.getItemSatck(), slotter.next());

				else cacheBuilder.addItem(item.getItemSatck(), slotter.next(), item.getExecutor());

				continue;
			}


		if(!paginator.contains(cacheBuilder))
			paginator.addPage(cacheBuilder);
	}

	public void openShop(Player player) {

		paginator.openInventory(player);
		return;

	}

	public static InventoryGUI InventoryHelp(){
		return new InventoryGUI();
	}
}
