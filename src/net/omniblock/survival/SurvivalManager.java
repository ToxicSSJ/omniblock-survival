package net.omniblock.survival;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.shop.systems.MysteryBoxHandler;
import net.omniblock.survival.board.SurvivalScoreBoard;
import net.omniblock.survival.systems.SurvivalBox;
import net.omniblock.survival.systems.commands.Back;
import net.omniblock.survival.systems.commands.Tpa;
import net.omniblock.survival.systems.commands.gui.RegisterGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import net.omniblock.network.library.utils.LocationUtils;
import net.omniblock.survival.config.ConfigType;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

/**
 * 
 * Con esta clase se administra los sistemas 
 * del survival. 
 * 
 * 
 * @author Luis Villegas
 *
 */
public class SurvivalManager {

	/**
	 * 
	 * 
	 * Mundo predeterminado del survival.
	 * 
	 * 
	 */
	protected static World world;
	
	/**
	 * 
	 * 
	 * Localizacion predeterminada del survival.
	 * 
	 * 
	 */
	protected static Location location;

	/**
	 *
	 * Sistema de caja misteriosa
	 *
	 *
	 *
	 */
	protected static SurvivalBox survivalBox;

	/**
	 *
	 *
	 * Bossbar para survival
	 *
	 *
	 */
	public static BossBar bar;
	
	/**
	 * 
	 * 
	 * Metodo estatico para iniciar los sistemas
	 * del survival.
	 * 
	 * 
	 */
	public static void setup() {
		
		if(ConfigType.CONFIG.getConfig().isSet("survival")) {
			
			try {
				
				Location loc = LocationUtils.deserializeLocation(ConfigType.CONFIG.getConfig().getString("survival." +  "city" + ".location"));
				loc.setPitch(-3);
				loc.setYaw(180);

				location = loc;
				world = location.getWorld();
				
			} catch (Exception e) { e.printStackTrace(); }
		}


		RegisterGUI.registerGUI(new SurvivalGUIExecutor());

		SurvivalExecutor executor = new SurvivalExecutor();
        String[] commands = new String[]{
				"dinero",
				"money",
				"spawn",
				"lobby",
				"hub",
				"fly",
				"pay",
				"ayuda",
				"help",
				"stoggle",
				"st"
		};
        String[] tpaCommands = new String[]{
                "tpa",
                "tpAccept",
                "tpDeny",
				"tpaHere",
				"tphAccept",
				"tphDeny"
        };

        for (String command : commands)
			SurvivalPlugin.getInstance().getCommand(command).setExecutor(new SurvivalExecutor());

		for(String tpaCommand : tpaCommands)
            SurvivalPlugin.getInstance().getCommand(tpaCommand).setExecutor(new Tpa());

        SurvivalPlugin.getInstance().getCommand("back").setExecutor(new Back());

		survivalBox = new SurvivalBox();
		MysteryBoxHandler.register(survivalBox);

        Back.saveLocations();

		SurvivalScoreBoard.initialize();

		bar = Bukkit.createBossBar(TextUtil.format("&b&lOmniblock Network &8« &aSurvival &8»"),
				BarColor.BLUE, BarStyle.SOLID, BarFlag.DARKEN_SKY);
	}
	
	/**
	 * 
	 * Metodo estatico para coger el mundo
	 * registrado en el config.yml
	 * 
	 * 
	 * @return Mundo del survival.
	 */
	public static World getWorld() {
		return world;
	}
	
	/**
	 * 
	 * Metodo estatico para coger la localizacion
	 * registrada en el config.yml
	 * 
	 * 
	 * @return Localizacion del survival.
	 */
	public static Location getLocation() {
		return location;
	}

}
