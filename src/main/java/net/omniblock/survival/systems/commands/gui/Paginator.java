/*
 * Omniblock Developers Team - Copyright (C) 2018 - All Rights Reserved
 *
 * 1. This software is not a free license software, you are not authorized to read, copy, modify, redistribute or
 * alter this file in any form without the respective authorization and consent of the Omniblock Developers Team.
 *
 * 2. If you have acquired this file violating the previous clause described in this Copyright Notice then you must
 * destroy this file from your hard disk or any other storage device.
 *
 * 3. As described in the clause number one, no third party are allowed to read, copy, modify, redistribute or
 * alter this file in any form without the respective authorization and consent of the Omniblock Developers Team.
 *
 * 4. Any concern about this Copyright Notice must be discussed at our support email: soporte.omniblock@gmail.com
 * -------------------------------------------------------------------------------------------------------------
 *
 * Equipo de Desarrollo de Omniblock - Copyright (C) 2018 - Todos los Derechos Reservados
 *
 * 1. Este software no es un software de libre uso, no está autorizado a leer, copiar, modificar, redistribuir
 * o alterar este archivo de ninguna manera sin la respectiva autorización y consentimiento del
 * Equipo de Desarrollo de Omniblock.
 *
 * 2. Si usted ha adquirido este archivo violando la clausula anterior descrita en esta Noticia de Copyright entonces
 * usted debe destruir este archivo de su unidad de disco duro o de cualquier otro dispositivo de almacenamiento.
 *
 * 3. Como se ha descrito en la cláusula número uno, ningun tercero está autorizado a leer, copiar, modificar,
 * redistribuir o alterar este archivo de ninguna manera sin la respectiva autorización y consentimiento del
 * Equipo de Desarrollo de Omniblock.
 *
 * 4. Cualquier duda acerca de esta Noticia de Copyright deberá ser discutido mediante nuestro correo de soporte:
 * soporte.omniblock@gmail.com
 */

package net.omniblock.survival.systems.commands.gui;

import com.google.common.collect.Lists;
import net.omniblock.dep.essentialsutils.inventory.InventoryBuilder;
import net.omniblock.dep.essentialsutils.inventory.PaginatorStyle;
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
