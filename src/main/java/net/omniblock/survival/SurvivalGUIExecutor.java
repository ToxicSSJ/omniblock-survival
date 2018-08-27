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

import net.omniblock.dep.essentialsutils.ItemBuilder;
import net.omniblock.dep.essentialsutils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.survival.systems.commands.gui.GuiExecutor;
import net.omniblock.survival.systems.commands.gui.ItemGUI;
import org.bukkit.Material;

public class SurvivalGUIExecutor implements GuiExecutor {

	@Override
	public ItemGUI[] onCreate() {
		return new ItemGUI[]{
			new ItemGUI(new ItemBuilder(Material.COMPASS)
					.name(TextUtil.format("&2Lobby"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
					.lore(TextUtil.format("&7Para volver al lobby principal."))
					.lore(TextUtil.format(""))
					.lore(TextUtil.format("&7úsalo así: "))
					.lore(TextUtil.format(" &7&e- &a/lobby"))
					.lore(TextUtil.format(" &7&e- &a/hub"))
					.build(), (Click, player) -> {

				Packets.STREAMER.streamPacket(new PlayerSendToServerPacket().setPlayername(player.getName())
						.setServertype(ServerType.MAIN_LOBBY_SERVER)
						.setParty(false)
						.build().setReceiver(PacketSenderType.OMNICORE));

				player.closeInventory();
				return;
			}),
			new ItemGUI(new ItemBuilder(Material.WHEAT)
					.name(TextUtil.format("&2Spawn"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
					.lore(TextUtil.format("&7Para volver a la ciudad principal."))
					.lore(TextUtil.format(""))
					.lore(TextUtil.format("&7úsalo así: "))
					.lore(TextUtil.format(" &7&e- &a/spawn"))
					.build(), (Click, player) -> {

				player.teleport(SurvivalManager.getLocation());
				player.closeInventory();
				return;

			}),
			new ItemGUI(new ItemBuilder(Material.SUNFLOWER)
					.name(TextUtil.format("&2Dinero"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
					.lore(TextUtil.format("&7para ver el dinero que has"))
					.lore(TextUtil.format("&7conseguido durante toda la partida"))
					.lore(TextUtil.format(""))
					.lore(TextUtil.format("&7úsalo así: "))
					.lore(TextUtil.format(" &7&e- &a/Dinero"))
					.build(), (Click, player) -> {

				player.sendMessage(TextUtil.format("&7Utiliza &e/Dinero"));
				player.closeInventory();
				return;

			}),
			new ItemGUI(new ItemBuilder(Material.GOLD_INGOT)
					.name(TextUtil.format("&2Pay"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
					.lore(TextUtil.format("&7para dar dinero a aquellos"))
					.lore(TextUtil.format("&7que tu decidas."))
					.lore(TextUtil.format(""))
					.lore(TextUtil.format("&7úsalo así: "))
					.lore(TextUtil.format(" &7&e- &a/pay <jugador> <dinero>"))
					.build(), (Click, player) -> {

				player.sendMessage(TextUtil.format("&7Utiliza &e/pay <jugador> <dinero>"));
				player.closeInventory();
				return;

			}),
				new ItemGUI(new ItemBuilder(Material.DIAMOND_SWORD)
						.name(TextUtil.format("&2PvP"))
						.lore("")
						.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
						.lore(TextUtil.format("&7para activar o desactivar"))
						.lore(TextUtil.format("&7tu estado de pvp."))
						.lore(TextUtil.format(""))
						.lore(TextUtil.format("&7úsalo así: "))
						.lore(TextUtil.format(" &7&e- &a/pvp [on|off]"))
						.build(), (Click, player) -> {

					player.sendMessage(TextUtil.format("&7Utiliza &e/pvp [on|off]"));
					player.closeInventory();
					return;

				}),
			new ItemGUI(new ItemBuilder(Material.PLAYER_HEAD)
					.name(TextUtil.format("&2tpa"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
					.lore(TextUtil.format("&7para teletransportarte"))
					.lore(TextUtil.format("&7hacia un jugador o un"))
					.lore(TextUtil.format("&7jugador hacia ti."))
					.lore(TextUtil.format(""))
					.lore(TextUtil.format("&7úsalo así: "))
					.lore(TextUtil.format(" &7&e- &a/tpa <jugador>"))
					.lore(TextUtil.format(" &7&e- &a/tpahere <jugador>"))

					.build(), (Click, player) -> {

					player.sendMessage(TextUtil.format("&7Utiliza &e/tpa <jugador>"));
					player.closeInventory();
					return;
			}),
			new ItemGUI(new ItemBuilder(Material.ENDER_PEARL)
					.name(TextUtil.format("&2Back"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
					.lore(TextUtil.format("&7para volver a tu posición"))
					.lore(TextUtil.format("&7anterior."))
					.lore(TextUtil.format(""))
					.lore(TextUtil.format("&7úsalo así: "))
					.lore(TextUtil.format(" &7&e- &a/back"))

					.build(), (Click, player) -> {

				player.sendMessage(TextUtil.format("&7Utiliza &e/back"));
				player.closeInventory();
				return;

			}),
		};
	}
}


