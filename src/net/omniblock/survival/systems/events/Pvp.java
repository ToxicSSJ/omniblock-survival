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

import net.omniblock.survival.config.ConfigType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 *
 * Clase encargada del pvp de la
 * modalidad survival tanto para el
 * servidor y a todos sus jugadores.
 *
 */
public class Pvp implements Listener {

	/**
	 * Variable que determina
	 * si hay pvp en general..
	 */
	private static boolean pvp = true;

	/**
	 * Lista de jugadores que
	 * habilitaron su pvp.
	 */
	private static Set<PvpPlayer> onPvpPlayers = new HashSet<>();

	/**
	 * Lista de jugadores que deberán
	 * morir al entrar al servidor.
	 */
	public static Set<String> deathList = new HashSet<>();

	/**
	 * Mapa que guarda como llave el
	 * jugador que fue golpeado y como
	 * objeto el que lo golpeo.
	 */
	public static Map<String, String> inCombatPlayers = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent e) {

		if(!(e.getEntity() instanceof  Player
				&& e.getDamager() instanceof Player))
			return;

		if(!pvp){
			e.setCancelled(true);
			return;
		}

		PvpPlayer damaged = onPvpPlayers.stream().filter(player -> player.equals((Player) e.getEntity())).findAny().orElse(null);
		PvpPlayer damager = onPvpPlayers.stream().filter(player -> player.equals((Player) e.getDamager())).findAny().orElse(null);

		if(damaged == null
				//|| damager == null
				){
			e.setCancelled(true);
			return;
		}

		inCombatPlayers.put(damaged.getPlayer().getName(), damager.getPlayer().getName());

		damaged.punch();
		damager.punch();

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!deathList.contains(e.getPlayer().getName())) return;

		deathListRemove(e.getPlayer().getName());
		e.getPlayer().getInventory().clear();
		e.getPlayer().setHealth(0.00D);

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e){

		if(hasPvp(e.getPlayer())) {
			getPvpPlayer(e.getPlayer()).quit(e.getPlayer());
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e){
			if(hasPvp(e.getEntity()))
				getPvpPlayer(e.getEntity()).died();
	}

	/////////////////////////////////////////PVP DE SURVIVAL

	/**
	 * Metodo para saber si hay
	 * pvp en el survival.
	 *
	 * @return Retorna si hay pvp
	 */
	public static boolean isPvp(){
		return pvp;
	}

	/**
	 * Metodo para cambiar el
	 * pvp en el survival.
	 *
	 * @param bool True o false.
	 */
	public static void setPvp(boolean bool){
		pvp = bool;
	}

	/**
	 * Metodo para cambiar el
	 * pvp en el survival.
	 */
	public static void switchSurvivalPvp(){
		if(pvp)
			pvp = false;
		else
			pvp = true;
	}

	/////////////////////////////////////////PVP DE JUGADOR

	/**
	 * Metodo para añadir a la lista a un jugador
	 * que deba morir al entrar al servidor.
	 * @param name Nombre del jugador que debe morir
	 */
	public static void deathListAdd(String name) {
		deathList.add(name);
		ConfigType.PVP.getConfig().set("deathList", new ArrayList<>(deathList));
		ConfigType.PVP.getConfigObject().save();
	}

	/**
	 * Metodo para añadir a la lista a un jugador
	 * que deba morir al entrar al servidor.
	 * @param name Nombre del jugador que debe morir
	 */
	public static void deathListRemove(String name) {
		deathList.remove(name);
		ConfigType.PVP.getConfig().set("deathList", new ArrayList<>(deathList));
		ConfigType.PVP.getConfigObject().save();
	}

	/**
	 * Metodo encargado de cargar la lista
	 * de jugadores que deben morir al entrar.
	 */
	public static void loadToDiePlayers(){

		deathList.addAll(ConfigType.PVP.getConfig().getStringList("deathList"));

	}

	/**
	 * Metodo para activar el pvp de un jugador.
	 *
	 * @param player Jugador que activa su pvp.
	 */
	public static void activate(Player player){

		PvpPlayer pvpPlayer = new PvpPlayer(player.getName());
		onPvpPlayers.add(pvpPlayer);

	}

	/**
	 * Metodo para desactivar el pvp de un jugador.
	 *
	 * @param player Jugador de desactiva su pvp.
	 */
	public static void desactivate(Player player){
		onPvpPlayers.remove(getPvpPlayer(player));
	}

	/**
	 * Metodo para intercambiar el modo de combate del jugador.
	 *
	 * @param player Jugador a cambiar el modo pvp.
	 */
	public static void switchPlayerPvp(Player player){

		if(hasPvp(player))
			onPvpPlayers.remove(getPvpPlayer(player));
		else {
			PvpPlayer pvpPlayer = new PvpPlayer(player.getName());
			onPvpPlayers.add(pvpPlayer);
		}
	}

	/**
	 *
	 * @param p Jugador que se quiere retornar.
	 * @return El objeto Pvp del jugador.
	 */
	public static PvpPlayer getPvpPlayer(Player p){
		return onPvpPlayers.stream().filter(pvpPlayer -> pvpPlayer.equals(p)).findAny().orElse(null);
	}

	/**
	 * @param p Jugador a buscar.
	 * @return Si el jugador tiene pvp activado
	 */
	public static boolean hasPvp(Player p){

		return getPvpPlayer(p) != null;
	}
}