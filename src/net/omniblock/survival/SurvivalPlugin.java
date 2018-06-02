package net.omniblock.survival;

import org.bukkit.plugin.java.JavaPlugin;

import net.omniblock.network.handlers.Handlers;
import net.omniblock.network.handlers.network.NetworkManager;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.survival.compose.SurvivalCompose;

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
		
		SurvivalCompose.compose();
		SurvivalListener.listen();
		
		Handlers.LOGGER.sendModuleMessage("Survival", "Se ha inicializado Survival correctamente!");
		
	}
	
	public static SurvivalPlugin getInstance() {
		return instance;
	}
	
}
