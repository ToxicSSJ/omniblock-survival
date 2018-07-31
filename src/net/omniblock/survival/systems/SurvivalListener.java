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

import net.omniblock.network.library.addons.resourceaddon.ResourceHandler;
import net.omniblock.network.library.addons.resourceaddon.type.ResourceType;
import net.omniblock.survival.SurvivalManager;
import net.omniblock.survival.SurvivalPlugin;
import net.omniblock.survival.systems.commands.Back;
import net.omniblock.survival.systems.events.God;
import net.omniblock.survival.systems.events.MovementDistanceView;
import net.omniblock.survival.systems.events.Pvp;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.ActionsPatcher;
import org.bukkit.event.player.PlayerQuitEvent;
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

								TextUtil.getCenteredMessage("&8---------------------------------------------------"),
								TextUtil.getCenteredMessage("&7Bienvenido al &b&lSURVIVAL &7" + e.getPlayer().getName() + "!"),
								"",
								TextUtil.format("&f&l(!) &7No olvides visitar nuestro foro para interactuar con toda"),
								TextUtil.format("&7la comunidad."),
								TextUtil.format("&f&l(!) &7No olvides que puedes recibir recompensas si votas por"),
								TextUtil.format("&7el servidor en los tops."),
								"",
								TextUtil.format("    &f&nFORO:&e www.omniblock.net       &f&nTIENDA:&a tienda.omniblock.net"),
								TextUtil.getCenteredMessage("&8---------------------------------------------------")

						});

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

						/*
						Actualizar el resource pack del jugador si tiene el de sky wars Z
						 */
						ResourceHandler.sendResourcePack(e.getPlayer(), ResourceType.OMNIBLOCK_DEFAULT);

                        /*
                        Añadir al jugador a la boss bar
                         */
						if (!SurvivalManager.bar.getPlayers().contains(e.getPlayer()))
							SurvivalManager.bar.addPlayer(e.getPlayer());

					}
				}.runTaskLater(SurvivalPlugin.getInstance(), 2L);

			}

		}, SurvivalPlugin.getInstance());

		pm.registerEvents(new Pvp(), SurvivalPlugin.getInstance());
		pm.registerEvents(new Back(), SurvivalPlugin.getInstance());
		pm.registerEvents(new God(), SurvivalPlugin.getInstance());
		pm.registerEvents(new MovementDistanceView(), SurvivalPlugin.getInstance());
	}
	
}
