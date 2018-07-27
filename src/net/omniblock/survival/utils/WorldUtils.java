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
