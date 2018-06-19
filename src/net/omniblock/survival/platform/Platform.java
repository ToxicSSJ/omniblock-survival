package net.omniblock.survival.platform;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.omniblock.survival.SurvivalPlugin;

public final class Platform {

	public static void runLater(Task task, int delay) {

		new BukkitRunnable() {

			@Override
			public void run() {
				task.start();
				return;
			}

		}.runTaskLater(SurvivalPlugin.getInstance(), delay);
	}
	
	public static BukkitTask runTimer(Task task, int delay) {
		return new BukkitRunnable() {

			@Override
			public void run() {
				task.start();
				return;
			}

		}.runTaskTimer(SurvivalPlugin.getInstance(), 0L, delay);
	}

	public interface Task {
		public abstract void start();
	}
}