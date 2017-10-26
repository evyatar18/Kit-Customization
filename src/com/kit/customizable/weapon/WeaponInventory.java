package com.kit.customizable.weapon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kit.customizable.ItemUtils;
import com.kit.customizable.Main;
import com.kit.customizable.weapon.WeaponItem.WeaponType;

public class WeaponInventory {
	
	/*
	 * ---------
	 * -wwwwww--
	 * --------a
	 */
	
	private final int invLines = 3;
	private final ItemStack[] inventoryContents = new ItemStack[9*invLines];
	
	private WeaponInventory() { 
		initItems();
		initInventory();
		
		Main.getPlugin(Main.class).getLogger().info("Weapon Inventory > Initialized.");
	}
	
	private static WeaponInventory instance = new WeaponInventory();
	public static WeaponInventory getInstance() { return instance; }
	
	// ordered by rating
	private WeaponItem[] weaponItems = new WeaponItem[WeaponType.values().length];
	
	public Map<WeaponItem, Integer> slots = new HashMap<>();
	
	private void initItems() {
		
		int iterator = 0;
		
		for (WeaponType weaponType : WeaponType.values()) {
			weaponItems[iterator] = WeaponGenerator.getInstance().getWeapon(weaponType);
			iterator++;
		}
	}

	private void initInventory() {
		Arrays.sort(weaponItems, (x, y) -> WeaponGenerator.rateWeapon(x.getType()) - WeaponGenerator.rateWeapon(y.getType()));
		
		for (int i = 0; i < invLines; i++) {
			for (int j = 0; j < 9; j++) {
				int currentSlot = i*9 + j;
				
				// 'apply' item
				if (currentSlot == inventoryContents.length - 1) {
					inventoryContents[currentSlot] = ItemUtils.APPLY;
					continue;
				}
				
				if (i == 0 || i == 2) {
					inventoryContents[currentSlot] = ItemUtils.BLANK;
				}
				else {
					if (j >= 1 && j <= 6) {
						inventoryContents[currentSlot] = weaponItems[j - 1].getMenuItem(); // since it starts from slot 1
						slots.put(weaponItems[j-1], currentSlot);
					}
					else {
						inventoryContents[currentSlot] = ItemUtils.BLANK;
					}
				}
			}
		}
	}
	
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, invLines*9, "Choose Your Weapon");
		inv.setContents(inventoryContents);
		return inv;
	}
}
