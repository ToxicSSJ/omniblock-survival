package net.omniblock.survival.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.CommandPatcher;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 *
 * En esta clase hay metodos estaticos
 * que podrían servir para ayudar a los
 * usuarios comprender los comandos.
 *
 * @author SoZyk
 *
 */
public class HelpUtil {

	/**
	 * Mensaje default para la ayuda del comando
	 */
	private static String[] utilidadesTop = new String[]{
			"",
			CommandPatcher.BAR,
			TextUtil.getCenteredMessage(" &b&lUtilidades » &7¡Te ha fallado algún argumento!"),
			TextUtil.getCenteredMessage(" &7Rercuerda que todos los datos deben estár puestos."),
			TextUtil.getCenteredMessage(" &7El formato actual del comando es el siguiente:"),
	};

	/**
	 * Envia un mensaje con formato de ayuda
	 * sobre un comando a un jugador.
	 *
	 * @param player Jugador que se le envía el mensaje.
	 * @param format Formato del comando correcto.
	 */
	public static void cmdFormat(Player player, String format){

		player.sendMessage(utilidadesTop);
		player.spigot().sendMessage(cmdFormatComponent(format));
		player.sendMessage(new String[]{
			CommandPatcher.BAR,
					""
		});
	}

	/**
	 * Envia un mensaje con formato de ayuda
	 * sobre un comando a un jugador.
	 *
	 * @param player Jugador que se le envía el mensaje.
	 * @param format Formato para usar el comando.
	 * @param example Ejemplo de uso del comando.
	 *                %player% para reemplazar por un nombre al azar.
	 */
	public static void cmdFormat(Player player, String format, String example){

		player.sendMessage(utilidadesTop);
		player.spigot().sendMessage(cmdFormatComponent(format));
		player.sendMessage(new String[]{
				TextUtil.getCenteredMessage(" &bEjemplo:  &7"+example.replace("%player%", Names.randomName())),
				CommandPatcher.BAR,
				""
		});
	}



	private static TextComponent cmdFormatComponent(String format){
		StringBuilder blankSpaces = new StringBuilder();
		int spaces = TextUtil.getCenteredMessage(format).length() / 2;
		for(int i = 1; i < spaces; i++)
			blankSpaces.append(" ");

		TextComponent message = new TextComponent(blankSpaces.toString());

		TextComponent cmd = new TextComponent(TextUtil.format("&e&l"+format));
		cmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(TextUtil.format("&bHaz click para copiar el comando. :)")).create()));
		cmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, format));

		message.addExtra(TextUtil.format("&bFormato: "));
		message.addExtra(cmd);

		return message;
	}



	/**
	 * Enum con nombres de usuario al azar
	 * para cualquier situación
	 */
	public enum Names{
		Steve,
		Alex,
		Notch,
		Herobrine,
		Edgron,
		ToxicNether,
		DCiancestraL,
		Boogst,
		Wirlie,
		SoZyk,
		Pedro,
		BobConstructor,
		JuanCarpintero,
		KamiKaze,
		Goku,
		Rodri;

		public static String randomName(){
			return values()[new Random().nextInt(values().length)].toString();
		}
	}
}

