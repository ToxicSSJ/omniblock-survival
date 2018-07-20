package net.omniblock.survival.systems.commands.gui;

import java.util.ArrayList;
import java.util.List;

public final class RegisterGUI {

	protected static List<GuiExecutor> Guis = new ArrayList<>();

	public static void registerGUI(GuiExecutor gui){

		if(Guis.contains(gui))
			return;

		Guis.add(gui);
	}

	public static List<GuiExecutor> getGuis() {
		return Guis;
	}
}
