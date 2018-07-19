package net.omniblock.survival;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.survival.base.SurvivalBankBase;
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

						Player cache = SurvivalPlugin.getInstance().getServer().getPlayer(playerCache);

						if (cache != null)
							if (cache.isOnline()) {

								SurvivalBankBase.removeMoney(player, moneyCache);
								SurvivalBankBase.addMoney(playerCache, moneyCache);

								player.sendMessage(TextUtil.format("&7Le diste &a" + moneyCache + "⛃ &7a " + playerCache));
								return true;
							}


					}catch (Exception e){

						player.sendMessage(TextUtil.format("&7/pay &a<jugador> <dinero>"));
						return true;

					}

					}

					player.sendMessage(TextUtil.format("&7/pay &a<jugador> <dinero>"));
					return true;
				}

			if(cmd.getName().equalsIgnoreCase("fly"))
				if(player.hasPermission("omniblock.network.moderator")){
					if(player.isFlying()){

						player.setAllowFlight(false);
						player.setFlying(false);
						player.sendMessage(TextUtil.format("&7Desactivaste el fly"));

					}else{

						player.setAllowFlight(true);
						player.setFlying(true);
						player.sendMessage(TextUtil.format("&7Activaste el fly"));
					}


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
