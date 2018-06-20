package net.omniblock.survival;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.ActionsPatcher;
import net.omniblock.survival.utils.WorldUtils;

public class SurvivalListener {

	public static void listen() {
		
		ActionsPatcher.setup();
		
		SurvivalPlugin.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {
				
				e.getPlayer().teleport(WorldUtils.getLocation("PlainsTwo", -67, 63, 151));
				
				e.getPlayer().sendMessage(new String[] {
						
						TextUtil.getCenteredMessage("&8---------------------------------------------------"),
						TextUtil.getCenteredMessage("&7¡Bienvenido al &b&lSURVIVAL &7" + e.getPlayer().getName() + "!"),
						"",
						TextUtil.format("&f&l(!) &7No olvides visitar nuestro foro para interactuar con toda"),
						TextUtil.format("&7la comunidad."),
						TextUtil.format("&f&l(!) &7No olvides que puedes recibir recompensas si votas por"),
						TextUtil.format("&7el servidor en los tops."),
						"",
						TextUtil.format("    &f&nFORO:&e www.omniblock.net       &f&nTIENDA:&a tienda.omniblock.net"),
						TextUtil.getCenteredMessage("&8---------------------------------------------------")
						
				});
				
				return;
			}
			
		}, SurvivalPlugin.getInstance());
		
	}
	
}
