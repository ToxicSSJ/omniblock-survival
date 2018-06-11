package net.omniblock.survival.platform;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.omniblock.survival.SurvivalPlugin;

public final class Platform {

	public static void runLater(Task runLater, int delay) {

		new BukkitRunnable() {

			@Override
			public void run() {
				runLater.start();
				return;
			}

		}.runTaskLater(SurvivalPlugin.getInstance(), delay * 20L);
	}
	
	public static BukkitTask runTimer(Task runLater, int delay) {
		return new BukkitRunnable() {

			@Override
			public void run() {
				runLater.start();
				return;
			}

		}.runTaskTimer(SurvivalPlugin.getInstance(), 5L, delay*20L);
	}

	public interface Task {
		public abstract void start();
	}
}