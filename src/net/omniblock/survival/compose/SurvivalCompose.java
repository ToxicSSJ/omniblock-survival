package net.omniblock.survival.compose;

import org.bukkit.Location;
import org.bukkit.World;

public interface SurvivalCompose {

	static final SurvivalLocations LOCS = new SurvivalLocations();
	static final SurvivalWorlds WORLDS = new SurvivalWorlds(); 
	
	public abstract void load();
	
	public static World getWorld(String id) {
		return WORLDS.getWorld(id);
	}
	
	public static Location getLocation(String id) {
		return LOCS.getLocation(id);
	}
	
	public static void compose() {
		
		WORLDS.load();
		LOCS.load();
		
	}
	
}
