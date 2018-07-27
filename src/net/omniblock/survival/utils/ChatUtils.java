package net.omniblock.survival.utils;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.CommandPatcher;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 *
 * En esta clase hay metodos estaticos
 * que podrían servir de ayuda en el
 * tema de los mensajes en el chat.
 *
 * @author SoZyk
 *
 */
public class ChatUtils {

	/**
	 * Envia un mensaje con formato de ayuda
	 * sobre un comando a un jugador.
	 *
	 * @param player Jugador que se le envía el mensaje.
	 * @param format Formato del comando correcto.
	 */
	public static void cmdHelpMessage(Player player, String format){
		player.sendMessage(cmdHelpMessage(format));
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
	public static void cmdHelpMessage(Player player, String format, String example){
		player.sendMessage(cmdHelpMessage(format, example));
	}

	public static String[] cmdHelpMessage(String format){
		return new String[]{
				"",
				CommandPatcher.BAR,
				TextUtil.getCenteredMessage(" &b&lU&btilidades &b&l» &7Te ha faltado un argumento!"),
				TextUtil.getCenteredMessage(" &7Rercuerda que todos los datos deben estár puestos!"),
				TextUtil.getCenteredMessage(" &7El formato actual de este comando es el siguiente:"),
				TextUtil.getCenteredMessage(" &bFormato:  &e"+format)
		};
	}

	public static String[] cmdHelpMessage(String format, String example){
		return new String[]{
				"",
				CommandPatcher.BAR,
				TextUtil.getCenteredMessage(" &b&lU&btilidades &b&l» &7Te ha faltado un argumento!"),
				TextUtil.getCenteredMessage(" &7Rercuerda que todos los datos deben estár puestos!"),
				TextUtil.getCenteredMessage(" &7El formato actual de este comando es el siguiente:"),
				TextUtil.getCenteredMessage(" &bFormato:  &e"+format),
				TextUtil.getCenteredMessage(" &bEjemplo:  &7"+example.replace("%player%", Names.randomName())),
				CommandPatcher.BAR,
				""
		};
	}

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
		Juan,
		KamiKaze,
		Goku,
		Rodri;

		public static String randomName(){
			return values()[new Random().nextInt(values().length)].toString();
		}
	}
}

