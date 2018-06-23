package net.omniblock.survival;

import org.bukkit.Location;
import org.bukkit.World;

import net.omniblock.network.library.utils.LocationUtils;
import net.omniblock.survival.config.ConfigType;

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
	 * 
	 * Metodo estático para iniciar los sistemas
	 * del survival.
	 * 
	 * 
	 */
	public static void setup() {
		
		if(ConfigType.CONFIG.getConfig().isSet("survival")) {
			
			try {
				
				location = LocationUtils.deserializeLocation(ConfigType.CONFIG.getConfig().getString("survival." +  "city" + ".location"));
				world = location.getWorld();
				
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * 
	 * Metodo estático para coger el mundo
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
	 * Metodo estático para coger la localizacion
	 * registrada en el config.yml
	 * 
	 * 
	 * @return Localizacion del survival.
	 */
	public static Location getLocation() {
		return location;
	}
}
