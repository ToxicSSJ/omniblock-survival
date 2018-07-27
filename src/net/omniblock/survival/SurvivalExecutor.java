package net.omniblock.survival;

import net.omniblock.modtools.api.SpigotVanishAPI;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.survival.base.SurvivalBankBase;
import net.omniblock.survival.board.SurvivalScoreBoard;
import net.omniblock.survival.systems.commands.Back;
import net.omniblock.survival.systems.commands.gui.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

			if(cmd.getName().equalsIgnoreCase("stoggle") ||
					cmd.getName().equalsIgnoreCase("st")){

				if(args.length > 0){

					if(args[0].equalsIgnoreCase("scoreboard") ||
							args[0].equalsIgnoreCase("sb")){

						if(SurvivalScoreBoard.blackList.contains(player)){

							SurvivalScoreBoard.blackList.remove(player);
							player.sendMessage(TextUtil.format("&aScoreboard activada."));
						}
						else {
							SurvivalScoreBoard.blackList.add(player);
							player.sendMessage(TextUtil.format("&aScoreboard desactivada."));
						}

						return true;
					}

					if(args[0].equalsIgnoreCase("bar") ||
							args[0].equalsIgnoreCase("bossbar")){

						if(SurvivalManager.bar.getPlayers().contains(player)) {

							SurvivalManager.bar.removePlayer(player);
							player.sendMessage(TextUtil.format("&aBossbar desactivada."));
						}
						else {
							SurvivalManager.bar.addPlayer(player);
							player.sendMessage(TextUtil.format("&aBossbar activada."));
						}

						return true;
					}
				}


				player.sendMessage(TextUtil.format("&7/stoogle &e<scoreboard | bar>"));
				return true;
			}
		}

		return false;
	}
}
