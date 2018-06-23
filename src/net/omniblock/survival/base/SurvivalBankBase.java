package net.omniblock.survival.base;

import java.sql.SQLException;

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

	/**
	 * 
	 * Metodo estático para obtener el dinero
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
	 * Metodo estático para obtener el dinero
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
	 * Metodo estático para obtener el dinero
	 * de un jugador, mediante su NetworkID
	 * 
	 * @param param Nombre del jugador.
	 * @param isNetworkID Si el nombre del jugador es el del networkID.
	 * @return Dinero del jugador.
	 */
	public static int getMoney(String param, boolean isNetworkID) {

		MakeSQLQuery msq = new MakeSQLQuery(TableType.SURVIVAL_BANK_DATA)
				.select("p_money")
				.where("p_id", isNetworkID ? param : Resolver.getNetworkIDByName(param));

		try {
			
			SQLResultSet sqr = msq.execute();
			if (sqr.next()) {
				return sqr.get("p_money");
			}
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return 0;
		
	}
	
	/**
	 * 
	 * Metodo estático para dar dinero
	 * a un jugador.
	 * 
	 * @param player Jugador al cual se le dara el dinero.
	 * @param quantity Cantidad de dinero a dar.
	 */
	public static void setMoney(Player player, int quantity) {

		setMoney(player.getName(), quantity, false);
		return;

	}
	
	/**
	 * 
	 * Metodo estático para dar dinero a
	 * un jugador, usando su nombre
	 * 
	 * @param name Nombre del jugador.
	 * @param quantity Cantidad de dinero a dar. 
	 */
	public static void setMoney(String name, int quantity) {

		setMoney(name, quantity, false);
		return;

	}
	
	/**
	 * 
	 * Metodo estático  para dar dinero a 
	 * un jugador, usando la NetworkID.
	 * 
	 * @param param Nombre del jugador.
	 * @param quantity Cantidad de dinero.
	 * @param isNetworkID Si el nombre registrado es un NetworkID.
	 */
	public static void setMoney(String param, int quantity, boolean isNetworkID) {

		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SURVIVAL_BANK_DATA, TableOperation.UPDATE);

		msu.rowOperation("p_money", quantity);
		msu.whereOperation("p_id", isNetworkID ? param : Resolver.getNetworkIDByName(param));

		try {

			msu.execute();
			return;

		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}

		return;

	}
	
	/**
	 * 
	 * Metodo para añadir dinero a un jugaor
	 * 
	 * @param player Jugador.
	 * @param quantity Cantidad.
	 */
	public static void addMoney(Player player, int quantity) {

		addMoney(player.getName(), quantity, false);
		return;

	}

	/**
	 * 
	 * Metodo estático para añadir dinero a
	 * un jugador, usando su nombre.
	 * 
	 * @param name Nombre del jugador.
	 * @param quantity Cantidad de dinero a dar. 
	 */
	public static void addMoney(String name, int quantity) {

		addMoney(name, quantity, false);
		return;

	}
	
	/**
	 * 
	 * Metodo estático  para añadir dinero a 
	 * un jugador, usando la NetworkID.
	 * 
	 * @param param Nombre del jugador.
	 * @param quantity Cantidad de dinero.
	 * @param isNetworkID Si el nombre registrado es un NetworkID.
	 */
	public static void addMoney(String param, int quantity, boolean isNetworkID) {

		int money = getMoney(param, isNetworkID);
		setMoney(param, quantity + money, isNetworkID);
		return;

	}
	
	/**
	 * 
	 * Metodo para remover dinero a un jugador.
	 * 
	 * @param player Jugador.
	 * @param quantity Dinero que se va a remover.
	 */
	public static void removeMoney(Player player, int quantity) {

		removeMoney(player.getName(), quantity, false);
		return;

	}

	/**
	 * 
	 * Metodo para remover dinero, usando el nombre 
	 * del jugador.
	 * 
	 * @param name Nombre del jugador
	 * @param quantity Cantidad de dinero que se va a remover.
	 */
	public static void removeMoney(String name, int quantity) {

		removeMoney(name, quantity, false);
		return;

	}
	
	/**
	 * 
	 * Metodo para remover dinero, usando la NetworkID.
	 * 
	 * @param param Nombre del jugador.
	 * @param quantity Cantidad de dinero a remover.
	 * @param isNetworkID 
	 */
	public static void removeMoney(String param, int quantity, boolean isNetworkID) {

		int money = getMoney(param, isNetworkID);
		setMoney(param, (money - quantity < 0) ? 0 : money - quantity, isNetworkID);
		return;

	}
}
