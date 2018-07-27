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

		Handlers.LOGGER.sendModuleMessage("Survival", "Se ha inicializado Survival correctamente!");


	}

	@Override
	public void onDisable(){


	}
	
	public static SurvivalPlugin getInstance() {
		return instance;
	}
	
}
