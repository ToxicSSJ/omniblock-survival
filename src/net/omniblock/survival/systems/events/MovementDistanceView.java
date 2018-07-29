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

package net.omniblock.survival.systems.events;

import net.omniblock.network.library.utils.ExpirablePlayerData;
import net.omniblock.survival.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MovementDistanceView implements Listener {

	private ExpirablePlayerData<MovementInfo> movementWatcher = new ExpirablePlayerData<>();

	public MovementDistanceView() {
		new BukkitRunnable() {
			@Override
			public void run() {
				movementWatcher.forEach((k, v) -> {
					MovementInfo minfo = v;
					long diff = System.currentTimeMillis() - minfo.movementPeriod;
					if(diff >= 1000L) {
						int blocksMovedLastSecond = minfo.blocksMoved;
						minfo.movementPeriod = System.currentTimeMillis();
						minfo.blocksMoved = 0;

						Player p = Bukkit.getPlayer(k);

						if (p != null) {
							if(minfo.movingUsingPotionOrVehicle) {
								if (p.getViewDistance() != 5) {
									p.setViewDistance(5);
								}
							} else {
								if (blocksMovedLastSecond < 15) {
									if (p.getViewDistance() != 16) {
										p.setViewDistance(16);
									}
								} else if (blocksMovedLastSecond < 20) {
									if (p.getViewDistance() != 8) {
										p.setViewDistance(8);
									}
								} else {
									if (p.getViewDistance() != 5) {
										p.setViewDistance(5);
									}
								}
							}
						}
					}
				});
			}
		}.runTaskTimer(SurvivalPlugin.getInstance(), 20, 20);
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Location from = e.getFrom();
		Location to = e.getTo();

		if(from.getBlockX() != to.getBlockX() ||
			from.getBlockY() != to.getBlockY() ||
			from.getBlockZ() != to.getBlockZ())
		{
			if(movementWatcher.containsPlayer(e.getPlayer())) {
				MovementInfo minfo = movementWatcher.get(e.getPlayer());
				minfo.blocksMoved++;
				minfo.movingUsingPotionOrVehicle = e.getPlayer().isInsideVehicle() || e.getPlayer().isGliding() || e.getPlayer().hasPotionEffect(PotionEffectType.SPEED);
			} else {
				movementWatcher.put(e.getPlayer(), new MovementInfo());
			}
		}
	}

	private static class MovementInfo {
		private long movementPeriod;
		private int blocksMoved = 0;
		private boolean movingUsingPotionOrVehicle = false;

		public MovementInfo() {
			this.movementPeriod = System.currentTimeMillis();
		}
	}

}
