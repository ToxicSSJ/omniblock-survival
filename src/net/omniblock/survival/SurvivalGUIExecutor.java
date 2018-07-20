package net.omniblock.survival;

import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.utils.TextUtil;
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
			new ItemGUI(new ItemBuilder(Material.DOUBLE_PLANT)
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
			new ItemGUI(new ItemBuilder(Material.SKULL_ITEM)
					.durability((short) 3)
					.name(TextUtil.format("&2tpa"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Utiliza este comando"))
					.lore(TextUtil.format("&7para teletransportarte"))
					.lore(TextUtil.format("&7hacia un jugador."))
					.lore(TextUtil.format(""))
					.lore(TextUtil.format("&7úsalo así: "))
					.lore(TextUtil.format(" &7&e- &a/tpa <jugador>"))

					.build(), (Click, player) -> {

					player.sendMessage(TextUtil.format("&7Utiliza &e/tpa <jugador>"));
					player.closeInventory();
					return;
			}),
			new ItemGUI(new ItemBuilder(Material.ENDER_PEARL)
					.durability((short) 3)
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


