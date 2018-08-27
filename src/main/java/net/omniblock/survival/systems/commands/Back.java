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

package net.omniblock.survival.systems.commands;

import net.omniblock.dep.essentialsutils.TextUtil;
import net.omniblock.survival.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Esta clase gestiona el sistema
 * de back, tanto comandos
 * como listeners
 *
 * @author SoZyk
 *
 */
public class Back implements CommandExecutor, Listener {

	private static Map<Player, Location> backLocations = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;

		//BACK Command
		if(cmd.getName().equalsIgnoreCase("back")){

			if(!backLocations.containsKey(player)){

				player.sendMessage(TextUtil.format("&cAhora mismo no tienes ninguna lugar al cual volver."));
				addPlayerLocation(player);
				return true;
			}

			player.sendMessage(TextUtil.format("&eSerás teletransportado en 3 segundos. ¡No te muevas!"));

			new BukkitRunnable() {
				int seconds = 3;
				Location loc = player.getLocation().clone();

				@Override
				public void run() {

					if(!player.isOnline()){
						cancel();
						return;
					}

					if(seconds < 3 && loc.distance(player.getLocation()) > 1D){

						player.sendMessage(TextUtil.format("&c¡Te has movido! Teletransporte cancelado."));
						cancel();
						return;
					}

					if(seconds==1)player.sendMessage(TextUtil.format("&eTeletransportando..."));

					if(seconds <= 0){

						player.teleport(backLocations.get(player));

						cancel();
						return;
					}

					seconds--;
				}
			}.runTaskTimer(SurvivalPlugin.getInstance(), 0, 20);

			return true;
		}
		return false;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(!backLocations.containsKey(e.getPlayer())) addPlayerLocation(e.getPlayer());
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		addPlayerLocation(e.getPlayer(), e.getFrom());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		addPlayerLocation(e.getEntity());
	}

	/**
	 *
	 * Metodo que sirve para guardar la
	 * la locacion de un jugador
	 * para la función de back.
	 *
	 * @param player
	 *          Jugador del que se registra la locación.
	 */
	public static void addPlayerLocation(Player player){
		addPlayerLocation(player, player.getLocation());
	}

	/**
	 *
	 * Metodo que sirve para guardar la
	 * locacipon de un jugador para
	 * la funcion de back.
	 *
	 * @param player
	 *          Jugador del que se registra la locación.
	 * @param loc
	 *          La locación del jugador.
	 */
	public static void addPlayerLocation(Player player, Location loc){
		if(backLocations.containsKey(player))
			if (backLocations.get(player).getWorld().getName().equals(loc.getWorld().getName()))
				if( backLocations.get(player).distanceSquared(loc) < 2.5)
					return;

		backLocations.put(player, loc.clone());
	}

	/**
	 *
	 * Metodo para guardar las locaciones
	 * de todos los jugadores en caso
	 * de que se haga reload del
	 * plugin y se eliminen las locaciones
	 * previamente guardadas
	 *
	 */
	public static void saveLocations(){
		for(Player player : Bukkit.getServer().getOnlinePlayers())
			addPlayerLocation(player);
	}
}
