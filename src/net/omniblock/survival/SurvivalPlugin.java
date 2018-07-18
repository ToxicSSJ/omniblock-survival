package net.omniblock.survival;

import net.omniblock.survival.systems.SurvivalCommands;
import net.omniblock.survival.systems.SurvivalListener;
import org.bukkit.plugin.java.JavaPlugin;

import net.omniblock.network.handlers.Handlers;
import net.omniblock.network.handlers.network.NetworkManager;
import net.omniblock.packets.object.external.ServerType;

public class SurvivalPlugin extends JavaPlugin {

	private static SurvivalPlugin instance;

	@Override
	public void onEnable() {
		
		instance = this;
		
		if(NetworkManager.getServertype() != ServerType.SURVIVAL) {
			
			Handlers.LOGGER.sendModuleInfo("&7Se ha registrado Survival v" + this.getDescription().getVersion() + "!");
			Handlers.LOGGER.sendModuleMessage("Survival", "Se ha inicializado Survival en modo API!");
			return;
			
		}
		
		Handlers.LOGGER.sendModuleInfo("&7Se ha registrado Survival v" + this.getDescription().getVersion() + "!");

		SurvivalListener.listen();
		SurvivalManager.setup();
		SurvivalCommands.commands();

		Handlers.LOGGER.sendModuleMessage("Survival", "Se ha inicializado Survival correctamente!");


	}

	@Override
	public void onDisable(){


	}
	
	public static SurvivalPlugin getInstance() {
		return instance;
	}
	
}
