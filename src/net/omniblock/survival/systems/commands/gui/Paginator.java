package net.omniblock.survival.systems.commands.gui;

import com.google.common.collect.Lists;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import net.omniblock.network.library.helpers.inventory.paginator.PaginatorStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class Paginator {

	protected List<InventoryBuilder> inventoryPages = Lists.newArrayList();
	protected PaginatorStyle style;

	public Paginator(PaginatorStyle style){

		this.style = style;

	}


	public Paginator addPage(InventoryBuilder inventoryBuilder) {

		if(inventoryBuilder.getSize() < 1 * 9)
			throw new UnsupportedOperationException("El inventario debe tener un tamaño mayor o igual a 9.");

		if(inventoryBuilder.isDeleteOnClose())
			throw new UnsupportedOperationException("El inventario no debe ser borrado al cerrarse.");

		inventoryBuilder.addItem(style.getNext(true), 8);

		if(inventoryPages.size() != 0)
			getLastPage().addItem(style.getNext(false), 8, new InventoryBuilder.Action() {

				@Override
				public void click(ClickType click, Player player) {

					inventoryBuilder.open(player);
					return;

				}

			});

		if(inventoryPages.size() == 0)
			inventoryBuilder.addItem(style.getBack(true), 0);

		if(inventoryPages.size() != 0)
			inventoryBuilder.addItem(style.getBack(false), 0, new InventoryBuilder.Action() {

				InventoryBuilder backPage = getLastPage();

				@Override
				public void click(ClickType click, Player player) {

					backPage.open(player);
					return;

				}

			});

		inventoryPages.add(inventoryBuilder);
		return this;

	}

	public InventoryBuilder getLastPage() {

		if(inventoryPages.size() == 0)
			return null;

		return inventoryPages.get(inventoryPages.size() - 1);
	}

	public boolean contains(InventoryBuilder inventoryBuilder) {

		return inventoryPages.contains(inventoryBuilder);
	}

	public void openInventory(Player player) {

		if(inventoryPages.size() < 1)
			return;

		inventoryPages.get(0).open(player);
		return;

	}

	public PaginatorStyle getStyle() {
		return style;
	}
}
