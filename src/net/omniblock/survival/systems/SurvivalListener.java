package net.omniblock.survival.systems;

import net.omniblock.network.library.addons.resourceaddon.ResourceHandler;
import net.omniblock.network.library.addons.resourceaddon.type.ResourceType;
import net.omniblock.survival.SurvivalManager;
import net.omniblock.survival.SurvivalPlugin;
import net.omniblock.survival.systems.commands.Back;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.ActionsPatcher;
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
		
		SurvivalPlugin.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {

				new BukkitRunnable(){

					@Override
					public void run() {

						//e.getPlayer().teleport(SurvivalManager.getLocation());
						e.getPlayer().setGameMode(GameMode.SURVIVAL);

						e.getPlayer().setAllowFlight(false);
						e.getPlayer().setFlying(false);

						e.getPlayer().sendMessage(new String[] {

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

						while (loc.getBlock().isEmpty() && loc.getBlockY() >= 0 && loc.getBlockY() <= 2000){
							loc.add(0, -1, 0);
						}

						if(0 > loc.getBlockY() || loc.getBlockY() > 2000)	//Si el jugador está fuera de los limites
							e.getPlayer().teleport(loc.getWorld().getHighestBlockAt(loc).getLocation());
						else
						{
							loc.add(0, 1, 0);
							e.getPlayer().teleport(loc);
						}

						/*
						Actualizar el resource pack del jugador si tiene el de sky wars Z
						 */
                        ResourceHandler.sendResourcePack(e.getPlayer(), ResourceType.OMNIBLOCK_DEFAULT);

					}
				}.runTaskLater(SurvivalPlugin.getInstance(), 2L);

			}
			
		}, SurvivalPlugin.getInstance());

		SurvivalPlugin.getInstance().getServer().getPluginManager().registerEvents(new Back(), SurvivalPlugin.getInstance());
		
	}
	
}
