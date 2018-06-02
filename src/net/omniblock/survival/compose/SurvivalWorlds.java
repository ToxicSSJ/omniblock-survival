package net.omniblock.survival.compose;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import net.omniblock.survival.SurvivalPlugin;

public class SurvivalWorlds implements SurvivalCompose {

	final Map<String, World> worlds = new HashMap<String, World>();
	
	@Override
	public void load() {
		
		worlds.put("ciudad", SurvivalPlugin.getInstance().getServer().getWorld("PlainsTwo"));
		
	}
	
	public World getWorld(String id) {
		return worlds.get(id);
	}
	
}
