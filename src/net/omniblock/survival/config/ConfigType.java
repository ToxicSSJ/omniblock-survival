package net.omniblock.survival.config;

import org.bukkit.configuration.file.FileConfiguration;

import net.omniblock.network.library.addons.configaddon.object.Config;
import net.omniblock.survival.SurvivalPlugin;

/**
 * 
 * Clase de tipo enumerador, encargada 
 * de crear el archivo config.yml y 
 * acceder a sus funciones.
 * 
 * @author Luis Villegas
 *
 */
public enum ConfigType {

		 CONFIG(new Config(SurvivalPlugin.getInstance(), "data/config.yml")),

	;

	private Config config;

	ConfigType(Config config) {
		this.config = config;
	}

	public Config getConfigObject() {
		return config;
	}

	public FileConfiguration getConfig() {
		return config.getConfigFile();
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
}
