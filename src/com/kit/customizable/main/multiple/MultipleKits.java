package com.kit.customizable.main.multiple;

import java.util.HashMap;
import java.util.Map;
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
import com.kit.customizable.armor.ArmorItem;
import com.kit.customizable.kit.KitCreationManager;
import com.kit.customizable.main.MainInventory;
import com.kit.customizable.skill.SkillType;
import com.kit.customizable.weapon.WeaponItem;


/*
 * TODO
 * Complete classes 
 */
public class MultipleKits {
	
	private MultipleKits() { }

	private static MultipleKits instance = new MultipleKits();
	public static MultipleKits getInstance() { return instance; }
	
	
	private Map<String, Integer> editing = new HashMap<String, Integer>();
	
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
					MultipleKits.this.onInventoryClick((InventoryClickEvent) event); 
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
		
		return 0;
	}
}
