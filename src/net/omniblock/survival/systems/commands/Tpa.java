package net.omniblock.survival.systems.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.survival.SurvivalPlugin;
import net.omniblock.survival.systems.events.God;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Esta clase gestiona el sistema
 * de /tpa
 *
 * @author SoZyk
 *
 */
public class Tpa implements CommandExecutor {

    /**
     * Mapa en la cual se añade al jugador que se le pide
     * el teletransporte y el jugador que hizo la petición
	 *
	 * toplayer - player
     */
    private static Map<Player, Player> TPA = new HashMap<>();

	/**
	 * Mapa en la cual se añade al jugador que se le pide
	 * el teletransporte y el jugador que hizo la petición
	 *
	 * toplayer - player
	 */
    private static Map<Player, Player> TPAHERE = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        /*
        TPA Command
         */
        if(cmd.getName().equalsIgnoreCase("tpa")){
            if(args.length == 0){
                player.sendMessage(TextUtil.format(
                        "&6Utiliza &e/tpa <jugador>&6 para enviar una petición de teletransportación a este &ejugador&6."));
                return true;
            }
            else {
                String toPlayerCache = args[0];

                for(Player toPlayer : Bukkit.getServer().getOnlinePlayers()){
                    if(toPlayer.getName().toLowerCase().contains(toPlayerCache.toLowerCase())){

                        if(toPlayer.getName().equals(player.getName())){
                            player.sendMessage(TextUtil.format("&cNo puedes enviarte peticiones a ti mismo."));
                            return true;
                        }

                        tpaRequest(player, toPlayer, true);
                        player.sendMessage(TextUtil.format(
                                "&aSe ha enviado una petición de teletransporte a &a"+toPlayer.getName()));
                        return true;
                    }
                }

                player.sendMessage(TextUtil.format(
                        "&cNo se ha encontrado a ningun usuario con este nombre."));

                return true;
            }
        }

        /*
        TPAHERE Command
         */
        if(cmd.getName().equalsIgnoreCase("tpahere")){
			if(args.length == 0){
				player.sendMessage(TextUtil.format(
						"&6Utiliza &e/tpahere <jugador>&6 para pedir a un &ejugador&6 que se teletransporte hacia ti."));
				return true;
			}
			else{
				String toPlayerCache = args[0];

				for(Player toPlayer : Bukkit.getServer().getOnlinePlayers()){
					if(toPlayer.getName().toLowerCase().contains(toPlayerCache.toLowerCase())){

						if(toPlayer.getName().equals(player.getName())){
							player.sendMessage(TextUtil.format("&cNo puedes enviarte peticiones a ti mismo."));
							return true;
						}

						tpaRequest(player, toPlayer, false);
						player.sendMessage(TextUtil.format(
								"&aSe ha enviado una petición de teletransporte a &a"+toPlayer.getName()));
						return true;
					}
				}

				player.sendMessage(TextUtil.format(
						"&cNo se ha encontrado a ningun usuario con este nombre."));

				return true;
			}
		}

        /*
        TPACEPT Command
         */
        if(cmd.getName().equalsIgnoreCase("tpaccept")) {
			if (!TPA.containsKey(player)) {
				player.sendMessage(TextUtil.format("&eActualmente no tienes ninguna solicitud de teletransporte."));
				return true;
			}

			Player requestedPlayer = TPA.get(player);

			if(!args[0].equals(requestedPlayer.getName())){
				player.sendMessage(TextUtil.format("&cLa petición de este usuario ya expiró."));
				return true;
			}

            /*
             Inicia el teletransporte
              */
			TPA.remove(player);

			player.sendMessage(new String[]{
					TextUtil.format("&aSolicitud aceptada."),
					TextUtil.format("&e" + requestedPlayer.getName() +
							"&a será teletransportado en &e3&a segundos.")
			});

			requestedPlayer.sendMessage(new String[]{
					TextUtil.format("&e"+player.getName()+"&a ha aceptado tu petición."),
					TextUtil.format("&aSerás teletransportado en &e3&a segundos. ¡No te muevas!")
			});

			teleport(requestedPlayer, player);

			return true;
		}

        /*
        TPDENY Command
         */
        if(cmd.getName().equalsIgnoreCase("tpdeny")){
            if (!TPA.containsKey(player)) {
                player.sendMessage(TextUtil.format("&eActualmente no tienes ninguna solicitud de teletransporte."));
                return true;
            }

            if(!args[0].equals(TPA.get(player).getName())){
                player.sendMessage(TextUtil.format("&cLa petición de este usuario ya está denegadao expirada."));
                return true;
            }

            TPA.remove(player);
            player.sendMessage(TextUtil.format("&eHas denegado la petición satisfactoriamente."));

            return true;
        }

