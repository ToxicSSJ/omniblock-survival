package net.omniblock.survival;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.shop.systems.MysteryBoxHandler;
import net.omniblock.survival.base.SurvivalBankBase;
import net.omniblock.survival.systems.SurvivalBox;
import net.omniblock.survival.systems.commands.Back;
import net.omniblock.survival.systems.commands.Tpa;
import org.bukkit.Location;
import org.bukkit.World;

import net.omniblock.network.library.utils.LocationUtils;
import net.omniblock.survival.config.ConfigType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * Con esta clase se administra los sistemas 
 * del survival. 
 * 
 * 
 * @author Luis Villegas
 *
 */
public class SurvivalManager {

	/**
	 * 
	 * 
	 * Mundo predeterminado del survival.
	 * 
	 * 
	 */
	protected static World world;
	
	/**
	 * 
	 * 
	 * Localizacion predeterminada del survival.
	 * 
	 * 
	 */
	protected static Location location;

	/**
	 *
	 * Sistema de caja misteriosa
	 *
	 *
	 *
	 */
	protected static SurvivalBox survivalBox;
	
	/**
	 * 
	 * 
	 * Metodo estatico para iniciar los sistemas
	 * del survival.
	 * 
	 * 
	 */
	public static void setup() {
		
		if(ConfigType.CONFIG.getConfig().isSet("survival")) {
			
			try {
				
				Location loc = LocationUtils.deserializeLocation(ConfigType.CONFIG.getConfig().getString("survival." +  "city" + ".location"));
				loc.setPitch(-3);
				loc.setYaw(180);

				location = loc;
				world = location.getWorld();
				
			} catch (Exception e) { e.printStackTrace(); }
		}


		SurvivalExecutor executor = new SurvivalExecutor();
        String[] commands = new String[]{
				"dinero",
				"money",
				"spawn",
				"lobby",
				"hub"
		};
        String[] tpaCommands = new String[]{
                "tpa",
                "tpaccept",
                "tpdeny"
        };

        for (String command : commands)
			SurvivalPlugin.getInstance().getCommand(command).setExecutor(new SurvivalExecutor());

		for(String tpaCommand : tpaCommands)
            SurvivalPlugin.getInstance().getCommand(tpaCommand).setExecutor(new Tpa());

        SurvivalPlugin.getInstance().getCommand("back").setExecutor(new Back());

		survivalBox = new SurvivalBox();
		MysteryBoxHandler.register(survivalBox);


	}
	
	/**
	 * 
	 * Metodo estatico para coger el mundo
	 * registrado en el config.yml
	 * 
	 * 
	 * @return Mundo del survival.
	 */
	public static World getWorld() {
		return world;
	}
	
	/**
	 * 
	 * Metodo estatico para coger la localizacion
	 * registrada en el config.yml
	 * 
	 * 
	 * @return Localizacion del survival.
	 */
	public static Location getLocation() {
		return location;
	}

	public static class SurvivalExecutor implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

			if(sender instanceof Player){

				Player player = ((Player) sender).getPlayer();

				if(cmd.getName().equalsIgnoreCase("money") ||
						cmd.getName().equalsIgnoreCase("dinero")){

					player.sendMessage(TextUtil.format("&7Dinero: &a" + SurvivalBankBase.getMoney(player) + "$"));
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

}
