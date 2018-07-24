package net.omniblock.survival.systems.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * En esta clase se administran los eventos
 * para el sistema de invulnerabilidad.
 *
 * @author SoZyk
 */
public class God implements Listener {

	public static Set<Player> GODS = new HashSet<>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && GODS.contains(e.getEntity())) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		GODS.remove(e.getPlayer());
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		GODS.remove(e.getPlayer());
	}

}
