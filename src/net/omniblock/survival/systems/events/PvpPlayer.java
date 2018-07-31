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

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.survival.SurvivalPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

/**
 *
 * Pequeña clase para gestionar
 * el pvp de un jugador.
 *
 */
class PvpPlayer{

	private String player;
	private boolean combatMode = false;
	private BukkitTask combatTask;

	/**
	 * Constructor de la clase.
	 *
	 * @param player Jugador
	 */
	PvpPlayer(String player){

		this.player = player;

	}

	/**
	 * Metodo para cada que un
	 * jugador golpee otro
	 */
	public void punch(){

		if(!combatMode){
			getPlayer().sendMessage(TextUtil.format("&c&l[PvP]&e» Estás en combate. ¡No te desconectes o morirás!"));
		}

		inCombat();
	}

	/**
	 * Metodo del cooldown de 10 segundos
	 */
	private void inCombat(){

		removeCombat();
		combatMode = true;

		combatTask = new BukkitRunnable() {

			int seconds = 15;

			@Override
			public void run() {

				if (seconds <= 0) {
					getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_CAT_PURREOW, 2, 2);
					getPlayer().sendMessage(TextUtil.format("&c&l[PvP]&e» Ya no estás en combate."));
					removeCombat();
				}

				seconds--;
			}
		}.runTaskTimer(SurvivalPlugin.getInstance(), 0L, 20L);
	}

	/**
	 * Cancelar el cooldown de pvp
	 */
	public void removeCombat(){

		if(combatTask != null)
			combatTask.cancel();
		combatMode = false;

	}

	/**
	 * Que hacer en caso de que el jugador muera.
	 */
	public void died() {
		if (!combatMode) return;
		removeCombat();
	}

	/**
	 * Que hacer si el jugador sale.
	 */
	public void quit(Player player) {
		if (!combatMode) return;
		removeCombat();
		Pvp.deathListAdd(player.getName());

		Bukkit.getServer().broadcastMessage(TextUtil.format("&c&l[PvP]»&e El jugador &a" + player.getName()
				+"&e ha muerto intentando huir de &a" + Pvp.inCombatPlayers.get(player.getName())));

		for(ItemStack item : player.getInventory())
			if(item != null)
				player.getWorld().dropItem(player.getLocation(), item);
	}

	///////////////////////////////////////////

	/**
	 * @return Retorna el jugador del objeto.
	 */
	public Player getPlayer(){
		return Bukkit.getServer().getPlayer(player);
	}

	/**
	 * Devuelve si los nombres
	 * de los dos jugadores coinciden.
	 *
	 * @param other Jugador Pvp a comparar.
	 * @return Devuelve si es el mismo.
	 */
	public boolean equals(PvpPlayer other){
		return this.equals(other.getPlayer());
	}

	/**
	 * Devuelve si los nombres
	 * de los dos jugadores coinciden.
	 *
	 * @param other Jugador a comparar.
	 * @return Devuelve si es el mismo.
	 */
	public boolean equals(Player other){
		return this.equals(other.getName());
	}

	/**
	 * Devuelve si los nombres
	 * de los dos jugadores coinciden.
	 *
	 * @param other Nombre a comparar.
	 * @return Devuelve si es el mismo.
	 */
	public boolean equals(String other){
		return (this.player.equals(other));
	}

	public int hashCode() {
		return Objects.hashCode(player);
	}
}
