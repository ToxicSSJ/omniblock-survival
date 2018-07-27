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

package net.omniblock.survival.systems;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.shop.systems.box.MysteryBox;
import net.omniblock.shop.systems.object.Element;
import net.omniblock.survival.SurvivalManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SurvivalBox extends MysteryBox  {

	public  SurvivalBox(){
		super(SurvivalManager.getLocation().clone().add(3,1,8));
	}

	@Override
	public void craftBox() {

		this.getBase().setType(Material.ENDER_PORTAL_FRAME);
		this.getBox().setType(Material.CHEST);
	}

	@Override
	public void contentBox() {

	}

	@Override
	public Listener getEvent() {
		return new Listener() {

			@EventHandler
			public void onClick(PlayerInteractEvent e){

				if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					if(e.getClickedBlock().getLocation().equals(getBox().getLocation()))
						if(e.getClickedBlock().getType().equals(Material.CHEST)){

							destroyBox();
							e.getPlayer().sendMessage(TextUtil.format("&8&lB&8oosters &c&l  &c&lProximamente.... "));

						}

			}
		};
	}

	@Override
	public void buy(Player player, Element element) {

	}
	
}
