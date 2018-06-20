package net.omniblock.survival.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class WorldUtils {

	public static World getWorld(String name) {

		for (World world : Bukkit.getWorlds()) {
			if (world.getName() == name)
				return world;
		}
		
		return null;
	}
	
	public static File getWorldFolder(World world){
		
		String path = Bukkit.getWorldContainer().getAbsolutePath() + world.getName();
		File folder = new File(path);
		
		return folder;
	}
	
	public static Location getLocation(String world, int x, int y , int z) {
		return new Location(getWorld(world), x + 0.5, y, z + 0.5);
	}
	
	public static Location getLocation(String world, int x, int y , int z, int yaw, int pitch) {
		return new Location(getWorld(world), x + 0.5, y, z + 0.5, yaw, pitch);
	}
}
