package net.omniblock.survival;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.survival.base.SurvivalBankBase;
import net.omniblock.survival.systems.commands.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
								
								SurvivalBankBase.removeMoney(player, moneyCache);
								SurvivalBankBase.addMoney(toPlayer, moneyCache);
								player.sendMessage(TextUtil.format("&7Le diste &a" + moneyCache + "⛃ &7a " + playerCache));
								return true;

							}
						}

						player.sendMessage(TextUtil.format("&7El jugador no es valido."));
						return true;

					}catch (Exception e){}
				}

				player.sendMessage(TextUtil.format("&7/pay &a<jugador> <dinero>"));
				return true;
			}

			if(cmd.getName().equalsIgnoreCase("hub") ||
					cmd.getName().equalsIgnoreCase("lobby")){

				Packets.STREAMER.streamPacket(new PlayerSendToServerPacket()
						.setPlayername(player.getName())
						.setServertype(ServerType.MAIN_LOBBY_SERVER)
						.setParty(false)
						.build().setReceiver(PacketSenderType.OMNICORE));

				return true;

			}

			if(cmd.getName().equalsIgnoreCase("spawn")){

				player.teleport(SurvivalManager.getLocation());
				return true;

			}
		}

		return false;
	}
}
