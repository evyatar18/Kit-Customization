package com.kit.customizable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.kit.customizable.armor.ArmorInventory;
import com.kit.customizable.kit.Kit;
import com.kit.customizable.kit.KitCreationManager;
import com.kit.customizable.main.MainInventoryManager;
import com.kit.customizable.skill.SkillTrigger;
import com.kit.customizable.weapon.WeaponInventoryManager;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(KitCreationManager.getInstance().logoutListener, this);
		getServer().getPluginManager().registerEvents(SkillTrigger.triggersListener, this);
		getLogger().info("Success");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if (label.equalsIgnoreCase("weaponInv")) {
			WeaponInventoryManager.getInstance().openInventory(player);
		}
		
		if (label.equalsIgnoreCase("armorInv")) {
			player.openInventory(ArmorInventory.getInstance().getInventory(player));
		}
		
		if (label.equalsIgnoreCase("mainInv")) {
			MainInventoryManager.getInstance().openInventory(player);
//			player.openInventory(MultipleKitsInventory.getInstance().getInventory(player));
		}
		
		if (label.equalsIgnoreCase("choose")) {
			Kit kit = KitCreationManager.getInstance().getKit(player);
			
			if (kit == null) {
				MessageSender.send(sender, "kit.cmd.no");
			}
			else {
				Kit.apply(kit, player);
				MessageSender.send(sender, "kit.cmd.yes");
			}
		}
		
		return true;
	}
	
	public static Main getInstance() {
		return Main.getPlugin(Main.class);
	}
}
