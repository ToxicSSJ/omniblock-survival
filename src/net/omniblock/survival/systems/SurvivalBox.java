package net.omniblock.survival.systems;

import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.shop.systems.box.MysteryBox;
import net.omniblock.shop.systems.object.Element;
import net.omniblock.survival.SurvivalManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SurvivalBox extends MysteryBox  {

	public  SurvivalBox(){
		super(SurvivalManager.getLocation().clone().add(3,1,8));
	}

	@Override
	public void craftBox() {

		this.getBase().setType(Material.ENDER_PORTAL_FRAME);
		this.getBox().setType(Material.CHEST);
	}

	@Override
	public void contentBox() {

	}

	@Override
	public Listener getEvent() {
		return new Listener() {

			@EventHandler
			public void onClick(PlayerInteractEvent e){

				if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					if(e.getClickedBlock().getLocation().equals(getBox().getLocation()))
						if(e.getClickedBlock().getType().equals(Material.CHEST)){

							destroyBox();
							e.getPlayer().sendMessage(TextUtil.format("&8&lB&8oosters &c&l  &c&lProximamente.... "));

						}

			}
		};
	}

	@Override
	public void buy(Player player, Element element) {

	}
	
}
