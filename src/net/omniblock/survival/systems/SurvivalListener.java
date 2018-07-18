package net.omniblock.survival.systems;

import net.omniblock.survival.SurvivalManager;
import net.omniblock.survival.SurvivalPlugin;
import net.omniblock.survival.systems.commands.Back;
import org.bukkit.GameMode;
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

						e.getPlayer().teleport(SurvivalManager.getLocation());
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

						Back.addPlayerLocation(e.getPlayer());

					}
				}.runTaskLater(SurvivalPlugin.getInstance(), 2L);

			}
			
		}, SurvivalPlugin.getInstance());

		SurvivalPlugin.getInstance().getServer().getPluginManager().registerEvents(new Back(), SurvivalPlugin.getInstance());
		
	}
	
}
