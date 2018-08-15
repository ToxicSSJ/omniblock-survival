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

package net.omniblock.survival;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.shop.systems.MysteryBoxHandler;
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
		if(!ConfigType.CONFIG.getConfig().isSet("hub")) {

			try {

				ConfigType.CONFIG.getConfig().set("hub", "lobby1");
				ConfigType.CONFIG.getConfigObject().save();

			} catch (Exception e) { e.printStackTrace(); }
		}

		RegisterGUI.registerGUI(new SurvivalGUIExecutor());

		SurvivalExecutor executor = new SurvivalExecutor();
        String[] commands = new String[]{
				"dinero",
				"money",
				"spawn",
				"fly",
				"pay",
				"ayuda",
				"help",
				"stoggle",
				"st",
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
