package net.omniblock.survival.board;

import net.omniblock.jobs.api.listener.jobs.structure.Job;
import net.omniblock.jobs.api.type.JobType;
import net.omniblock.jobs.base.object.PlayerJobWrapper;
import net.omniblock.modtools.api.SpigotVanishAPI;
import net.omniblock.network.handlers.base.bases.type.RankBase;
import net.omniblock.network.library.helpers.scoreboard.ScoreboardUtil;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.rank.type.RankType;
import net.omniblock.survival.SurvivalPlugin;
import net.omniblock.survival.base.SurvivalBankBase;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Clase encargada del scoreboard
 * en la modalidad Survival
 *
 * @author SoZyk
 */
public class SurvivalScoreBoard {
	/**
	 *
	 * Lista que guarda los jugadores
	 * que decidan NO ver la scoreboard.
	 *
	 */
	public static Set<Player> blackList = new HashSet<>();

	public static BukkitTask brunnable;

	public static void initialize(){
		brunnable = new BukkitRunnable(){

			String title = TextUtil.format("&9&l     Survival     ");
			int round = 0;
			int pos = 0;

			@Override
			public void run() {

				if (pos != 34) {
					pos++;
					title = sbTitleNormal(pos);
				} else {
					if (round != 490) {
						round++;
					} else {
						pos = 0;
						round = 0;
					}
				}

				int onlinePlayers = Bukkit.getOnlinePlayers().size() - SpigotVanishAPI.getVanishedPlayers().size();

				for(Player player : Bukkit.getOnlinePlayers())
					if(!blackList.contains(player))
						updateScoreboard(player, title, onlinePlayers);
					else
						player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

			}
		}.runTaskTimer(SurvivalPlugin.getInstance(), 0L, 10L);
	}

	private static void updateScoreboard(Player player, String title, int onlinePlayers){

		RankType rank = RankBase.getRank(player);
		PlayerJobWrapper playerJob = null;

		for(JobType jobType : JobType.values())
			if(Job.isActivated(player, jobType)) {

				playerJob = Job.getJobs(player).getJob(jobType);
			}

		if(playerJob != null)
			ScoreboardUtil.unrankedSidebarDisplay(player, new String[]{
				title,
				TextUtil.format(" "),
				TextUtil.format("&7Jugador: &b"+player.getName()),
				TextUtil.format("&7Rango: &b" + (rank == RankType.USER ? "&b(&7Usuario&b)" : rank.getPrefix())),
				TextUtil.format("&7Dinero: &b"+ SurvivalBankBase.getMoney(player) + " ⛃"),
				TextUtil.format("  "),
				TextUtil.format("&7Trabajo: &b" + playerJob.getJobType().getName()),
				TextUtil.format("&7Nivel: &b" + playerJob.getPrestige()),
				TextUtil.format("&7Exp: &b" + playerJob.getXP() + "&8 / &b" + playerJob.getPrestige() * 4 + "k"),
				TextUtil.format("   "),
				TextUtil.format("&7Jugadores: &b" + onlinePlayers),
				TextUtil.format("&7Ping: &b" + ((CraftPlayer) player).getHandle().ping),
				TextUtil.format("    "),
				TextUtil.format("&emc.omniblock.net")

			}, false);
		else
			ScoreboardUtil.unrankedSidebarDisplay(player, new String[]{
					title,
					TextUtil.format(" "),
					TextUtil.format("&7Jugador: &b"+player.getName()),
					TextUtil.format("&7Rango: &b" + (rank == RankType.USER ? "&b(&7Usuario&b)" : rank.getPrefix())),
					TextUtil.format("&7Dinero: &b"+ SurvivalBankBase.getMoney(player) + " ⛃"),
					TextUtil.format("  "),
					TextUtil.format("&7Trabajo: &bSin empleo"),
					TextUtil.format("   "),
					TextUtil.format("&7Jugadores: &b" + onlinePlayers),
					TextUtil.format("&7Ping: &b" + ((CraftPlayer) player).getHandle().ping),
					TextUtil.format("    "),
					TextUtil.format("&emc.omniblock.net")

			}, false);
	}

	public static String sbTitleNormal(int a) {
		switch (a) {
			case 1:
				return TextUtil.format("&9&l     Survival     ");
			case 2:
				return TextUtil.format("&8&l     Survival     ");
			case 3:
				return TextUtil.format("&b&l     Survival     ");
			case 4:
				return TextUtil.format("&9&l     &b&lSurvival     ");
			case 5:
				return TextUtil.format("&8&l     &9&lS&b&lurvival     ");
			case 6:
				return TextUtil.format("&8&l     S&9&lu&b&lrvival     ");
			case 7:
				return TextUtil.format("&8&l     Su&9&lr&b&lvival     ");
			case 8:
				return TextUtil.format("&8&l     Sur&9&lv&b&lival     ");
			case 9:
				return TextUtil.format("&8&l     Surv&9&li&b&lval     ");
			case 10:
				return TextUtil.format("&8&l     Survi&9&lv&b&lal     ");
			case 11:
				return TextUtil.format("&8&l     Surviv&9&la&b&ll     ");
			case 12:
				return TextUtil.format("&8&l     Surviva&9&ll&b&l     ");
			case 13:
				return TextUtil.format("&8&l     Survival     ");
			case 14:
				return TextUtil.format("&8&l     Survival     ");
			case 15:
				return TextUtil.format("&8&l     Survival     ");
			case 16:
				return TextUtil.format("&8&l     Survival     ");
			case 18:
				return TextUtil.format("&8&l     Survival     ");
			case 19:
				return TextUtil.format("&8&l     Survival     ");
			case 20:
				return TextUtil.format("&8&l     Survival     ");
			case 21:
				return TextUtil.format("&9&l     &8&lSurvival     ");
			case 22:
				return TextUtil.format("&b&l     &9&lS&8&lurvival     ");
			case 23:
				return TextUtil.format("&b&l     S&9&lu&8&lrvival     ");
			case 24:
				return TextUtil.format("&b&l     Su&9&lr&8&lvival     ");
			case 25:
				return TextUtil.format("&b&l     Sur&9&lv&8&lival     ");
			case 26:
				return TextUtil.format("&b&l     Surv&9&li&8&lval     ");
			case 27:
				return TextUtil.format("&b&l     Survi&9&lv&8&lal     ");
			case 28:
				return TextUtil.format("&b&l     Surviv&9&la&8&ll     ");
			case 29:
				return TextUtil.format("&b&l     Surviva&9&ll     ");
			case 30:
				return TextUtil.format("&b&l     Survival     ");
			case 31:
				return TextUtil.format("&b&l     Survival     ");
			case 32:
				return TextUtil.format("&b&l     Survival     ");
			case 33:
				return TextUtil.format("&8&l     Survival     ");
			case 34:
				return TextUtil.format("&9&l     Survival     ");

		}
		return TextUtil.format("&9&l     Survival     ");
	}
}
