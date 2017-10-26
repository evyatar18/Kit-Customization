package com.kit.customizable.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kit.customizable.ItemUtils;
import com.kit.customizable.Main;

public class MainInventory {
	
	private final int invLines = 4;
	private final ItemStack[] inventoryContents = new ItemStack[9*invLines];
	
	private MainInventory() {
		
		WEAPON_ITEM = ItemUtils.createItem(Material.DIAMOND_SWORD, 1, "Weapon Selection");
		ARMOUR_ITEM = ItemUtils.createItem(Material.IRON_CHESTPLATE, 1, "Armor Selection");
		SKILL_ITEM  = ItemUtils.createItem(Material.BLAZE_POWDER, 1, "Skill Selection");
		
		initInventory();
		Main.getPlugin(Main.class).getLogger().info("Main Inventory > Initialized.");
	}
	
	

	private static final MainInventory instance = new MainInventory();
	public static MainInventory getInstance() { return instance; }
	
	public final ItemStack WEAPON_ITEM,
						   ARMOUR_ITEM,
						   SKILL_ITEM;
	
	public final int WEAPON_SLOT = 2 + 9,
					 ARMOUR_SLOT = 6 + 9,
					 SKILL_SLOT = 4 + 9;
	
	public final ItemStack CREATE_KIT = ItemUtils.createItem(Material.WOOL, (short) 5, "Create your kit");
	
	private void initInventory() {
		int invSize = this.inventoryContents.length - 1;
		
		for (int i = 0; i < this.invLines; i++) {
			for (int j = 0; j < 9; j++) {
				int currentSlot = i*9 + j;
				
				if (i == 1) {
					if (j == (WEAPON_SLOT % 9)) {
						this.inventoryContents[currentSlot] = WEAPON_ITEM;
					}
					else if (j == (SKILL_SLOT %9)) {
						this.inventoryContents[currentSlot] = SKILL_ITEM;
					}
					else if (j == (ARMOUR_SLOT % 9)) {
						this.inventoryContents[currentSlot] = ARMOUR_ITEM;
					}
					else {
						this.inventoryContents[currentSlot] = ItemUtils.BLANK;
					}
				}
				else {
					if (currentSlot == invSize) {
						this.inventoryContents[currentSlot] = CREATE_KIT;
						continue;
					}
					
					this.inventoryContents[currentSlot] = ItemUtils.BLANK;
				}
			}
		}
	}
	
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, this.inventoryContents.length, "Main Inventory");
		inv.setContents(this.inventoryContents);
		return inv;
	}
}
