package com.kit.customizable.main.multiple;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.kit.customizable.ItemUtils;
import com.kit.customizable.Main;

public class MultipleKitsInventory {
	
	/*
	 * 000000 
	 * FF0000 256
	 * 00FF00 256 power 2
	 * FFFF00 256 power 2 + 256
	 * 0000FF 256 power 3
	 * FF00FF 256 power 3 + 256
	 * 00FFFF 256 power 3 + 256 power 2
	 * FFFFFF 256 power 3 + 256 power 2 + 256 
	 */
	
	@SuppressWarnings("unused")
	private int level(int color) {
		if (color <= 0xFF) { return 1; }
		else if (color <= 0x00FF) { return 2; }
		else if (color <= 0xFFFF) { return 1; }
		else if (color <= 0x0000FF) { return 3; }
		else if (color <= 0xFF00FF) { return 1; }
		else if (color <= 0x00FFFF) { return 2; }
		else if (color <= 0xFFFFFF) { return 3; }
		else {
			return 0;
		}
	}
	
	
	public static final int AMOUNT_OF_KITS = 4;
	
	private MultipleKitsInventory() {
		initInventory();
		Main.getPlugin(Main.class).getLogger().info("Multiple Kits Inventory > Initialized.");
	}

	private static MultipleKitsInventory instance = new MultipleKitsInventory();
	public static MultipleKitsInventory getInstance() { return instance; }
	
	private final int invLines = 3;
	private final ItemStack[] inventoryContents = new ItemStack[9*invLines];
	
	public Color color(int level) {
		switch(level) {
		case 1: { return Color.RED; }
		case 2: { return Color.GREEN; }
		case 3: { return Color.BLUE; }
		case 4: { return Color.YELLOW; }
		}
		return null;
	}
	
	public void initInventory() {		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta armorMeta = (LeatherArmorMeta) chestplate.getItemMeta();
		
		int space = 9/(AMOUNT_OF_KITS % 9);
		List<String> lore = Arrays.asList("",
					ChatColor.YELLOW + "Right-Click" + ChatColor.GRAY + " to edit",
					ChatColor.YELLOW + "Left-Click" + ChatColor.GRAY + " to choose");
		
		for (int i = 0; i < invLines; i++) {
			for (int j = 0; j < 9; j++) {
				int current = i*9 + j;
				
				if (i == 1) {
					
					int armorIndex = j + 1;
					if (armorIndex % space == 0) {
						armorIndex /= 2;
						armorMeta.setColor(color(armorIndex));
						armorMeta.setDisplayName(ChatColor.BOLD + "Kit #" + (armorIndex));
						armorMeta.setLore(lore);
						armorMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						chestplate.setItemMeta(armorMeta);
						this.inventoryContents[current] = chestplate.clone();
					}
					else {
						this.inventoryContents[current] = ItemUtils.BLANK;
					}
				}
				else {
					this.inventoryContents[current] = ItemUtils.BLANK;
				}
			}
		}
	}
	
	public Inventory getInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, this.inventoryContents.length, "Choose a Kit to Edit");
		inv.setContents(this.inventoryContents);
		return inv;
	}
}
