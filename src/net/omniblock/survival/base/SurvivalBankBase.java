package net.omniblock.survival.base;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import net.omniblock.network.handlers.base.sql.make.MakeSQLQuery;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate.TableOperation;
import net.omniblock.network.handlers.base.sql.type.TableType;
import net.omniblock.network.handlers.base.sql.util.Resolver;
import net.omniblock.network.handlers.base.sql.util.SQLResultSet;

public class SurvivalBankBase {

	public static int getMoney(Player player) {
		return getMoney(player.getName(), false);
	}

	public static int getMoney(String name) {
		return getMoney(name, false);
	}
	
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
	
	public static void setMoney(Player player, int quantity) {

		setMoney(player.getName(), quantity, false);
		return;

	}

	public static void setMoney(String name, int quantity) {

		setMoney(name, quantity, false);
		return;

	}
	
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
	
	public static void addMoney(Player player, int quantity) {

		addMoney(player.getName(), quantity, false);
		return;

	}

	public static void addMoney(String name, int quantity) {

		addMoney(name, quantity, false);
		return;

	}
	
	public static void addMoney(String param, int quantity, boolean isNetworkID) {

		int money = getMoney(param, isNetworkID);
		setMoney(param, quantity + money, isNetworkID);
		return;

	}
	
	public static void removeMoney(Player player, int quantity) {

		removeMoney(player.getName(), quantity, false);
		return;

	}

	public static void removeMoney(String name, int quantity) {

		removeMoney(name, quantity, false);
		return;

	}
	
	public static void removeMoney(String param, int quantity, boolean isNetworkID) {

		int money = getMoney(param, isNetworkID);
		setMoney(param, (money - quantity < 0) ? 0 : money - quantity, isNetworkID);
		return;

	}
	
}