        /*
        TPHACCEPT Command
         */
		if(cmd.getName().equalsIgnoreCase("tphaccept")) {
			if (!TPAHERE.containsKey(player)) {
				player.sendMessage(TextUtil.format("&eActualmente no tienes ninguna solicitud de teletransporte."));
				return true;
			}

			Player requestedPlayer = TPAHERE.get(player);

			if(!args[0].equals(requestedPlayer.getName())){
				player.sendMessage(TextUtil.format("&cLa petición de este usuario ya expiró."));
				return true;
			}

            /*
             Inicia el teletransporte
              */
			TPAHERE.remove(player);

			player.sendMessage(new String[]{
					TextUtil.format("&aSolicitud aceptada."),
					TextUtil.format("&aSerás teletransportado a &e" +requestedPlayer.getName() + "&a en &e3&a segundos.")
			});

			requestedPlayer.sendMessage(new String[]{
					TextUtil.format("&e"+player.getName()+"&a ha aceptado tu petición."),
					TextUtil.format("&aSerá teletransportado hacia ti en &e3&a segundos.")
			});

			teleport(player, requestedPlayer);

			return true;
		}

		/*
        TPHDENY Command
         */
		if(cmd.getName().equalsIgnoreCase("tphdeny")){
			if (!TPAHERE.containsKey(player)) {
				player.sendMessage(TextUtil.format("&eActualmente no tienes ninguna solicitud de teletransporte."));
				return true;
			}

			if(!args[0].equals(TPAHERE.get(player).getName())){
				player.sendMessage(TextUtil.format("&cLa petición de este usuario ya está denegadao expirada."));
				return true;
			}

			TPAHERE.remove(player);
			player.sendMessage(TextUtil.format("&eHas denegado la petición satisfactoriamente."));

			return true;
		}

