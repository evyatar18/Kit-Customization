package com.kit.customizable.weapon;

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
import com.kit.customizable.kit.KitCreationManager;
import com.kit.customizable.main.MainInventoryManager;

public class WeaponInventoryManager {
	
	private WeaponInventoryManager() { }
	
	private static WeaponInventoryManager instance = new WeaponInventoryManager();
	public static WeaponInventoryManager getInstance() { return instance; }
	
	public void openInventory(Player player) {
		Inventory inventory = WeaponInventory.getInstance().getInventory(player);
		WeaponItem item = KitCreationManager.getInstance().getWeapon(player);
		
		if (item != null) {
			ItemUtils.enchant(inventory.getItem(WeaponInventory.getInstance().slots.get(item)));
		}
		
		Consumer<InventoryEvent> interactionListener = new Consumer<InventoryEvent>() {
			
			// InventoryCloseEvent, InventoryClickEvent, InventoryDragEvent, InventoryOpenEvent
			
			private ItemStack previousItem;
			private WeaponItem previousWeapon;
			private final int SLOT = 22;
			private boolean changeToPrevious = true;
			
			@Override
			public void accept(InventoryEvent event) {
				if (event instanceof InventoryCloseEvent) {
					((InventoryCloseEvent) event).getPlayer().getInventory().setItem(SLOT, this.previousItem);
					
					if (this.changeToPrevious) {
						KitCreationManager.getInstance().setWeapon(player, this.previousWeapon);
						MessageSender.send(((InventoryCloseEvent) event).getPlayer(), "kit.content.changed.previous", "Weapon");
					}
					else {
						MessageSender.send(((InventoryCloseEvent) event).getPlayer(), "kit.content.changed.selected", "Weapon");
					}
				}
				else if (event instanceof InventoryClickEvent) { 
					// When even is click
					int i = WeaponInventoryManager.this.onInventoryClick((InventoryClickEvent) event);
					if (i == 1) {
						// when changing weapon
						((InventoryClickEvent) event).getWhoClicked()
							.getInventory().setItem(22, KitCreationManager.getInstance().getUsedKGItem(player));
					}
					if (i == 2) {
						// when clicking on 'Apply'
						this.changeToPrevious = false;
						((InventoryClickEvent) event).getWhoClicked().closeInventory();
						MainInventoryManager.getInstance().openInventory(player);
					}
					else { this.changeToPrevious = true; }
				}
				else if (event instanceof InventoryDragEvent) {
					((InventoryDragEvent) event).setCancelled(true);
				}
				else if (event instanceof InventoryOpenEvent) { 
					this.previousItem = ((InventoryOpenEvent) event).getPlayer().getInventory().getItem(SLOT);
					this.previousWeapon = KitCreationManager.getInstance().getWeapon(player);
					((InventoryOpenEvent) event).getPlayer().
						getInventory().setItem(SLOT, KitCreationManager.getInstance().getUsedKGItem(player));
				}
				else {
					System.err.println("Couldn't handle event: " + event.getEventName());
				}
			}
			
		};
		
		InventoryListenerUtil.openInventory(player, inventory, interactionListener);
	}
	
	private int onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		ItemStack currentItem = event.getCurrentItem();
		
		if (currentItem == null)
			return 0;
		
		event.setCancelled(true);
		
		if (ItemUtils.isSimilar(currentItem, ItemUtils.APPLY, false)) {
			return 2;
		}
		
		WeaponItem wanted = WeaponGenerator.getInstance().getWeapon(currentItem, true);
				
		if (wanted == null) 
			return 0;
		
		int currentKG = KitCreationManager.getInstance().getUsedKilograms(player),
			maxKG = KitCreationManager.getInstance().getMaxKilograms(player);
		
		WeaponItem previousItem;
		if ((previousItem = KitCreationManager.getInstance().getWeapon(player)) != null) {
			currentKG -= previousItem.getWeight();
		}
		
		if (currentKG + wanted.getWeight() > maxKG) {
			// player doesn't have enough KG
			MessageSender.send(player, "kit.content.use.no", wanted.getName());
		}
		else {
			// player has enough KG
			
			if (previousItem != null) {
				int previousSlot = WeaponInventory.getInstance().slots.get(previousItem);
				ItemUtils.removeEnchant(event.getInventory().getItem(previousSlot));
			}
			
			
			KitCreationManager.getInstance().setWeapon(player, wanted);
			
			int wantedSlot = WeaponInventory.getInstance().slots.get(wanted);
			ItemUtils.enchant(event.getInventory().getItem(wantedSlot));
			
			MessageSender.send(player, "kit.content.use.yes", wanted.getName());
			return 1;
		}
		
		return 0;
	}
	
	
}
