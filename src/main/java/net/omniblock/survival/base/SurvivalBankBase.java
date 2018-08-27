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
import java.util.UUID;

import net.omniblock.dep.databaseutil.Database;
import net.omniblock.dep.databaseutil.make.MakeSQLQuery;
import net.omniblock.dep.databaseutil.make.MakeSQLUpdate;
import net.omniblock.dep.databaseutil.util.SQLResultSet;
import net.omniblock.dep.essentialsutils.cache.PlayerCache;
import net.omniblock.survival.SurvivalPlugin;
import org.bukkit.entity.Player;

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

	private static PlayerCache<Integer> playerCache = new PlayerCache<>();

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
		return getMoney(player.getUniqueId());
	}
	
	/**
	 * 
	 * Metodo est�tico para obtener el dinero
	 * de un jugador, mediante su NetworkID
	 *
	 * @return Dinero del jugador.
	 */
	public static int getMoney(UUID playerUUID) {
		if(playerCache.containsPlayer(playerUUID)) {
			return playerCache.get(playerUUID);
		}

		String networkId = null;

		try {
			networkId = tryToResolveOldNetworkId(playerUUID);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}

		SurvivalPlugin instance = SurvivalPlugin.getInstance();
		Database omnibase = instance.getOmnibaseDatabase();

		if(networkId != null) {
			//intentar migrar
			try {
				tryToMigrate(networkId, playerUUID);
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			}
		}

		MakeSQLQuery msq = omnibase.makeSQLQuery("survival_bank_data")
				.select("p_money")
				.where("p_id", playerUUID.toString());

		try {
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				int money = sqr.get("p_money");
				playerCache.put(playerUUID, money);
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
		setMoney(player.getUniqueId(), amount);
	}
	
	/**
	 * 
	 * Metodo est�tico  para dar dinero a 
	 * un jugador, usando la NetworkID.
	 *
	 * @param amount Cantidad de dinero.
	 */
	public static void setMoney(UUID playerUUID, int amount) {
		String networkId = null;

		try {
			networkId = tryToResolveOldNetworkId(playerUUID);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		if(networkId != null) {
			//probablemente usa el anterior sistema de network ids
			try {
				tryToMigrate(networkId, playerUUID);
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}

		SurvivalPlugin instance = SurvivalPlugin.getInstance();
		Database omnibase = instance.getOmnibaseDatabase();

		MakeSQLUpdate msu = omnibase.makeSQLUpdate("survival_bank_data", MakeSQLUpdate.TableOperation.UPDATE);

		msu.rowOperation("p_money", amount);
		msu.whereOperation("p_id", playerUUID.toString());

		try {
			msu.execute();
			playerCache.put(playerUUID, amount);
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
		addMoney(player.getUniqueId(), amount);
	}

	/**
	 * 
	 * Metodo estatico  para añadir dinero a
	 * un jugador, usando la NetworkID.
	 *
	 * @param amount Cantidad de dinero.
	 */
	public static void addMoney(UUID uuid, int amount) {
		int money = getMoney(uuid);
		setMoney(uuid, amount + money);
	}
	
	/**
	 * 
	 * Metodo para remover dinero a un jugador.
	 * 
	 * @param player Jugador.
	 * @param amount Dinero que se va a remover.
	 */
	public static void removeMoney(Player player, int amount) {
		removeMoney(player.getUniqueId(), amount);
	}

	/**
	 * 
	 * Metodo para remover dinero, usando la NetworkID.
	 *
	 * @param amount Cantidad de dinero a remover.
	 */
	public static void removeMoney(UUID uuid, int amount) {
		int money = getMoney(uuid);
		setMoney(uuid, money - amount);
	}

	private static String tryToResolveOldNetworkId(UUID playerUUID) throws SQLException {
		SurvivalPlugin survivalPlugin = SurvivalPlugin.getInstance();
		Database omnibase = survivalPlugin.getOmnibaseDatabase();

		SQLResultSet res = omnibase.makeSQLQuery("uuid_resolver").select("p_resolver").where("p_offline_uuid", playerUUID.toString()).execute();
		if(res.next()) {
			return res.get("p_resolver");
		} else {
			res = omnibase.makeSQLQuery("uuid_resolver").select("p_resolver").where("p_online_uuid", playerUUID.toString()).execute();
			if(res.next()) {
				return res.get("p_resolver");
			}
		}

		return null;
	}

	private static void tryToMigrate(String nid, UUID uuid) throws SQLException {
		SurvivalPlugin instance = SurvivalPlugin.getInstance();
		Database omnibase = instance.getOmnibaseDatabase();
		SQLResultSet res = omnibase.makeSQLQuery("survival_bank_data").select("p_money").where("p_id", nid).execute();
		if(res.next()) {
			instance.getLogger().info("Migrando economía NID: " + nid + " a UUID: " + uuid);
			omnibase.makeSQLUpdate("survival_bank_data", MakeSQLUpdate.TableOperation.UPDATE).rowOperation("p_id", uuid.toString()).whereOperation("p_id", nid).execute();
			instance.getLogger().info("OK, economía migrada usando UUID en lugar de NID.");
		}
	}
}
