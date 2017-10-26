package com.kit.customizable.armor;

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
import com.kit.customizable.armor.ArmorItem.ArmorPart;
import com.kit.customizable.kit.KitCreationManager;
import com.kit.customizable.main.MainInventoryManager;

public class ArmorInventoryManager {
	
	private ArmorInventoryManager() { }

	private static ArmorInventoryManager instance = new ArmorInventoryManager();
	public static ArmorInventoryManager getInstance() { return instance; }
	
	public void openInventory(Player player) {
		Inventory inventory = ArmorInventory.getInstance().getInventory(player);
		
		ArmorItem[] armorItems = KitCreationManager.getInstance().getArmorComponents(player);
		if (armorItems != null) {
			for (ArmorItem armorItem : armorItems) {
				if (armorItem != null) {
					ItemUtils.enchant(inventory.getItem(ArmorInventory.getInstance().slots.get(armorItem)));
				}
			}
		}
		
		Consumer<InventoryEvent> interactionListener = new Consumer<InventoryEvent>() {
			
			private ItemStack previousItem;
			private final int SLOT = 22;
			private ArmorItem[] previousArmor;
			private boolean changeToPrevious = true;

			// InventoryCloseEvent, InventoryClickEvent, InventoryDragEvent, InventoryOpenEvent
				
			@Override
			public void accept(InventoryEvent event) {
				if (event instanceof InventoryCloseEvent) { 
					((InventoryCloseEvent) event).getPlayer().getInventory().setItem(SLOT, this.previousItem);
					
					if (this.changeToPrevious) {
						KitCreationManager.getInstance().setArmorComponents(player, this.previousArmor);
						MessageSender.send(((InventoryCloseEvent) event).getPlayer(), "kit.content.changed.previous", "Armor");
					}
					else {
						MessageSender.send(((InventoryCloseEvent) event).getPlayer(), "kit.content.changed.selected", "Armor");
					}
				}
				else if (event instanceof InventoryClickEvent) {
					int i = ArmorInventoryManager.this.onInventoryClick((InventoryClickEvent) event);
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
					this.previousArmor = KitCreationManager.getInstance().getArmorComponents(player);
					
					if (this.previousArmor == null){
						this.previousArmor = new ArmorItem[ArmorPart.values().length];
					}
					
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
		ItemStack currentItem = event.getCurrentItem();
		
		if (currentItem == null)
			return 0;
		
		event.setCancelled(true);
		
		if (ItemUtils.isSimilar(currentItem, ItemUtils.APPLY, false))
			return 2;
		
		Player player = (Player) event.getWhoClicked();
		
		
		String displayName;
		if (currentItem.hasItemMeta() && (displayName = currentItem.getItemMeta().getDisplayName()) != null && 
				displayName.startsWith("Reset")) {
			
			ArmorPart part;
			
			if (ItemUtils.isSimilar(ArmorInventory.RESET_HELMET, currentItem, false)) {
				part = ArmorPart.HELMET;
			}
			else if (ItemUtils.isSimilar(ArmorInventory.RESET_CHESTPLATE, currentItem, false)) {
				part = ArmorPart.CHESTPLATE;
			}
			else if (ItemUtils.isSimilar(ArmorInventory.RESET_LEGGINGS, currentItem, false)) {
				part = ArmorPart.LEGGINGS;
			}
			else if (ItemUtils.isSimilar(ArmorInventory.RESET_BOOTS, currentItem, false)) {
				part = ArmorPart.BOOTS;
			}
			else {
				return 0;
			}
			
			ArmorItem previousItem = KitCreationManager.getInstance().getArmorComponent(player, part);
			KitCreationManager.getInstance().removeArmorPart(player, part);
			
			if (previousItem != null) {
				ItemUtils.removeEnchant(event.getInventory().getItem(ArmorInventory.getInstance().slots.get(previousItem)));
			}
			
			MessageSender.send(player, "kit.content.reset", part.getName());
			return 1;
		}
		
		ArmorItem wanted = ArmorGenerator.getInstance().getArmor(currentItem, true);
		
		if (wanted == null)
			return 0;
		
		int currentKG = KitCreationManager.getInstance().getUsedKilograms(player),
				maxKG = KitCreationManager.getInstance().getMaxKilograms(player);
			
		ArmorItem previousItem;
		if ((previousItem = KitCreationManager.getInstance().getArmorComponent(player, wanted.armorPart)) != null) {
			currentKG -= previousItem.getWeight();
		}
			
		if (currentKG + wanted.getWeight() > maxKG) {
			// player doesn't have enough KG
			MessageSender.send(player, "kit.content.use.no", wanted.getName());
		}
		else {
			// player has enough KG
			
			if (previousItem != null) {
				int previousSlot = ArmorInventory.getInstance().slots.get(previousItem);
				ItemUtils.removeEnchant(event.getInventory().getItem(previousSlot));
			}
			
			
			KitCreationManager.getInstance().setArmorComponent(player, wanted);
			
			int wantedSlot = ArmorInventory.getInstance().slots.get(wanted);
			ItemUtils.enchant(event.getInventory().getItem(wantedSlot));
			
			MessageSender.send(player, "kit.content.use.yes", wanted.getName());
			return 1;
		}	
			
		return 0;
	}
}
