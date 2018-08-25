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

package net.omniblock.survival.base;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import net.omniblock.network.library.utils.ExpirableCache;
import org.bukkit.entity.Player;

import net.omniblock.network.handlers.base.sql.make.MakeSQLQuery;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate.TableOperation;
import net.omniblock.network.handlers.base.sql.type.TableType;
import net.omniblock.network.handlers.base.sql.util.Resolver;
import net.omniblock.network.handlers.base.sql.util.SQLResultSet;

/**
 * 
 * Clase encargada de almacener y utilizar
 * el dinero de los jugadores, en el servidor
 * de survival.
 * 
 * @author Luis Villegas
 *
 */
public class SurvivalBankBase {

	private static ExpirableCache<String, Integer> moneyCache = new ExpirableCache<>(10, TimeUnit.MINUTES);

	/**
	 * 
	 * Metodo est�tico para obtener el dinero
	 * de un jugador. 
	 * 
	 * @param player Jugador
	 * @return Dinero del jugador.
	 * 
	 */
	public static int getMoney(Player player) {
		return getMoney(player.getName(), false);
	}

	/**
	 * 
	 * Metodo est�tico para obtener el dinero
	 * de un jugador, mediante su nombre.
	 * 
	 * @param name Nombre del jugador. 
	 * @return Dinero del jugador.
	 * 
	 */
	public static int getMoney(String name) {
		return getMoney(name, false);
	}
	
	/**
	 * 
	 * Metodo est�tico para obtener el dinero
	 * de un jugador, mediante su NetworkID
	 * 
	 * @param param Nombre del jugador.
	 * @param isNetworkID Si el nombre del jugador es el del networkID.
	 * @return Dinero del jugador.
	 */
	public static int getMoney(String param, boolean isNetworkID) {
		String networkId = isNetworkID ? param : Resolver.getNetworkIDByName(param);

		if(moneyCache.containsKey(networkId)) {
			return moneyCache.get(networkId);
		}

		MakeSQLQuery msq = new MakeSQLQuery(TableType.SURVIVAL_BANK_DATA)
				.select("p_money")
				.where("p_id", networkId);

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				int money = sqr.get("p_money");
				moneyCache.put(networkId, money);

				return money;
			}
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	/**
	 * 
	 * Metodo est�tico para dar dinero
	 * a un jugador.
	 * 
	 * @param player Jugador al cual se le dara el dinero.
	 * @param amount Cantidad de dinero a dar.
	 */
	public static void setMoney(Player player, int amount) {
		setMoney(player.getName(), amount, false);
	}
	
	/**
	 * 
	 * Metodo est�tico para dar dinero a
	 * un jugador, usando su nombre
	 * 
	 * @param name Nombre del jugador.
	 * @param amount Cantidad de dinero a dar. 
	 */
	public static void setMoney(String name, int amount) {
		setMoney(name, amount, false);
	}
	
	/**
	 * 
	 * Metodo est�tico  para dar dinero a 
	 * un jugador, usando la NetworkID.
	 * 
	 * @param param Nombre del jugador.
	 * @param amount Cantidad de dinero.
	 * @param isNetworkID Si el nombre registrado es un NetworkID.
	 */
	public static void setMoney(String param, int amount, boolean isNetworkID) {
		String networkId = isNetworkID ? param : Resolver.getNetworkIDByName(param);
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SURVIVAL_BANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_money", amount);
		msu.whereOperation("p_id", networkId);

		try {
			msu.execute();
			moneyCache.put(networkId, amount);
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Metodo para a�adir dinero a un jugaor
	 * 
	 * @param player Jugador.
	 * @param amount Cantidad.
	 */
	public static void addMoney(Player player, int amount) {
		addMoney(player.getName(), amount, false);
	}

	/**
	 * 
	 * Metodo est�tico para a�adir dinero a
	 * un jugador, usando su nombre.
	 * 
	 * @param name Nombre del jugador.
	 * @param amount Cantidad de dinero a dar. 
	 */
	public static void addMoney(String name, int amount) {
		addMoney(name, amount, false);
	}
	
	/**
	 * 
	 * Metodo estatico  para añadir dinero a
	 * un jugador, usando la NetworkID.
	 * 
	 * @param param Nombre del jugador.
	 * @param amount Cantidad de dinero.
	 * @param isNetworkID Si el nombre registrado es un NetworkID.
	 */
	public static void addMoney(String param, int amount, boolean isNetworkID) {
		int money = getMoney(param, isNetworkID);
		setMoney(param, amount + money, isNetworkID);
	}
	
	/**
	 * 
	 * Metodo para remover dinero a un jugador.
	 * 
	 * @param player Jugador.
	 * @param amount Dinero que se va a remover.
	 */
	public static void removeMoney(Player player, int amount) {
		removeMoney(player.getName(), amount, false);
	}

	/**
	 * 
	 * Metodo para remover dinero, usando el nombre 
	 * del jugador.
	 * 
	 * @param name Nombre del jugador
	 * @param amount Cantidad de dinero que se va a remover.
	 */
	public static void removeMoney(String name, int amount) {
		removeMoney(name, amount, false);
	}
	
	/**
	 * 
	 * Metodo para remover dinero, usando la NetworkID.
	 * 
	 * @param param Nombre del jugador.
	 * @param amount Cantidad de dinero a remover.
	 * @param isNetworkID Si el nombre registrado es un NetworkID.
	 */
	public static void removeMoney(String param, int amount, boolean isNetworkID) {
		int money = getMoney(param, isNetworkID);
		setMoney(param, money - amount, isNetworkID);
	}
}
