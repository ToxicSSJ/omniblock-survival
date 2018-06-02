package net.omniblock.survival.compose;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class SurvivalLocations implements SurvivalCompose {

	final Map<String, Location> locations = new HashMap<String, Location>();
	
	@Override
	public void load() {
		
		locations.put("spawn", getLocation(SurvivalCompose.WORLDS.getWorld("ciudad"),
										   -67.5,
										   63,
										   151.5,
										   180,
										   0));
		
	}
	
	public Location getLocation(String id) {
		return locations.get(id);
	}
	
	public Location getLocation(World world, double x, double y, double z) {
		return new Location(world, x, y, z);
	}

	public Location getLocation(World world, double x, double y, double z, float pitch, float yaw) {
		return new Location(world, x, y, z, pitch, yaw);
	}
	
}
