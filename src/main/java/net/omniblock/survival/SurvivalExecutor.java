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

import net.omniblock.modtools.api.SpigotVanishAPI;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.survival.base.SurvivalBankBase;
import net.omniblock.survival.config.ConfigType;
import net.omniblock.survival.systems.commands.Back;
import net.omniblock.survival.systems.commands.gui.InventoryGUI;
import net.omniblock.survival.utils.HelpUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.DataOutputStream;
import java.io.IOException;

public class SurvivalExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


		if(sender instanceof Player){

			Player player = ((Player) sender).getPlayer();

			if(cmd.getName().equalsIgnoreCase("money") ||
					cmd.getName().equalsIgnoreCase("dinero")){

				player.sendMessage(TextUtil.format("&7Dinero: &a$" + SurvivalBankBase.getMoney(player)));
				return true;

			}

			if(cmd.getName().equalsIgnoreCase("help") ||
					cmd.getName().equalsIgnoreCase("ayuda")){

				InventoryGUI gui = InventoryGUI.InventoryHelp();
				gui.openShop(player);

				return true;
			}

			if(cmd.getName().equalsIgnoreCase("pay")){
				if(args.length >= 2) {

					try{

						int senderMoney = SurvivalBankBase.getMoney(player);

						String playerCache = args[0];
						int moneyCache = Integer.parseInt(args[1]);

						if (senderMoney < moneyCache) {
							player.sendMessage(TextUtil.format("&7Te hacen falta &c$" + (senderMoney - moneyCache) + " &7para poder pagar."));
							return true;

						}

						for(Player toPlayer : Bukkit.getServer().getOnlinePlayers()){
							if(toPlayer.getName().toLowerCase().contains(playerCache.toLowerCase())){

								if(toPlayer.getName().equals(player.getName())){
									player.sendMessage(TextUtil.format("&cNo puedes darte dinero a ti mismo."));
									return true;
								}

								if(SpigotVanishAPI.getVanishedPlayers().contains(toPlayer)) continue;

								SurvivalBankBase.removeMoney(player, moneyCache);
								SurvivalBankBase.addMoney(toPlayer, moneyCache);
								player.sendMessage(TextUtil.format("&7Le diste &a" + moneyCache + "⛃ &7a &a" + toPlayer.getName()));
								toPlayer.sendMessage(TextUtil.format("&7Has recibido &a" + moneyCache + "⛃ &7de &a" + player.getName()));
								return true;

							}
						}

						player.sendMessage(TextUtil.format("&7El jugador no es valido."));
						return true;

					}catch (Exception ignored){}
				}

				HelpUtil.cmdFormat(player, "/pay <jugador> <dinero>", "/pay %player% 500");
				return true;
			}

			/*
			if(cmd.getName().equalsIgnoreCase("hub") ||
					cmd.getName().equalsIgnoreCase("lobby")){

				String lobby = ConfigType.CONFIG.getConfig().getString("hub");

				player.sendMessage(TextUtil.format("&b&lOmniblock :: &7Conectando a Lobby"));
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				try {
					out.writeUTF("Connect");
					out.writeUTF(lobby);
				} catch (IOException eee) {
					Bukkit.getLogger().info("Error al conectar con lobby1");
				}
				player.sendPluginMessage(SurvivalPlugin.getInstance(), "BungeeCord", b.toByteArray());


				return true;

			}
			*/

			if(cmd.getName().equalsIgnoreCase("spawn")){

				player.sendMessage(TextUtil.format("&eSerás teletransportado en 3 segundos. ¡No te muevas!"));

				new BukkitRunnable() {
					int seconds = 3;
					Location loc = player.getLocation().clone();

					@Override
					public void run() {

						if(!player.isOnline()){
							cancel();
							return;
						}

						if(seconds < 3 &&
								(loc.getX() != player.getLocation().getX() ||
										loc.getY() != player.getLocation().getY() ||
										loc.getZ() != player.getLocation().getZ())){

							player.sendMessage(TextUtil.format("&c¡Te has movido! Teletransporte cancelado."));
							cancel();
							return;
						}

						if(seconds==1)player.sendMessage(TextUtil.format("&eTeletransportando..."));

						if(seconds <= 0){

							Back.addPlayerLocation(player);
							player.teleport(SurvivalManager.getLocation());

							cancel();
							return;
						}

						seconds--;
					}
				}.runTaskTimer(SurvivalPlugin.getInstance(), 0, 20);

				return true;

			}

		}

		return false;
	}
}
