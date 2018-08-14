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

import net.omniblock.survival.SurvivalManager;
import net.omniblock.survival.SurvivalPlugin;
import net.omniblock.survival.systems.commands.Back;
import net.omniblock.survival.systems.events.God;
import net.omniblock.survival.systems.events.MovementDistanceView;
import net.omniblock.survival.utils.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.ActionsPatcher;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * 
 * Con esta clase se registran todos
 * los eventos del survival.
 * 
 * 
 * @author Luis Villegas
 *
 */
public class SurvivalListener {

	public static void listen() {

		ActionsPatcher.setup();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Listener() {

			@EventHandler
			public void onJoin(PlayerJoinEvent e) {

				new BukkitRunnable() {

					@Override
					public void run() {

						//e.getPlayer().teleport(SurvivalManager.getLocation());
						e.getPlayer().setGameMode(GameMode.SURVIVAL);

						e.getPlayer().setAllowFlight(false);
						e.getPlayer().setFlying(false);

						e.getPlayer().sendMessage(new String[]{

								"",
								"",
								TextUtil.getCenteredMessage("&f Bienvenido/a a &a&lSURVIVAL &f" + e.getPlayer().getName() + "!"),
								"",
								TextUtil.getCenteredMessage("&a&l(!) &aVisita nuestra web: &eomniblock.net"),
								TextUtil.getCenteredMessage("&a&l(!) &aTienda: &etienda.omniblock.net"),
								TextUtil.getCenteredMessage("&a&l(!) &aDiscord: &ediscord.gg/nYj6S2V"),
								"",
								TextUtil.getCenteredMessage("&aAyuda: &b/ayuda"),
								""

						});

						TitleUtil.sendTitle(e.getPlayer(), TextUtil.format("&f&k..&a&l SURVIVAL &f&k.."));
						TitleUtil.sendSubTitle(e.getPlayer(), TextUtil.format("&fBienvenido/a &b"+e.getPlayer().getName()));

						/*
        				Sistema para evitar que un jugador muera por estár en el aire al conectarse

        				Está pensado igualmente para jugadores que de pura casualidad están en el vacio.
        				De igual manera está limitado a 200 repeticiones (100 bloques de altura)
        				para no causar problemas al servidor
        				*/
						Location loc = e.getPlayer().getLocation().add(0, -1, 0).clone();

						while (loc.getBlock().isEmpty() && loc.getBlockY() >= 0 && loc.getBlockY() <= 2000) {
							loc.add(0, -1, 0);
						}

						if (0 > loc.getBlockY() || loc.getBlockY() > 2000)    //Si el jugador está fuera de los limites
							e.getPlayer().teleport(loc.getWorld().getHighestBlockAt(loc).getLocation());
						else {
							loc.add(0, 1, 0);
							e.getPlayer().teleport(loc);
						}

					}
				}.runTaskLater(SurvivalPlugin.getInstance(), 2L);

			}

		}, SurvivalPlugin.getInstance());

		pm.registerEvents(new Back(), SurvivalPlugin.getInstance());
		pm.registerEvents(new God(), SurvivalPlugin.getInstance());
		pm.registerEvents(new MovementDistanceView(), SurvivalPlugin.getInstance());
	}
	
}
