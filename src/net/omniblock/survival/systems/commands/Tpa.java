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

import javax.xml.soap.Text;
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

    private static Map<Player, Player> requestedPlayers = new HashMap<>();

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
                String toPlayerName = args[0];

                for(Player toPlayer : Bukkit.getServer().getOnlinePlayers()){
                    if(toPlayer.getName().toLowerCase().contains(toPlayerName.toLowerCase())){

                        if(toPlayer.getName().equals(player.getName())){
                            player.sendMessage(TextUtil.format("&cNo puedes enviarte peticiones a ti mismo."));
                            return true;
                        }

                        tpRequest(toPlayer, player);
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
            if (!requestedPlayers.containsKey(player)) {
                player.sendMessage(TextUtil.format("&eActualmente no tienes ninguna solicitud de teletransporte."));
                return true;
            }

            Player requestedPlayer = requestedPlayers.get(player);

            if(!args[0].equals(requestedPlayer.getName())){
                player.sendMessage(TextUtil.format("&cLa petición de este usuario ya expiró."));
                return true;
            }

            /*
             Inicia el teletransporte
              */
            requestedPlayers.remove(player);

            player.sendMessage(TextUtil.format("&aSolicitud aceptada. " + requestedPlayer.getName() +
                    " será teletransportado en &e3&a segundos."));

            requestedPlayer.sendMessage(new String[]{
                    TextUtil.format("&e"+player.getName()+"&a ha aceptado tu petición."),
                    TextUtil.format("&aSerás teletransportado en &e3&a segundos. ¡No te muevas!")
            });

            new BukkitRunnable() {
                int god = 6;
                int seconds = 3 + god; // + 15 segundos de invulnerabilidad
                Location firstLoc = requestedPlayer.getLocation().clone();

                @Override
                public void run() {

                    if(god <= seconds && seconds <= 3 + god){

                        if(!requestedPlayer.isOnline()){
                            player.sendMessage(TextUtil.format("&e"+requestedPlayer.getName() +
                                    "&c se ha desconectado, teletransportación cancelada."));
                            cancel();
                            return;
                        }

                        if(!player.isOnline()){
                            player.sendMessage(TextUtil.format("&e"+requestedPlayer.getName() +
                                    "&c se ha desconectado, la teletransportación ha sido cancelada."));
                            cancel();
                            return;
                        }

                        if(seconds <= 2 + god &&
                                (firstLoc.getX() != requestedPlayer.getLocation().getX() ||
                                        firstLoc.getY() != requestedPlayer.getLocation().getY() ||
                                        firstLoc.getZ() != requestedPlayer.getLocation().getZ())){

                            requestedPlayer.sendMessage(TextUtil.format("&c¡Te has movido! Teletransporte cancelado."));
                            player.sendMessage(TextUtil.format("&c"+requestedPlayer.getName() +
                                    "&e se ha movido durante el teletransporte y se ha cancelado."));
                            cancel();
                            return;
                        }

                        if(seconds==1 + god)
                            requestedPlayer.sendMessage(TextUtil.format("&eTeletransportando..."));

                        if(seconds <= god){
                            Back.addPlayerLocation(requestedPlayer);
                            requestedPlayer.teleport(player);

                            if(!God.GODS.contains(requestedPlayer)){
                                requestedPlayer.sendMessage(TextUtil.format("&eTienes &a"+god+" &esegundos de inmunidad."));
                                God.GODS.add(requestedPlayer);
                            }else{
                                cancel();
                                return;
                            }
                        }
                    }

                    if(seconds <= 0 && God.GODS.contains(requestedPlayer)){
                        God.GODS.remove(requestedPlayer);
                        requestedPlayer.sendMessage(TextUtil.format("&eTu inmunidad ha acabado. ¡Ten cuidado!"));

                        cancel();
                    }

                    seconds--;
                }
            }.runTaskTimer(SurvivalPlugin.getInstance(), 0, 20);
            return true;
        }

        /*
        TPDENY Command
         */
        if(cmd.getName().equalsIgnoreCase("tpdeny")){
            if (!requestedPlayers.containsKey(player)) {
                player.sendMessage(TextUtil.format("&eActualmente no tienes ninguna solicitud de teletransporte."));
                return true;
            }

            if(!args[0].equals(requestedPlayers.get(player).getName())){
                player.sendMessage(TextUtil.format("&cLa petición de este usuario ya está denegadao expirada."));
                return true;
            }

            requestedPlayers.remove(player);
            player.sendMessage(TextUtil.format("&eHas denegado la petición satisfactoriamente."));

            return true;
        }

        return false;
    }

	/**
	 * Metodo para gestionar las peticiones
	 *      * de la función /tpa
	 *
	 * @param toPlayer Jugador al que se le pide el teletransporte
	 * @param player Jugador que pide teletransporte
	 */
	private void tpRequest(Player toPlayer, Player player) {

        requestedPlayers.put(toPlayer, player);

        TextComponent message = new TextComponent("                ");

        TextComponent accept = new TextComponent(TextUtil.format("&a&lAceptar"));
        TextComponent deny = new TextComponent(TextUtil.format("&c&lDenegar"));

        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&a&l✔  &7Aceptar la petición de &6&l" + player.getName())).create()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&c&l✖  &7Denegar la petición de &6&l" + player.getName())).create()));

        accept.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tpaccept " + player.getName() ));
        deny.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tpdeny " + player.getName() ));

        message.addExtra(accept);
        message.addExtra(TextUtil.format("     &8&l|&r     "));
        message.addExtra(deny);

        toPlayer.sendMessage("");
        toPlayer.sendMessage(TextUtil.format(" &8» &eEl jugador &6" + player.getName() + " &e ha solicitado teletransportarse contigo."));
        toPlayer.spigot().sendMessage(message);
        toPlayer.sendMessage("");

        /*
        toPlayer.sendMessage(new String[]{
                "",
                TextUtil.format("&r&e&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"),
                TextUtil.format("&eEl jugador &a" + player.getName() + "&e ha solicitado teletransportarse contigo."),
                "",
        });
        toPlayer.spigot().sendMessage(
                new ComponentBuilder("Para responder clickea")
                        .color(ChatColor.YELLOW)
                        .append(" ACEPTAR")
                        .color(ChatColor.GREEN)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept "+player.getName()))
                        .append(" o")
                        .color(ChatColor.YELLOW)
                        .append(" DENEGAR")
                        .color(ChatColor.RED)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny "+player.getName()))
                        .create()
        );
        toPlayer.sendMessage(TextUtil.format("&r&e&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        */

        new BukkitRunnable() {
            int seconds = 40;

            @Override
            public void run() {

                if(!requestedPlayers.containsKey(toPlayer)) {
                    cancel();
                    return;
                }

                //Si el jugador que realiza la petición cambia
                if(!requestedPlayers.get(toPlayer).getName().equals(player.getName())){
                    cancel();
                    return;
                }

                //Si algun jugador se desconecta o el conteo llega a 0
                if(!(player.isOnline() && toPlayer.isOnline())
                        || seconds <= 0){

                    requestedPlayers.remove(toPlayer);
                    player.sendMessage(TextUtil.format("&cTu petición a &e"+toPlayer.getName()+"&c ha expirado."));
                    toPlayer.sendMessage(TextUtil.format("&eLa petición de &a"+player.getName()+"&e ha expirado."));
                    cancel();
                    return;
                }

                seconds--;
            }
        }.runTaskTimer(SurvivalPlugin.getInstance(), 0, 20);
    }
}