        return false;
    }

	/**
	 * Metodo para gestionar las peticiones
	 *      * de la función /tpa
	 *
	 * @param player Jugador que pide teletransporte
	 * @param toPlayer Jugador al que se le pide el teletransporte
	 */
	private void tpaRequest(Player player, Player toPlayer, boolean isTpa) {

		String cAccept, cDeny, msg;

		if(isTpa){
			TPA.put(toPlayer, player);
			cAccept = "/tpAccept ";
			cDeny = "/tpDeny ";
			msg = " &8» &eEl jugador &6" + player.getName() + " &e quiere teletransportarse contigo.";
		}else{
			TPAHERE.put(toPlayer, player);
			cAccept = "/tphAccept ";
			cDeny = "/tphDeny ";
			msg = " &8» &eEl jugador &6" + player.getName() + " &e quiere que te teletransportes a él.";
		}

        TextComponent message = new TextComponent("                ");

        TextComponent accept = new TextComponent(TextUtil.format("&a&lAceptar"));
        TextComponent deny = new TextComponent(TextUtil.format("&c&lDenegar"));

        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&a&l✔  &7Aceptar la petición de &6&l" + player.getName())).create()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&c&l✖  &7Denegar la petición de &6&l" + player.getName())).create()));

        accept.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, cAccept + player.getName() ));
        deny.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, cDeny + player.getName() ));

        message.addExtra(accept);
        message.addExtra(TextUtil.format("     &8&l|&r     "));
        message.addExtra(deny);

        toPlayer.sendMessage("");
        toPlayer.sendMessage(TextUtil.format(msg));
        toPlayer.spigot().sendMessage(message);
        toPlayer.sendMessage("");


        new BukkitRunnable() {
            int seconds = 40;
            Map<Player, Player> TP;

            @Override
            public void run() {

            	if(isTpa)
            		TP = TPA;
            	else
            		TP = TPAHERE;


                if(!TP.containsKey(toPlayer)) {
                    cancel();
                    return;
                }

                //Si el jugador que realiza la petición cambia
                if(!TP.get(toPlayer).getName().equals(player.getName())){
                    cancel();
                    return;
                }

                //Si algun jugador se desconecta o el conteo llega a 0
                if(!(player.isOnline() && toPlayer.isOnline())
                        || seconds <= 0){

                    TP.remove(toPlayer);
                    player.sendMessage(TextUtil.format("&cTu petición a &e"+toPlayer.getName()+"&c ha expirado."));
                    toPlayer.sendMessage(TextUtil.format("&eLa petición de &a"+player.getName()+"&e ha expirado."));
                    cancel();
                    return;
                }

                seconds--;
            }
        }.runTaskTimer(SurvivalPlugin.getInstance(), 0, 20);
    }

	/**
	 * Metodo para gestionar las peticiones
	 *      * de la función /tpahere
	 *
	 * @param player Jugador que pide teletransporte
	 * @param toPlayer Jugador al que se le pide el teletransporte
	 */
	/*
    private void tpaHereRequest(Player player, Player toPlayer){
		TPAHERE.put(toPlayer, player);

		TextComponent message = new TextComponent("                ");

		TextComponent accept = new TextComponent(TextUtil.format("&a&lAceptar"));
		TextComponent deny = new TextComponent(TextUtil.format("&c&lDenegar"));

		accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&a&l✔  &7Aceptar la petición de &6&l" + player.getName())).create()));
		deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&c&l✖  &7Denegar la petición de &6&l" + player.getName())).create()));

		accept.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tphAccept " + player.getName() ));
		deny.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tphDeny " + player.getName() ));

		message.addExtra(accept);
		message.addExtra(TextUtil.format("     &8&l|&r     "));
		message.addExtra(deny);

		toPlayer.sendMessage("");
		toPlayer.sendMessage(TextUtil.format(" &8» &eEl jugador &6" + player.getName() + " &e quiere que te teletransportes a él."));
		toPlayer.spigot().sendMessage(message);
		toPlayer.sendMessage("");


		new BukkitRunnable() {
			int seconds = 40;

			@Override
			public void run() {

				if(!TPAHERE.containsKey(toPlayer)) {
					cancel();
					return;
				}

				//Si el jugador que realiza la petición cambia
				if(!TPAHERE.get(toPlayer).getName().equals(player.getName())){
					cancel();
					return;
				}

				//Si algun jugador se desconecta o el conteo llega a 0
				if(!(player.isOnline() && toPlayer.isOnline())
						|| seconds <= 0){

					TPAHERE.remove(toPlayer);
					player.sendMessage(TextUtil.format("&cTu petición a &e"+toPlayer.getName()+"&c ha expirado."));
					toPlayer.sendMessage(TextUtil.format("&eLa petición de &a"+player.getName()+"&e ha expirado."));
					cancel();
					return;
				}

				seconds--;
			}
		}.runTaskTimer(SurvivalPlugin.getInstance(), 0, 20);
	}
	*/

	/**
	 *
	 * @param player Jugador que se teletransportará
	 * @param toPlayer Jugador al que se teletransportará {player}
	 */
	private void teleport(Player player, Player toPlayer){

		new BukkitRunnable() {
			int god = 6;
			int seconds = 3 + god; // + 15 segundos de invulnerabilidad
			Location firstLoc = player.getLocation().clone();

			@Override
			public void run() {

				if(god <= seconds && seconds <= 3 + god){

					if(!toPlayer.isOnline()){
						player.sendMessage(TextUtil.format("&e"+toPlayer.getName() +
								"&c se ha desconectado, teletransportación cancelada."));
						cancel();
						return;
					}

					if(!player.isOnline()){
						player.sendMessage(TextUtil.format("&e"+toPlayer.getName() +
								"&c se ha desconectado, la teletransportación ha sido cancelada."));
						cancel();
						return;
					}

					if(seconds <= 2 + god &&
							(firstLoc.getX() != player.getLocation().getX() ||
									firstLoc.getY() != player.getLocation().getY() ||
									firstLoc.getZ() != player.getLocation().getZ())){

						player.sendMessage(TextUtil.format("&c¡Te has movido! Teletransporte cancelado."));
						toPlayer.sendMessage(TextUtil.format("&c"+player.getName() +
								"&e se ha movido durante el teletransporte y se ha cancelado."));
						cancel();
						return;
					}

					if(seconds==1 + god)
						player.sendMessage(TextUtil.format("&eTeletransportando..."));

					if(seconds <= god){
						Back.addPlayerLocation(player);
						player.teleport(toPlayer);

						if(!God.GODS.contains(player)){
							player.sendMessage(TextUtil.format("&eTienes &a"+god+" &esegundos de inmunidad."));
							God.GODS.add(player);
						}else{
							cancel();
							return;
						}
					}
				}

				if(seconds <= 0){
					if(God.GODS.contains(player)){
						God.GODS.remove(player);
						player.sendMessage(TextUtil.format("&eTu inmunidad ha acabado. ¡Ten cuidado!"));
					}
					cancel();
				}

				seconds--;
			}
		}.runTaskTimer(SurvivalPlugin.getInstance(), 0, 20);
	}
}
