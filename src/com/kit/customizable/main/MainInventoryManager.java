package com.kit.customizable.main;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kit.customizable.InventoryListenerUtil;
import com.kit.customizable.ItemUtils;
import com.kit.customizable.MessageSender;
import com.kit.customizable.armor.ArmorInventoryManager;
import com.kit.customizable.armor.ArmorItem;
import com.kit.customizable.kit.KitCreationManager;
import com.kit.customizable.skill.SkillType;
import com.kit.customizable.weapon.WeaponInventoryManager;
import com.kit.customizable.weapon.WeaponItem;

public class MainInventoryManager {
	
	private MainInventoryManager() { }

	private static MainInventoryManager instance = new MainInventoryManager();
	public static MainInventoryManager getInstance() { return instance; }
	

	
	public void openInventory(Player player) {
		Inventory inventory = MainInventory.getInstance().getInventory(player);
		
		inventory.setItem(MainInventory.getInstance().WEAPON_SLOT + 9, KitCreationManager.getInstance().
				getChosenItem(player,WeaponItem.class));
		inventory.setItem(MainInventory.getInstance().SKILL_SLOT + 9, KitCreationManager.getInstance().
				getChosenItem(player, SkillType.class));
		inventory.setItem(MainInventory.getInstance().ARMOUR_SLOT + 9, KitCreationManager.getInstance().
				getChosenItem(player, ArmorItem.class));
		
		Consumer<InventoryEvent> interactionListener = new Consumer<InventoryEvent>() {
			
			// InventoryCloseEvent, InventoryClickEvent, InventoryDragEvent, InventoryOpenEvent
				
			@Override
			public void accept(InventoryEvent event) {
				if (event instanceof InventoryCloseEvent) { }
				else if (event instanceof InventoryClickEvent) {
					MainInventoryManager.this.onInventoryClick((InventoryClickEvent) event); 
				}
				else if (event instanceof InventoryDragEvent) {
					((InventoryDragEvent) event).setCancelled(true);
				}
				else if (event instanceof InventoryOpenEvent) { }
				else {
					System.err.println("Couldn't handle event: " + event.getEventName());
				}
			}
			
		};
		
		InventoryListenerUtil.openInventory(player, inventory, interactionListener);
	}

	private int onInventoryClick(InventoryClickEvent event) {
		ItemStack currentItem = event.getCurrentItem();
		
		if (currentItem == null)
			return 0;
		
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		
		if (ItemUtils.isSimilar(currentItem, MainInventory.getInstance().WEAPON_ITEM, false)) {
			player.closeInventory();
			WeaponInventoryManager.getInstance().openInventory(player);
		}
		
		if (ItemUtils.isSimilar(currentItem, MainInventory.getInstance().SKILL_ITEM, false)) {
			// TODO: open skill inventory
		}
		
		if (ItemUtils.isSimilar(currentItem, MainInventory.getInstance().ARMOUR_ITEM, false)) {
			// TODO: open armor inventory
			player.closeInventory();
			ArmorInventoryManager.getInstance().openInventory(player);
		}
		
		if (ItemUtils.isSimilar(currentItem, MainInventory.getInstance().CREATE_KIT, false)) {
			player.closeInventory();
			
			int result = KitCreationManager.getInstance().createKit(player);
			if (result == 1) { 
				MessageSender.send(player, "kit.creation.success");
			}
			else {
				MessageSender.send(player, "kit.creation.fail", "(Weapon or Armor were not set)");
			}
		}
		
		return 0;
	}
}
