package com.kit.customizable;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryListenerUtil {
	
	/**
	 * Calls
	 * <br>
	 * InventoryCloseEvent, InventoryClickEvent, InventoryDragEvent, InventoryOpenEvent
	 * @param player
	 * @param inv
	 * @param interactionListener
	 */
	public static void openInventory(Player player, Inventory inv, Consumer<InventoryEvent> interactionListener) {
		Listener listener = new Listener() {
			
			@EventHandler
			public void onInventoryClose(InventoryCloseEvent event) {
				if (event.getPlayer().getEntityId() == player.getEntityId()) {
					HandlerList.unregisterAll(this);
					interactionListener.accept(event);
				}
			}
			
			@EventHandler
			public void onInventoryClick(InventoryClickEvent event) {
				if (event.getWhoClicked().getEntityId() == player.getEntityId()) {
					interactionListener.accept(event);
				}
			}
			
			@EventHandler
			public void onInventoryDrag(InventoryDragEvent event) { 
				if (event.getWhoClicked().getEntityId() == player.getEntityId()) {
					interactionListener.accept(event);
				}
			}
			
			@EventHandler
			public void onInventoryOpen(InventoryOpenEvent event) {
				if (event.getPlayer().getEntityId() == player.getEntityId()) {
					interactionListener.accept(event);
				}
			}
		};
		
		Bukkit.getPluginManager().registerEvents(listener, JavaPlugin.getPlugin(Main.class));
		player.openInventory(inv);
	}
}
